package org.example.rpc.common.constant;

/**
 * @description: 消息类型
 * @Author: Xhy
 * @gitee: https://gitee.com/XhyQAQ
 * @copyright: B站: https://space.bilibili.com/152686439
 * @CreateTime: 2023-04-30 12:39
 */
public enum  MsgType {

    REQUEST,
    RESPONSE,
    HEARTBEAT;

    public static MsgType findByType(int type) {

        return MsgType.values()[type];
    }
}
