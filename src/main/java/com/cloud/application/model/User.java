package com.cloud.application.model;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="User")
public class User implements UserDetails{
	@Id
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
	@Column(name = "id", unique = true, nullable = false, updatable = false)
	private String id;

	@NotEmpty @NotNull(message="First name cannot be missing or empty")
	@Column(name = "first_name")
	private String first_name;

	@NotEmpty @NotNull(message="Last name cannot be missing or empty")
	@Column(name = "last_name")
	private String last_name;

	@NotEmpty @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "password")
	private String password;
	
	@Email(message="Email not valid") @NotEmpty
	@Column(name = "username", unique = true, nullable = false)
    @NotNull(message="Email cannot be missing or empty")
    private String  username;
    
	@CreationTimestamp
	@Column(name = "account_created")
	private LocalDateTime accountCreated;

	@UpdateTimestamp
	@Column(name = "account_updated", nullable = false)
	private LocalDateTime accountUpdated;
    
//    private boolean verified;
//    private String verified_on;

	public User() {

	}

	public User(String first_name, String last_name, String password, String username) {
        this.id = UUID.randomUUID().toString();
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.username = username;
//        this.account_created = OffsetDateTime.now(Clock.systemUTC()).toString();
//        this.account_updated = OffsetDateTime.now(Clock.systemUTC()).toString();
//        this.verified = false;
//        this.verified_on = "";//OffsetDateTime.now(Clock.systemUTC()).toString();
//    }
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	public LocalDateTime getAccountCreated() {
		return accountCreated;
	}

	public LocalDateTime getAccountUpdated() {
		return accountUpdated;
	}

	@Override
	public boolean isAccountNonExpired() {
	    return true;
	}

	@Override
	public boolean isAccountNonLocked() {
	    return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	@Override
	public boolean isEnabled() {
	    return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
	    authorities.add(new SimpleGrantedAuthority("USER"));
	    return authorities;
	}


	
	
}
