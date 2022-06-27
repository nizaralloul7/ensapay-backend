package com.ensa.ENSAPAY.controllers;

import com.ensa.ENSAPAY.services.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {
	private final SmsService smsService;

	public SmsController(SmsService smsService) {
		this.smsService = smsService;
	}


	@GetMapping(value = "/sendSMS")
	public ResponseEntity<String> sendSMS() {
		return smsService.sendSMS("+212643164124","This is test for sms");
	}

}