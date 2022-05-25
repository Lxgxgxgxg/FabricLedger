package com.hyperledger.activemq;

import com.hyperledger.commit.ReadBigInteger;

import javax.jms.*;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName ReserveRandomFromOtherBanks.java
 * @Description TODO
 * @createTime 2021年11月10日 11:32:00
 */
public class AcceptRandomFromOtherBanks implements Runnable{

    String outBankStr;
    String inBankStr;

    public AcceptRandomFromOtherBanks() {
    }

    public AcceptRandomFromOtherBanks(String outBankStr, String inBankStr) {
        this.outBankStr = outBankStr;
        this.inBankStr = inBankStr;
    }

    /**
     * 从自己相对应的消息队列中获取自己交易时的随机数，并保存到自己的bankAllRandom.txt文件中
     */
    /**
     *
     * @param outBankStr 其他银行
     * @param inBanksStr  本银行
     * @throws JMSException
     */
    public static void acceptRandomFromOtherBanks(String outBankStr, String inBanksStr) throws JMSException {

        //新建一个connection
        Connection connection = CreateConnection.createQueueConnection();

        //3.启动连接
        connection.start();

        //4.获取session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        StringBuilder sbStr = new StringBuilder();
        sbStr.append(outBankStr).append("To").append(inBanksStr);

        //5.创建队列对象
        Queue queue = session.createQueue(sbStr.toString());

        //6.创建消息消费者
        MessageConsumer consumer = session.createConsumer(queue);

        //7.接受消息
        acceptMessage(sbStr.toString(), consumer, inBanksStr);
        System.out.println("~~~~~~~~~~~~~~~~队列已经启动");

    }

    /**
     *
     * @param sbStr 通道名
     * @param messageConsumer 消息消费者
     * @param inBankStr 接受银行ID
     */
    public static void acceptMessage(String sbStr, MessageConsumer messageConsumer, String inBankStr){

        while (true){
            try {
                messageConsumer.setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message message) {
                        TextMessage textMessage = (TextMessage)message;
//                        textMessage.acknowledge();
                        //这里是把银行的转账的金额也发送了过来，字符串之间是用+连接的，现在分隔开，第一个就是转账金额，第二个就是承诺的随机数
                        String[] str = textMessage.toString().split("\\+");
                        String moneyAndFromBank = str[0];
                        System.out.println(moneyAndFromBank);
                        writeMoneyToFile(str[0], inBankStr);
                        String textMessage1 = str[1];
                        try{
                            //把信息写进文件
                            /**
                             * 其他银行发来的每个承诺的随机数
                             */
                            FileWriter file = new FileWriter(inBankStr+"AllRandom.txt", true);
                            file.write(textMessage1+"\n");
                            file.close();
                        }catch (IOException e){
                            System.out.println(sbStr+"消息接受失败！");
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void run() {
        try {
            acceptRandomFromOtherBanks(outBankStr, inBankStr);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    public static void writeMoneyToFile(String moneyAndBank, String inBankStr){
        int len = moneyAndBank.length();
        String bank = moneyAndBank.substring(0, 6);
        BigInteger money = new BigInteger(moneyAndBank.substring(6, len));

        /**
         * 这里是对面银行发来的转账信息，把发来的金额加到资产和中,并记录交易
         */
        BigInteger assetsSum = ReadBigInteger.readBigInteger(inBankStr+"AssetsRecord.txt");
        FileWriter file2 = null;
        try {
            file2 = new FileWriter("bank1AssetsRecord1.txt",true);
            file2.write(bank+" -> "+ inBankStr + money + "\n");
            file2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                file2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (money.compareTo(BigInteger.valueOf(0)) == 1){
            //说明转账的金额是大于0
            //先读取原值

            FileWriter file1 = null;
            FileWriter file3 = null;
            FileWriter file4 = null;
            FileWriter file5 = null;
            FileWriter file6 = null;
            try {
                //资产和增加
                file1 = new FileWriter(inBankStr + "AssetsRecord.txt");
                file1.write(assetsSum.add(money)+"\n");

                //交易次数增加
                //先读取原值
                BigInteger transactionTime = ReadBigInteger.readBigInteger(inBankStr+"TransactionTime.txt");
                file3 = new FileWriter(inBankStr+"TransactionTime.txt");
                file3.write(transactionTime.add(BigInteger.valueOf(1))+"\n");

                //金额的二次方
                //先读取原值
                BigInteger doubleComSum = ReadBigInteger.readBigInteger(inBankStr+"DoubleComSum.txt");
                file4 = new FileWriter(inBankStr+"DoubleComSum.txt");
                file4.write(doubleComSum.add(money.pow(2))+"\n");

                //金额的三次方
                //先读取原值
                BigInteger tripleComSum = ReadBigInteger.readBigInteger(inBankStr+"TripleComSum.txt");
                file5 = new FileWriter(inBankStr+"TripleComSum.txt");
                file5.write(tripleComSum.add(money.pow(3))+"\n");


                //金额的四次方
                //先读取原值
                BigInteger quadraComSum = ReadBigInteger.readBigInteger(inBankStr+"QuadraComSum.txt");
                file6 = new FileWriter(inBankStr+"QuadraComSum.txt");
                file6.write(quadraComSum.add(money.pow(4))+"\n");

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    file1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    file3.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    file4.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    file5.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    file6.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
