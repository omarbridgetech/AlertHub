package com.mst.smsms.service;


import com.mst.smsms.model.SmsModel;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
public class SmsService {

    @Value("${TWILIO_ACCOUNT_SID}")
     String ACCOUNT_SID;

    @Value("${TWILIO_AUTH_TOKEN}")
    String AUTH_TOKEN;

    @Value("${TWILIO_OUTGOING_SMS_NUMBER}")
    String OUTGOING_SMS_NUMBER;


        public SmsService() {


        }

        @PostConstruct
        private void setup(){
            log.info("Creating class :SmsService");
            //log.info("ACCOUNT_SID:"+ACCOUNT_SID);
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        }




    public String sendSms(String smsNumber,String smsMessage) {
        log.info("sendSms started sendRequst: ");

        Message message =Message.creator(
               new PhoneNumber(smsNumber),
               new PhoneNumber(OUTGOING_SMS_NUMBER),smsMessage
        ).create();
        return message.getStatus().toString();
    }
}
