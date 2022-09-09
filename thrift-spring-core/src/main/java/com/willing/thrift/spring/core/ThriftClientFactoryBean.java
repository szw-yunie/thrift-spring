package com.willing.thrift.spring.core;

import com.google.common.base.Objects;
import com.willing.thrift.spring.core.proxy.TClientProxyFactory;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.async.TAsyncClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * @author szw
 */
@SuppressWarnings({"unchecked"})
class ThriftClientFactoryBean
        implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Class<?> clientType;
    private Object proxyObject;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.applicationContext, "ApplicationContext id must be set");
        Assert.notNull(this.clientType, "ClientType id must be set");

        TClientProxyFactory clientProxyFactory = applicationContext.getBean(TClientProxyFactory.class);
        Assert.notNull(clientProxyFactory,
                "No bean with type com.willing.thrift.spring.core.proxy.TClientProxyFactory.");

        // 创建同步或异步 client 的 invocationHandler
        if (TServiceClient.class.isAssignableFrom(clientType)) {

            Class<TServiceClient> type = (Class<TServiceClient>) this.clientType;
            proxyObject = clientProxyFactory.getTServiceClientProxy(type);
        } else if (TAsyncClient.class.isAssignableFrom(clientType)) {

            Class<TAsyncClient> type = (Class<TAsyncClient>) this.clientType;
            proxyObject = clientProxyFactory.getTAsyncClientProxy(type);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object getObject() throws Exception {
        return proxyObject;
    }

    @Override
    public Class<?> getObjectType() {
        return this.clientType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    //========== Setter and Getter ==========//

    public Class<?> getClientType() {
        return clientType;
    }

    public void setClientType(Class<?> clientType) {
        this.clientType = clientType;
    }

    public Object getProxyObject() {
        return proxyObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThriftClientFactoryBean that = (ThriftClientFactoryBean) o;
        return Objects.equal(applicationContext, that.applicationContext)
                && Objects.equal(clientType, that.clientType)
                && Objects.equal(proxyObject, that.proxyObject);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(applicationContext, clientType, proxyObject);
    }

    @Override
    public String toString() {
        return "ThriftClientFactoryBean{" +
                "type=" + this.clientType + ", " +
                "thriftInvocationHandler='" + this.proxyObject + "', " +
                "applicationContext=" + this.applicationContext +
                "}";
    }
}
