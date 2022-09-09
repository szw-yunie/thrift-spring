package com.willing.thrift.spring.boot.client.autoconfig;

import com.willing.nodemgr.TConnection;
import com.willing.nodemgr.TConnectionPool;
import com.willing.thrift.spring.core.TConfigProvider;
import lombok.SneakyThrows;
import org.apache.thrift.TServiceClient;

import java.lang.reflect.Method;
import java.util.Objects;

public class TClientInvocationHandlerHandler extends TClientInvocationHandler {

    private final String zkAddress;
    private final TConfigProvider<String, ?> configProvider;

    private TConnectionPool<TServiceClient> connectionPool;

    public TClientInvocationHandlerHandler(TConfigProvider<String, ?> configProvider,
                                           String zkAddress) {
        this.configProvider = configProvider;
        this.zkAddress = zkAddress;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Objects.isNull(connectionPool)) {
            throw new NullPointerException("Connection pool is null.");
        }

        TConnection<TServiceClient> conn = connectionPool.getConnection();
        try {

            if (conn == null) {
                throw new NullPointerException("Thrift connection is null.");
            }

            return method.invoke(conn.getClient(), args);

        } catch (Exception e) {
            //must use pool to destroy
            connectionPool.destroyConnection(conn);
            //must set null if conn is broken
            conn = null;
            // 向上抛出异常
            throw e;
        } finally {
            //if conn is broken must be null
            connectionPool.returnConnection(conn);
        }
    }

    @SneakyThrows
    public void init(Class<? extends TServiceClient> type, String config) {
        String zkNode = configProvider.getClientConfig(type);
        connectionPool = TConnectionPool.create(zkAddress, zkNode, null);
    }
}
