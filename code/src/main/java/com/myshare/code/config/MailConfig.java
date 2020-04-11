package com.myshare.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * 邮件配置类
 */
@Configuration
public class MailConfig {
	/**
	 * 获取邮件发送实例
	 */
	@Bean
	public MailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.qq.com"); //指定发送邮件的服务器主机名
		mailSender.setPort(587);        //默认端口
		//ovuhfgwpcfzqdcdj
		mailSender.setUsername("weiwspring@qq.com");
		mailSender.setPassword("ovuhfgwpcfzqdcdj");
		return mailSender;
	}
}
