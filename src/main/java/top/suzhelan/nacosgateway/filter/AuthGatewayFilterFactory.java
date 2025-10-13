package top.suzhelan.nacosgateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;
import top.suzhelan.nacosgateway.entity.CommonResult;

import java.nio.charset.StandardCharsets;

/**
 * 命名有严格要求的,我个人更倾向于让服务自己进行鉴权,网关不应该频繁更新或者做消耗性能的事
 * 例如 AuthGatewayFilterFactory,在路由filter的name就只能写Auth
 */
@Component
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(AuthGatewayFilterFactory.class);

    public AuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            //获取请求路径
            String path = request.getPath().value();
            //如果路径被排除 则直接发到微服务
            if (config.isExclude(path)) {
                return chain.filter(exchange);
            }
            // 检查鉴权信息
            String token = request.getHeaders().getFirst("Authorization");
            // 是否过期
            if (isAuthExpired(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                String responseBody = CommonResult.error("鉴权无效").toJSON();
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
                return exchange.getResponse().writeWith(Mono.just(buffer));
            }
            //没有过期 正常请求
            return chain.filter(exchange);
        };
    }

    private boolean isAuthExpired(String token) {
        // 实现具体的鉴权逻辑
        return token == null || !token.startsWith("Bearer ");
    }


    public static class Config {
        private String exclude;

        public String getExclude() {
            return exclude;
        }

        public void setExclude(String exclude) {
            this.exclude = exclude;
        }

        public boolean isExclude(String path) {
            if (this.exclude == null || this.exclude.isEmpty()) {
                return false;
            }
            String[] excludeList = this.exclude.split(",");
            AntPathMatcher pathMatcher = new AntPathMatcher();
            for (String pattern : excludeList) {
                if (pathMatcher.match(pattern, path)) {
                    return true;
                }
            }
            return false;
        }
    }
}

