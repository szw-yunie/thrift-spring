package com.willing.thrift.spring.core.chain;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Params {
    private static final ParameterNameDiscoverer parameterNameDiscoverer;

    static {
        parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    }

    private final String[] paramNames;
    private final Object[] paramValues;
    private final Class<?>[] paramTypes;

    private final Map<String, Integer> paramMap;

    public static Params of(Method method, Object[] args) {
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterNames == null) {
            return null;
        }

        if (parameterNames.length != args.length || args.length != parameterTypes.length) {
            throw new IllegalArgumentException();
        }

        return new Params(parameterNames, args, parameterTypes);
    }

    private Params(String[] paramNames, Object[] paramValues, Class<?>[] paramTypes) {
        this.paramNames = paramNames;
        this.paramValues = paramValues;
        this.paramTypes = paramTypes;

        paramMap = new HashMap<>(paramNames.length);
        for (int i = 0; i < paramNames.length; i++) {
            paramMap.put(paramNames[i], i);
        }
    }

    public int getParamCount() {
        return paramNames.length;
    }

    private boolean checkOrder(int order) {
        return order > 0 && order < getParamCount();
    }

    public String getParamName(int order) {
        if (!checkOrder(order)) {
            return null;
        }
        return paramNames[order];
    }

    public Object getParamValue(int order) {
        if (!checkOrder(order)) {
            return null;
        }
        return paramValues[order];
    }

    public boolean setParamValue(int order, Object value) {
        if (!checkOrder(order)) {
            return false;
        }
        if (value == null || paramTypes[order].isAssignableFrom(value.getClass())) {
            paramValues[order] = value;
            return true;
        }

        return false;
    }

    public Class<?> getParamType(int order) {
        return paramTypes[order];
    }

    public Object getParamValue(String paramName) {
        Integer order = paramMap.get(paramName);
        if (order != null) {
            return paramValues[order];
        }
        return null;
    }

    public boolean setParamValue(String paramName, Object value) {
        Integer order = paramMap.get(paramName);
        if (order == null) {
            return false;
        }

        if (value == null || paramTypes[order].isAssignableFrom(value.getClass())) {
            paramValues[order] = value;
            return true;
        }

        return false;
    }

    public Class<?> getParamType(String paramName) {
        Integer order = paramMap.get(paramName);
        if (order != null) {
            return paramTypes[order];
        }
        return null;
    }

    public Object[] getParamValues() {
        return Arrays.copyOf(paramValues, getParamCount());
    }
}
