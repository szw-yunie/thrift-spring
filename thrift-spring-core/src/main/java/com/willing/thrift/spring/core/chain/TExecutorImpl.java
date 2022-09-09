package com.willing.thrift.spring.core.chain;

import java.util.Collection;
import java.util.function.Function;

public class TExecutorImpl implements TExecutor {
    private ChainImpl chain;

    private TExecutorImpl() {
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Object execute(Params params, Function<Params, Object> request) throws Exception {
        chain.add((p, c) -> request.apply(p));
        return chain.proceed(params);
    }

    public static class Builder {
        private ChainImpl chain = new ChainImpl();
        private TExecutorImpl executor = new TExecutorImpl();


        public void add(Interceptor interceptor) {
            chain.add(interceptor);
        }

        public void addAll(Collection<Interceptor> interceptors) {
            chain.addAll(interceptors);
        }

        TExecutorImpl build() {
            executor.chain = chain;
            return executor;
        }
    }
}
