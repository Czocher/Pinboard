package com.example.model;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private int active;

	private String email;

	private String login;

	private String password;

	@Column(name="reg_date")
	private Timestamp regDate;

	//bi-directional many-to-one association to Log
	@OneToMany(mappedBy="user")
	private List<Log> logs;

	public User() {
	}
	  
	 public User(User user) {
	         this.id = user.getId();
	         this.login = user.getLogin();
	         this.email = user.getEmail();       
	         this.password = user.getPassword();
	         this.active=user.getActive();
	         this.regDate=user.getRegDate();
	 }

	public User(String email, String password, String login) {
		this.setEmail(email);
		this.setLogin(login);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		this.setPassword(passwordEncoder.encode(password));
		this.setActive(0);
		java.util.Date date= new java.util.Date();
		this.setRegDate(new Timestamp(date.getTime()));
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getActive() {
		return this.active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getRegDate() {
		return this.regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public List<Log> getLogs() {
		return this.logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	public Log addLog(Log log) {
		getLogs().add(log);
		log.setUser(this);

		return log;
	}

	public Log removeLog(Log log) {
		getLogs().remove(log);
		log.setUser(null);

		return log;
	}

}