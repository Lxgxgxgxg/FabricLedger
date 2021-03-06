package com.hyperledger.commit.getRangeProof;

import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName GetVectorMul.java
 * @Description TODO
 * @createTime 2021年11月05日 17:40:00
 */
public interface GetVectorMul {


    /**
     * 向量对应位相乘，输出也是一个新的向量
     */
    static BigInteger[] getVectorMul(BigInteger[] v1, BigInteger[] v2, BigInteger[] arr){
        BigInteger[] newVector = new BigInteger[v1.length];
        for (int i = 0; i<v1.length;i++){
            newVector[i] = v1[i].multiply(v2[i]).mod(arr[1]);
        }
        return newVector;
    }
}
