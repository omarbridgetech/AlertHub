package com.mst.processorms.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loaderClient", url = "http://localhost:8083/loader_api")
public interface LoaderClient {

    @GetMapping("/checkLabelThreshold")
    boolean checkLabelThreshold(@RequestParam String ownerId,
                                @RequestParam String label,
                                @RequestParam int hours,
                                @RequestParam int threshold);
}