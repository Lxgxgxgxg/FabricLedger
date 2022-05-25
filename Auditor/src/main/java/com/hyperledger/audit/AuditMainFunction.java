package com.hyperledger.audit;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName AuditMainFunction.java
 * @Description TODO
 * @createTime 2021年11月10日 20:40:00
 */

import com.hyperledger.bank.BankAudit;
import com.hyperledger.bank.BankAuditResult;
import com.hyperledger.socket.Client;
import org.glassfish.json.JsonUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;

import static com.hyperledger.audit.EveryBankAudit.auditEveryBank;

/**
 * 这里的函数主要进行审计员审计的操作
 */
public class AuditMainFunction {

    /**
     *
     * @param allTransactionMap     所有交易零知识证明之后保存的结果集，包括初始化资金承诺之后的值
     * @param initializeHashMap     初始化得到每一个银行资金的结果集
     * @param hashMapComAndRSum     审计时得到的银行和具体资产和、几次方和相对应致盲因子的结果集
     * @param initializeAssetsRandom       在对初始资金进行承诺时，使用到一个随机数，这个数组保存的就是每个银行使用到的随机数
     * @param banks     银行数组
     * @param IP        银行IP数组
     * @param portNum   银行端口数组
     * @param arr       基本参数数组
     */
    public static void auditLedger(HashMap<String, HashMap<String, BankAudit>> allTransactionMap, HashMap<String, String> initializeHashMap,
                                   HashMap<String, BigInteger> hashMapComAndRSum, BigInteger[] initializeAssetsRandom,
                                   String[] banks, String[] IP, Integer[] portNum, BigInteger[] arr) {
        Scanner scanner = new Scanner(System.in);
        /**
         * 审计员与银行进行交互，得到相应承诺的致盲因子的和
         */
        for (int i = 0; i < banks.length; i++) {
            Client.getComAndRSumClient(hashMapComAndRSum, "2", banks[i], IP[i], portNum[i]);
        }

        System.out.println("审计员对银行资产进行结算审计：");

        BankAuditResult[] bankAuditResults = new BankAuditResult[banks.length];

//        while (true){
//            System.out.println("=================================");
//            System.out.println("请输入审计模式：");
//            System.out.println("1. 银行指标");
//            System.out.println("2. 系统指标");
//            System.out.println("3. 退出审计");
//            String str = scanner.nextLine();
//
//            if(str.equals("1")) {
//                for (int i = 0; i < banks.length; i++) {
//                    bankAuditResults[i] = auditEveryBank(allTransactionMap, initializeHashMap, hashMapComAndRSum, banks[i], initializeAssetsRandom, arr);
//                }
//            }else if (str.equals("2")){
//                System.out.println("正在审计整个银行系统的资产情况：");
//                BigInteger allBankSystemAssetsSum = allBankSystemAudit.getAllBankSystemAssetsSum(bankAuditResults);
//
//                //此时段的交易平均值
//                BigDecimal realTimeTransactionAverageValue = allBankSystemAudit.getRealTimeTransactionAverageValue(bankAuditResults, initializeHashMap, banks);
//                //得到每一个银行的市场占有率
//                int len = bankAuditResults.length;
//                for (int i = 0; i < len; i++) {
//                    BigDecimal temp = allBankSystemAudit.getEveryBankMarketRate(bankAuditResults[i], allBankSystemAssetsSum);
//                    //把市场占有率赋给每一个银行
//                    bankAuditResults[i].setBankMarketRate(temp);
//                    //把资产和赋给每一个银行
//                    bankAuditResults[i].setAllBankSystemAssetsSum(allBankSystemAssetsSum);
//                    //实时交易平均价格，赋给每一个银行
//                    bankAuditResults[i].setRealTimeTransactionAverageValue(realTimeTransactionAverageValue);
//                }
//
//                //求HHI
//                for (int i = 0; i < len; i++) {
//                    BigDecimal temp = allBankSystemAudit.getHHIIndex(bankAuditResults);
//                    bankAuditResults[i].setHHIIndex(temp);
//                }
//
//                System.out.println("审计完成！");
//                for (int i = 0; i < len; i++) {
//                    System.out.println(bankAuditResults[i]);
//                    initializeHashMap.put(banks[i], bankAuditResults[i].getAssetsSum().toString());
//                }
//
//                /**
//                 * 这里记住，一定要把现在的银行的值写进初始化的hashMap中去，就是下一次审计的初始值，利用上边的for循环
//                 */
//
//            }else if (str.equals("3")){
//                break;
//            }else {
//                System.out.println("请输入正确的选项参数！");
//            }
//
//        }


//        while (true){
//            System.out.println("=================================");
//            System.out.println("请输入审计模式：");
//            System.out.println("1. 银行指标");
//            System.out.println("2. 系统指标");
//            System.out.println("3. 退出审计");
//            String str = scanner.nextLine();

//            if(str.equals("1")) {
                for (int i = 0; i < banks.length; i++) {
                    bankAuditResults[i] = auditEveryBank(allTransactionMap, initializeHashMap, hashMapComAndRSum, banks[i], initializeAssetsRandom, arr);
                }
//            }else if (str.equals("2")){
                System.out.println("正在审计整个银行系统的资产情况：");
                BigInteger allBankSystemAssetsSum = allBankSystemAudit.getAllBankSystemAssetsSum(bankAuditResults);

                //此时段的交易平均值
                BigDecimal realTimeTransactionAverageValue = allBankSystemAudit.getRealTimeTransactionAverageValue(bankAuditResults, initializeHashMap, banks);
                //得到每一个银行的市场占有率
                int len = bankAuditResults.length;
                for (int i = 0; i < len; i++) {
                    BigDecimal temp = allBankSystemAudit.getEveryBankMarketRate(bankAuditResults[i], allBankSystemAssetsSum);
                    //把市场占有率赋给每一个银行
                    bankAuditResults[i].setBankMarketRate(temp);
                    //把资产和赋给每一个银行
                    bankAuditResults[i].setAllBankSystemAssetsSum(allBankSystemAssetsSum);
                    //实时交易平均价格，赋给每一个银行
                    bankAuditResults[i].setRealTimeTransactionAverageValue(realTimeTransactionAverageValue);
                }

                //求HHI
                for (int i = 0; i < len; i++) {
                    BigDecimal temp = allBankSystemAudit.getHHIIndex(bankAuditResults);
                    bankAuditResults[i].setHHIIndex(temp);
                }

                System.out.println("审计完成！");
                for (int i = 0; i < len; i++) {
                    System.out.println(bankAuditResults[i]);
                    initializeHashMap.put(banks[i], bankAuditResults[i].getAssetsSum().toString());
                }

                /**
                 * 这里记住，一定要把现在的银行的值写进初始化的hashMap中去，就是下一次审计的初始值，利用上边的for循环
                 */

//            }else if (str.equals("3")){
//                break;
//            }else {
//                System.out.println("请输入正确的选项参数！");
//            }

//        }

    }

}
