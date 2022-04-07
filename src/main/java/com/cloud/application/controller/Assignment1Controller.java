package com.cloud.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timgroup.statsd.StatsDClient;

@RestController
public class Assignment1Controller{

	@Autowired
    private StatsDClient statsd;
	
	@GetMapping(value="/health")
	public ResponseEntity<String> getMethodName() {
       
		statsd.increment("Calls - Get Healthz");
        long start = System.currentTimeMillis();
		statsd.recordExecutionTime("DB Response Time - Get user/self", System.currentTimeMillis() - start);

		return new ResponseEntity<>("Hello", HttpStatus.OK);
		

	}
}
