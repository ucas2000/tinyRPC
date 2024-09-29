package org.example.rpc.protocol.serialization;



/**
 * @Description  序列化工厂
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class SerializationFactory {


    public static RpcSerialization get(String serialization) throws Exception {

        return ExtensionLoader.getInstance().get(serialization);

    }

    public static void init() throws Exception {
        ExtensionLoader.getInstance().loadExtension(RpcSerialization.class);
    }
}
