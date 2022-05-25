package com.hyperledger.commit;

import com.hyperledger.activemq.SendRandomToOtherBanks;
import com.hyperledger.commit.getRangeProof.*;
import com.hyperledger.socket.SendMessage;

//import javax.jms.JMSException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName Commit.java
 * @Description TODO
 * @createTime 2021年11月05日 15:24:00
 */
public class Commit {

    //新建承诺方法
    public static BigInteger[] commitSelfBank(String[] banks, String[] IP, Integer[] port, String outBankStr, BigInteger m, BigInteger[] arr) throws IOException {
        //Pedersen承诺的形式c = g^m * h^r (m是要隐藏的金额，r是致盲因子)
        //产生random c，在2到q - 2之间
        SecureRandom random = new SecureRandom();
        BigInteger r1 = GetRandom.getRandom(arr);
        BigInteger r2 = GetRandom.getRandom(arr);
        BigInteger x = GetRandom.getRandom(arr);
        BigInteger y = GetRandom.getRandom(arr);
        BigInteger z = GetRandom.getRandom(arr);

        //下面的字段用于bitComZKP（非0即1）的随机数
        BigInteger a = GetRandom.getRandom(arr);
        BigInteger s = GetRandom.getRandom(arr);
        BigInteger t = GetRandom.getRandom(arr);
        BigInteger challenge = GetRandom.getRandom(arr);

        /**
         * 下面的字段用于证明资产只转给一个人的中间变量,其中的s选取一个定值，就以之前出现的s = 898363816870
         */
        //证明g的s次方的随机数
        BigInteger sTemp = new BigInteger("898363816870");  //s
        //证明com的1/0+s次方的随机数
        BigInteger rToOne = GetRandom.getRandom(arr);
        BigInteger sAndBinTemp = GetRandom.getRandom(arr);

        //计算承诺
        BigInteger[] comarr = new BigInteger[47];

        /**
         * 读取银行1的资产
         */
        BigInteger assets = ReadBigInteger.readBigInteger(outBankStr+"AssetsRecord.txt");
//        BigInteger assets = ReadBigInteger.readBigInteger("bank1AssetsRecord.txt");
        if (assets.compareTo(m) != 1){
            System.out.println("余额不足，请重新选择 ！");
//            Thread.currentThread().interrupt();
//            System.exit(0);
            CommitGenerateAll.election(banks, IP, port);
        }else {
            //交易金额的承诺
            comarr[0] = (arr[2].modPow(m, arr[0])).multiply((arr[3]).modPow(r1, arr[0])).mod(arr[0]);

            //比特承诺，是否参与了交易，1为参与交易，0为没有参与交易
            //        comarr[1] = (arr[2].modPow(BigInteger.valueOf(0), arr[0])).multiply((arr[3]).modPow(r2, arr[0])).mod(arr[0]);
            comarr[1] = (arr[2].modPow(BigInteger.valueOf(1), arr[0])).multiply((arr[3]).modPow(r2, arr[0])).mod(arr[0]);

            //是否为交易的发起者
            comarr[2] = BigInteger.valueOf(1);
            /**
             * 银行作为交易的发起者，参与交易的次数加1
             */
//            BigInteger transactionTime = ReadBigInteger.readBigInteger("bank1TransactionTime.txt");
            BigInteger transactionTime = ReadBigInteger.readBigInteger(outBankStr+"TransactionTime.txt");
//            FileWriter fileTransactionTime = new FileWriter("bank1TransactionTime.txt");
            FileWriter fileTransactionTime = new FileWriter(outBankStr+"TransactionTime.txt");
            fileTransactionTime.write(transactionTime.add(BigInteger.valueOf(1))+"\n");
            fileTransactionTime.close();

            //隐藏值的二次方的承诺
            comarr[3] = (arr[2].modPow(m.pow(2), arr[0])).multiply((arr[3]).modPow(x, arr[0])).mod(arr[0]);
            /**
             * 交易额的平方写进文件
             */
//            BigInteger doubleComSum = ReadBigInteger.readBigInteger("bank1DoubleComSum.txt");
            BigInteger doubleComSum = ReadBigInteger.readBigInteger(outBankStr+"DoubleComSum.txt");
//            FileWriter fileDoubleComSum = new FileWriter("bank1DoubleComSum.txt");
            FileWriter fileDoubleComSum = new FileWriter(outBankStr+"DoubleComSum.txt");
            fileDoubleComSum.write(doubleComSum.add(m.pow(2))+"\n");
            fileDoubleComSum.close();

            //隐藏值的三次方的承诺
            comarr[4] = (arr[2].modPow(m.pow(3), arr[0])).multiply((arr[3]).modPow(y, arr[0])).mod(arr[0]);
            /**
             * 交易金额的立方写进文件
             */
//            BigInteger tripleComSum = ReadBigInteger.readBigInteger("bank1TripleComSum.txt");
            BigInteger tripleComSum = ReadBigInteger.readBigInteger(outBankStr+"TripleComSum.txt");
//            FileWriter fileTripleComSum = new FileWriter("bank1TripleComSum.txt");
            FileWriter fileTripleComSum = new FileWriter(outBankStr+"TripleComSum.txt");
            fileTripleComSum.write(tripleComSum.add(m.pow(3))+"\n");
            fileTripleComSum.close();

            //隐藏值的四次方的承诺
            comarr[5] = (arr[2].modPow(m.pow(4), arr[0])).multiply((arr[3]).modPow(z, arr[0])).mod(arr[0]);
            /**
             * 交易金额的四次方写进文件
             */
//            BigInteger quadraComSum = ReadBigInteger.readBigInteger("bank1QuadraComSum.txt");
            BigInteger quadraComSum = ReadBigInteger.readBigInteger(outBankStr+"QuadraComSum.txt");
//            FileWriter fileQuadraComSum = new FileWriter("bank1QuadraComSum.txt");
            FileWriter fileQuadraComSum = new FileWriter(outBankStr+"QuadraComSum.txt");
            fileQuadraComSum.write(quadraComSum.add(m.pow(4))+"\n");
            fileQuadraComSum.close();

            //下面的字段是用于证明比特承诺非0即1的字段
            //中间承诺变量1
            comarr[6] = (arr[2].modPow(a, arr[0]).multiply(arr[3].modPow(s, arr[0]))).mod(arr[0]);
            //中间承诺变量2
            comarr[7] = (arr[2].modPow(a.multiply(BigInteger.valueOf(1)), arr[0]).multiply(arr[3].modPow(t, arr[0]))).mod(arr[0]);
            //挑战
            comarr[8] = challenge;
            //挑战的哈希
            comarr[9] = new BigInteger(HashFunction.getSHA256StrJava(challenge.toString())).mod(arr[1]);
            //响应1
            comarr[10] = (challenge.multiply(BigInteger.valueOf(1)).add(a)).mod(arr[1]);
            //响应2
            comarr[11] = (r2.multiply(challenge).add(s)).mod(arr[1]);
            //响应3
            comarr[12] = (r2.multiply(challenge.subtract(comarr[10])).add(t)).mod(arr[1]);

            //下面的字段用于证明g的s次方中间变量
            comarr[13] = arr[2].modPow(sTemp, arr[0]);  //g的s次方
            BigInteger tTemp = arr[2].modPow(rToOne, arr[0]); //g的随机数次方
            String str = arr[2].toString() + comarr[13].toString() +tTemp.toString();
            comarr[14] = new BigInteger(HashFunction.getSHA256StrJava(str)).mod(arr[1]);
            comarr[15] = (sTemp.multiply(comarr[14]).add(rToOne)).mod(arr[1]);

            //bitCom和g的s次方相乘
            comarr[16] = comarr[1].multiply(comarr[13]).mod(arr[0]);
            //com的1/0+s次方
            comarr[17] = comarr[0].modPow((sTemp.add(BigInteger.valueOf(1))), arr[0]).mod(arr[0]);
            //com的随机数次方
            BigInteger tAndBinTempCom = comarr[0].modPow(sAndBinTemp, arr[0]).mod(arr[0]);
            String str1 = comarr[0].toString() + comarr[17].toString() + tAndBinTempCom.toString();
            comarr[18] = new BigInteger(HashFunction.getSHA256StrJava(str1)).mod(arr[1]);
            comarr[19] = ((sTemp.add(BigInteger.valueOf(1))).multiply(comarr[18])).add(sAndBinTemp).mod(arr[1]);

            //下面的字段证明comarr[16]g的指数和comarr[17]中gvhr的指数的相等，三个随机数
            BigInteger v1 = GetRandom.getRandom(arr);
            BigInteger v2 = GetRandom.getRandom(arr);
            BigInteger v3 = v2;

            comarr[20] = arr[3].modPow(v1, arr[0]).multiply(arr[2].modPow(v2,arr[0])).multiply(comarr[0].modPow(v3, arr[0])).mod(arr[0]);
            String str2 = arr[3].toString() + arr[2].toString() + comarr[0].toString() + comarr[20].toString();
            BigInteger hashc = new BigInteger(HashFunction.getSHA256StrJava(str2)).mod(arr[1]);
            //        System.out.println("comarr[20]:"+comarr[20]);
            //        System.out.println("hashc:" + hashc);
            comarr[21] = (v1.subtract(hashc.multiply(r2))).mod(arr[1]);
            comarr[22] = (v2.subtract(hashc.multiply(sTemp.add(BigInteger.valueOf(1))))).mod(arr[1]);
            comarr[23] = (v3.subtract(hashc.multiply(sTemp.add(BigInteger.valueOf(1))))).mod(arr[1]);

            /**
             * 下面的是证明doubleCom的隐藏值是com隐藏值的平方
             */
            comarr[24] = comarr[0].modPow(sTemp.add(m), arr[0]).mod(arr[0]);    //com的s+v次方
            comarr[25] = comarr[0].modPow(sTemp, arr[0]).mod(arr[0]);   //com的s次方
            //证明h的表达
            BigInteger index = (r1.multiply(m).subtract(x)).mod(arr[1]);
            comarr[26] = arr[3].modPow(index,arr[0]).mod(arr[0]);
            BigInteger hComRandom = new BigInteger(40,random);    //h承诺的随机数
            hComRandom = GetRandom.getRandom(arr);
            BigInteger hCom = arr[3].modPow(hComRandom, arr[0]).mod(arr[0]);
            comarr[27] = new BigInteger(HashFunction.getSHA256StrJava(arr[3].toString() + comarr[26].toString() + hCom.toString())).mod(arr[1]);
            comarr[28] = (comarr[27].multiply(index).add(hComRandom)).mod(arr[1]);

            /**
             * 下面的是证明tripleCom的隐藏值是com隐藏值的立方
             */
            comarr[29] = comarr[0].modPow(sTemp.add(m.pow(2)), arr[0]).mod(arr[0]); //com的s+v^2次方
            //证明h的表达
            BigInteger index1 = (r1.multiply(m.pow(2)).subtract(y)).mod(arr[1]);
            comarr[30] = arr[3].modPow(index1,arr[0]).mod(arr[0]);
            BigInteger hComRandom1 = new BigInteger(40,random);    //h承诺的随机数
            hComRandom1 = GetRandom.getRandom(arr);
            BigInteger hCom1 = arr[3].modPow(hComRandom1, arr[0]).mod(arr[0]);
            comarr[31] = new BigInteger(HashFunction.getSHA256StrJava(arr[3].toString() + comarr[30].toString() + hCom1.toString())).mod(arr[1]);
            comarr[32] = (comarr[31].multiply(index1).add(hComRandom1)).mod(arr[1]);

            /**
             * 下面的是证明quadraCom的隐藏值是com隐藏值的四次方
             */
            comarr[33] = comarr[0].modPow(sTemp.add(m.pow(3)), arr[0]).mod(arr[0]); //com的s+v^2次方
            //证明h的表达
            BigInteger index2 = (r1.multiply(m.pow(3)).subtract(z)).mod(arr[1]);
            comarr[34] = arr[3].modPow(index2,arr[0]).mod(arr[0]);
            BigInteger hComRandom2 = new BigInteger(40,random);    //h承诺的随机数
            hComRandom2 = GetRandom.getRandom(arr);
            BigInteger hCom2 = arr[3].modPow(hComRandom2, arr[0]).mod(arr[0]);
            comarr[35] = new BigInteger(HashFunction.getSHA256StrJava(arr[3].toString() + comarr[34].toString() + hCom2.toString())).mod(arr[1]);
            comarr[36] = (comarr[35].multiply(index2).add(hComRandom2)).mod(arr[1]);


            /**
             * 现在进行范围证明的字段生成，我们只验证v的四次方，不然全验证字段太多。v的最大值1千万，n需要94位。
             */
            BigInteger[] gVector = ReadVectorFile.readFile("gVector.txt");
            BigInteger[] hVector = ReadVectorFile.readFile("hVector.txt");
            BigInteger Alpha = GetRandom.getRandom(arr);
            BigInteger[] al = GetAl.getAl(m.pow(4));
            BigInteger[] ar = GetAr.getAr(al,arr);
            comarr[37] = GetA.getA(gVector,hVector, al, ar, Alpha, arr);
            BigInteger Rho = GetRandom.getRandom(arr);
            BigInteger[] Sl = Get_SlAnd_SrAnd_S.getSl(al, arr);
            BigInteger[] Sr = Get_SlAnd_SrAnd_S.getSr(al, arr);
            comarr[38] = Get_SlAnd_SrAnd_S.getS(gVector, hVector, Sl, Sr, Rho, arr);
            //y
            comarr[39] = new BigInteger(HashFunction.getSHA256StrJava(comarr[37].toString()+comarr[38].toString())).mod(arr[1]);
            //z
            comarr[40] = new BigInteger(HashFunction.getSHA256StrJava(comarr[37].toString()+comarr[38].toString()+comarr[39].toString())).mod(arr[1]);
            //x
            comarr[41] = new BigInteger(HashFunction.getSHA256StrJava(comarr[37].toString()+comarr[38].toString()+comarr[39].toString()+comarr[40].toString())).mod(arr[1]);
            BigInteger tao1 = GetRandom.getRandom(arr);
            comarr[42] = GetT1and_T2.getT1(al, ar, Sl, Sr, comarr[39], comarr[40], tao1, arr);
            BigInteger tao2 = GetRandom.getRandom(arr);
            comarr[43] = GetT1and_T2.getT2(Sl, Sr, comarr[39], tao2, arr);
            //lx
            BigInteger[] lx = GetLxAnd_Rx_T.getLx(al, Sl, comarr[41], comarr[40], arr);
            String lxStr = "";
            for (int i=0;i<lx.length;i++){
                lxStr = lxStr + lx[i] + ",";
            }
            //        comarr[44] = new BigInteger(lxStr);
            //lx向量不好存储，先放进文件，在读取
//            FileWriter fileLx = new FileWriter("bank1FileLx.txt");
            FileWriter fileLx = new FileWriter(outBankStr+"FileLx.txt");
            fileLx.write(lxStr+"\n");
            fileLx.close();
            //rx
            BigInteger[] rx = GetLxAnd_Rx_T.getRx(ar, Sr, comarr[41], comarr[39], comarr[40], arr);
            String rxStr = "";
            for (int i=0;i<lx.length;i++){
                rxStr = rxStr + rx[i] + ",";
            }
            //        comarr[45] = new BigInteger(rxStr);
            //t
            //rx向量不好存储，先放进文件，在读取
//            FileWriter fileRx = new FileWriter("bank1FileRx.txt");
            FileWriter fileRx = new FileWriter(outBankStr+"FileRx.txt");
            fileRx.write(rxStr+"\n");
            fileRx.close();
            comarr[44] = GetLxAnd_Rx_T.getT(lx, rx, arr);
            comarr[45] = GetTaox.getTaox(comarr[41], tao1, tao2, comarr[40], z, arr);
            comarr[46] = GetMiu.getMiu(Alpha, Rho, comarr[41], arr);


            //写入文件
            //把银行1本次转账的金额和余额也保存进文件中
            FileWriter file3 = new FileWriter("Transactionamount.txt");
//            FileWriter file2 = new FileWriter("bank1AssetsRecord.txt");
            FileWriter file2 = new FileWriter(outBankStr+"AssetsRecord.txt");
            file2.write(assets.subtract(m)+ "\n");
            file3.write(m+"\n");
            file2.close();
            file3.close();


            //        FileWriter file = new FileWriter("bank1.txt",true);
            FileWriter file1 = new FileWriter(outBankStr+"random.txt");
//            FileWriter file1 = new FileWriter("bank1random.txt");
//            FileWriter fileRandom = new FileWriter("bankAllRandom.txt", true);
            FileWriter fileRandom = new FileWriter(outBankStr+"AllRandom.txt", true);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{").append("\"r1\":").append(r1).append(",\"r2\":").append(r2).append(",\"x\":").
                    append(x).append(",\"y\":").append(y).append(",\"z\":").append(z).append("}");
            file1.write(stringBuilder+"\n");

            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("{").append("\"r1\":").append(BigInteger.valueOf(0).subtract(r1).mod(arr[1])).append(",\"r2\":").append(r2).append(",\"x\":").
                    append(x).append(",\"y\":").append(y).append(",\"z\":").append(z).append("}");
            fileRandom.write(stringBuilder1+"\n");
            //file.close();
            file1.close();
            fileRandom.close();
        }
        //写进文件的承诺和致盲因子的类型是String

//        System.out.println("成功写入文件！");
        return comarr;
    }



