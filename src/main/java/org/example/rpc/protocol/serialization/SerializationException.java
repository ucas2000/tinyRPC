package org.example.rpc.protocol.serialization;

/**
 * @Description  序列化异常
 * @Author: lyc
 * @Date: 2024/9/29
 */
public class SerializationException extends RuntimeException{

    private static final long serialVersionUID = -1;

    public SerializationException() {
        super();
    }

    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}
