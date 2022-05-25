package com.hyperledger.commit;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName CommitToObject.java
 * @Description TODO
 * @createTime 2021年11月05日 18:00:00
 */

import com.alibaba.fastjson.JSON;
import com.hyperledger.bank.Bank;
import com.hyperledger.bank.BankBalanceAndBitZKP;
import com.hyperledger.connection.Connection;
import com.hyperledger.connection.Invoke;
import com.hyperledger.socket.SendMain;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.TreeMap;

import static com.hyperledger.commit.Commit.commitOtherBank;
import static com.hyperledger.commit.Commit.commitSelfBank;

/**
 * 四个银行的承诺生成
 */
public class CommitToObject {

    /**
     *
     * @param outBankStr 支出行
     * @param inBankStr 收款行
     * @param banks 银行数组
     * @param value 转账金额
     * @param arr 加密参数
     * @throws IOException
     */
    public static void commitAll01(String outBankStr, String inBankStr, String[] banks, String[] IP, Integer[] port,
                                   BigInteger value, BigInteger[] arr) throws IOException {

        /**
         * 该文件记录银行1的资产转给了哪个银行
         */
        FileWriter file = new FileWriter(outBankStr+"AssetsRecord1.txt",true);
        file.write(outBankStr + " -> "+ inBankStr + ":" + value + "\n");
        file.close();

        TreeMap<String, Bank> treeMap = new TreeMap<>();

        for (int i = 0; i < banks.length; i++) {
            if (banks[i].equals(outBankStr)){
                Bank bankOut = new Bank();
                BigInteger[] outBankArr = commitSelfBank(banks, IP, port, banks[i], value, arr);
                arrayAndObjectMap(outBankArr, bankOut, banks[i]);

                treeMap.put(outBankStr, bankOut);
            }else if (banks[i].equals(inBankStr)){
                Bank bankIn = new Bank();
                BigInteger[] inBankArr = commitOtherBank(outBankStr, IP[i], port[i],  banks[i], value, arr);
                arrayAndObjectMap(inBankArr, bankIn, banks[i]);

                treeMap.put(inBankStr, bankIn);
            }else {
                Bank otherBank = new Bank();
                BigInteger[] otherBankArr = commitOtherBank(outBankStr, IP[i], port[i], banks[i], BigInteger.valueOf(0), arr);
                arrayAndObjectMap(otherBankArr, otherBank, banks[i]);

                treeMap.put(banks[i], otherBank);
            }
        }
        mappingGen(treeMap, banks, outBankStr, inBankStr);
    }


    public static void arrayAndObjectMap(BigInteger[] bankArr, Bank bank, String BankStr){
        bank.setCom(bankArr[0]);
        bank.setBitCom(bankArr[1]);
        bank.setisFrom((bankArr[2].intValue()));
        bank.setDoubleCom(bankArr[3]);
        bank.setTripleCom(bankArr[4]);
        bank.setQuadraCom(bankArr[5]);
        bank.setBitComTemp1(bankArr[6]);
        bank.setBitComTemp2(bankArr[7]);
        bank.setChallenge(bankArr[8]);
        bank.setHash_challenge(bankArr[9]);
        bank.setBitComResponse1(bankArr[10]);
        bank.setBitComResponse2(bankArr[11]);
        bank.setBitComResponse3(bankArr[12]);
        bank.setTemp1(bankArr[13]);
        bank.setTemp1C(bankArr[14]);
        bank.setTemp1S(bankArr[15]);
        bank.setBitComMulTemp1(bankArr[16]);
        bank.setComTemp1(bankArr[17]);
        bank.setComTemp1C(bankArr[18]);
        bank.setComTemp1S(bankArr[19]);
        bank.setIndexEqualTemp1(bankArr[20]);
        bank.setIndexEqualTemp2(bankArr[21]);
        bank.setIndexEqualTemp3(bankArr[22]);
        bank.setIndexEqualTemp4(bankArr[23]);
        bank.setComSAddVIndex(bankArr[24]);
        bank.setComSIndex(bankArr[25]);
        bank.sethBaseIndex(bankArr[26]);
        bank.setHash_h(bankArr[27]);
        bank.setH_Response(bankArr[28]);
        bank.setComSAddVSquareIndex(bankArr[29]);
        bank.sethBaseIndex1(bankArr[30]);
        bank.setHash_h1(bankArr[31]);
        bank.setH_Response1(bankArr[32]);
        bank.setComSAddVTripleIndex(bankArr[33]);
        bank.sethBaseIndex2(bankArr[34]);
        bank.setHash_h2(bankArr[35]);
        bank.setH_Response2(bankArr[36]);
        bank.setA(bankArr[37]);
        bank.setS(bankArr[38]);
        bank.setyHash(bankArr[39]);
        bank.setzHash(bankArr[40]);
        bank.setxHash(bankArr[41]);
        bank.setT1(bankArr[42]);
        bank.setT2(bankArr[43]);
        bank.setLx(ReadFileLxAndRx.readFile(BankStr+"FileLx.txt"));
//        System.out.println(bank1.getLx());
        bank.setRx(ReadFileLxAndRx.readFile(BankStr+"FileRx.txt"));
        bank.setT(bankArr[44]);
        bank.setTaox(bankArr[45]);
        bank.setMiu(bankArr[46]);
    }



