package com.hyperledger.zkp;

import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName GetInnerProduct.java
 * @Description TODO
 * @createTime 2021年11月05日 17:40:00
 */
public interface GetInnerProduct {


    /**
     * 得到向量的内积
     */
    static BigInteger getInnerProduct(BigInteger[] v1, BigInteger[] v2, BigInteger[] arr){
        BigInteger innerProduct = new BigInteger("0");
        for (int i = 0;i<v1.length;i++){
            innerProduct = (innerProduct.add(v1[i].multiply(v2[i]))).mod(arr[1]);
        }
        return innerProduct;
    }
}
