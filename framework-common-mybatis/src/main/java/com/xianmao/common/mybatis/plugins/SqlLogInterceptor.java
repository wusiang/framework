package com.xianmao.common.mybatis.plugins;


import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * MyBatis 性能拦截器，用于输出每条 SQL 语句及其执行时间
 *
 */
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "query",
        args = {Statement.class, ResultHandler.class}
), @Signature(
        type = StatementHandler.class,
        method = "update",
        args = {Statement.class}
), @Signature(
        type = StatementHandler.class,
        method = "batch",
        args = {Statement.class}
)})
@Component
@Slf4j
public class SqlLogInterceptor extends com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object firstArg = invocation.getArgs()[0];
        Statement statement;
        if (Proxy.isProxyClass(firstArg.getClass())) {
            statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
        } else {
            statement = (Statement) firstArg;
        }
        MetaObject stmtMetaObj = SystemMetaObject.forObject(statement);
        try {
            statement = (Statement) stmtMetaObj.getValue("stmt.statement");
        } catch (Exception var20) {
        }

        if (stmtMetaObj.hasGetter("delegate")) {
            try {
                statement = (Statement) stmtMetaObj.getValue("delegate");
            } catch (Exception var19) {
            }
        }

        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = invocation.proceed();
            printSql(false, start, invocation, statement, result);
        } catch (Exception e) {
            printSql(true, start, invocation, statement, result);
            throw e;
        }
        return result;
    }

    /**
     * 打印SQL
     * @param isError   SQL执行是否出现异常
     * @param startTime 执行开始时间
     * @param invocation
     * @param statement
     */
    private void printSql(boolean isError, long startTime, Invocation invocation, Statement statement, Object result){
        long timing = System.currentTimeMillis() - startTime;
        try {
            Object target = PluginUtils.realTarget(invocation.getTarget());
            MetaObject metaObject = SystemMetaObject.forObject(target);
            MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
            String sql = statement.toString().replaceAll("com\\..*?:", "");
            if (isError) {
                log.error(showDBRows(result) + " - SQL执行失败 - 耗时：" + timing + " ms" + " - id:" + ms.getId() + " - Sql:" + sql.replaceAll("\\n", ""));
            } else {
                log.info(showDBRows(result) + " - 耗时：" + timing + " ms" + " - id:" + ms.getId() + " - Sql:" + sql.replaceAll("\\n", ""));
            }
        } catch (Exception e) {
            log.error("格式化SQL失败.", e);
        }
    }

    /**
     * 获取影响行数
     * @param result
     * @return
     */
    private String showDBRows(Object result) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(" - rows:");
        try {
            if (result == null) {
                stringBuffer.append(0);
            } else if (result instanceof Integer) {
                Integer integer = (Integer) result;
                stringBuffer.append(integer);
            } else if (result instanceof List) {
                List list = (List) result;
                stringBuffer.append(list.size());
            } else if (result instanceof Map) {
                Map map = (Map) result;
                stringBuffer.append(map.size());
            }
        } catch (Exception e) {
            log.error("PerformanceInterceptor.showDBRows", e);
        }
        return stringBuffer.toString();
    }
}