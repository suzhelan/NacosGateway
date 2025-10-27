package top.suzhelan.nacosgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.suzhelan.nacosgateway.entity.CommonResult;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/actuator/health")
    public CommonResult<Map<String, Object>> health() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("service", "NacosGateway");
        
        Map<String, Object> details = new HashMap<>();
        details.put("timestamp", System.currentTimeMillis());
        healthInfo.put("details", details);
        
        return CommonResult.success(healthInfo);
    }
}