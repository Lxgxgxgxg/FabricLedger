package com.hyperledger.commit;

import com.alibaba.fastjson.JSON;
import com.hyperledger.bank.RandomClass;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName balanceZkpGenerate.java
 * @Description TODO
 * @createTime 2021年11月08日 10:32:00
 */
public interface balanceZkpGenerate {


    static BigInteger[] generateBalanceProofField(String[] banks, String outBankStr, String inBankStr, BigInteger[] arr) throws IOException {
        Random random = new Random();
        BigInteger s = new BigInteger("898363816870");

        //读取此笔交易的金额，银行1的交易金额的随机数，银行2，银行3，银行4的交易承诺的随机数
        FileReader file = new FileReader("Transactionamount.txt");
        BufferedReader br = new BufferedReader(file);
        BigInteger m = null;
        String mInput = br.readLine();
        try{
            m = new BigInteger(mInput);
//            System.out.println(m);
        }catch (NumberFormatException | NullPointerException e){
            System.out.println("读取银行1转账金额失败！balanceZkpGenerate");
        }


        /**
         * 新建两个数组，依次读取每一个银行的随机数
         */
        int n = banks.length;
        BigInteger[] bankR1 = new BigInteger[n];
        BigInteger[] bankR2 = new BigInteger[n];

        for (int i = 0; i < n; i++) {
            FileReader file1 = new FileReader(banks[i]+"random.txt");
            BufferedReader br1 = new BufferedReader(file1);
//            BigInteger bank1r1 = null;
//            BigInteger bank1r2 = null;
            String json1 = br1.readLine();
            //读取进来的是json字符串，把读取进来的字符串转换成Random类
            RandomClass randomclass1 = JSON.parseObject(String.valueOf(json1), RandomClass.class);
            try {
                bankR1[i] = randomclass1.getR1();
                bankR2[i] = randomclass1.getR2();
            }catch (NumberFormatException | NullPointerException e){
                System.out.println("读取"+banks[i]+"的随机数失败！");
            }
        }

//        FileReader file1 = new FileReader("bank1random.txt");
//        BufferedReader br1 = new BufferedReader(file1);
//        BigInteger bank1r1 = null;
//        BigInteger bank1r2 = null;
//        String json1 = br1.readLine();
//        //读取进来的是json字符串，把读取进来的字符串转换成Random类
//        RandomClass randomclass1 = JSON.parseObject(String.valueOf(json1), RandomClass.class);
//        try {
//            bank1r1 = randomclass1.getR1();
//            bank1r2 = randomclass1.getR2();
//        }catch (NumberFormatException | NullPointerException e){
//            System.out.println("读取银行1的随机数失败！");
//        }
//
//        FileReader file2 = new FileReader("bank2random.txt");
//        BufferedReader br2 = new BufferedReader(file2);
//        BigInteger bank2r1 = null;
//        BigInteger bank2r2 = null;
//        String json2 = br2.readLine();
//        //读取进来的是json字符串，把读取进来的字符串转换成Random类
//        RandomClass randomclass2 = JSON.parseObject(String.valueOf(json2), RandomClass.class);
//        try {
//            bank2r1 = randomclass2.getR1();
//            bank2r2 = randomclass2.getR2();
//        }catch (NumberFormatException | NullPointerException e){
//            System.out.println("读取银行2的随机数失败！");
//        }
//
//        FileReader file3 = new FileReader("bank3random.txt");
//        BufferedReader br3 = new BufferedReader(file3);
//        BigInteger bank3r1 = null;
//        BigInteger bank3r2 = null;
//        String json3 = br3.readLine();
//        //读取进来的是json字符串，把读取进来的字符串转换成Random类
//        RandomClass randomclass3 = JSON.parseObject(String.valueOf(json3), RandomClass.class);
//        try {
//            bank3r1 = randomclass3.getR1();
//            bank3r2 = randomclass3.getR2();
//        }catch (NumberFormatException | NullPointerException e){
//            System.out.println("读取银行3的随机数失败！");
//        }
//
//        FileReader file4 = new FileReader("bank4random.txt");
//        BufferedReader br4 = new BufferedReader(file4);
//        BigInteger bank4r1 = null;
//        BigInteger bank4r2 = null;
//        String json4 = br4.readLine();
//        //读取进来的是json字符串，把读取进来的字符串转换成Random类
//        RandomClass randomclass4 = JSON.parseObject(String.valueOf(json4), RandomClass.class);
//        try {
//            bank4r1 = randomclass4.getR1();
//            bank4r2 = randomclass4.getR2();
//        }catch (NumberFormatException | NullPointerException e){
//            System.out.println("读取银行4的随机数失败！");
//        }

        /**
         * 首先进行验证的是交易金额承诺的平衡性
         */
        BigInteger a1 = GetRandom.getRandom(arr);
        BigInteger a2 = GetRandom.getRandom(arr);
        BigInteger a3 = GetRandom.getRandom(arr);

        //产生两个承诺
        //求新的两个承诺
        BigInteger Com1 = (arr[2].modPow(a1, arr[0])).multiply((arr[3]).modPow(a2, arr[0])).mod(arr[0]);
        BigInteger Com2 = (arr[2].modPow(a1, arr[0])).multiply((arr[3]).modPow(a3, arr[0])).mod(arr[0]);
        String str = Com1.toString() + Com2.toString();
        String c = new BigInteger(HashFunction.getSHA256StrJava(str)).mod(arr[1]).toString();
        BigInteger c1 = new BigInteger(c);

        BigInteger D = (a1.add(m.multiply(c1))).mod(arr[1]);
        //银行1的承诺
//        BigInteger D1 = (a2.add(bank1r1.multiply(c1))).mod(arr[1]);
        BigInteger D1 = BigInteger.valueOf(1);
        BigInteger r1_sum = BigInteger.valueOf(0);
        for (int i = 0; i < n; i++) {
            if (banks[i].equals(outBankStr)){
                D1 = (a1.add(bankR1[i].multiply(c1))).mod(arr[1]);
            }else {
                r1_sum = r1_sum.add(bankR1[i]);
            }
        }
        //计算银行234的交易承诺的随机数和
//        BigInteger r1_sum = bank2r1.add(bank3r1).add(bank4r1);
        BigInteger D2 = (a3.add(r1_sum.multiply(c1))).mod(arr[1]);


        /**
         * 验证bit承诺的值相等
         */
        BigInteger a4 = GetRandom.getRandom(arr);
        BigInteger a5 = GetRandom.getRandom(arr);
        BigInteger a6 = GetRandom.getRandom(arr);

        //产生两个新的承诺承诺
        BigInteger Com3 = (arr[2].modPow(a4, arr[0])).multiply((arr[3]).modPow(a5, arr[0])).mod(arr[0]);
        BigInteger Com4 = (arr[2].modPow(a4, arr[0])).multiply((arr[3]).modPow(a6, arr[0])).mod(arr[0]);
        String str1 = Com3.toString() + Com4.toString();
        String h = new BigInteger(HashFunction.getSHA256StrJava(str1)).mod(arr[1]).toString();
        BigInteger h1 = new BigInteger(h);


        BigInteger M = (a4.add(h1.multiply(BigInteger.valueOf(1)))).mod(arr[1]);
        //银行1的比特承诺
//        BigInteger M1 = (a5.add(bank1r2.multiply(h1))).mod(arr[1]);
        //计算银行234的比特承诺的随机数和
//        BigInteger r2_sum = bank2r2.add(bank3r2).add(bank4r2);
        BigInteger M1 = BigInteger.valueOf(1);
        BigInteger r2_sum = BigInteger.valueOf(0);
        for (int i = 0; i < n; i++) {
            if (banks[i].equals(outBankStr)){
                M1 = (a5.add(bankR2[i].multiply(h1))).mod(arr[1]);
            }else {
                r2_sum = r2_sum.add(bankR2[i]);
            }
        }
        BigInteger M2 = (a6.add(r2_sum.multiply(h1))).mod(arr[1]);

        /**
         * com和bitCom结合之后证明资产只转给一个人
         */
        BigInteger a7 = GetRandom.getRandom(arr);
        BigInteger a8 = GetRandom.getRandom(arr);
        BigInteger a9 = GetRandom.getRandom(arr);

        //产生两个新的承诺
        BigInteger Com5 = arr[2].modPow(a7, arr[0]).multiply(arr[3].modPow(a8, arr[0])).mod(arr[0]);
        BigInteger Com6 = arr[2].modPow(a7, arr[0]).multiply(arr[3].modPow(a9, arr[0])).mod(arr[0]);
        BigInteger h2 = new BigInteger(HashFunction.getSHA256StrJava(Com5.toString() + Com6.toString())).mod(arr[1]);
        BigInteger r3_sum = BigInteger.valueOf(1);   //计算其他银行的随机数

//        if(bankStr.equals("bank2")){
//            r3_sum = (bank2r1.multiply(s.add(BigInteger.valueOf(1))).add(bank3r1.multiply(s)).add(bank4r1.multiply(s))).mod(arr[1]);
//        } else if (bankStr.equals("bank3")){
//            r3_sum = (bank3r1.multiply(s.add(BigInteger.valueOf(1))).add(bank2r1.multiply(s)).add(bank4r1.multiply(s))).mod(arr[1]);
//        }else {
//            r3_sum = (bank4r1.multiply(s.add(BigInteger.valueOf(1))).add(bank3r1.multiply(s)).add(bank2r1.multiply(s))).mod(arr[1]);
//        }

        for (int i = 0; i < n; i++) {
            if (banks[i].equals(outBankStr)){
                continue;
            }else if (banks[i].equals(inBankStr)){
                r3_sum = (r3_sum.multiply(bankR1[i].multiply(s.add(BigInteger.valueOf(1))))).mod(arr[1]);
            }else {
                r3_sum = (r3_sum.multiply(bankR1[i].multiply(s))).mod(arr[1]);
            }
        }

        BigInteger temp1 = m.multiply(s.add(BigInteger.valueOf(1)));    //bank1承诺的隐藏值，m(1+s)
//        BigInteger temp2 = (bank1r1.multiply(s.add(BigInteger.valueOf(1))));    //随机数 bankr1(s+1)
        BigInteger temp2 = BigInteger.valueOf(1);
        for (int i = 0; i < n; i++) {
            if (banks[i].equals(outBankStr)){
                temp2 = (bankR1[i].multiply(s.add(BigInteger.valueOf(1)))).mod(arr[1]);
            }
        }
        BigInteger N = (a7.add(h2.multiply(temp1))).mod(arr[1]);
        BigInteger N1 = (a8.add(h2.multiply(temp2))).mod(arr[1]);
        BigInteger N2 = (a9.add(h2.multiply(r3_sum))).mod(arr[1]);

        //声明一个数组，把值保存进去
        BigInteger[] arrzkp = new BigInteger[14];
        arrzkp[0] = c1;
        arrzkp[1] = D;
        arrzkp[2] = D1;
        arrzkp[3] = D2;
        arrzkp[4] = h1;
        arrzkp[5] = M;
        arrzkp[6] = M1;
        arrzkp[7] = M2;

        /**
         * 在给数组添加两个元素，一个放置2，一个放置所有比特承诺致盲因子和
         */
//        BigInteger r2_allsum = r2_sum.add(bank1r2);
        BigInteger r2_allSum = BigInteger.valueOf(0);
        for (int i = 0; i < n; i++) {
            if (banks[i].equals(outBankStr)){
                r2_allSum = r2_sum.add(bankR2[i]);
            }
        }
        arrzkp[8] = BigInteger.valueOf(2);
        arrzkp[9] = r2_allSum;
        arrzkp[10] = h2;
        arrzkp[11] = N;
        arrzkp[12] = N1;
        arrzkp[13] = N2;

        return arrzkp;
    }
}
