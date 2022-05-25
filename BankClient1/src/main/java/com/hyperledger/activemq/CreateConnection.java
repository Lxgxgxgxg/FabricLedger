package com.hyperledger.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName CreateQueueProducer.java
 * @Description TODO
 * @createTime 2021年11月09日 22:31:00
 */
public interface CreateConnection {

    static Connection createQueueConnection() throws JMSException {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.40.132:61616");
        //2.获取连接
        Connection connection = connectionFactory.createConnection();

        return connection;
    }
}
