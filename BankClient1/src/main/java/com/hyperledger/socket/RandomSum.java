package com.hyperledger.socket;

import com.alibaba.fastjson.JSON;
import com.hyperledger.bank.RandomClass;

import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName RandomSun.java
 * @Description TODO
 * @createTime 2021年11月13日 19:40:00
 */
public class RandomSum {

    public static String sum(String bank, BigInteger[] arr){
        /**
         * 1、首先读取bankAllRandom.txt文件中的数据，保存到string[]
         */
        String[] oneInfor = ReadStringArray.ReadStrArray(bank + "AllRandom.txt");

        /**
         * 处理每一条数据，把信息转换成RandomClass,然后进行数据操作
         */
        BigInteger r1 = new BigInteger("0");
        BigInteger r2 = new BigInteger("0");
        BigInteger x = new BigInteger("0");
        BigInteger y = new BigInteger("0");
        BigInteger z = new BigInteger("0");
        for (int i = 0; i < oneInfor.length; i++) {
            RandomClass rc = JSON.parseObject(oneInfor[i], RandomClass.class);
            r1 = r1.add(rc.getR1());
            r2 = r2.add(rc.getR2());
            x = x.add(rc.getX());
            y = y.add(rc.getY());
            z = z.add(rc.getZ());
        }
        /**
         * 对最后的结果进行取模
         */
        r1 = r1.mod(arr[1]);
        r2 = r2.mod(arr[1]);
        x = x.mod(arr[1]);
        y = y.mod(arr[1]);
        z = z.mod(arr[1]);

        /**
         * 保存最后的值到本地，并且发送给审计员
         */
        RandomClass randomClass = new RandomClass(r1, r2, x, y, z);
        String str = JSON.toJSONString(randomClass);
        return str;
    }
}
