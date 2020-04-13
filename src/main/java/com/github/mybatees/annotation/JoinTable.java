package com.github.mybatees.annotation;

import com.github.mybatees.metamodel.Attribute;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定字段关联关系由关联关系表维护
 *
 * @author yuechao
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinTable {
  
  /**
   * 指定关联关系表名
   *
   * @see com.github.mybatees.util.NamingUtils#defaultJoinColumn(Attribute)
   */
  String name() default "";
  
  /**
   * 指定外键名
   *
   * @see com.github.mybatees.util.NamingUtils#defaultForeignColumn(Attribute)
   */
  String foreignColumn() default "";
}