    public static BigInteger[] commitOtherBank(String outBankStr, String IP, Integer port, String inBankStr, BigInteger m,
                                               BigInteger[] arr) throws IOException {
        //Pedersen承诺的形式c = g^m * h^r (m是要隐藏的金额，r是致盲因子)
        //产生random c，在2到q - 2之间
        SecureRandom random = new SecureRandom();
        BigInteger r1 = GetRandom.getRandom(arr);
        BigInteger r2 = GetRandom.getRandom(arr);
        BigInteger x = GetRandom.getRandom(arr);;
        BigInteger y = GetRandom.getRandom(arr);;
        BigInteger z = GetRandom.getRandom(arr);;

        //下面的字段用于bitComZKP的随机数
        BigInteger a = GetRandom.getRandom(arr);;
        BigInteger s = GetRandom.getRandom(arr);;
        BigInteger t = GetRandom.getRandom(arr);;
        BigInteger challenge = GetRandom.getRandom(arr);;

        /**
         * 下面的字段用于证明资产只转给一个人的中间变量,其中的s选取一个定值，就以之前出现的s = 898363816870
         */
        //证明g的s次方的随机数
        BigInteger rToOne = GetRandom.getRandom(arr);;
        BigInteger sTemp = new BigInteger("898363816870");  //s
        BigInteger sAndBinTemp = GetRandom.getRandom(arr);;     //证明com的1/0+s次方的随机数

        //计算承诺
        BigInteger[] comarr = new BigInteger[47];

        //交易金额的承诺
        comarr[0] = (arr[2].modPow(m, arr[0])).multiply((arr[3]).modPow(r1, arr[0])).mod(arr[0]);
        /**
         * 给其他银行发送金额，网络编程过去
         */

        //是否为交易的发起者
        comarr[2] = BigInteger.valueOf(0);

        //隐藏值的二次方的承诺
        comarr[3] = (arr[2].modPow(m.pow(2), arr[0])).multiply((arr[3]).modPow(x, arr[0])).mod(arr[0]);

        //隐藏值的三次方的承诺
        comarr[4] = (arr[2].modPow(m.pow(3), arr[0])).multiply((arr[3]).modPow(y, arr[0])).mod(arr[0]);

        //隐藏值的四次方的承诺
        comarr[5] = (arr[2].modPow(m.pow(4), arr[0])).multiply((arr[3]).modPow(z, arr[0])).mod(arr[0]);

        //下面的字段是用于证明比特承诺非0即1的字段
        //中间承诺变量1
        comarr[6] = (arr[2].modPow(a, arr[0]).multiply(arr[3].modPow(s, arr[0]))).mod(arr[0]);
//        //中间承诺变量2
//        comarr[7] = (arr[2].modPow(a.multiply(m), arr[0]).multiply(arr[3].modPow(t, arr[0]))).mod(arr[0]);
        //挑战
        comarr[8] = challenge;
        //挑战的哈希
        comarr[9] = new BigInteger(HashFunction.getSHA256StrJava(challenge.toString())).mod(arr[1]);
        //响应1
//        comarr[10] = (challenge.multiply(BigInteger.valueOf(1)).add(a)).mod(arr[1]);
        //响应2
        comarr[11] = (r2.multiply(challenge).add(s)).mod(arr[1]);
        //响应3
//        comarr[12] = (r2.multiply(challenge.subtract(comarr[10])).add(t)).mod(arr[1]);

        //下面的字段用于证明g的s次方中间变量
        comarr[13] = arr[2].modPow(sTemp, arr[0]);  //g的s次方
        BigInteger tTemp = arr[2].modPow(rToOne, arr[0]); //
        String strtemp = arr[2].toString() + comarr[13].toString() +tTemp.toString();
        comarr[14] = new BigInteger(HashFunction.getSHA256StrJava(strtemp)).mod(arr[1]).mod(arr[1]);
        comarr[15] = (sTemp.multiply(comarr[14]).add(rToOne)).mod(arr[1]);

        //bitCom和g的s次方相乘
//        comarr[16] = comarr[1].multiply(comarr[13]).mod(arr[0]);

        //下面的字段证明comarr[16]g的指数和comarr[17]中gvhr的指数的相等，三个随机数
        BigInteger v1 = GetRandom.getRandom(arr);
        BigInteger v2 = GetRandom.getRandom(arr);
        BigInteger v3 = v2;

        //比特承诺，是否参与了交易，1为参与交易，0为没有参与交易
        if (m.compareTo(BigInteger.valueOf(0)) == 0) {
//            System.out.println("没有参与了交易");
            comarr[1] = (arr[2].modPow(BigInteger.valueOf(0), arr[0])).multiply((arr[3]).modPow(r2, arr[0])).mod(arr[0]);
            //中间承诺变量2
            comarr[7] = (arr[2].modPow(a.multiply(BigInteger.valueOf(0)), arr[0]).multiply(arr[3].modPow(t, arr[0]))).mod(arr[0]);
            //响应1
            comarr[10] = challenge.multiply(BigInteger.valueOf(0)).add(a).mod(arr[1]);
            //bitCom和g的s次方相乘
            comarr[12] = (r2.multiply(challenge.subtract(comarr[10])).add(t)).mod(arr[1]);
            comarr[16] = comarr[1].multiply(comarr[13]).mod(arr[0]);
            //com的1/0+s次方
            comarr[17] = comarr[0].modPow((sTemp.add(BigInteger.valueOf(0))), arr[0]).mod(arr[0]);
            //com的随机数次方
            BigInteger tAndBinTempCom = comarr[0].modPow(sAndBinTemp, arr[0]).mod(arr[0]);
            String str1 = comarr[0].toString() + comarr[17].toString() + tAndBinTempCom.toString();
            comarr[18] = new BigInteger(HashFunction.getSHA256StrJava(str1)).mod(arr[1]);
            comarr[19] = ((sTemp.add(BigInteger.valueOf(0))).multiply(comarr[18])).add(sAndBinTemp).mod(arr[1]);
            comarr[20] = arr[3].modPow(v1, arr[0]).multiply(arr[2].modPow(v2,arr[0])).multiply(comarr[0].modPow(v3, arr[0])).mod(arr[0]);
            String str2 = arr[3].toString() + arr[2].toString() + comarr[0].toString()+ comarr[20].toString();
            BigInteger hashc = new BigInteger(HashFunction.getSHA256StrJava(str2)).mod(arr[1]);
            comarr[21] = (v1.subtract(hashc.multiply(r2))).mod(arr[1]);
            comarr[22] = (v2.subtract(hashc.multiply(sTemp.add(BigInteger.valueOf(0))))).mod(arr[1]);
            comarr[23] = (v3.subtract(hashc.multiply(sTemp.add(BigInteger.valueOf(0))))).mod(arr[1]);

        }else {
//            System.out.println("参与了交易");
            comarr[1] = (arr[2].modPow(BigInteger.valueOf(1), arr[0])).multiply((arr[3]).modPow(r2, arr[0])).mod(arr[0]);
            //中间承诺变量2
            comarr[7] = (arr[2].modPow(a.multiply(BigInteger.valueOf(1)), arr[0]).multiply(arr[3].modPow(t, arr[0]))).mod(arr[0]);
            //响应1
            comarr[10] = challenge.multiply(BigInteger.valueOf(1)).add(a).mod(arr[1]);
            comarr[12] = (r2.multiply(challenge.subtract(comarr[10])).add(t)).mod(arr[1]);
            //bitCom和g的s次方相乘
            comarr[16] = comarr[1].multiply(comarr[13]).mod(arr[0]);
            //com的1/0+s次方
            comarr[17] = comarr[0].modPow(sTemp.add(BigInteger.valueOf(1)), arr[0]).mod(arr[0]);
            //com的随机数次方
            BigInteger tAndBinTempCom = comarr[0].modPow(sAndBinTemp, arr[0]).mod(arr[0]);
            String str1 = comarr[0].toString() + comarr[17].toString() + tAndBinTempCom.toString();
            comarr[18] = new BigInteger(HashFunction.getSHA256StrJava(str1)).mod(arr[1]);
            comarr[19] = ((sTemp.add(BigInteger.valueOf(1))).multiply(comarr[18])).add(sAndBinTemp).mod(arr[1]);
            comarr[20] = arr[3].modPow(v1, arr[0]).multiply(arr[2].modPow(v2,arr[0])).multiply(comarr[0].modPow(v3, arr[0])).mod(arr[0]);
//            System.out.println("comarr[20]:" + comarr[20]);
            String str2 = arr[3].toString() + arr[2].toString() + comarr[0].toString()+ comarr[20].toString();
            BigInteger hashc = new BigInteger(HashFunction.getSHA256StrJava(str2)).mod(arr[1]);
//            System.out.println("hachc:" + hashc);
            comarr[21] = (v1.subtract(hashc.multiply(r2))).mod(arr[1]);
            comarr[22] = (v2.subtract(hashc.multiply(sTemp.add(BigInteger.valueOf(1))))).mod(arr[1]);
            comarr[23] = (v3.subtract(hashc.multiply(sTemp.add(BigInteger.valueOf(1))))).mod(arr[1]);
        }

        /**
         * 下面的是证明doubleCom的隐藏值是com隐藏值的平方
         */
        comarr[24] = comarr[0].modPow(sTemp.add(m), arr[0]);    //com的s+v次方
        comarr[25] = comarr[0].modPow(sTemp, arr[0]);   //com的s次方
        //证明h的表达
        BigInteger index = r1.multiply(m).subtract(x);
        comarr[26] = arr[3].modPow(index,arr[0]).mod(arr[0]);
        BigInteger hComRandom = GetRandom.getRandom(arr);    //h承诺的随机数

        BigInteger hCom = arr[3].modPow(hComRandom, arr[0]).mod(arr[0]);
        comarr[27] = new BigInteger(HashFunction.getSHA256StrJava(arr[3].toString() + comarr[26].toString() + hCom.toString())).mod(arr[1]);
        comarr[28] = comarr[27].multiply(index).add(hComRandom).mod(arr[1]);

        /**
         * 下面的是证明tripleCom的隐藏值是com隐藏值的立方
         */
        comarr[29] = comarr[0].modPow(sTemp.add(m.pow(2)), arr[0]).mod(arr[0]); //com的s+v^2次方
        //证明h的表达
        BigInteger index1 = (r1.multiply(m.pow(2)).subtract(y)).mod(arr[1]);
        comarr[30] = arr[3].modPow(index1,arr[0]).mod(arr[0]);
        BigInteger hComRandom1 = GetRandom.getRandom(arr);    //h承诺的随机数

        BigInteger hCom1 = arr[3].modPow(hComRandom1, arr[0]).mod(arr[0]);
        comarr[31] = new BigInteger(HashFunction.getSHA256StrJava(arr[3].toString() + comarr[30].toString() + hCom1.toString())).mod(arr[1]);
        comarr[32] = (comarr[31].multiply(index1).add(hComRandom1)).mod(arr[1]);

        /**
         * 下面的是证明quadraCom的隐藏值是com隐藏值的四次方
         */
        comarr[33] = comarr[0].modPow(sTemp.add(m.pow(3)), arr[0]).mod(arr[0]); //com的s+v^2次方
        //证明h的表达
        BigInteger index2 = (r1.multiply(m.pow(3)).subtract(z)).mod(arr[1]);
        comarr[34] = arr[3].modPow(index2,arr[0]).mod(arr[0]);
        BigInteger hComRandom2 = GetRandom.getRandom(arr);    //h承诺的随机数

        BigInteger hCom2 = arr[3].modPow(hComRandom2, arr[0]).mod(arr[0]);
        comarr[35] = new BigInteger(HashFunction.getSHA256StrJava(arr[3].toString() + comarr[34].toString() + hCom2.toString())).mod(arr[1]);
        comarr[36] = (comarr[35].multiply(index2).add(hComRandom2)).mod(arr[1]);

        /**
         * 现在进行范围证明的字段生成，我们只验证v的四次方，不然全验证字段太多。v的最大值1亿，n需要107位。
         */
        BigInteger[] gVector = ReadVectorFile.readFile("gVector.txt");
        BigInteger[] hVector = ReadVectorFile.readFile("hVector.txt");
        BigInteger Alpha = GetRandom.getRandom(arr);
        BigInteger[] al = GetAl.getAl(m.pow(4));
        BigInteger[] ar = GetAr.getAr(al,arr);
        comarr[37] = GetA.getA(gVector,hVector, al, ar, Alpha, arr);
        BigInteger Rho = GetRandom.getRandom(arr);
        BigInteger[] Sl = Get_SlAnd_SrAnd_S.getSl(al, arr);
        BigInteger[] Sr = Get_SlAnd_SrAnd_S.getSr(al, arr);
        comarr[38] = Get_SlAnd_SrAnd_S.getS(gVector, hVector, Sl, Sr, Rho, arr);
        //y
        comarr[39] = new BigInteger(HashFunction.getSHA256StrJava(comarr[37].toString()+comarr[38].toString())).mod(arr[1]);
        //z
        comarr[40] = new BigInteger(HashFunction.getSHA256StrJava(comarr[37].toString()+comarr[38].toString()+comarr[39].toString())).mod(arr[1]);
        //x
        comarr[41] = new BigInteger(HashFunction.getSHA256StrJava(comarr[37].toString()+comarr[38].toString()+comarr[39].toString()+comarr[40].toString())).mod(arr[1]);
        BigInteger tao1 = GetRandom.getRandom(arr);
        comarr[42] = GetT1and_T2.getT1(al, ar, Sl, Sr, comarr[39], comarr[40], tao1, arr);
        BigInteger tao2 = GetRandom.getRandom(arr);
        comarr[43] = GetT1and_T2.getT2(Sl, Sr, comarr[39], tao2, arr);
        //lx
        BigInteger[] lx = GetLxAnd_Rx_T.getLx(al, Sl, comarr[41], comarr[40], arr);
        String lxStr = "";
        for (int i=0;i<lx.length;i++){
            lxStr = lxStr + lx[i] + ",";
        }
//        comarr[44] = new BigInteger(lxStr);
        //lx向量不好存储，先放进文件，在读取
        FileWriter fileLx = new FileWriter(inBankStr+"FileLx.txt");
        fileLx.write(lxStr+"\n");
        fileLx.close();
        //rx
        BigInteger[] rx = GetLxAnd_Rx_T.getRx(ar, Sr, comarr[41], comarr[39], comarr[40], arr);
        String rxStr = "";
        for (int i=0;i<lx.length;i++){
            rxStr = rxStr + rx[i] + ",";
        }
//        comarr[45] = new BigInteger(rxStr);
        //t
        //rx向量不好存储，先放进文件，在读取
        FileWriter fileRx = new FileWriter(inBankStr+"FileRx.txt");
        fileRx.write(rxStr+"\n");
        fileRx.close();
        comarr[44] = GetLxAnd_Rx_T.getT(lx, rx, arr);
        comarr[45] = GetTaox.getTaox(comarr[41], tao1, tao2, comarr[40], z, arr);
        comarr[46] = GetMiu.getMiu(Alpha, Rho, comarr[41], arr);

        /**
         * 下面的字段用于证明银行有足够的钱去转账
         */



        //写入文件
        FileWriter file4 = new FileWriter(inBankStr+"random.txt");
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder.append("{").append("\"r1\":").append(r1).append(",\"r2\":").append(r2).append(",\"x\":").
                append(x).append(",\"y\":").append(y).append(",\"z\":").append(z).append("}");

        stringBuilder1.append(outBankStr).append("+").append(m).append("+").append("{").append("\"r1\":").append(r1).append(",\"r2\":").append(r2).append(",\"x\":").
                append(x).append(",\"y\":").append(y).append(",\"z\":").append(z).append("}");
        file4.write(stringBuilder+"\n");
        file4.close();


        /**
         * 发送每一个银行的随机数到消息队列
         */

//        try {
//            SendRandomToOtherBanks.sendRandomToOtherBanks(outBankStr, inBankStr, stringBuilder1.toString());
//            System.out.println(stringBuilder1);
//        } catch (JMSException e) {
//            e.printStackTrace();
//        }

        /**
         * 这里改用socket的发送
         */
        SendMessage.sendMessage("1", stringBuilder1.toString(), IP, port);
        return comarr;
    }


}
