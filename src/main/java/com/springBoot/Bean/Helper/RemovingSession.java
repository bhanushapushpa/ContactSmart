package com.springBoot.Bean.Helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class RemovingSession {

	public void removeVerificationMessageFromSession() {
        try {
        	HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");
        } catch (RuntimeException ex) {
           ex.getMessage();
        }
    }
}
