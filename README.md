# 工程设计初衷
在学习dubbo过程中，需要对dubbo使用场景以及核心解决的问题有充分的了解，才能深入学习dubbo架构
# 案例业务简介
模拟一个典型电商交易业务场景的简化版本，会涉及到下单，以及系统之间依赖关系管理。
# 案例采用框架
spring mvc + dubbo + mybatis结构<br/>
DB采用mysql


#启动时检查
```java
	@Reference(check=false)//启动检查关闭，该为测试时使用，服务启动时默认用户服务已经存在 否则会报错
	UserService userService;
```
默认应该开启，这样可以保证服务在启动时会检查注册中心是否有userService的服务，若无则直接报错

