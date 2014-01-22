package com.ws.web.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ws.web.action.NewSMSUtil.Status;

public class TextCodeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		try {
			String phone = request.getParameter("phone");
			String code = String.valueOf(NewSMSUtil.createCode());
			Status temp = NewSMSUtil.send(phone, NewSMSUtil.TIP_NEW_MEMBER.replace("{code}", code).replace("{name}", "饭超人"));
			if(!temp.isClose() && !temp.isSuccess()){
				System.out.println("警告: 新会员注册信息短信发送失败, 远程接口响应异常.");
			}
			System.out.println("响应结果:  " + temp.getMsg());
			response.getWriter().print(temp.getMsg() + "验证码:" + code);
		} catch (Exception e) {
			System.out.println("异常: 新会员注册信息短信发送失败, 程序运行异常.");
			e.printStackTrace();
			response.getWriter().print("短信发送失败, 系统异常.");
		}
	}

	
}
