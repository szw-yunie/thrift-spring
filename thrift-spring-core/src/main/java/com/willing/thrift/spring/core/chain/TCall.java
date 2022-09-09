package com.willing.thrift.spring.core.chain;

import java.util.function.Function;

/**
 * 一个 TCall 代表一次 Thrift 远程调用
 *
 * @author szw
 */
public interface TCall extends Function<Params, Object> {

    @Override
    Object apply(Params params);
}
