/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lkl.dcloud.provider;


public class TradeApplication {

    public static void main(String[] args) throws Exception {
        //Prevent to get IPV6 address,this way only work in debug mode
        //But you can pass use -Djava.net.preferIPv4Stack=true,then it work well whether in debug mode or not
        System.setProperty("java.net.preferIPv4Stack", "true");
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/dubbo-demo-trade.xml"});
//        context.start();

        args = new String[]{"spring","jetty","log4j"};
        com.alibaba.dubbo.container.Main.main(args);
//        OrderService orderService = context.getBean(OrderService.class);
//        orderService.submitOrder("1");
        
//        OrderServiceSyc orderServiceSyc = context.getBean(OrderServiceSyc.class);
//        orderServiceSyc.submitSycOrder("1");
//        System.in.read(); // press any key to exit
        
    }

}
