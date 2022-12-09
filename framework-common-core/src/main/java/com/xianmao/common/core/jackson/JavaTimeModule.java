package com.xianmao.common.core.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.xianmao.common.core.constants.DatePattern;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class JavaTimeModule extends SimpleModule {

    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD_HH_MM_SS);
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD);
    public static final DateTimeFormatter TIME_FORMAT =  DateTimeFormatter.ofPattern(DatePattern.HH_MM_SS);

    public JavaTimeModule() {
        super(PackageVersion.VERSION);
        // ======================= 时间反序列化规则 ===============================
        // yyyy-MM-dd HH:mm:ss
        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATETIME_FORMAT));
        // yyyy-MM-dd
        this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMAT));
        // HH:mm:ss
        this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(TIME_FORMAT));
        // ======================= 时间反序列化规则 ==============================
        // yyyy-MM-dd HH:mm:ss
        this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATETIME_FORMAT));
        // yyyy-MM-dd
        this.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMAT));
        // HH:mm:ss
        this.addSerializer(LocalTime.class, new LocalTimeSerializer(TIME_FORMAT));
    }
}
