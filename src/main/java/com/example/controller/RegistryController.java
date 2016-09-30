package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.MailMail;
import com.example.model.User;
import com.example.repositories.UserRepository;

@Controller
public class RegistryController {
	@Autowired
	private UserRepository repository;
	private ApplicationContext context;
	
	@RequestMapping("/registry")
	public String registryForm(Model model){
		return "registryForm";
	}
	@RequestMapping(value="/registry/create", method = RequestMethod.POST)
	public String create_user(@RequestParam("email") String email,  
							@RequestParam("login") String login, 
							@RequestParam("password") String password, 
							@RequestParam("rep_password") String password2, 
							Model model){
		if(repository.findByEmail(email)==null){
			if(password.equals(password2)){
				User user = new User(email, password, login);
				repository.save(user);
			    context = new ClassPathXmlApplicationContext("Spring-Mail.xml");
			    MailMail mm = (MailMail) context.getBean("mailMail");
			    mm.sendMail("giny21@gmail.com", user.getEmail(), "Pin Board - Account Activation",
			        		 	 "Hello <br> This is your activation email for Pin Board platform <br>"
			        		 	 + "Click this link: <a href=''>link</a>"); //zmienic na link!
			}
			else return "redirect:/registry?err=-2";
		}
		else return "redirect:/registry?err=-1";
		return "redirect:/?u=1";
	}
}
