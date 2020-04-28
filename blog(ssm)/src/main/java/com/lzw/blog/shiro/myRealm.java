package com.lzw.blog.shiro;

import com.lzw.blog.entity.Blogger;
import com.lzw.blog.service.BloggerService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/11:12
 * @Description:
 */
public class myRealm extends AuthorizingRealm {

	@Resource
	private BloggerService bloggerService;

	/*获取后台权限*/
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

	/** 登录验证 **/
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//从令牌中获取信息
		String userName = (String) token.getPrincipal();
		//shiro验证信息
		Blogger blogger = bloggerService.getByUserName(userName);
		if (blogger != null) {
			SecurityUtils.getSubject().getSession().setAttribute("currentUser", blogger);
			AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(blogger.getUserName(),
					blogger.getPassword(), getName());
			return authenticationInfo;
		}
		return null;
	}
}
