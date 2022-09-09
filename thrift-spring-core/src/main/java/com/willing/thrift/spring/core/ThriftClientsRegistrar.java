package com.willing.thrift.spring.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.async.TAsyncClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author SZW
 */
@Slf4j
public class ThriftClientsRegistrar
        implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware, BeanFactoryAware {

    private ResourceLoader resourceLoader;
    private Environment environment;
    private BeanFactory beanFactory;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        // Binder.get(environment).bind("test",Test).get()
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);

        Set<String> basePackages = getBasePackages(metadata);

        // FIXME: 2022/8/7
        ObjectProvider<TClassProvider> beanProvider = beanFactory.getBeanProvider(TClassProvider.class);
        TClassProvider classProvider = beanProvider.getIfAvailable();
        if (classProvider != null) {

            Collection<Class<? extends TServiceClient>> clientClasses = classProvider.getClientClasses();
            Collection<Class<? extends TAsyncClient>> asyncClientClasses = classProvider.getAsyncClientClasses();

            if (!CollectionUtils.isEmpty(clientClasses)) {
                for (Class<? extends TServiceClient> clientClass : clientClasses) {
                    registerThriftClient(registry, clientClass.getName());
                }
            }

            if (!CollectionUtils.isEmpty(asyncClientClasses)) {
                for (Class<? extends TAsyncClient> asyncClientClass : asyncClientClasses) {
                    registerThriftClient(registry, asyncClientClass.getName());
                }
            }
        } else {

            Map<String, Object> attrs = metadata
                    .getAnnotationAttributes(EnableThriftClients.class.getName());

            final boolean enableServiceClient = attrs != null && (Boolean) attrs.get("enableServiceClient");
            final boolean enableAsyncClient = attrs != null && (Boolean) attrs.get("enableAsyncClient");
            if (!enableServiceClient && !enableAsyncClient) {
                log.error("No client enabled");
                return;
            }
            if (enableServiceClient) {
                AssignableTypeFilter clientFilter = new AssignableTypeFilter(TServiceClient.class);
                scanner.addIncludeFilter(clientFilter);
            }
            if (enableAsyncClient) {
                AssignableTypeFilter asyncClientFilter = new AssignableTypeFilter(TAsyncClient.class);
                scanner.addIncludeFilter(asyncClientFilter);
            }

            for (String basePackage : basePackages) {
                // 实际返回的是 ScannedGenericBeanDefinition
                Set<BeanDefinition> candidateComponents = scanner
                        .findCandidateComponents(basePackage);
                for (BeanDefinition component : candidateComponents) {
                    if (component instanceof AnnotatedBeanDefinition)
                        registerThriftClient(registry, component.getBeanClassName());
                }
            }
        }
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableThriftClients.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

    private void registerThriftClient(BeanDefinitionRegistry registry, String className) {

        BeanDefinitionBuilder definitionBuilder
                = BeanDefinitionBuilder
                .genericBeanDefinition(ThriftClientFactoryBean.class);

        definitionBuilder.addPropertyValue("clientType", className);
        definitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        AbstractBeanDefinition beanDefinition = definitionBuilder.getBeanDefinition();
        beanDefinition.setPrimary(true);

        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, null);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isIndependent()
                        && !beanDefinition.getMetadata().isAnnotation();
            }
        };
    }
}
