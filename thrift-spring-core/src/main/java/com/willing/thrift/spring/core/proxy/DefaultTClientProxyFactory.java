package com.willing.thrift.spring.core.proxy;

import com.willing.thrift.spring.core.chain.Params;
import com.willing.thrift.spring.core.chain.TClientExecutorFactory;
import com.willing.thrift.spring.core.chain.TExecutor;
import com.willing.thrift.spring.core.factory.TAsyncClientFactory;
import com.willing.thrift.spring.core.factory.TClientFactoryProvider;
import com.willing.thrift.spring.core.factory.TServiceClientFactory;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.async.TAsyncClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 默认的 thrift 客户端代理工厂，基于 JDK 动态代理实现。
 *
 * @author szw
 */
public class DefaultTClientProxyFactory implements TClientProxyFactory {

    private final ClassLoader classLoader;
    private final TClientExecutorFactory executorFactory;
    private final TClientFactoryProvider clientFactoryProvider;

    public DefaultTClientProxyFactory(TClientExecutorFactory executorFactory, TClientFactoryProvider clientFactoryProvider) {
        this(ClassLoader.getSystemClassLoader(), executorFactory, clientFactoryProvider);
    }

    public DefaultTClientProxyFactory(ClassLoader classLoader, TClientExecutorFactory executorFactory, TClientFactoryProvider clientFactoryProvider) {
        this.classLoader = classLoader;
        this.executorFactory = executorFactory;
        this.clientFactoryProvider = clientFactoryProvider;
    }

    @Override
    public Object getTServiceClientProxy(Class<? extends TServiceClient> serviceClientClass) {
        return Proxy.newProxyInstance(
                classLoader,
                serviceClientClass.getInterfaces(),
                getTServiceInvocationHandler(serviceClientClass)
        );
    }

    @Override
    public Object getTAsyncClientProxy(Class<? extends TAsyncClient> asyncClientClass) {
        return Proxy.newProxyInstance(
                classLoader,
                asyncClientClass.getInterfaces(),
                getTAsyncInvocationHandler(asyncClientClass)
        );
    }


    private InvocationHandler getTServiceInvocationHandler(Class<? extends TServiceClient> serviceClientClass) {
        return (proxy, method, args) -> {
            TServiceClientFactory<?> TServiceClientFactory
                    = clientFactoryProvider.getServiceClientFactory(serviceClientClass);
            TExecutor executor
                    = executorFactory.getTServiceClientExecutor(serviceClientClass);

            Params params = Params.of(method, args);
            return executor.execute(
                    params,
                    p -> {
                        try {
                            return method.invoke(TServiceClientFactory.getClient(), p.getParamValues());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        };
    }

    private InvocationHandler getTAsyncInvocationHandler(Class<? extends TAsyncClient> asyncClientClass) {
        return (proxy, method, args) -> {
            TAsyncClientFactory<?> TAsyncClientFactory
                    = clientFactoryProvider.getAsyncClientFactory(asyncClientClass);
            TExecutor executor
                    = executorFactory.getTAsyncClientExecutor(asyncClientClass);

            Params params = Params.of(method, args);
            return executor.execute(
                    params,
                    p -> {
                        try {
                            return method.invoke(TAsyncClientFactory.getAsyncClient(), p.getParamValues());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        };
    }
}
