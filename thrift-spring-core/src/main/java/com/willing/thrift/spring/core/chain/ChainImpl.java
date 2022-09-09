package com.willing.thrift.spring.core.chain;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

class ChainImpl implements Interceptor.Chain {
    private final List<Interceptor> interceptorList = new ArrayList<>();
    private Iterator<Interceptor> iterator;

    public void add(Interceptor interceptor) {
        interceptorList.add(interceptor);
    }

    public void addAll(Collection<Interceptor> interceptors) {
        interceptorList.addAll(interceptors);
    }

    @Override
    public Object proceed(Params param) throws Exception {
        if (iterator == null) {
            iterator = interceptorList.iterator();
        }

        Assert.state(iterator.hasNext(),
                "The last interceptor in the intercepting chain cannot call the proceed method.");

        return iterator.next().intercept(param, this);
    }
}
