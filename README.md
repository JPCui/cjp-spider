# cjp-spider

## 前言

> 文档

[cjp-spider 文档](https://github.com/JPCui/doc/tree/master/cjp-spider)

> 开发用途：

1. 纯兴趣而已
2. 可以分析新浪微博手机端页面的设计，分析微博系统设计的一些技术（RestApi命名规范.etc）
3. 可以自己写一个自动发送微博、自动评论、自动点赞 . . .的utils

> 效率：

sina.weibo.GetFansThread : 10个用户的粉丝/min（使用代理）

## 实现内容
1. 目前可以利用微博手机端网页抓取新浪微博各种信息（微博、粉丝、发布微博 [附带地理位置信息] ）
 - 目前正在加入多线程，已考虑Connection、爬虫队列同步问题
 - 线程池
 - 代理服务中间层（proxy），利用代理池实现动态更换IP，但**请求会出现阻塞**，对策是：开启线程监视该请求，超时则放弃请求；
 - 怎么做多账号切换

 - *-----  ↑ 已解决 | 未解决 ↓ -----*
 - Concurrent 引入并发库
 - Connection连接池
 - redis做爬虫URL队列（LPOP），SolrCloud存储层

2. 加入了一个朋友网爬虫demo，抓取用户关系数据

3. 后面可能会加上新闻的抓取
（考虑到新闻站点抓取较为容易，其实主要本系统主要目的在于爬虫的技术架构，而非所爬的内容）
设计上：
 - 会考虑抓取策略，以前写了一个队三大新闻网站的抓取，但代码结构不是很好，有时间重构一下，然后放上来；
 - 会采用分布式solr进行存储（solrCloud），*搭建的方式及环境的教程，自己写的，欢迎来喷：[单机搭建伪分布SolrCloud](http://wenku.baidu.com/view/8d858fb2360cba1aa911da59.html)*

4. 暂时该项目只会完成上述两项，如果有时间、有能力的话，会做一些更有技术含量的抓取

 - 图片、视频抓取
 - 暗网抓取
	
## 更新日志

### TIME
2015-04-21
### CONTENT

1. √ 要考虑纳入多线程，使用代理防止新浪的屏蔽
2. √ 考虑添加analyzer中间层：<br>
	> JSON/HTML --> ANALYZER --> DOMAIN
	> 
	> 将获取到的JSON/HTML交给分析器ANALYZER进行处理，最后返回领域实体DOMAIN
3. √ 添加代理服务(Proxy)
	> 考虑如何扩展使用代理服务器来抓取数据，不然会被对方屏蔽

### TIME
2015-04-28
### CONTENT

1. √ 将Jsoup替换成HttpClient
2. √ 加入了代理（HttpHost）
3. √ 登录的时候不能使用代理！
4. √ 多账号登录、多代理抓取测试

### TIME
2015-05-10
### CONTENT

1. 使用redis（lpush、lpop）做抓取队列
2. cjp-spider-web 做抓取监控、线程监控

