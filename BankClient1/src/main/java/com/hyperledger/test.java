package com.hyperledger;

import com.hyperledger.commit.CommitGenerateAll;
import com.hyperledger.exit.AssetsReSet;
import com.hyperledger.exit.FlushFile;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName test.java
 * @Description TODO
 * @createTime 2021年11月04日 17:12:00
 */
public class test {

    public static void main(String[] args) {
        /**
         * 这是银行的客户端程序
         */
        Scanner sc = new Scanner(System.in);
        System.out.println("进入银行的客户端：");
        System.out.println("===================================");

        String[] banks = {"bank1", "bank2"};
//        String[] banks = {"bank1", "bank2", "bank3"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6", "bank7"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6", "bank7", "bank8"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6", "bank7", "bank8", "bank9"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6", "bank7", "bank8", "bank9", "bank10"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6", "bank7", "bank8", "bank9", "bank10", "bank11", "bank12"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6", "bank7", "bank8", "bank9", "bank10", "bank11", "bank12", "bank13", "bank14"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6", "bank7", "bank8", "bank9", "bank10", "bank11", "bank12", "bank13", "bank14", "bank15", "bank16"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6", "bank7", "bank8", "bank9", "bank10", "bank11", "bank12", "bank13", "bank14", "bank15", "bank16",
//                "bank17", "bank18"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6", "bank7", "bank8", "bank9", "bank10", "bank11", "bank12", "bank13", "bank14", "bank15", "bank16",
//                "bank17", "bank18", "bank19", "bank20"};

        String[] IP = {"127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};

//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1",
//                "127.0.0.1", "127.0.0.1"};

//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1",
//                "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};

//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1",
//                "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};

//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1",
//                "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
//
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1",
//                "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};

        Integer[] port = {8881, 8882};
//        Integer[] port = {8881, 8882, 8883};
//        Integer[] port = {8881, 8882, 8883, 8884};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886, 8887};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886, 8887, 8888};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886, 8887, 8888, 8889};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886, 8887, 8888, 8889, 8890};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886, 8887, 8888, 8889, 8890, 8891, 8892};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886, 8887, 8888, 8889, 8890, 8891, 8892, 8893, 8894};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886, 8887, 8888, 8889, 8890, 8891, 8892, 8893, 8894, 8895, 8896};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886, 8887, 8888, 8889, 8890, 8891, 8892, 8893, 8894, 8895, 8896, 8897, 8898};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886, 8887, 8888, 8889, 8890, 8891, 8892, 8893, 8894, 8895, 8896, 8897, 8898, 8899, 8900};



        while (true){
            System.out.println("1、bank");
            System.out.println("2、break");

            String selection = sc.nextLine();
            if (selection.equals("1")){
                try {
                    CommitGenerateAll.election(banks, IP, port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (selection.equals("2")){
                System.exit(0);
            }else {
                System.out.println("请输入正确的选项参数：");
            }
        }
    }

    public static void flush(String[] banks, String outBankStr){
        int len = banks.length;
        FlushFile.flushFile("allTransactions.txt");
//                FlushFile.flushFile("oneTransaction.txt");

        for (int i = 0; i < len; i++) {
            FlushFile.flushFile(banks[i]+"FileLx.txt");
            FlushFile.flushFile(banks[i]+"FileRx.txt");
            FlushFile.flushFile(banks[i]+"random.txt");
        }
        FlushFile.flushFile(outBankStr + "AllRandom.txt");
        AssetsReSet.assetsReset(outBankStr + "AssetsRecord.txt", "1000000");
        AssetsReSet.assetsReset(outBankStr + "TransactionTime.txt", "0");
        AssetsReSet.assetsReset(outBankStr + "DoubleComSum.txt","0");
        AssetsReSet.assetsReset(outBankStr + "TripleComSum.txt","0");
        AssetsReSet.assetsReset(outBankStr + "QuadraComSum.txt","0");
        FlushFile.flushFile(outBankStr + "AssetsRecord1.txt");
    }
}
