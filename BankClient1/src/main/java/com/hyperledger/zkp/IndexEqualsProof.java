package com.hyperledger.zkp;

import com.hyperledger.commit.HashFunction;

import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName IndexEqualsProof.java
 * @Description TODO
 * @createTime 2021年11月08日 17:29:00
 */
public interface IndexEqualsProof {


    //指数相等证明
    static boolean indexEqualsProof(BigInteger com1, BigInteger com2, BigInteger com3, BigInteger t, BigInteger s1, BigInteger s2, BigInteger s3, BigInteger[] arr){
        boolean result;
        String str2 = arr[3].toString() + arr[2].toString() + com1.toString()+ t.toString();
        BigInteger c = new BigInteger(HashFunction.getSHA256StrJava(str2)).mod(arr[1]);
//        System.out.println(c);
        BigInteger t1 = arr[3].modPow(s1, arr[0]).multiply(arr[2].modPow(s2, arr[0])).multiply(com1.modPow(s3, arr[0])).multiply((com2.multiply(com3)).modPow(c,arr[0])).mod(arr[0]);
        BigInteger t2 = s1.multiply(BigInteger.valueOf(0)).add(s2.multiply(BigInteger.valueOf(1))).add(s3.multiply(BigInteger.valueOf(-1)));
//        System.out.println("t:"+t);
//        System.out.println("t1:"+t1);
//        System.out.println("t2:"+t2);
        if (t.equals(t1) && t2.equals(BigInteger.valueOf(0))){
            result = true;
        }else{
            result = false;
        }
        return result;
    }

}
