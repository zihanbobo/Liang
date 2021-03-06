package com.we.common.utils;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.util.Properties;

public class MailUtil {
	public static void main(String[] args) {
		// javamail 发邮件
		MailUtil.sendMailByJava("810522154@qq.com", "您的密码",
				"<htm><body>您的密码为:<a href=\"http://candy.club\">123456</a></body></html>",Type.HTML);
		MailUtil.sendMailByApache("810522154@qq.com", "apache",
				"<htm><body>您的密码为:<a href=\\\"http://candy.club\\\">123456</a></body></html>");

		// apache email 发邮件
		// MailUtil.send();

	}

	public static final String smtphost = "smtp.exmail.qq.com"; // 发送邮件服务器
	public static final String user = "service@candy.club"; // 邮件服务器登录用户名
	public static final String password = "Ser123654"; // 邮件服务器登录密码
	public static final String from = "service@candy.club"; // 发送人邮件地址
	public static final String port = "465";
	
	public enum Type{
		TEXT,HTML
	}

	/**
	 * 发送电子邮件
	 * 
	 * @param to
	 *            接收者的email地址
	 * @param subject
	 *            eamil标题
	 * @param body
	 *            email内容
	 * @return
	 */
	public static boolean sendMailByJava(String to, String subject, String body,Type type) {

		// 以下为发送程序,用户无需改动
		try {
			System.out.println("eamil:" + to);
			Properties props = new Properties();
			props.put("mail.smtp.host", smtphost);
			props.put("mail.smtp.auth", "true");
			// if (smtphost.indexOf("smtp.gmail.com") >= 0) {
			props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.port", port);
			props.setProperty("mail.smtp.socketFactory.port", port);
			// }
			javax.mail.Session ssn = javax.mail.Session.getInstance(props, null);
			MimeMessage message = new MimeMessage(ssn);
			InternetAddress fromAddress = new InternetAddress(from);
			message.setFrom(fromAddress);
			InternetAddress toAddress = new InternetAddress(to);
			message.addRecipient(Message.RecipientType.TO, toAddress);
			message.setSubject(subject);
			if(type.equals(Type.TEXT)) {
				message.setText(body);
			}else {
				Multipart mainPart = new MimeMultipart();
		        // 创建一个包含HTML内容的MimeBodyPart
		        BodyPart html = new MimeBodyPart();
		        html.setContent(body, "text/html; charset=utf-8");
		        mainPart.addBodyPart(html);
		        message.setContent(mainPart);
			}
			Transport transport = ssn.getTransport("smtp");
			transport.connect(smtphost, user, password);
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
			return true;
		} catch (Exception m) {
			m.printStackTrace();
		}
		return false;
	}

	// // apache email
	public static boolean sendMailByApache(String to, String subject, String msg) {
		SimpleEmail email = new SimpleEmail();

		try {
			// email.setTLS(true); //是否TLS校验,,某些邮箱需要TLS安全校验,同理有SSL校验
			email.setSSL(true);
			email.setHostName(smtphost);
			email.setAuthentication(user, password); // 用户名和密码
			email.setSslSmtpPort(port);
			email.setFrom(from, password);
			email.setCharset("utf8");
			email.addTo(to); // 接收方
			// email.addCc(" aliyunzixun@xxx.com"); //抄送方
			// email.addBcc(" aliyunzixun@xxx.com"); //秘密抄送方
			email.setSubject(subject); // 标题
			//email.setMsg(msg); // 内容
			email.setContent(msg, "text/html; charset=utf-8");
			email.send();
			return true;
		} catch (EmailException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// private JavaMailSenderImpl mailSender;
	// private Properties p;
	// private int timeout = 25000;
	// private String subject = "Grade·Inform";
	// private StringBuilder content;
	//
	// public MailUtil(ArrayList<Grade> arrayList){
	// mailSender = new JavaMailSenderImpl();
	// p = new Properties();
	// content = new StringBuilder();
	// initMail(mailSender,p,arrayList);
	// }
	//
	// private void initMail(JavaMailSenderImpl mailSender, Properties
	// p,ArrayList<Grade> arrayList){
	// // 设置参数
	// mailSender.setHost("smtp.163.com");
	// mailSender.setUsername("xxx@163.com"); //你的邮箱
	// mailSender.setPassword("xxx"); //密码，这里必须先在网易邮箱里开启smtp服务
	// //设置property
	// p.put("userName","jobbridge@163.com");
	// p.setProperty("mail.smtp.timeout",timeout+"");
	// p.setProperty("mail.smtp.auth","true");
	// mailSender.setJavaMailProperties(p);
	// //设置内容格式
	// content.append("<body>");
	// content.append("<span style=\"font-size:15px\">Hi. This is the New
	// Grade·Inform.</span>");
	// content.append("<hr>");
	// //简单版本
	// /*String text;
	// for (Grade grade:arrayList) {
	// text = "";
	// text += grade.toString();
	// content.append(text);
	// content.append("<br>");
	// }*/
	// //表格版本
	// content.append("<table>");
	// content.append("<thead>");
	// content.append("<tr>");
	// content.append("<th>课程名</th>");
	// content.append("<th>分数</th>");
	// content.append("<th>绩点</th>");
	// content.append("<th>学分</th>");
	// content.append("<th>属性</th>");
	// content.append("</tr>");
	// content.append("</thead>");
	// content.append("<tbody>");
	// for (Grade grade: arrayList) {
	// content.append("<tr>");
	// content.append("<td
	// style=\"text-align:center\">"+grade.getCourseName()+"</td>");
	// content.append("<td>"+grade.getScore()+"</td>");
	// content.append("<td>"+grade.getGpa()+"</td>");
	// content.append("<td>"+grade.getCredit()+"</td>");
	// content.append("<td>"+grade.getAttribute()+"</td>");
	// content.append("</tr>");
	// }
	// content.append("</tbody>");
	// content.append("</table>");
	//
	// content.append("<hr>");
	// content.append("<span style=\"font-size:17px\">Thank you! </span></body>");
	// }
	//
	// /**
	// *
	// * @param recipient 收件人
	// *
	// *
	// */
	//
	// public void send(String recipient) throws MessagingException {
	// // 构建简单邮件对象，见名知意
	// MimeMessage msg = mailSender.createMimeMessage();
	// // *** 关键 ***
	// msg.addRecipients(MimeMessage.RecipientType.CC,
	// InternetAddress.parse(p.getProperty("userName")));
	// MimeMessageHelper helper = new MimeMessageHelper(msg,true,"utf-8");
	// // 设定邮件参数
	// helper.setFrom(mailSender.getUsername());
	// helper.setTo(recipient);
	// helper.setSubject(subject);
	// helper.setText(content.toString(),true);
	// System.out.println(">>> content: "+content);
	// // 发送邮件
	// mailSender.send(msg);
	// }
}