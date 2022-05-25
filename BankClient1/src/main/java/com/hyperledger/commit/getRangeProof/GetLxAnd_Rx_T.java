package com.hyperledger.commit.getRangeProof;

import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName GetLxAnd_Rx_T.java
 * @Description TODO
 * @createTime 2021年11月05日 17:30:00
 */
public interface GetLxAnd_Rx_T {


    /**
     * 验证着给证明者挑战x，生成计算代入x的多项式
     */
    //得到lx
    static BigInteger[] getLx(BigInteger[] al, BigInteger[] Sl, BigInteger x, BigInteger z, BigInteger[] arr){

        BigInteger[] zVector = new BigInteger[al.length];
        for (int i = 0;i<al.length;i++){
            zVector[i] = z;
        }
        //生成l(x)
        BigInteger[] lxLeft = new BigInteger[al.length];
        for (int i = 0;i<al.length;i++){
            lxLeft[i] = al[i].subtract(zVector[i]).mod(arr[1]);
        }
//        Verify.display(lxLeft);
        BigInteger[] lxRight = new BigInteger[Sl.length];
        for (int i = 0; i<Sl.length;i++){
            lxRight[i] = Sl[i].multiply(x);
        }
//        Verify.display(lxRight);
        //l(x)
        BigInteger[] lx = new BigInteger[al.length];
        for (int i = 0;i<al.length;i++){
            lx[i] = lxLeft[i].add(lxRight[i]).mod(arr[1]);
        }
//        Verify.display(lx);
        return lx;
    }

    //得到rx
    static BigInteger[] getRx(BigInteger[] ar, BigInteger[] Sr, BigInteger x, BigInteger y, BigInteger z, BigInteger[] arr){

        //生成y的N次方向量
        BigInteger[] yVector = GetValueN.getValueN(y,ar,arr);
        BigInteger[] zVector = new BigInteger[ar.length];
        for (int i = 0;i<ar.length;i++){
            zVector[i] = z;
        }
        //生成r(x)
        BigInteger[] rxLeft;
        BigInteger[] temp1Left = new BigInteger[ar.length];
        for (int i = 0;i<ar.length;i++){
            temp1Left[i] = (ar[i].add(zVector[i]).add(Sr[i].multiply(x))).mod(arr[1]);
        }
//        Verify.display(temp1Left);
        rxLeft = GetVectorMul.getVectorMul(temp1Left, yVector,arr);
//        Verify.display(rxLeft);
        BigInteger[] rxRight = new BigInteger[ar.length];
        //得到2的递增次方相邻
        BigInteger[] twoN = GetValueN.getValueN(BigInteger.valueOf(2),ar,arr);
        for (int i = 0; i < rxRight.length;i++){
            rxRight[i] = (z.pow(2).multiply(twoN[i])).mod(arr[1]);
        }
//        Verify.display(rxRight);
        BigInteger[] rx = new BigInteger[ar.length];
        for (int i = 0; i < ar.length;i++){
            rx[i] = rxLeft[i].add(rxRight[i]);
        }
//        Verify.display(rx);
        return rx;
    }

    /**
     * 计算l(x),r(x)的内积
     */
    static BigInteger getT(BigInteger[] lx,BigInteger[] rx, BigInteger[] arr){
        BigInteger t = GetInnerProduct.getInnerProduct(lx, rx, arr);
        return t;
    }
}
