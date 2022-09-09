package com.willing.thrift.spring.core;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ThriftClientsRegistrar.class)
public @interface EnableThriftClients {
    /**
     * 指定要扫描的包，是 {@link #basePackages()} 的别称
     */
    String[] value() default {};

    /**
     * 指定要扫描的包
     */
    String[] basePackages() default {};

    /**
     * 指定要扫描的包
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * 是否扫描 TServiceClient
     * <p>
     * 只有当指定了要扫描的包时，这个属性才会生效
     */
    boolean enableServiceClient() default true;

    /**
     * 是否扫描 TAsyncClient
     * <p>
     * 只有当指定了要扫描的包时，这个属性才会生效
     */
    boolean enableAsyncClient() default true;
}
