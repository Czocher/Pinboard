package com.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.User;
import com.example.repositories.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository repository;
	@Autowired
	private HttpSession session;
	@Autowired
	protected AuthenticationManager authenticationManager;
	
	@RequestMapping(value="/user", method = RequestMethod.POST)
	public String login(@RequestParam("login") String login, 
					@RequestParam("password") String password, 
					HttpServletRequest request, 
					Model model){
		User u = repository.findByEmail(login);
		if(u!=null){
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			if(passwordEncoder.matches(password, u.getPassword())){
				UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(login, password);
				request.getSession();
				
				SecurityContext securityContext = SecurityContextHolder.getContext();
				securityContext.setAuthentication(authRequest);
				session = request.getSession(true);
				session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
				session.setAttribute("login", u.getLogin());
				session.setAttribute("password", u.getPassword());
				System.out.println(session.getAttribute("SPRING_SECURITY_CONTEXT"));
				System.out.println(session.getAttribute("password"));
				
				return "redirect:/user/"+u.getLogin();
			}
		}
		return "redirect:/";
	}
	@RequestMapping("/user/{login}")
	public String panel(Model model){
		model.addAttribute("login", session.getAttribute("login"));
		model.addAttribute("password", session.getAttribute("password"));
		System.out.println("dupa");
		return "logged";
	}
}
