package org.example.rpc.common.constant;

/**
 * @Description
 * @Author: lyc
 * @Date: 2024/9/26
 */
public enum  MsgType {

    REQUEST,
    RESPONSE,
    HEARTBEAT;

    public static MsgType findByType(int type) {

        return MsgType.values()[type];
    }
}
