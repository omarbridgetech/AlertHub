package com.mst.smsms.service;


import com.mst.smsms.client.LoggerClient;
import com.mst.smsms.model.SmsModel;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private LoggerClient loggerClient;


        public SmsService() {


        }

        @PostConstruct
        private void setup(){
            log.info("Creating class :SmsService");
            //log.info("ACCOUNT_SID:"+ACCOUNT_SID);
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            loggerClient.info("SmsService initialized successfully");

        }




    public String sendSms(String smsNumber,String smsMessage) {
        log.info("sendSms started for: {}", smsNumber);

        try {
            Message message = Message.creator(
                    new PhoneNumber(smsNumber),
                    new PhoneNumber(OUTGOING_SMS_NUMBER), smsMessage
            ).create();
            String status = message.getStatus().toString();

            log.info("SMS sent successfully to: {} with status: {}", smsNumber, status);
            loggerClient.info("SMS sent successfully to: " + smsNumber + " with status: " + status);

            return status;
        }catch (Exception e){
            // Log error to Logger microservice
            log.error("Failed to send SMS to {}: {}", smsNumber, e.getMessage());
            loggerClient.error("Failed to send SMS to " + smsNumber + ": " + e.getMessage());
            throw e;
        }
    }
}
