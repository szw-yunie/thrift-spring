package com.willing.thrift.spring.core.factory;

import com.willing.thrift.spring.core.TConfigProvider;
import org.apache.thrift.TServiceClient;

/**
 * @author szw
 */
public abstract class TServiceClientFactory<T> {
    protected final Class<? extends TServiceClient> clientType;
    protected final T clientConfig;

    public TServiceClientFactory(TConfigProvider<T, ?> configProvider, Class<? extends TServiceClient> clientType) {
        this.clientType = clientType;
        this.clientConfig = configProvider.getClientConfig(clientType);
    }

    /**
     * 返回一个新的 {@link TServiceClient}
     *
     * @return {@link TServiceClient}
     */
    public abstract TServiceClient getClient();
}
