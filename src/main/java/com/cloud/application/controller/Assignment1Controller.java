package com.cloud.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Assignment1Controller{
	@GetMapping(value="/healthz")
	public ResponseEntity<String> getMethodName() {
		return new ResponseEntity<>("The request is successfully received", HttpStatus.OK);

	}
}