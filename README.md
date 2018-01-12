# 工程设计初衷
在学习dubbo过程中，需要对dubbo使用场景以及核心解决的问题有充分的了解，才能深入学习dubbo架构
# 案例业务简介
模拟一个典型电商交易业务场景的简化版本，会涉及到下单，以及系统之间依赖关系管理。
# 案例采用框架
spring mvc + dubbo + mybatis结构<br/>
DB采用mysql


## 启动时检查
```java
	@Reference(check=false)//启动检查关闭，该为测试时使用，服务启动时默认用户服务已经存在 否则会报错
	UserService userService;
```
默认应该开启，这样可以保证服务在启动时会检查注册中心是否有userService的服务，若无则直接报错

## 错误重试机制
```java
@Reference(check=false,retries=2)//retries 失败重试2次
UserService userService;
```

也可以用cluster定义自己的[容错机制](https://dubbo.gitbooks.io/dubbo-dev-book/impls/cluster.html)<br/>
<dubbo:service cluster="failsafe" /> 快速失败<br/>
cluster="forking" 并行处理，只要有一个成功则成功<br/>
## 负载均衡
loadbalance="roundrobin" 基于service 和 reference都可以
## 线程模型
```xml
<dubbo:protocol name="dubbo" dispatcher="all" threadpool="fixed" threads="100" />
```

    all 所有消息都派发到线程池，包括请求，响应，连接事件，断开事件，心跳等。
    direct 所有消息都不派发到线程池，全部在 IO 线程上直接执行。
    message 只有请求响应消息派发到线程池，其它连接断开事件，心跳等消息，直接在 IO 线程上执行。
    execution 只请求消息派发到线程池，不含响应，响应和其它连接断开事件，心跳等消息，直接在 IO 线程上执行。
    connection 在 IO 线程上，将连接断开事件放入队列，有序逐个执行，其它消息派发到线程池。
## 直连提供者
 <dubbo:reference id="xxxService" interface="com.alibaba.xxx.XxxService" url="dubbo://localhost:20890" />

## 禁用注册
<dubbo:registry address="10.20.153.10:9090" register="false" />
主要用于测试，为了让服务调用方能够不掉用该服务
## 不订阅
<dubbo:registry id="qdRegistry" address="10.20.141.150:9090" subscribe="false" />