    /**
     *
     * @param treeMap
     * @param inBankStr 接受款的银行
     * @throws IOException
     */
    public static void mappingGen(TreeMap<String, Bank> treeMap, String[] banks, String outBankStr, String inBankStr) throws IOException {
        //银行与银行里边的每个字段对应是key-value,其中key是字符串"bank1", value是bank对象
        //新建TreeMap,按键值进行排序
//        TreeMap<String, Bank> treemap = new TreeMap<>();
//        treemap.put("bank1", bank1);
//        treemap.put("bank2", bank2);
//        treemap.put("bank3", bank3);
//        treemap.put("bank4", bank4);
//        System.out.println(treemap);
//        for(String key: treemap.keySet()){
//            System.out.println("key:" + key + "\t" + "value:" + treemap.get(key));
//        }

        String str = JSON.toJSONString(treeMap);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = "{" + "\"time\":" + "\"" + df.format(new Date()) + "\""+",\"bank1\"";
        str = str.replace("{\"bank1\"",time);


        //下面调用BalanceAndBitProofParam()，把生成的json字符串进行拼接
        String str1 = BalanceAndBitProofParam(banks, outBankStr, inBankStr);
        str1 = str1.replaceFirst("\\{","},");
//        System.out.println(str1);
        //将str字符串的最后一个}替换成str1
        str = str.replace("}}",str1);

        //测试阶段，将生成的10条交易全部保存进oneTransaction.txt中，后序直接进行验证10条信息
        FileWriter file1 = new FileWriter("oneTransaction.txt", true);
        file1.write(str+"\n");
        file1.close();


//        str = str.replaceAll("\"","\\\\\"");
//        System.out.println(getType(str));
//        System.out.println(str);
//        FileWriter file1 = new FileWriter("bank11000元.txt");
//        file1.write(str);
//        file1.close();

        /**
         * 发送各个银行承诺的随机数到各个银行
         */
//        SendMain.send();
        /**
         * 下面开始调用上链的invoke函数，数据上链
         */
        //链上保存的交易信息是键值对的形式，所以键我们设置成id,让它自增，然后转换成String形式，传给invoke函数作为参数
//        numId++;
//        String keyNum = String.valueOf(numId);

//        System.out.println(numId);
        Invoke.invoke(Connection.getNetwork(), str);

    }



    /**
     * 该函数是为了把证明资产平衡证明和01比特证明的字段添加到具体交易信息的后部去，需要新建BankBalanceAndBitZKP类，然后把该类添加哈希表，转换成json,
     * 之后拼接到交易信息的json字符串之后。
     * 这里的str主要是在证明指数相等的时候，记录把钱转给了哪个银行。
     */
    public static String BalanceAndBitProofParam(String[] banks, String outBankStr,String inBankStr) throws IOException {
        String str1 = null;

        //零知识证明的参数表
        BankBalanceAndBitZKP ZKPPraam = new BankBalanceAndBitZKP();
        BigInteger[] arr = balanceZkpGenerate.generateBalanceProofField(banks, outBankStr, inBankStr, ReadParameter.readBase());
        ZKPPraam.setC1(arr[0]);
        ZKPPraam.setD(arr[1]);
        ZKPPraam.setD1(arr[2]);
        ZKPPraam.setD2(arr[3]);
        ZKPPraam.setC2(arr[4]);
        ZKPPraam.setM(arr[5]);
        ZKPPraam.setM1(arr[6]);
        ZKPPraam.setM2(arr[7]);
        ZKPPraam.setTwoObject(arr[8]);
        ZKPPraam.setAllR2_Sum(arr[9]);
        ZKPPraam.setH2(arr[10]);
        ZKPPraam.setN(arr[11]);
        ZKPPraam.setN1(arr[12]);
        ZKPPraam.setN2(arr[13]);

        TreeMap<String, BankBalanceAndBitZKP> treemap1 = new TreeMap<>();
        treemap1.put("balProofParam", ZKPPraam);

        str1 = JSON.toJSONString(treemap1);

        return str1;
    }


}
