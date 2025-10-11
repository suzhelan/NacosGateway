package top.suzhelan.nacosgateway.route;

import jakarta.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 动态路由操作类
 */
@RestController
@Component
public class DynamicRouteManager implements ApplicationEventPublisherAware {

    private static final Logger logger = LogManager.getLogger(DynamicRouteManager.class);
    private final RouteDefinitionWriter routeDefinitionWriter;
    private final RouteDefinitionRepository routeDefinitionRepository;
    private ApplicationEventPublisher publisher;

    public DynamicRouteManager(RouteDefinitionWriter routeDefinitionWriter, RouteDefinitionRepository routeDefinitionRepository) {
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.routeDefinitionRepository = routeDefinitionRepository;
    }

    @Override
    public void setApplicationEventPublisher(@Nonnull ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 增加路由
     */
    public void add(RouteDefinition definition) {
        logger.info("添加路由:{}", definition.toString());
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 判断路由是否存在
     *
     * @param routeId 路由ID
     * @return true:存在 false:不存在
     */

    public boolean isRouteDefinitionExistsById(String routeId) {
        return routeDefinitionRepository.getRouteDefinitions()
                .any(routeDefinition -> routeDefinition.getId().equals(routeId))
                .blockOptional().orElse( false);
    }

    /**
     * 更新路由
     */
    public void update(RouteDefinition definition) {
        logger.info("更新路由:{}", definition.toString());
        //判断是否有该路由,有才删除
        if (isRouteDefinitionExistsById(definition.getId())) {
            routeDefinitionWriter.delete(Mono.just(definition.getId())).block();
        }
        routeDefinitionWriter.save(Mono.just(definition)).block();
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 批量更新路由
     */
    public void updateList(List<RouteDefinition> routeDefinitions) {
        routeDefinitions.forEach(this::update);
    }

    /**
     * 删除路由
     */
    public void delete(String id) {
        logger.info("删除路由:{}", id);
        routeDefinitionWriter.delete(Mono.just(id)).block();
    }


    @GetMapping("/route")
    public Mono<List<RouteDefinition>> getRouteDefinitions() {
        return routeDefinitionRepository.getRouteDefinitions().collectList();
    }


}
