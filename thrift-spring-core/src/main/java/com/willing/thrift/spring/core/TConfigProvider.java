package com.willing.thrift.spring.core;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.async.TAsyncClient;

import java.util.Map;
import java.util.function.Function;

/**
 * @param <T> {@link TServiceClient} 配置的类型
 * @param <U> {@link TAsyncClient} 异步客户端配置的类型
 * @author szw
 */
public interface TConfigProvider<T, U> {

    default T getClientConfig(Class<? extends TServiceClient> client) {
        return null;
    }

    default U getAsyncClientConfig(Class<? extends TAsyncClient> client) {
        return null;
    }

    static <T, U> TConfigProvider<T, U> of(
            Function<Class<? extends TServiceClient>, T> clientConfigProvider,
            Function<Class<? extends TAsyncClient>, U> asyncClientConfigProvider) {
        return new DelegateTConfigProvider<>(clientConfigProvider, asyncClientConfigProvider);
    }

    static <T, U> TConfigProvider<T, U> of(
            Map<Class<? extends TServiceClient>, T> clientConfigMap,
            Map<Class<? extends TAsyncClient>, U> asyncConfigMap) {
        return new DelegateTConfigProvider<>(
                clientConfigMap == null ? null : clientConfigMap::get,
                asyncConfigMap == null ? null : asyncConfigMap::get
        );
    }

    class DelegateTConfigProvider<T, U> implements TConfigProvider<T, U> {
        private final Function<Class<? extends TServiceClient>, T> clientConfigProvider;
        private final Function<Class<? extends TAsyncClient>, U> asyncClientConfigProvider;

        public DelegateTConfigProvider(
                Function<Class<? extends TServiceClient>, T> clientConfigProvider,
                Function<Class<? extends TAsyncClient>, U> asyncClientConfigProvider) {
            this.clientConfigProvider = clientConfigProvider;
            this.asyncClientConfigProvider = asyncClientConfigProvider;
        }

        @Override
        public T getClientConfig(Class<? extends TServiceClient> client) {
            if (clientConfigProvider != null) {
                return clientConfigProvider.apply(client);
            }
            return null;
        }

        @Override
        public U getAsyncClientConfig(Class<? extends TAsyncClient> client) {
            if (asyncClientConfigProvider != null) {
                return asyncClientConfigProvider.apply(client);
            }
            return null;
        }
    }
}
