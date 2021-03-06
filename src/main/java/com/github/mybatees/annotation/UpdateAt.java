package com.github.mybatees.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定字段为更新时间，字段类型必须为{@link java.util.Date}
 * 使用<code>UpdateAt</code>注解的字段，在更新时，会默认填充当前时间
 *
 * @author yuechao
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateAt {
}
