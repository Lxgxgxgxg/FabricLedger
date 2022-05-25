package com.hyperledger.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName SendRandomToOtherBanks.java
 * @Description TODO
 * @createTime 2021年11月09日 22:40:00
 */
public class SendRandomToOtherBanks {

    /**
     * 把生成交易时使用到的随机数发送给其他对应的银行，使用的是消息队列
     * @param outBankStr
     * @param
     * @throws JMSException
     */
    public static void sendRandomToOtherBanks(String outBankStr, String inBankStr, String transactionInformation) throws JMSException {

        /**
         * 把各自的随机数读取出来之后，发送到对应的消息队列，即可
         */
        Connection connection = CreateConnection.createQueueConnection();
        //3.启动连接
        connection.start();
        //4.获取session  (参数1：是否启动事务,参数2：消息确认模式)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        StringBuilder sbStr = new StringBuilder();
        sbStr.append(outBankStr).append("To").append(inBankStr);

        //5.创建队列对象
        Queue queue = session.createQueue(sbStr.toString());
        //6.创建消息生产者
        MessageProducer producer = session.createProducer(queue);
        //读取信息
//        String transactionInfor = ReadStringFromFile.readStringFromFile(banks[i].concat("random.txt"));

        //7.创建消息
        TextMessage textMessage = session.createTextMessage(transactionInformation);

        //8.发送消息
        producer.send(textMessage);
//        session.commit();
        producer.close();

        //关闭资源
        session.close();
        connection.close();

    }
}
