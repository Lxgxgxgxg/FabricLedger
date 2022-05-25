package com.hyperledger.connection;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.sdk.Peer;

import java.util.EnumSet;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName Invoke.java
 * @Description TODO
 * @createTime 2021年11月15日 16:15:00
 */
public class Invoke {

    public static void invoke(Network network, String newTrans){
        // 增加新的资产
        byte[] invokeResult = new byte[0];
        Contract contract = network.getContract("mycc");
        try {
            invokeResult = contract.createTransaction("set")
                    .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                    .submit(newTrans);
//            System.out.println(new String(invokeResult, StandardCharsets.UTF_8));
            System.out.println("交易信息已成功上链！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
