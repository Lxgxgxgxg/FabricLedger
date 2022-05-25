package com.hyperledger.socket;

import com.alibaba.fastjson.JSON;
import com.hyperledger.bank.AssetsInformation;
import com.hyperledger.bank.RandomClass;
import com.hyperledger.commit.Commit;
import com.hyperledger.commit.ReadBigInteger;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName SendAssetsInformationToAuditor.java
 * @Description TODO
 * @createTime 2021年11月13日 19:35:00
 */
public class SendAssetsInformationToAuditor {

    public static String send(String bank, BigInteger[] arr) {
        /**
         * 读取资产和，r1，交易次数， r2, 二倍资产和， x， 三倍资产和， y， 四倍资产和， z
         */
        /**
         * 读取每个资产
         */
        BigInteger comSum = ReadBigInteger.readBigInteger(bank+"AssetsRecord.txt");
        int time = ReadBigInteger.readBigInteger(bank + "TransactionTime.txt").intValue();
        BigInteger doubleComSum = ReadBigInteger.readBigInteger(bank + "DoubleComSum.txt");
        BigInteger tripleComSum = ReadBigInteger.readBigInteger(bank + "TripleComSum.txt");
        BigInteger quadraComSum = ReadBigInteger.readBigInteger(bank + "QuadraComSum.txt");
        String temp = RandomSum.sum(bank, arr);
        RandomClass randomClass = JSON.parseObject(temp, RandomClass.class);
        BigInteger r1 = randomClass.getR1();
        BigInteger r2 = randomClass.getR2();
        BigInteger x = randomClass.getX();
        BigInteger y = randomClass.getY();
        BigInteger z = randomClass.getZ();
        AssetsInformation infor = new AssetsInformation(comSum, r1, time, r2, doubleComSum, x,
                tripleComSum, y, quadraComSum, z);
        String strInfor = JSON.toJSONString(infor);
        /**
         * 保存到当地
         */
        try {
            FileWriter file = new FileWriter("randomSum.txt");
            file.write(strInfor+"\n");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strInfor;
    }
}
