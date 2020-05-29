package com.xianmao.enums;

public enum ArrayType {
    /**
     * 字符数组
     */
    CHAR_ARRAY(char[].class),
    /**
     * 字节数组
     */
    BYTE_ARRAY(byte[].class),
    /**
     * 短整型数组
     */
    SHORT_ARRAY(short[].class),
    /**
     * 整型数组
     */
    INT_ARRAY(int[].class),
    /**
     * 长整型数组
     */
    LONG_ARRAY(long[].class),
    /**
     * 单精度浮点型数组
     */
    FLOAT_ARRAY(float[].class),
    /**
     * 双精度浮点型数组
     */
    DOUBLE_ARRAY(double[].class),
    /**
     * 布尔型数组
     */
    BOOLEAN_ARRAY(boolean[].class),
    /**
     * 对象数组
     */
    OBJECT_ARRAY(Object[].class);

    /**
     * 类型
     */
    private Class type;

    /**
     * 数组类型构造
     * @param type 对应类型
     */
    ArrayType(Class type) {
        this.type = type;
    }

    /**
     * 获取对应类型
     * @return 返回对应类型
     */
    public Class getType() {
        return type;
    }
}
