package org.example.rpc.protocol.serialization;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Description  Hessian 序列化.Hessian格式在网络传输中非常轻量，支持复杂对象的序列化，有助于提升性能和减少带宽消耗
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class HessianSerialization implements RpcSerialization{
    @Override
    public <T> byte[] serialize(T obj) {
        if(obj==null) {
            throw new NullPointerException();
        }
        byte[] result;
        HessianSerializerOutput hessianOutput;
        try(ByteArrayOutputStream os=new ByteArrayOutputStream()){
            hessianOutput = new HessianSerializerOutput(os);
            hessianOutput.writeObject(obj);
            hessianOutput.flush();
            result = os.toByteArray();
        }catch(Exception e){
            throw new SerializationException(e);
        }
        return result;

    }
    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        if(data==null) {
            throw new NullPointerException();
        }
        T result;
        try(ByteArrayInputStream is=new ByteArrayInputStream(data)){
            HessianSerializerInput hessianInput=new HessianSerializerInput(is);
            result=(T) hessianInput.readObject(clazz);
        }catch (Exception e){
            throw new SerializationException(e);
        }
        return result;
    }
}
