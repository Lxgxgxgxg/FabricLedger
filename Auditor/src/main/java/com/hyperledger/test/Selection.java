package com.hyperledger.test;

import com.hyperledger.audit.AuditMainFunction;
import com.hyperledger.bank.BankAudit;
import com.hyperledger.connection.Connection;
import com.hyperledger.connection.Query;
import com.hyperledger.ecc.ReadBase;
import com.hyperledger.socket.InitializeAssets;
import com.hyperledger.zkp.ZKPProof;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName Selection.java
 * @Description TODO
 * @createTime 2021年11月10日 20:15:00
 */
public class Selection {

    /**
     * 审计员进入系统之后，得先从账本上获取数据，之后进行资产审计
     */
    public static void auditorMainFunction(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("资产审计员选择模式：");

        /**
         * 零知识证明之后，所有的审计字段保存的map
         */
        HashMap<String, HashMap<String, BankAudit>> allTransactionsMap = new HashMap<>();

        System.out.println("正在初始化账本：");
//        String[] banks = {"bank1", "bank2"};
//        String[] banks = {"bank1", "bank2", "bank3"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6", "bank7"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6", "bank7", "bank8"};
//        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6", "bank7", "bank8", "bank9"};
        String[] banks = {"bank1", "bank2", "bank3", "bank4","bank5", "bank6", "bank7", "bank8", "bank9", "bank10"};

//        String[] IP = {"127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
//        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
        String[] IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};

//        Integer[] port = {8881, 8882};
//        Integer[] port = {8881, 8882, 8883};
//        Integer[] port = {8881, 8882, 8883, 8884};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886, 8887};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886, 8887, 8888};
//        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886, 8887, 8888, 8889};
        Integer[] port = {8881, 8882, 8883, 8884, 8885, 8886, 8887, 8888, 8889, 8890};
        //创建hashMap保存从每一个银行得到的结果
        HashMap<String, String> initializeHashMap = new HashMap<>();
        /**
         * 初始化资金时需要一个随机数，这个随机数是生成Com的随机数，在后面审计时，需要用到，故我们使用数组来保存吧
         */
        BigInteger[] initializeAssetsRandom = new BigInteger[banks.length];
        //读取基本参数
        BigInteger[] arr = new BigInteger[0];
        try {
            arr = ReadBase.readBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InitializeAssets.initializeAssets(allTransactionsMap, initializeHashMap, initializeAssetsRandom, banks, IP, port, arr);

        /**
         * 新建list,把从链上获取到一条交易信息保存进去
         */
        ArrayList<String> getOneTransactionStringFromChainList = new ArrayList<>();
        /**
         * 新建list,把从链上获取到全部交易信息保存进去
         */
//        ArrayList<String> getAllTransactionStringFromChainList = new ArrayList<>();
        ArrayList<String> getAllTransactionStringFromChainList = new ArrayList<>();


        /**
         * 审计时需要和银行交互，保存得到的承诺和和只忙因子和，用map保存
         */
        HashMap<String, BigInteger> hashMapComAndRSum = new HashMap<>();


        while (true){
            System.out.println("=================================");
            System.out.println("1.获取数据");
            System.out.println("2.资产审计");
            System.out.println("3.退出程序");
            String string = scanner.nextLine();
            if (string.equals("1")){
                while (true){
                    System.out.println("===============================");
                    System.out.println("1.获取一条交易信息");
                    System.out.println("2.获取全部交易信息");
                    System.out.println("3.ZKP最新交易");
                    System.out.println("4.退出");
                    String string1 = scanner.nextLine();
                    if (string1.equals("1")){
                        System.out.println("请输入读取交易的ID:");
                        int i = scanner.nextInt();

                        //每次从链上获取交易信息时先清空list
                        getOneTransactionStringFromChainList.clear();
                        long startTme = System.nanoTime();
                        getOneTransactionStringFromChainList = Query.queryOneTransaction(String.valueOf(i), Connection.getContract(Connection.getNetwork()));
                        long endTime = System.nanoTime();
                        System.out.println("链上获取一条交易数据所需的时间为：" + (endTime - startTme)/1000000 + "ms");

                    }else if (string1.equals("2")){
                        System.out.println("输入交易起始ID:");
                        System.out.println("输入交易终止ID:");
                        int x = scanner.nextInt();
                        int y = scanner.nextInt();
                        //每次从链上获取多条交易信息时先清空list
                        getAllTransactionStringFromChainList.clear();

                        long startTme = System.nanoTime();
                        getAllTransactionStringFromChainList = Query.queryAllTransaction(x, y, Connection.getContract(Connection.getNetwork()));
                        long endTime = System.nanoTime();

                        System.out.println("链上获取" + (y-x+1) + "条交易数据所需的时间为：" + (endTime - startTme) / 1000000 + "ms");
                    }else if (string1.equals("3")){
                        System.out.println("1.验证一条交易信息");
                        System.out.println("2.验证全部交易信息");
                        int z = scanner.nextInt();
                        if (z == 1){
                            ZKPProof.ZKPMain(getOneTransactionStringFromChainList, allTransactionsMap, banks, arr);
                        }else {
                            long startTime = System.nanoTime();
                            ZKPProof.ZKPMain(getAllTransactionStringFromChainList, allTransactionsMap, banks, arr);
                            long endTIme = System.nanoTime();

                            System.out.println("银行客户端零知识证明"+getAllTransactionStringFromChainList.size()+"条交易信息所需的时间"+(endTIme-startTime)/1000000+"ms");
                        }
                    }else if (string1.equals("4")){
                        break;
                    } else {
                        System.out.println("请输入正确的选项参数！");
                    }
                }
            }else if (string.equals("2")){

                /**
                 * 资产审计，审计员先于各个银行进行交互，得到交易后的每一个资产的和和致盲因子的和
                 */
                long startTime = System.nanoTime();
                AuditMainFunction.auditLedger(allTransactionsMap, initializeHashMap, hashMapComAndRSum,
                        initializeAssetsRandom, banks, IP, port, arr);
                long endTime = System.nanoTime();

                System.out.println("审计一条交易记录所需的时间为：" + (endTime - startTime) / 1000000 + "ms");

            }else if (string.equals("3")){
                break;
            }else {
                System.out.println("请输入正确的选项参数！");
            }
        }
    }
}
