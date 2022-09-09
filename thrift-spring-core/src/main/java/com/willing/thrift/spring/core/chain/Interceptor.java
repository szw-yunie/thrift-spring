package com.willing.thrift.spring.core.chain;

@FunctionalInterface
public interface Interceptor {

    /**
     * 拦截器的拦截方法
     */
    Object intercept(Params params, Chain chain) throws Exception;

    interface Chain {

        /**
         * 继续执行剩下的拦截器
         */
        Object proceed(Params param) throws Exception;
    }
}