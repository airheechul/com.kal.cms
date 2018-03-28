package com.easycompany.cmm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.kal.cms.admin.vo.AdminInfo;

// import com.easycompany.cmm.vo.UserVO;



public class AuthenticInterceptor extends HandlerInterceptorAdapter {
	
	/**
	 * 세션에 계정정보(Account)가 있는지 여부로 인증 여부를 체크한다.
	 * 계정정보(Account)가 없다면, 로그인 페이지로 이동한다.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {		
		
//		AdminInfo adminInfo = (AdminInfo)WebUtils.getSessionAttribute(request, "UserAccount");
//
//        if (adminInfo != null) {
//        	return true;
//        } else {
//        	System.out.println("redirect when login session is not exist");
//        	
//			ModelAndView modelAndView = new ModelAndView("redirect:/login/login.do");
//			throw new ModelAndViewDefiningException(modelAndView);
//        }
		boolean isPermittedURL = false;

		AdminInfo adminInfo = (AdminInfo)WebUtils.getSessionAttribute(request, "UserAccount");

		if(adminInfo != null){
System.out.println("AuthenticInterceptor authorized");  			
			return true;
		} else if(!isPermittedURL){
System.out.println("AuthenticInterceptor not authorized --> isPermittedURL");
			
			ModelAndView modelAndView = new ModelAndView("redirect:/login/login.do");
			throw new ModelAndViewDefiningException(modelAndView);
		} else {
System.out.println("AuthenticInterceptor not authorized --> isPermittedURL");
			return true;
		}


	}

}
