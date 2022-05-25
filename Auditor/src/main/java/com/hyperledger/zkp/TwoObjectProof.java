package com.hyperledger.zkp;

import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName TwoObjectProof.java
 * @Description TODO
 * @createTime 2021年11月08日 17:11:00
 *
 * 一条交易只有两个对象参与
 */
public interface TwoObjectProof {

    //获取每一个对象的bitCom，相乘之后，与新的承诺进行比较
    static boolean twoObjectCompare(BigInteger[] bitCom, BigInteger bankNum,
                                    BigInteger r2Sum, BigInteger[] arr){
        boolean result;
        BigInteger temp1 = BigInteger.valueOf(1);
//        BigInteger temp1 = bitCom1.multiply(bitCom2).multiply(bitCom3).multiply(bitCom4).mod(arr[0]);
        for (int i = 0; i < bitCom.length; i++) {
            temp1 = temp1.multiply(bitCom[i]).mod(arr[0]);
        }
        BigInteger temp2 = arr[2].modPow(bankNum, arr[0]).multiply(arr[3].modPow(r2Sum, arr[0])).mod(arr[0]);
        if(temp1.equals(temp2)){
            result = true;
        }else{
            result = false;
        }
        return result;
    }
}
