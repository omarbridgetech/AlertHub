package com.mst.loogerms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogRequest {

    private String serviceName;

    private String logLevel;

    private String message;
}
