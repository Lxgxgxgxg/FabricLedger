package com.hyperledger.commit.getRangeProof;

import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName GetValueN.java
 * @Description TODO
 * @createTime 2021年11月05日 17:37:00
 */
public interface GetValueN {

    /**
     *递乘的向量
     */
    static BigInteger[] getValueN(BigInteger x, BigInteger[] al, BigInteger[] arr){
        BigInteger[] Vector = new BigInteger[al.length];
        Vector[0] = new BigInteger("1");
        for (int i = 1;i<al.length;i++){
            Vector[i] = Vector[i-1].multiply(x).mod(arr[1]);
        }
        return Vector;
    }
}
