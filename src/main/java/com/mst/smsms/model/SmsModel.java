package com.mst.smsms.model;


import lombok.Data;

@Data
public class SmsModel {
    private String destinationSmsNumber;

    private String smsMessage;

}
