package com.hyperledger.socket;

import com.alibaba.fastjson.JSON;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName Client.java
 * @Description TODO
 * @createTime 2021年11月10日 20:43:00
 *
 * 这里主要是审计员和银行进行交互，得到相应相应致盲因子的和
 */
public class Client {

    /**
     * 审计交互时使用，得到资产和和致盲因子和
     * @param hashMapComAndRSum
     * @param flag
     * @param bankId
     * @param IP
     * @param portNum
     */
    @Test
    public static void getComAndRSumClient(HashMap<String, BigInteger> hashMapComAndRSum, String flag, String bankId, String IP, Integer portNum){
//    public void getComAndRSumClient(){
        try {
            InetAddress inetAddress = InetAddress.getByName(IP);
            Socket socket = new Socket(inetAddress, portNum);

            /**
             * 审计员发送审计请求
             */
            OutputStream os = socket.getOutputStream();
            os.write(flag.getBytes());
            socket.shutdownOutput();

            /**
             * 审计员接受银行返回的信息
             */
            InputStream inputStream = socket.getInputStream();
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            byte[] buffer = new byte[200];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1){
                bas.write(buffer, 0, len);
            }

            String infor = bas.toString();
            /**
             * 这里的话，我们直接把交互来的字符串进行处理，保存进map的结果就是bankid+comSum,value的话就是具体的值，不然在审计的时候，又要处理字符串，比较麻烦
             */
//           String json =  "{\"comSum\":1000,\"doubleComSum\":0,\"quadraComSum\":0,\"r1\":0,\"r2\":0,\"transactionTime\":0,\"tripleComSum\":0,\"x\":0,\"y\":0,\"z\":0}";
//            System.out.println(json);
           String data = StringEscapeUtils.unescapeCsv(infor);
            System.out.println(infor);
//            System.out.println(data);
            Map map = JSON.parseObject(data, Map.class);
            System.out.println(map);
//           System.out.println(map.get("comSum"));
            hashMapComAndRSum.put(bankId+"comSum", new BigInteger(String.valueOf(map.get("comSum"))));
            hashMapComAndRSum.put(bankId+"r1", new BigInteger(String.valueOf(map.get("r1"))));
            hashMapComAndRSum.put(bankId+"transactionTime", new BigInteger(String.valueOf(map.get("transactionTime"))));
            hashMapComAndRSum.put(bankId+"r2", new BigInteger(String.valueOf(map.get("r2"))));
            hashMapComAndRSum.put(bankId+"doubleComSum", new BigInteger(String.valueOf(map.get("doubleComSum"))));
            hashMapComAndRSum.put(bankId+"x", new BigInteger(String.valueOf(map.get("x"))));
            hashMapComAndRSum.put(bankId+"tripleComSum", new BigInteger(String.valueOf(map.get("tripleComSum"))));
            hashMapComAndRSum.put(bankId+"y", new BigInteger(String.valueOf(map.get("y"))));
            hashMapComAndRSum.put(bankId+"quadraComSum", new BigInteger(String.valueOf(map.get("quadraComSum"))));
            hashMapComAndRSum.put(bankId+"z", new BigInteger(String.valueOf(map.get("z"))));
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 初始化交互的函数，得到银行的初始资金
     * @param hashMapComAndRSum
     * @param flag
     * @param bankId
     * @param IP
     * @param portNum
     */
    public static void getInitializeAssets(HashMap<String, String> initializeHashMap, String flag, String bankId, String IP, Integer portNum){
        try {
            InetAddress inetAddress = InetAddress.getByName(IP);
            Socket socket = new Socket(inetAddress, portNum);

            /**
             * 审计员发送审计请求
             */
            OutputStream os = socket.getOutputStream();
            os.write(flag.getBytes());
            socket.shutdownOutput();

            /**
             * 审计员接受银行返回的信息
             */
            InputStream inputStream = socket.getInputStream();
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            byte[] buffer = new byte[200];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1){
                bas.write(buffer, 0, len);
            }

            String infor = bas.toString();
//            String str1 = infor.substring(0,1);
//            String str2 = str1.substring(1, infor.length());
//            if (str1.equals("2")){
//                /**
//                 * 应答的资金和和致盲因子的和
//                 */
//                hashMapComAndRSum.put(bankId, str2 + "\n");
//            }else {
//                /**
//                 * 这里应该是初始化的资金和
//                 */
//                initializeHashMap.put(bankId, str2 + "\n");
//            }
            initializeHashMap.put(bankId, infor);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
