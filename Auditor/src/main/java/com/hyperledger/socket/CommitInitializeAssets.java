package com.hyperledger.socket;

import com.alibaba.fastjson.JSON;
import com.hyperledger.bank.BankAudit;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName commitInitializeAssets.java
 * @Description TODO
 * @createTime 2021年11月11日 11:08:00
 */
public class CommitInitializeAssets {

    /**
     * 把初始化拿到的资金进行账本的初始化，然后保存
     */
    public static void commitInitializeAssets(HashMap<String, HashMap<String, BankAudit>> allTransactionsMap, HashMap<String, String> initializeHashMap,
                                              BigInteger[] initializeAssetsRandom, BigInteger[] arr){
        //map的长度
        int size = initializeHashMap.size();

        BankAudit[] nums = new BankAudit[size];
        //allTransactionsMap保存的key以时间为主吧
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());

        HashMap<String, BankAudit> bankAuditHashMap = new HashMap<>();

        /**
         * 把得到的初始化资金进行承诺的初始化计算
         */
        for (int i = 1; i <= size; i++) {
            BigInteger value = new BigInteger(initializeHashMap.get("bank"+i));
            nums[i-1] = commitSelfBankInitializeAssets(i, initializeAssetsRandom, value, arr);
            bankAuditHashMap.put("bank"+i, nums[i-1]);
        }


        /**
         * 生成map,就像零知识证明玩的字段一样，保存进allTransactionMap,后续审计的时候就是用这个Map
         */
//        String initializeStr = mappingGen(nums);

        allTransactionsMap.put(time, bankAuditHashMap);
    }


    /**
     * 对每一个银行进行初始化，返回审计银行对象
     * @param bankAssets
     * @param arr
     * @return
     */
    public static BankAudit commitSelfBankInitializeAssets(int i, BigInteger[] initializeAssetsRandom, BigInteger bankAssets, BigInteger[] arr){
        //Pedersen承诺的形式c = g^m * h^r (m是要隐藏的金额，r是致盲因子)
        //产生random c，在2到q - 2之间
        BigInteger r1 = GetRandom.getRandom(arr);
        //计算承诺
        BigInteger[] comarr = new BigInteger[6];
        //交易金额的承诺
        comarr[0] = (arr[2].modPow(bankAssets, arr[0])).multiply((arr[3]).modPow(r1, arr[0])).mod(arr[0]);
        comarr[1] = BigInteger.valueOf(1);
        //是否为交易的发起者
        comarr[2] = BigInteger.valueOf(0);
        //隐藏值的二次方的承诺
        comarr[3] = BigInteger.valueOf(1);
        //隐藏值的三次方的承诺
        comarr[4] = BigInteger.valueOf(1);
        //隐藏值的四次方的承诺
        comarr[5] = BigInteger.valueOf(1);

        //将生成comarr[0]的随机数r1保存进数组initializeAssetsRandom
        initializeAssetsRandom[i-1] = r1;

        BankAudit bankAudit = new BankAudit();
        bankAudit.setCom(comarr[0]);
        bankAudit.setBitCom(comarr[1]);
        bankAudit.setIsFrom(comarr[2].intValue());
        bankAudit.setDoubleCom(comarr[3]);
        bankAudit.setTripleCom(comarr[4]);
        bankAudit.setQuadraCom(comarr[5]);

        return bankAudit;
    }



    public static String mappingGen(BankAudit[] bank){
        //银行与银行里边的每个字段对应是key-value,其中key是字符串"bank1", value是bank对象
        //新建TreeMap,按键值进行排序
        TreeMap<String, BankAudit> treeMap = new TreeMap<>();
        for (int i = 1; i <= bank.length; i++) {
            treeMap.put("bank"+i, bank[i-1]);
        }

        String str = JSON.toJSONString(treeMap);

        return str;
    }
}
