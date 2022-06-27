package com.ensa.ENSAPAY.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    private static final String TWILIO_ACCOUNT_SID = "AC2f7e4fc190d36ce96f85d5ab0e653e94";
    private static final String TWILIO_AUTH_TOKEN = "8e813cd52c217cce68bba0ec29f23fe9";
    public  ResponseEntity<String> sendSMS(String toNumber,String content){
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);

        Message.creator(new PhoneNumber(toNumber),
                new PhoneNumber("+17754426942"), content).create();
        System.out.println("SMS sent to: "+ toNumber + ", Content: "+ content);
        return new ResponseEntity<String>("Message sent successfully", HttpStatus.OK);
    }
}
