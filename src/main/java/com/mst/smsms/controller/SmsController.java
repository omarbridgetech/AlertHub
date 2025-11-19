package com.mst.smsms.controller;


import com.mst.smsms.model.SmsModel;
import com.mst.smsms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/notifications/sms")
@Slf4j
public class SmsController {

    @Autowired
    SmsService smsService;
    @PostMapping("/sendSms")
    public String sendSmsMessage(@RequestBody SmsModel smsModel) {


        log.info("sendSms started sendRequst: "+smsModel.toString());
        return smsService.sendSms(smsModel.getDestinationSmsNumber(),smsModel.getSmsMessage());

    }
}
