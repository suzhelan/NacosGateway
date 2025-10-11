package top.suzhelan.nacosgateway.route;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.annotation.NacosConfigListener;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 动态路由监听器
 *
 * @author Chill
 */
@Component
public class NacosDynamicRouteListener implements InitializingBean {

    public static final String NACOS_DATA_ID = "routes";
    public static final String NACOS_GROUP = "DEFAULT_GROUP";
    private final DynamicRouteManager dynamicRouteManager;
    private final NacosConfigManager nacosConfigManager;

    public NacosDynamicRouteListener(DynamicRouteManager dynamicRouteManager, NacosConfigManager nacosConfigManager) {
        this.dynamicRouteManager = dynamicRouteManager;
        this.nacosConfigManager = nacosConfigManager;
    }

    /**
     * 监听Nacos配置更改
     */
    @NacosConfigListener(dataId = NACOS_DATA_ID, group = NACOS_GROUP)
    public void onRouteListener(String routesConfig) {
        //序列化成RouteDefinition对象
        JSONObject configJson = JSON.parseObject(routesConfig);
        JSONArray configList = configJson.getJSONArray("data");
        List<RouteDefinition> routeDefinitions = configList.toJavaList(RouteDefinition.class);
        dynamicRouteManager.updateList(routeDefinitions);
    }

    /**
     * 主动获取
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ConfigService configService = nacosConfigManager.getConfigService();
        String routesConfig = configService.getConfig(NACOS_DATA_ID, NACOS_GROUP, 5000);
        //序列化成RouteDefinition对象
        JSONObject configJson = JSON.parseObject(routesConfig);
        JSONArray configList = configJson.getJSONArray("data");
        List<RouteDefinition> routeDefinitions = configList.toJavaList(RouteDefinition.class);
        dynamicRouteManager.updateList(routeDefinitions);
    }


}
