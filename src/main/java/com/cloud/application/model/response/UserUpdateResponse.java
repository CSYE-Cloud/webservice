package com.cloud.application.model.response;

public class UserUpdateResponse {
	private String id;
	private String firstName;
	private String lastName;
	private String account_created;
	private String account_updated;
	private String username;
	private boolean verified;
	private String verified_on;
	
	public String getVerified_on() {
		return verified_on;
	}
	public void setVerified_on(String verified_on) {
		this.verified_on = verified_on;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount_created() {
		return account_created;
	}
	public void setAccount_created(String account_created) {
		this.account_created = account_created;
	}
	public String getAccount_updated() {
		return account_updated;
	}
	public void setAccount_updated(String account_updated) {
		this.account_updated = account_updated;
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

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
