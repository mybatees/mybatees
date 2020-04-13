package com.github.mybatees.annotation;

import com.github.mybatees.metamodel.Attribute;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定属性的映射信息
 *
 * @author yuechao
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
  
  /**
   * 指定列名
   *
   * @see com.github.mybatees.util.NamingUtils#defaultColumn(Attribute)
   */
  String name() default "";
  
  /**
   * 指定 JdbcType，默认值：{@link JdbcType#UNDEFINED}
   *
   * @see JdbcType
   */
  JdbcType jdbcType() default JdbcType.UNDEFINED;
  
  /**
   * 指定 TypeHandler，默认值：{@link UnknownTypeHandler}
   *
   * @see TypeHandler
   */
  Class<? extends TypeHandler> typeHandler() default UnknownTypeHandler.class;
}
