package com.xianmao.swagger.util;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import springfox.documentation.RequestHandler;

import java.util.List;
import java.util.function.Predicate;


public class SwaggerUtil {

    /**
     * 获取包集合
     *
     * @param basePackages 多个包名集合
     */
    public static Predicate<RequestHandler> basePackages(final List<String> basePackages) {
        return input -> declaringClass(input).transform(handlerPackage(basePackages)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final List<String> basePackages) {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackages) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

}
