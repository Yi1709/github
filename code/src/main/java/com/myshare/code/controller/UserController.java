package com.myshare.code.controller;

import com.alibaba.druid.sql.dialect.odps.ast.OdpsAddStatisticStatement;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.myshare.code.entity.*;
import com.myshare.code.lucene.ArticleIndex;
import com.myshare.code.service.*;
import com.myshare.code.util.*;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.execchain.TunnelRefusedException;
import org.apache.logging.log4j.ThreadContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.aspectj.weaver.ast.Var;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.w3c.dom.html.HTMLParagraphElement;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Resource
	private MailSender mailSender;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ArticleIndex articleIndex;

	@Autowired
	private UserDownloadService userDownloadService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private MessageService messageService;

	@Value("${imgFilePath}")
	private String imgFilePath;

	@ResponseBody
	@PostMapping("/register")
	public Map<String, Object> register(@Valid User user, BindingResult bindingResult) {
		Map<String, Object> map = new HashMap<>();
		System.out.println("1111");
		if (bindingResult.hasErrors()) {
			map.put("success", false);
			map.put("errorInfo", bindingResult.getFieldError().getDefaultMessage());

		} else if (userService.findByUserName(user.getUserName()) != null) {
			map.put("success", false);
			map.put("errorInfo", "用户名已存在");
		} else if (userService.findByEmail(user.getEmail()) != null) {
			map.put("success", false);
			map.put("errorInfo", "邮箱已存在");
		} else {
			user.setRegistrationDate(new Date());
			user.setLatelyLoginTime(new Date());
			user.setHeadPortrait("tou.jpg");
			user.setPassword(CryptographyUtil.md5(user.getPassword(), CryptographyUtil.SALT));
			userService.save(user);
			map.put("success", true);
		}

		return map;
	}

	/**
	 * 用户登录
	 */
	@ResponseBody
	@PostMapping("/login")
	public Map<String, Object> login(User user, HttpSession httpSession) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isBlank(user.getUserName())) {
			map.put("sucess", false);
			map.put("errorInfo", "请输入用户名！");
		} else if (StringUtils.isBlank(user.getPassword())) {
			map.put("success", false);
			map.put("errorInfo", "请输入密码！");
		} else {

			Subject subject = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(),
					CryptographyUtil.md5(user.getPassword(), CryptographyUtil.SALT));
			System.out.println(token.getPassword());
			try {
				subject.login(token);//登录验证
				String userName = (String) SecurityUtils.getSubject().getPrincipal();
				User currentUser = this.userService.findByUserName(userName);
				if (currentUser.isOff()) {
					map.put("success", false);
					map.put("errorInfo", "该账户已被封禁,请联系管理员!");
					subject.logout();
				} else {
					currentUser.setLatelyLoginTime(new Date());
					this.userService.save(currentUser);
					//未读消息数
					Integer messageCount = messageService.getCountByUserId(currentUser.getUserId());
					currentUser.setMessageCount(messageCount);
					//失效资源数
					Article s_article = new Article();
					s_article.setUserful(false);
					s_article.setUser(currentUser);
					httpSession.setAttribute(Consts.UN_USEFUL_AERICLE_COUNT, articleService.getcount(s_article,
							null, null, null));
					httpSession.setAttribute(Consts.CURRENT_USER, currentUser);
					map.put("success", true);
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put("success", false);
				map.put("errorInfo", "用户名或密码错误");
			}
		}
		return map;
	}

	/**
	 * 找回密码
	 */
	@ResponseBody
	@PostMapping("/sendEmail")
	public Map<String, Object> sendEmail(String email, HttpSession httpSession) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isBlank(email)) {
			map.put("sucess", false);
			map.put("errorInfo", "邮箱不能为空！");
			return map;
		}
		//验证邮件是否存在
		User user = this.userService.findByEmail(email);
		if (user == null) {
			map.put("success", false);
			map.put("errorInfo", "邮箱不存在");
			return map;
		}
		String code = StringUtil.getSixRandom();
		//发送邮件
		SimpleMailMessage message = new SimpleMailMessage(); //声明一个消息构造器
		message.setFrom("weiwspring@qq.com");
		message.setTo(email);
		message.setSubject("资源分享网-用户找回密码");
		message.setText("您本次的验证码是：" + code);
		mailSender.send(message);
		System.out.println(code);
		httpSession.setAttribute(Consts.MAIL_CODE_NAME, code);
		httpSession.setAttribute(Consts.USER_ID_NAME, user.getUserId());
		map.put("success", true);


		return map;
	}

	/**
	 * 邮件验证码判断
	 */
	@ResponseBody
	@PostMapping("/checkYzm")
	public Map<String, Object> checkYzm(String yzm, HttpSession httpSession) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isBlank(yzm)) {
			map.put("sucess", false);
			map.put("errorInfo", "验证码不能为空！");
			return map;
		}
		String mailcode = (String) httpSession.getAttribute(Consts.MAIL_CODE_NAME);
		Integer userId = (Integer) httpSession.getAttribute(Consts.USER_ID_NAME);
		if (!yzm.equals(mailcode)) {
			map.put("success", false);
			map.put("errorInfo", "验证码不符，请重新输入！");
			return map;
		}
		//重置用户密码
		User user = userService.getById(userId);
		user.setPassword(CryptographyUtil.md5(Consts.PASSWORD, CryptographyUtil.SALT));
		this.userService.save(user);
		map.put("success", true);


		return map;
	}

	/**
	 * 资源管理
	 */
	@GetMapping("/articleManager")
	public String articleManager() {
		return "/user/articleManager";
	}

	/**
	 * 根据条件查询资源信息列表（只显示当前登录用户）
	 */

	@ResponseBody
	@RequestMapping("/articleList")
	public Map<String, Object> articleList(Article s_article, @RequestParam(value = "page", required = false) Integer
			page, @RequestParam
			                                       (value = "limit", required = false) Integer pageSize, HttpSession
			                                       session) {
		Map<String, Object> map = new HashMap<>();
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		s_article.setUser(currentUser);
		map.put("data", this.articleService.list(s_article, null, null, null, page, pageSize, Sort.Direction.DESC,
				"publishDate"));
		map.put("count", this.articleService.getcount(s_article, null, null, null));
		map.put("code", 0); //返回码
		return map;
	}

	@GetMapping("/toAddArticle")
	public String toAddArticle() {
		return "/user/AddArticle.html";
	}

	@ResponseBody
	@PostMapping("/saveArticle")
	public Map<String, Object> saveArticle(Article article, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<>();
		if (article.getPoints() < 0 || article.getPoints() > 10) {
			map.put("success", false);
			map.put("errorInfo", "积分超出正常区间");
			return map;
		}
		if (!CheckShareLinkEnableUtil.check(article.getDownload())) {
			map.put("success", false);
			map.put("errorInfo", "该链接无效，请重新发布!");
			return map;
		}
		System.out.println(article.getPublishDate());
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		if (article.getArticleId() == null) {
			article.setPublishDate(new Date());
			article.setUser(currentUser);
			if (article.getPoints() == 0) {
				article.setFree(true);
			}
			article.setState(1);
			article.setClick(50 + new Random().nextInt(150));
			articleService.save(article);
			map.put("success", true);
		} else {  //修改资源
			Article oldArticle = this.articleService.getById(article.getArticleId());
			if (oldArticle.getUser().getUserId().intValue() == currentUser.getUserId().intValue()) {
				oldArticle.setName(article.getName());
				oldArticle.setArcType(article.getArcType());
				oldArticle.setDownload(article.getDownload());
				oldArticle.setPassword(article.getPassword());
				oldArticle.setKeywords(article.getKeywords());
				oldArticle.setDescription(article.getDescription());
				oldArticle.setContent(article.getContent());
				if (oldArticle.getState() == 3) { //状态为未通过，则重新提交,状态设为未审核
					oldArticle.setState(1);
				}
				this.articleService.save(oldArticle);
				//-TODO 更新新资源时，需要把消息放入lucene
				if (oldArticle.getState().intValue() == 2) {
					articleIndex.updateIndex(oldArticle);
				}
				map.put("success", true);
			}
		}
		return map;
	}

	/**
	 * 验证资源发布者
	 */
	@ResponseBody
	@RequestMapping("/checkArticleUser")
	public Map<String, Object> checkArticleUser(Integer articleId, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		User curretUser = (User) session.getAttribute(Consts.CURRENT_USER);
		Article article = articleService.getById(articleId);
		if (article.getUser().getUserId().intValue() != curretUser.getUserId().intValue()) {
			resultMap.put("success", false);
			resultMap.put("errorInfo", "你不是发布者，无法编辑!");
			return resultMap;
		} else {
			resultMap.put("success", true);
			return resultMap;
		}
	}

	/**
	 * 跟新资源
	 *
	 * @param articleId
	 * @return
	 */
	@GetMapping("/toEditArticle/{articleId}")
	public ModelAndView toEditArticle(@PathVariable(name = "articleId", required = true) Integer articleId) {
		ModelAndView mv = new ModelAndView();
		Article article = this.articleService.getById(articleId);
		mv.addObject("article", article);
		mv.setViewName("/user/editArticle");
		return mv;
	}

	/**
	 * 删除资源
	 */
	@ResponseBody
	@RequestMapping("/articleDelete")
	public Map<String, Object> articleDelete(Integer articleId, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		Article article = this.articleService.getById(articleId);
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		if (article.getUser().getUserId().intValue() == currentUser.getUserId().intValue()) {
			// TODO: 2020/4/5 需要先删除评论
			this.commentService.deleteByArticleId(articleId);
			this.articleService.delete(articleId);
			// TODO: 2020/4/5 从lucene中删除
			this.articleIndex.deleteIndex(String.valueOf(articleId));
			resultMap.put("success", true);
		} else {
			resultMap.put("success", false);
			resultMap.put("errorInfo", "你不是发布者，无法删除该资源！");
		}
		return resultMap;
	}

	/**
	 * 判读某资源是否被用户下载过
	 */
	@ResponseBody
	@RequestMapping("/userDownloadExit")
	public boolean userDownloadExit(Integer articleId, HttpSession session) {
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		Integer count = this.userDownloadService.getCountByUserIdAAndArticleId(currentUser
				.getUserId(), articleId);
		if (count > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 判断下载积分是否足够
	 *
	 * @param points
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/userDownEnough")
	public boolean userDownEnough(Integer points, HttpSession session) {
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		if (currentUser.getPoints() >= points) {
			return true;

		} else {
			return false;
		}
	}

	/**
	 * 下载资源
	 */
	@RequestMapping("/toDownloadPage/{articleId}")
	public ModelAndView toDownloadPage(@PathVariable(name = "articleId", required = true) Integer articleId,
	                                   HttpSession session) {
		Article article = this.articleService.getById(articleId);
		if (article == null && article.getState().intValue() != 2) {
			return null;
		}
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		//判断当前用户是否下载过
		int count = this.userDownloadService.getCountByUserIdAAndArticleId(currentUser.getUserId(), articleId);
		if (count == 0) {
			if (!article.isFree()) { //不是免费
				if (currentUser.getPoints() - article.getPoints() < 0) {
					return null;
				}
				currentUser.setPoints(currentUser.getPoints() - article.getPoints());
				userService.save(currentUser);
				//资源分享人获得积分
				User articleUser = article.getUser();
				articleUser.setPoints(articleUser.getPoints() + article.getPoints());
				this.userService.save(articleUser);
			}
			//保存用户下载
			UserDownload userDownload = new UserDownload();
			userDownload.setArticle(article);
			userDownload.setDownloadDate(new Date());
			userDownload.setUser(currentUser);
			this.userDownloadService.save(userDownload);
		}
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("article", article);
		modelAndView.setViewName("/article/downloadPage");
		return modelAndView;
	}

	/**
	 * 判断用户是否是vip
	 */
	@ResponseBody
	@RequestMapping("/isVip")
	public boolean isVip(HttpSession session) {
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		return currentUser.isVip();
	}

	/**
	 * vIP下载资源
	 */
	@RequestMapping("/toVipDownloadPage/{articleId}")
	public ModelAndView toVipDownloadPage(@PathVariable(name = "articleId", required = true) Integer articleId,
	                                      HttpSession session) {
		Article article = this.articleService.getById(articleId);
		if (article == null && article.getState().intValue() != 2) {
			return null;
		}
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		if (!currentUser.isVip()) {
			return null;
		}
		//判断当前用户是否下载过
		int count = this.userDownloadService.getCountByUserIdAAndArticleId(currentUser.getUserId(), articleId);
		if (count == 0) {
			//保存用户下载
			UserDownload userDownload = new UserDownload();
			userDownload.setArticle(article);
			userDownload.setDownloadDate(new Date());
			userDownload.setUser(currentUser);
			this.userDownloadService.save(userDownload);
		}
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("article", article);
		modelAndView.setViewName("/article/downloadPage");
		return modelAndView;
	}

	/**
	 * 进入失效资源页面
	 */
	@RequestMapping("/toUnUsefulArticleManage")
	public String toUnUsefulArticleManage(HttpSession session) {
		this.unUsefulArticleCount(session);
		return "/user/unUsefulArticleManage";
	}

	/**
	 * 用户失效资源数
	 */
	public void unUsefulArticleCount(HttpSession session) {
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		Article s_article = new Article();
		s_article.setUserful(false);
		s_article.setUser(currentUser);
		session.setAttribute(Consts.UN_USEFUL_AERICLE_COUNT, articleService.getcount(s_article, null, null, null));
	}

	/**
	 * 修改失效链接
	 */
	@ResponseBody
	@RequestMapping("/modifyArticleShareLink")
	public Map<String, Object> modifyShareLink(Article article, HttpSession session) throws IOException {
		Map<String, Object> resultMap = new HashMap<>();
		if (CheckShareLinkEnableUtil.check(article.getDownload())) {
			Article oldarticle = articleService.getById(article.getArticleId());
			User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
			if (oldarticle.getUser().getUserId().intValue() == currentUser.getUserId()) {
				oldarticle.setPassword(article.getPassword());
				oldarticle.setDownload(article.getDownload());
				oldarticle.setUserful(true);
				articleService.save(oldarticle);
				resultMap.put("success", true);
				this.unUsefulArticleCount(session);
			} else {
				resultMap.put("success", false);
				resultMap.put("errorInfo", "你不是该资源发布者，无法修改！");
			}
		} else {
			resultMap.put("success", false);
			resultMap.put("errorInfo", "该链接无效，请重新修改!");
		}
		return resultMap;
	}

	/**
	 * 进入已下载资源列表
	 */
	@GetMapping(value = "/toHaveDownloaded/{currentPage}")
	public ModelAndView toHaveDownloaded(@PathVariable(value = "currentPage", required = false) Integer currentPage,
	                                     HttpSession session) {
		this.unUsefulArticleCount(session);
		ModelAndView mav = new ModelAndView();
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);

		Page<UserDownload> userDownloadPage = userDownloadService.list(currentUser.getUserId(), currentPage, Consts
				.PAGE_SIZE, Sort.Direction.DESC, "downloadDate");
		mav.addObject("userDownloadList", userDownloadPage.getContent());

		mav.addObject("pageStr", HTMLUtil.getPagetion("/user/toHaveDownloaded", userDownloadPage.getTotalPages(),
				currentPage,
				"没有下载任何资源"));
		mav.setViewName("user/haveDownloaded");


		return mav;
	}

	/**
	 * 我的消息
	 */
	@GetMapping(value = "/userMessage/{currentPage}")
	public ModelAndView userMessage(@PathVariable(value = "currentPage", required = false) Integer currentPage,
	                                HttpSession session) {
		this.unUsefulArticleCount(session);
		ModelAndView mav = new ModelAndView();
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		//进入页面则认为用户阅读了所有消息
		if (currentUser.getMessageCount() == null || currentUser.getMessageCount() > 0) {
			messageService.updateState(currentUser.getUserId());
			currentUser.setMessageCount(0);
		}
		session.setAttribute(Consts.CURRENT_USER, currentUser);

		Page<Message> messagePage = messageService.list(currentUser.getUserId(), currentPage, Consts.PAGE_SIZE, Sort
				.Direction.ASC, "publishDate");
		mav.addObject("messageList", messagePage.getContent());

		mav.addObject("pageStr", HTMLUtil.getPagetion("/user/userMessage", messagePage.getTotalPages(),
				currentPage,
				"没有任何系统消息"));
		mav.setViewName("user/userMessage");


		return mav;
	}

	/**
	 * 我的主页
	 */
	@GetMapping("/home")
	public ModelAndView home(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		Page<UserDownload> downloadPage = this.userDownloadService.list(currentUser.getUserId(), 1, Consts.PAGE_SIZE,
				Sort.Direction.DESC,
				"downloadDate");
		if (downloadPage.getTotalElements() > 0) {
			modelAndView.addObject("userDownloadList", downloadPage.getContent());
		}
		Page<Message> messagePage = messageService.list(currentUser.getUserId(), 1, Consts.PAGE_SIZE, Sort
				.Direction.ASC, "publishDate");
		if (messagePage.getTotalElements() > 0) {
			modelAndView.addObject("messageList", messagePage.getContent());
		}
		modelAndView.setViewName("/user/home");

		return modelAndView;
	}

	/**
	 * 上传头像
	 */
	@ResponseBody
	@RequestMapping("/updateHeadPortrait")
	public Map<String, Object> updateHeadPortrait(MultipartFile file, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<>();
		if (!file.isEmpty()) {
			//获取文件名
			String fileName = file.getOriginalFilename();
			//获取文件的后缀名
			String suffixName = fileName.substring(fileName.lastIndexOf("."));
			//新文件名
			String newfileName = DateUtil.getCurrentDateStr() + suffixName;
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(imgFilePath + newfileName));
			map.put("success", true);
			map.put("imgName", newfileName);
			User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
			currentUser.setHeadPortrait(newfileName);
			userService.save(currentUser);
			session.setAttribute(Consts.CURRENT_USER, currentUser);
		}

		return map;
	}

	/**
	 * 用户中心
	 */
	@GetMapping("/userCenter")
	public ModelAndView userCenter(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		this.unUsefulArticleCount(session);
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		modelAndView.addObject("currentUser", currentUser);
		modelAndView.setViewName("/user/userCenter");
		return modelAndView;

	}

	/**
	 * 修改基本信息
	 */
	@ResponseBody
	@RequestMapping("/userUpdate")
	public Map<String, Object> userUpdate(User user, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		currentUser.setNickname(user.getNickname());
		currentUser.setSex(user.getSex());
		this.userService.save(currentUser);
		session.setAttribute(Consts.CURRENT_USER, currentUser);
		resultMap.put("success", true);
		return resultMap;
	}

	/**
	 * 修改密码
	 */
	@ResponseBody
	@RequestMapping("/updatePassword")
	public Map<String, Object> updatePassword(String oldPassword, String newPassword, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		if (!currentUser.getPassword().equals(CryptographyUtil.md5
				(oldPassword, CryptographyUtil.SALT))) {
			resultMap.put("success", false);
			resultMap.put("errorInfo", "旧密码与原密码不匹配，请重新输入！");
		} else {
			User oldUser = this.userService.getById(currentUser.getUserId());
			oldUser.setPassword(CryptographyUtil.md5(newPassword, CryptographyUtil.SALT));
			this.userService.save(oldUser);
			session.setAttribute(Consts.CURRENT_USER, oldUser);
			resultMap.put("success", true);
		}
		return resultMap;
	}


}
