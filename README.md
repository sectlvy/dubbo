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
## 静态服务
<dubbo:registry address="10.20.141.150:9090" dynamic="false" />
需要手动上线的服务，服务提供者初次注册时为禁用状态，需人工启用。断线时，将不会被自动删除，需人工禁用
## 多协议
这个比较复杂 后续研究
有RMI DUBBO HESSION
## 多注册中心
<dubbo:registry id="hangzhouRegistry" address="10.20.141.150:9090" />
## 合并结果扩展
将多个result合并 <dubbo:method merger="xxx" /> 应用场景 待确定<br/>
```xml
 <dubbo:reference id="userService" group="*" interface="com.patty.dubbo.api.service.UserService"
                     timeout="10000" retries="3" mock="true">
        <dubbo:method name="findAllUsers" merger="myMerger">
        </dubbo:method>
 </dubbo:reference>
```
可以有不同的group,比如有2个，然后将查询到的user进行合并
## 可用组
<dubbo:reference id="barService" interface="com.foo.BarService" group="*" />
2.2.0 以上版本支持，总是只调一个可用组的实现
## 多版本
```xml
<dubbo:service interface="com.foo.BarService" version="2.0.0" />
<dubbo:reference id="barService" interface="com.foo.BarService" version="2.0.0" />
```
## 分组聚合
```xml
<dubbo:reference interface="com.xxx.MenuService" group="*" merger="true" />
<dubbo:reference interface="com.xxx.MenuService" group="*">
    <dubbo:method name="getMenuItems" merger="true" />
</dubbo:service>
```
## validator
maven 依赖validation-api 和 hibernate-validator
参考com.lkl.dcloud.trade.service.OrderService
```java
		SpOrderCountVo spOrderCountVo = new SpOrderCountVo();
		spOrderCountVo.setOrderNo(spOrder.getOrderNo());//若注释掉则会报 ConstraintViolationImpl{interpolatedMessage='订单号不能为空',
		spOrderCountVo.setUserId(spUserVo.getUserId());
		userCountService.addOrderCount(spOrderCountVo);
```
当然要抛出更友好的异常还需要根据业务系统做具体的拦截
## 缓存
<dubbo:reference interface="com.foo.BarService" cache="lru" />
## 泛化引用 pass
主要用于框架代码编写，可以获取一个任意接口，通过invoke()方法和方法字符串调用
## 泛化实现
```java
package com.foo;
public class MyGenericService implements GenericService {

    public Object $invoke(String methodName, String[] parameterTypes, Object[] args) throws GenericException {
        if ("sayHello".equals(methodName)) {
            return "Welcome " + args[0];
        }
    }
}
```
## 回声测试 

## 上下文信息
```java
// 远程调用
xxxService.xxx();
// 本端是否为消费端，这里会返回true
boolean isConsumerSide = RpcContext.getContext().isConsumerSide();
// 获取最后一次调用的提供方IP地址
String serverIP = RpcContext.getContext().getRemoteHost();
// 获取当前服务配置信息，所有配置信息都将转换为URL的参数
String application = RpcContext.getContext().getUrl().getParameter("application");
// 注意：每发起RPC调用，上下文状态会变化
```
## 隐式参数
```java
RpcContext.getContext().setAttachment("index", "1"); // 隐式传参，后面的远程调用都会隐式将这些参数发送到服务器端，类似cookie，用于框架集成，不建议常规业务使用
xxxService.xxx(); // 远程调用
```
类似日志链路追踪
## 异步调用
服务支持异步(不建议使用，异步一般有调用方决定)
```java
@Service(timeout = 5000,group="u-public",async=true)
@Component
public class UserServiceImpl implements UserService {
....
}
```
reference支持异步调用

不成功，注释貌似不好用，暂时pass OrderServiceSyc
## 参数回调
参数回调方式与调用本地 callback 或 listener 相同,Dubbo 将基于长连接生成反向代理,应该很消耗服务器资源
## 事件通知
异步回调模式：async=true onreturn="xxx"
同步回调模式：async=false onreturn="xxx" 
可以用于分布式事务
## 本地伪装
<dubbo:service interface="com.foo.BarService" mock="com.foo.BarServiceMock" />
## 延迟暴露
```xml
<dubbo:service delay="-1" /> 
<dubbo:service delay="5000" /> 延迟5秒
```
## 并发限制
```xml
<dubbo:service interface="com.foo.BarService" actives="10" />
```
## 连接控制
```xml
<dubbo:provider protocol="dubbo" accepts="10" />
<dubbo:reference interface="com.foo.BarService" connections="10" />
```
## 粘滞连接
粘滞连接用于有状态服务，尽可能让客户端总是向同一提供者发起调用
```xml
<dubbo:protocol name="dubbo" sticky="true" />
```
## 路由规则
## 配置规则
类似服务降级等
## 优雅停机
停止时，不再发起新的调用请求，所有新的调用在客户端即报错。
停止时，先标记为不接收新请求，新请求过来时直接报错，让客户端重试其它机器。
## 日志
```xml
<dubbo:application logger="log4j" />
logback待验证
<dubbo:protocol accesslog="true" />
将当前日志输出到 对应的日志 在admin后台可以查看

```

## 容器启动

    启动一个内嵌 Jetty，用于汇报状态。
    配置：
        dubbo.jetty.port=8080：配置 jetty 启动端口
        dubbo.jetty.directory=/foo/bar：配置可通过 jetty 直接访问的目录，用于存放静态文件
        dubbo.jetty.page=log,status,system：配置显示的页面，缺省加载所有页面
## netty4
```java
<dubbo:provider server="netty4" />
<dubbo:consumer client="netty4" />
```
