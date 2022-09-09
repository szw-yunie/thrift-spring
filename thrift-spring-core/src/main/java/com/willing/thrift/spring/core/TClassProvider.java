package com.willing.thrift.spring.core;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.async.TAsyncClient;

import java.util.Collection;

/**
 * @author szw
 */
public interface TClassProvider {

    Collection<Class<? extends TServiceClient>> getClientClasses();

    Collection<Class<? extends TAsyncClient>> getAsyncClientClasses();

    static TClassProvider of(
            Collection<Class<? extends TServiceClient>> clientClasses,
            Collection<Class<? extends TAsyncClient>> asyncClientClasses) {
        return new TClassProvider() {
            @Override
            public Collection<Class<? extends TServiceClient>> getClientClasses() {
                return clientClasses;
            }

            @Override
            public Collection<Class<? extends TAsyncClient>> getAsyncClientClasses() {
                return asyncClientClasses;
            }
        };
    }
}
