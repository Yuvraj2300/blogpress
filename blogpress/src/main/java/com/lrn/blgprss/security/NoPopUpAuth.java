package com.lrn.blgprss.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class NoPopUpAuth implements AuthenticationEntryPoint {

	//ADDING UNAUTH HEADER
	@Override
	public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException authEx)
			throws IOException, ServletException {
		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, authEx.getMessage());

	}
}
