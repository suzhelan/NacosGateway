# Nacos API Gateway API动态网关实战

Nacos搭建Spring Cloud Gateway完成微服务 高性能架构搭建实战


### 实现的功能  
- API网关统一接口,根据路径转发到对应的微服务
- 负载均衡,将请求按照指定策略转发到对应的微服务
- 限流,访问次数限制,例如同IP每秒最多只访问一次
- 鉴权,在网关进行权限验证,无需将鉴权逻辑交给微服务,省去重复造轮子
- 动态路由,在Nacos配置中心修改路由规则,无需重启即可完成路由更新


可采用nacos集群或单机模式部署,推荐使用docker部署  
nacos在[docker](./docker)文件夹启动  
网关(本项目的docker)在[libs](./libs)启动 也可以直接运行本项目  

已经写好docker-compose.yml文件,直接运行后将route.json文件添加到nacos配置中心即可(项目启动会自动创建)

在运行好访问 http://localhost:8992/api/user/sss 即可查看鉴权拦截效果  
在运行后访问 http://localhost:8992/api/user/login 即可查看鉴权排除路由效果