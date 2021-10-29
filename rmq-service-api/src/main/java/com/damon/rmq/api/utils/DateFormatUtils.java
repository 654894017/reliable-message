package com.damon.rmq.api.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DateFormatUtils {
    private DateFormatUtils() {
        throw new RuntimeException("DateFormatUtils.class can't be instantiated");
    }

    private static final String SPLIT_PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter SPLIT_FORMAT_DATETIME = DateTimeFormatter.ofPattern(SPLIT_PATTERN_DATETIME);

    /**
     * <p>使用时间格式（yyyy-MM-dd HH:mm:ss）进行时间格式化</p>
     *
     * @param date 待格式化时间
     * @return 格式化后的时间字符串
     */
    public static String formatDateTime(LocalDateTime date) {
        return date.format(SPLIT_FORMAT_DATETIME);
    }
}
