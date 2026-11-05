package com.renault.vehicle.analysis.client;

import com.renault.vehicle.analysis.dto.response.RecallResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "recall-provider",
        url = "${recall.provider.url}"
)
public interface RecallProviderClient {

    @GetMapping("/api/recalls/{vin}")
    RecallResponse getRecallInformation(@PathVariable String vin);
}