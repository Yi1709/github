package com.myshare.code.controller.admin;

import com.myshare.code.entity.User;
import com.myshare.code.service.UserService;
import com.myshare.code.util.Consts;
import com.myshare.code.util.CryptographyUtil;
import com.myshare.code.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.MergedAnnotationPredicates;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.plugin.cache.OldCacheEntry;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/user")
public class UserAdminController {

	@Autowired
	private UserService userService;

	/**
	 * 根据条件分页查询用户信息
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions(value = "分页查询用户信息")
	public Map<String, Object> list(User s_user, @RequestParam(value = "latelyLoginTime", required = false) String
			latelyLoginTime, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value =
			"pageSize", required = false) Integer pageSize) {
		String s_blatelyLoginTime = null;
		String s_elatelyLoginTime = null;
		if (StringUtil.notEmpty(latelyLoginTime)) {
			String[] latelyLoginTimeStr = latelyLoginTime.split(" - ");
			s_blatelyLoginTime = latelyLoginTimeStr[0];
			s_elatelyLoginTime = latelyLoginTimeStr[1];
		}
		Map<String, Object> map = new HashMap<>();
		map.put("data", this.userService.list(s_user, s_blatelyLoginTime, s_elatelyLoginTime, page, pageSize,
				Sort.Direction.DESC, "registrationDate"));
		map.put("total", userService.getCount(s_user, s_blatelyLoginTime, s_elatelyLoginTime));
		map.put("errorNo", 0);
		return map;
	}

	@ResponseBody
	@RequestMapping("/updateVipState")
	@RequiresPermissions(value = "修改用户vip状态")
	public Map<String, Object> updateVipState(Integer userId, boolean isVip) {
		User olduser = this.userService.getById(userId);
		olduser.setVip(isVip);
		userService.save(olduser);
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		return map;
	}

	@ResponseBody
	@RequestMapping("/updateUserState")
	@RequiresPermissions(value = "修改用户状态")
	public Map<String, Object> updateUserState(Integer userId, boolean isOff) {
		User olduser = this.userService.getById(userId);
		olduser.setOff(isOff);
		userService.save(olduser);
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		return map;

	}

	/**
	 * 重置用户密码
	 */
	@ResponseBody
	@RequestMapping("/resetPassword")
	@RequiresPermissions(value = "重置用户密码")
	public Map<String, Object> resetPassword(Integer userId) {
		User olduser = this.userService.getById(userId);
		olduser.setPassword(CryptographyUtil.md5("123456", CryptographyUtil.SALT));
		userService.save(olduser);
		Map<String, Object> map = new HashMap<>();
		map.put("errorNo", 0);
		return map;

	}

	@ResponseBody
	@RequestMapping("/updateVipGrade")
	@RequiresPermissions(value = "用户VIP等级修改")
	public Map<String, Object> updateVipGrade(User user) {
		User olduser = this.userService.getById(user.getUserId());
		olduser.setVipGrade(user.getVipGrade());
		userService.save(olduser);
		Map<String, Object> map = new HashMap<>();
		map.put("errorNo", 0);
		return map;

	}

	@ResponseBody
	@RequestMapping("/addPoints")
	@RequiresPermissions(value = "充值用户积分")
	public Map<String, Object> addPoints(User user) {
		User olduser = this.userService.getById(user.getUserId());
		olduser.setPoints(olduser.getPoints() + user.getPoints());
		userService.save(olduser);
		Map<String, Object> map = new HashMap<>();
		map.put("errorNo", 0);
		return map;

	}


	@ResponseBody
	@RequestMapping("/modifyPassword")
	@RequiresPermissions(value = "管理员修改个人密码")
	public Map<String, Object> modifyPassword(String oldPassword, String newPassword, HttpSession session) {
		User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
		Map<String, Object> map = new HashMap<>();
		if (!currentUser.getPassword().equals(CryptographyUtil.md5(oldPassword, CryptographyUtil.SALT))) {
			map.put("success", false);
			map.put("errorInfo", "原密码错误，请重新输入");
			return map;
		}
		User oldUser = this.userService.getById(currentUser.getUserId());
		oldUser.setPassword(CryptographyUtil.md5(newPassword, CryptographyUtil.SALT));
		this.userService.save(oldUser);
		map.put("success", true);
		return map;
	}


	@RequestMapping("/logout")
	@RequiresPermissions(value = "安全退出")
	public String logout(){
		SecurityUtils.getSubject().logout();
		return "redirect:/admin/login.html";
	}

}
