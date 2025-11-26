package top.suzhelan.nacosgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.suzhelan.nacosgateway.entity.CommonResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查，网关自身也是微服务，所以需要提供健康检查接口
 */
@RestController
public class HealthController {

    /**
     * 健康检查接口 能访问得通就行
     * @return CommonResult
     */
    @GetMapping("/actuator/health")
    public Map<String, Object> health() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("service", "NacosGateway");
        Map<String, Object> details = new HashMap<>();
        details.put("timestamp", System.currentTimeMillis());
        healthInfo.put("details", details);
        return healthInfo;
    }
}