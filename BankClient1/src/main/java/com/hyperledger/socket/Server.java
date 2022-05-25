package com.hyperledger.socket;

import com.hyperledger.commit.ReadBigInteger;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName Server.java
 * @Description TODO
 * @createTime 2021年11月13日 18:51:00
 */
public class Server implements Runnable{

    /**
     * 因为run函数不能传参，现在只能通过变量和构造器，get/set方法传进去
     * @param bank
     * @param arr
     */
    private String bank;    //当前银行
    private BigInteger[] arr;   //基本参数数组

    public Server() {
    }

    public Server(String bank, BigInteger[] arr) {
        this.bank = bank;
        this.arr = arr;
    }

    public static void listen(String bank, BigInteger[] arr){
        ServerSocket ss = null;
        Socket socket = null;
        InputStream is = null;
        ByteArrayOutputStream bas = null;
        OutputStream os = null;
        while (true){
            try{
                ss = new ServerSocket(8881);
                socket = ss.accept();
                is = socket.getInputStream();

                bas = new ByteArrayOutputStream();
                byte[] buffer = new byte[200];
                int len = 0;
                while ((len = is.read(buffer)) != -1){
                    bas.write(buffer, 0, len);
                }
                /**
                 * 这个地方要进行字符串的判断，因为致盲因子通过消息队列发送过去了，现在这里只监听来自审计员的信息
                 * 如果是1，就是其他银行发来的每一个承诺的随机数,其中包含发送的金额，都是一条字符串，我们需要处理就行
                 * falg + bankid + 转账金额m + 随机数
                 * 如果是2，就是审计的信号，发送每一个资产和和致盲因子的和发送过去
                 * 如果是3，就是审计员发起的初始化账本的信号，每个银行则需把自己的资产和金额发送过去就行
                 */
                String str = bas.toString();
                String str1 = str.substring(0, 1);
                if (str1.equals("1")){
                    String[] strings = str.split("\\+");
//                    for (int i = 0; i < strings.length; i++) {
//                        System.out.println(strings[i]);
//                    }

                    String fromBank = strings[1];
                    BigInteger money = new BigInteger(strings[2]);
                    writeMoneyToFile(fromBank, money, bank);
                    FileWriter file1 = new FileWriter(bank+"AllRandom.txt", true);
                    file1.write(strings[3]+"\n");
                    file1.close();
                }else if (str1.equals("2")){
                    /**
                     * 审计员和银行交互，实现资产和，致盲因子的发送
                     */
                    String temp = SendAssetsInformationToAuditor.send(bank, arr);
                    os = socket.getOutputStream();
                    os.write(temp.getBytes());
                }else if (str1.equals("3")){
                    /**
                     * 这里是进行初始化的flag
                     */
                    FileReader file = new FileReader(bank+"AssetsRecord.txt");
                    BufferedReader br = new BufferedReader(file);
                    String assetsSum = br.readLine();
                    os = socket.getOutputStream();
                    os.write(assetsSum.getBytes());
                }
            } catch (IOException e){
                e.printStackTrace();
            }finally {
                if ( os != null){
                    try {
                        os.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }

                if (bas != null){
                    try {
                        bas.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }

                if (is != null){
                    try {
                        is.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }

                if (socket != null){
                    try {
                        socket.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }

                if (ss != null){
                    try{
                        ss.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        listen(bank, arr);
    }



    public static void writeMoneyToFile(String fromBank, BigInteger money, String inBankStr){


        /**
         * 这里是对面银行发来的转账信息，把发来的金额加到资产和中,并记录交易
         */
        BigInteger assetsSum = ReadBigInteger.readBigInteger(inBankStr+"AssetsRecord.txt");


        if (money.compareTo(BigInteger.valueOf(0)) == 1){
            //说明转账的金额是大于0
            //先读取原值
            FileWriter file2 = null;
            try {
                file2 = new FileWriter(inBankStr+"AssetsRecord1.txt",true);
                file2.write(fromBank+" -> "+ inBankStr + " : "+ money + "\n");
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
