package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	Assignment1Controller assignment1Controller;
	
	@Test
	void test() {
	assignment1Controller = new Assignment1Controller();
	ResponseEntity<String> responseInvoiceResponseEntity = assignment1Controller.getMethodName() ;
	assertEquals("400",responseInvoiceResponseEntity.getStatusCode().toString());
	}

}
