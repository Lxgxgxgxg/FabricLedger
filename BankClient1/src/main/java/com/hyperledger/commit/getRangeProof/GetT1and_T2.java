package com.hyperledger.commit.getRangeProof;

import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName GetT1and_T2.java
 * @Description TODO
 * @createTime 2021年11月05日 17:31:00
 */
public interface GetT1and_T2 {


    /**
     * 零知识体系构造,此时需要l(x)和r(x)做内积，求出X一次方和系数t1,二次项的系数t2，
     */
    //在笔记本上自己手算验算出t1和t2,首先验证n的位数为107
    /**
     * 求t1,t2
     */
    static BigInteger getT1(BigInteger[] al, BigInteger[] ar, BigInteger[] Sl, BigInteger[] Sr, BigInteger y, BigInteger z, BigInteger tao1, BigInteger[] arr){
        BigInteger[] yVector = GetValueN.getValueN(y,al,arr);

        BigInteger[] zVector = new BigInteger[al.length];
        for (int i = 0;i<al.length;i++){
            zVector[i] = z.multiply(BigInteger.valueOf(1));
        }
//        Verify.display(zVector);
        BigInteger T1 = null;
        BigInteger temp1 = null;
        BigInteger temp2 = null;
        BigInteger temp3 = null;
        //得到temp1的内积左边,右边是Sl
        BigInteger[] temp1Left = new BigInteger[ar.length];
        for (int i = 0;i<ar.length;i++){
            temp1Left[i] = ar[i].add(zVector[i]);
        }
        BigInteger[] temp1Left1 = GetVectorMul.getVectorMul(temp1Left, yVector,arr);
//        Verify.display(temp1Left);
//        Verify.display(temp1Left1);
        temp1 = GetInnerProduct.getInnerProduct(temp1Left1, Sl,arr);
//        System.out.println(temp1);

        //得到temp2左边，右边Sl
        BigInteger[] temp2Left = new BigInteger[Sl.length];

        //得到2的n次方
        BigInteger[] twoN = GetValueN.getValueN(BigInteger.valueOf(2),al,arr);
//        Verify.display(twoN);
        for (int i = 0; i < temp2Left.length;i++){
            temp2Left[i] = (z.pow(2).multiply(twoN[i])).mod(arr[1]);
        }
//        Verify.display(temp2Left);

        temp2 = GetInnerProduct.getInnerProduct(temp2Left, Sl,arr);
//        System.out.println(temp2);
        //d得到temp3左边，右边
        BigInteger[] temp3Left = new BigInteger[al.length];
        for (int i = 0;i<al.length;i++){
            temp3Left[i] = al[i].subtract(zVector[i]).mod(arr[1]);
        }
//        Verify.display(temp3Left);
        BigInteger[] temp3Right = GetVectorMul.getVectorMul(Sr, yVector,arr);
//        Verify.display(temp3Right);
        temp3 = GetInnerProduct.getInnerProduct(temp3Left, temp3Right,arr);
//        System.out.println(temp3);

        BigInteger t1 = (temp1.add(temp2).add(temp3)).mod(arr[1]);

        T1 = arr[2].modPow(t1, arr[0]).multiply(arr[3].modPow(tao1, arr[0])).mod(arr[0]);

        return T1;
    }

    /**
     * 得到T2
     */
    static BigInteger getT2(BigInteger[] Sl, BigInteger[] Sr, BigInteger y, BigInteger tao2, BigInteger[] arr){
        BigInteger t2 = null;
        BigInteger[] t2Right = null;
//        BigInteger y = new BigInteger(Verify.readFile("y.txt"));
        //生成y的N次方向量
        BigInteger[] yVector = GetValueN.getValueN(y,Sl,arr);
//        Verify.display(yVector);
        t2Right = GetVectorMul.getVectorMul(Sr, yVector,arr);
//        Verify.display(t2Right);
        t2 = GetInnerProduct.getInnerProduct(Sl, t2Right,arr);

        BigInteger T2 = arr[2].modPow(t2, arr[0]).multiply(arr[3].modPow(tao2, arr[0])).mod(arr[0]);
        return T2;
    }
}
