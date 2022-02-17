package com.cloud.application.model.response;

import java.time.LocalDateTime;

public class UserRegistrationResponse {

	private String id;
	private String firstName;
	private String lastName;
	private LocalDateTime account_created;
	private LocalDateTime account_updated;
	private String username;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public LocalDateTime getAccount_created() {
		return account_created;
	}
	public void setAccount_created(LocalDateTime account_created) {
		this.account_created = account_created;
	}
	public LocalDateTime getAccount_updated() {
		return account_updated;
	}
	public void setAccount_updated(LocalDateTime account_updated) {
		this.account_updated = account_updated;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
