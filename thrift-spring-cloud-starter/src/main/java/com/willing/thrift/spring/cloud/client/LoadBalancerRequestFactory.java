package com.willing.thrift.spring.cloud.client;

import com.willing.thrift.spring.core.chain.Params;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;

/**
 * @author szw
 */
public class LoadBalancerRequestFactory {

    public LoadBalancerRequest<Object> createRequest(Params) {
        return instance -> {

        }
    }
}
