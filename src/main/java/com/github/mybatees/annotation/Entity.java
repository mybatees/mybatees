package com.github.mybatees.annotation;

import com.github.mybatees.metamodel.EntityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定实体的映射信息
 *
 * @author yuechao
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
  
  /**
   * 指定实体对应表名
   *
   * @see com.github.mybatees.util.NamingUtils#defaultTable(EntityType)
   */
  String table() default "";
}
