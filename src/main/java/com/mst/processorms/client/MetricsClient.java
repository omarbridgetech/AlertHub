package com.mst.processorms.client;



import com.mst.processorms.dto.MetricResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "metrics-service", url = "http://localhost:8085")
public interface MetricsClient {

    @GetMapping("/api/metrics/{metricId}")
    MetricResponseDTO getMetricDetails(@PathVariable("metricId") String metricId);
}
