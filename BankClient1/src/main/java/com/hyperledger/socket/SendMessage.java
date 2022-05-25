package com.hyperledger.socket;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName SendMessage.java
 * @Description TODO
 * @createTime 2021年11月15日 14:17:00
 */
public class SendMessage {

    /**
     * 银行1产生交易之后，发送承诺随机数给其他银行的函数
     * @param infor 发送的信息
     * @param IP    目标银行服务器的ip
     * @param port  目标银行服务器的port
     */
    public static void sendMessage(String flag, String infor, String IP, Integer port){
        try {
            InetAddress inet = InetAddress.getByName(IP);
            Socket socket = new Socket(inet, port);
            OutputStream os = socket.getOutputStream();
            os.write((flag+"+"+infor).getBytes());

            os.close();
            socket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
