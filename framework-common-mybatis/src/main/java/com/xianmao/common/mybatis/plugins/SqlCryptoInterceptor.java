package com.xianmao.common.mybatis.plugins;

import com.xianmao.common.mybatis.utils.CryptoUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class SqlCryptoInterceptor extends com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor {
    private static final String UPDATE = "update";
    private static final String QUERY = "query";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        if (target instanceof Executor) {
            Method method = invocation.getMethod();
            if (UPDATE.equals(method.getName())) {
                Object[] args = invocation.getArgs();
                MappedStatement ms = (MappedStatement) args[0];
                if (ms.getSqlCommandType() == SqlCommandType.INSERT
                        || ms.getSqlCommandType() == SqlCommandType.UPDATE) {
                    Object parameter = args[1];
                    Object obj = null;
                    if (parameter instanceof Map<?, ?>) {
                        Map<?, ?> map = (Map<?, ?>) parameter;
                        for (Map.Entry<?, ?> entry : map.entrySet()) {
                            String key = (String) entry.getKey();
                            Object value = entry.getValue();
                            if (!("ew".equals(key) || "param2".equals(key)
                                    || (value == null || value.getClass().isPrimitive()) || value instanceof String)) {
                                obj = value;
                                break;
                            }
                        }
                    } else {
                        obj = parameter;
                    }
                    if (obj != null) {
                        List<?> list = null;
                        if (obj instanceof List<?>) {
                            list = (List<?>) obj;
                            CryptoUtils.encryptList(list);
                        } else {
                            CryptoUtils.encryptObject(obj);
                        }
                    }
                }
                return invocation.proceed();
            } else if (QUERY.equals(method.getName())) {
                Object retVal = invocation.proceed();
                if (retVal instanceof ArrayList<?>) {
                    List<?> list = (List<?>) retVal;
                    if (list.size() == 0
                            || list.get(0) instanceof Integer
                            || list.get(0) instanceof Long
                            || list.get(0) instanceof Map<?, ?>) {
                        return retVal;
                    }
                    CryptoUtils.decryptList(list);
                }
                return retVal;
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
