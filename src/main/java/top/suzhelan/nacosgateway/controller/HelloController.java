package top.suzhelan.nacosgateway.controller;


import com.alibaba.cloud.nacos.annotation.NacosConfig;
import com.alibaba.cloud.nacos.annotation.NacosConfigListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @NacosConfig(group = "DEFAULT_GROUP", dataId = "demo", key = "date")
    public String date;

    @RequestMapping("/")
    public String hello() {
        return "今天是:" + date;
    }

    @NacosConfigListener(dataId = "demo", group = "DEFAULT_GROUP")
    public void rate(String rateConfig) {
        System.out.println("receiveRateConfig:" + rateConfig);
    }
}

