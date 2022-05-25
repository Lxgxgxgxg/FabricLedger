package com.hyperledger.commit;

import com.hyperledger.activemq.AcceptRandomFromOtherBanks;
import com.hyperledger.bank.Bank;
import com.hyperledger.exit.FlushFile;
import com.hyperledger.socket.Server;
import com.hyperledger.test;
import com.hyperledger.zkp.ZKPProof;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName CommitGenerateAll.java
 * @Description TODO
 * @createTime 2021年11月04日 22:09:00
 */
public class CommitGenerateAll {

    //这个函数就是进入银行的选择模式
    public static void election(String[] banks, String[] IP, Integer[] port) throws IOException {
        Scanner sc = new Scanner(System.in);

        /**
         * 此客户端是银行1
         */
//        System.out.println("请输入你的银行ID：");
//        int id = sc.nextInt();
        int id = 1;


        /**
         * 这个地方放置的函数就是多线程启动，一直监听其他银行的消息
         */
//        int len = banks.length;
//        String selfBankStr = "bank".concat(String.valueOf(id));
//        for (int i = 0; i < len; i++) {
//            if (!banks[i].equals(selfBankStr)){
//                AcceptRandomFromOtherBanks acceptRandomFromOtherBanks = new AcceptRandomFromOtherBanks(banks[i], selfBankStr);
//                Thread thread = new Thread(acceptRandomFromOtherBanks);
//                thread.start();
//            }
//        }


        //读取加密的参数
        BigInteger[] arr = ReadParameter.readBase();

        //启动socket程序，一直监听审计员的交互
        Thread thread = new Thread(new Server("bank"+id, arr));
        thread.start();

        //保存最后交易信息的hashmap
        HashMap<String, String> allTransactionsMap = new HashMap<>();

//        LinkedList<String> list = new LinkedList<>();

        
        while (true){
            System.out.println("==============="+"bank"+id+"===============");
            System.out.println("1.转账交易：");
            System.out.println("2.获取数据：");
            System.out.println("3.零知识证明：");
            System.out.println("4.退   出");

            String string = String.valueOf(sc.nextInt());

            if (string.equals("1")){
                System.out.println("========================");
                System.out.println("现在是bank"+id+"发起交易,请输入转账交易的接受者：");
                for (int i = 0; i < banks.length; i++) {
                    if (i+1 == id){
                        continue;
                    }
                    System.out.println((i+1)+".bank"+(i+1));
                }
                String bankNum = String.valueOf(sc.nextInt());

                test("bank"+id, banks[Integer.parseInt(bankNum)-1], banks, IP, port, arr);

                System.out.println("****");
//                System.out.println(list);
            }else if (string.equals("2")){
                //链上获取交易的信息
                System.out.println("请输入读取交易的ID:");
                int assetsId = sc.nextInt();
            }else if (string.equals("3")){
                //对链上获取的数据进行零知识证明
//                System.out.println(list);

                String[] tranactions = readTenTransactionFromFile();
                for (int i = 0; i < 10; i++) {
                    LinkedList<String> temp = new LinkedList<>();
                    temp.add(tranactions[i]);
                    long startTime = System.nanoTime();
                    boolean res = ZKPProof.ReadFileAndZKP(banks, temp, allTransactionsMap, arr);
                    long endTime = System.nanoTime();
                    System.out.println((endTime-startTime)/1000000+"ms");
                    if (res){
                        System.out.println("该条交易零知识证明通过！");
                    }else {
                        System.out.println("该条交易零知识证明不通过！");
                    }
                }

//                LinkedList<String> temp = new LinkedList<>();
//                temp.add(readNewestTransactionFromFile());
//                long startTime = System.nanoTime();
//                boolean res = ZKPProof.ReadFileAndZKP(banks, temp, allTransactionsMap, arr);
//                long endTime = System.nanoTime();
//                System.out.println((endTime-startTime)/1000000+"ms");
//                if (res){
//                    System.out.println("该条交易零知识证明通过！");
//                }else {
//                    System.out.println("该条交易零知识证明不通过！");
//                }
            }else if (string.equals("4")){
                test.flush(banks, "bank"+id);
                break;
            }else {
                System.out.println("请输入正确的选项参数！");
            }
        }
    }

    public static void test(String outBankStr, String inBankStr, String[] banks, String[] IP, Integer[] port, BigInteger[] arr) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("输入转账的金额：");
        String mInput = sc.nextLine();
        BigInteger m = null;
        try {
            m = new BigInteger(mInput);
        }catch (NumberFormatException e){
            System.out.println("输入金额类型错误！");
        }


        FlushFile.flushFile("oneTransaction.txt");
        if (m.compareTo(BigInteger.valueOf(0)) == 1){
            long startTime = System.nanoTime();
            for (int i = 1; i <= 20; i++) {
                long startTime1 = System.nanoTime();
                CommitToObject.commitAll01(outBankStr, inBankStr, banks, IP, port, BigInteger.valueOf(i), arr);
                long endTime1 = System.nanoTime();
                System.out.println((endTime1-startTime1)/1000000+"ms");
                System.out.println("已完成交易"+i);
            }
            long endTime = System.nanoTime();
            System.out.println("银行交易200条所需的时间" + (endTime - startTime) /1000000 + "ms");
//            long startTime = System.nanoTime();
//            CommitToObject.commitAll01(outBankStr, inBankStr, banks, IP, port, m, arr);
//            long endTime = System.nanoTime();x
//            System.out.println((endTime-startTime)/1000000+"ms");
        }else {
            System.out.println("输入的金额有误！");
        }
    }


    public static String readNewestTransactionFromFile() throws IOException {
        FileReader file1 = new FileReader("oneTransaction.txt");
        BufferedReader br = new BufferedReader(file1);
        String tempInput = br.readLine();
//        String str1 = tempInput.replaceFirst("\\{\"time\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\",", "{");

        return tempInput;
    }


    /**
     * 从文件中读取刚才生成的交易进行零知识证明
     */
    public static String[] readTenTransactionFromFile(){
        FileReader file = null;
        String str = "oneTransaction.txt";
        String[] g = new String[10];
        String str1 = null;
        try {
            file = new FileReader(str);
            BufferedReader br = new BufferedReader(file);
            int i = 0;
            while((str1 = br.readLine()) != null){
                g[i] = str1;
                i++;
            }
            br.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return g;
    }
}
