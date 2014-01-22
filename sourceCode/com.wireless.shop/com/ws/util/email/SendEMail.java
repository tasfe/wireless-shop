package com.ws.util.email;

public final class SendEMail {
	private SendEMail(){}
	
	private static MailSenderInfo mailInfo;
	private static SimpleMailSender sms;
	static{
		mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.qq.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("87874344@qq.com");
		mailInfo.setPassword("feng0107");
		mailInfo.setFromAddress("87874344@qq.com");
		sms = new SimpleMailSender();
	}
	/**
	 * 发送普通文本邮件
	 * @param toAddress
	 * @param title
	 * @param text
	 */
	public static synchronized boolean SendTextMail(String toAddress, String title, String text){
		mailInfo.setToAddress(toAddress);
		mailInfo.setSubject(title);
		mailInfo.setContent(text);
		return sms.sendTextMail(mailInfo);
	}
	/**
	 * 发送HTML格式内容邮件
	 * @param toAddress
	 * @param title
	 * @param html
	 * @return
	 */
	public static synchronized boolean SendHtmlMail(String toAddress, String title, String html){
		mailInfo.setToAddress(toAddress);
		mailInfo.setSubject(title);
		mailInfo.setContent(html);
		return sms.sendHtmlMail(mailInfo);
	}
	
	public static void main(String[] args){
//		//这个类主要是设置邮件
//		MailSenderInfo mailInfo = new MailSenderInfo();    
//		mailInfo.setMailServerHost("smtp.qq.com");    
//		mailInfo.setMailServerPort("25");    
//		mailInfo.setValidate(true);    
//		mailInfo.setUserName("87874344@qq.com");    
//		mailInfo.setPassword("feng0107");//邮箱密码    
//		mailInfo.setFromAddress("87874344@qq.com");    
//		mailInfo.setToAddress("1030021@qq.com");    
//		mailInfo.setSubject("test title22222");    
//		mailInfo.setContent("邮件发送测试22222................");
//		//这个类主要来发送邮件   
//		SimpleMailSender sms = new SimpleMailSender();
//		sms.sendTextMail(mailInfo);//发送文体格式 
//		mailInfo.setContent("<font color=\"red\">html->22222邮件发送测试................</font>");
//		sms.sendHtmlMail(mailInfo);//发送html格式 
		
		for(int i = 0; i < 3; i++){
			final int random = (int)(Math.random() * 100);
			System.out.println("random: " + random);
			if(random % 2 == 0){
				new Thread(new Runnable() {
					public void run() {
						SendEMail.SendHtmlMail("1030021@qq.com", "title:"+random, "this is html content: "+random*random);
					}
				}).start();
			}else{
				new Thread(new Runnable() {
					public void run() {
						SendEMail.SendTextMail("1030021@qq.com", "title:"+random, "this is text content: "+random*random);
					}
				}).start();
			}
			
		}
   } 
}
