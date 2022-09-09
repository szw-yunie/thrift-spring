package com.willing.thrift.spring.core.chain;

import org.apache.thrift.async.AsyncMethodCallback;

/**
 * thrift 远程调用执行器
 *
 * @author szw
 */
public interface TExecutor {

    Object execute(Params params, TCall TCall) throws Exception;

    Object executeAsync(Params params, TCall call, AsyncMethodCallback<Object> callback);
}
