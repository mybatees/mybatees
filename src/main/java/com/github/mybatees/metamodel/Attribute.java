package com.github.mybatees.metamodel;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yuechao
 */
@Getter
@Setter
public class Attribute {
  
  private static final JdbcType DEFAULT_JDBC_TYPE = JdbcType.UNDEFINED;
  private static final Class<? extends TypeHandler<?>> DEFAULT_TYPE_HANDLER = UnknownTypeHandler.class;
  
  private EntityType entity;
  private Field field;
  private AtomicBoolean initialized = new AtomicBoolean();
  private String property;
  private String column;
  private Class<?> javaType;
  private JdbcType jdbcType = DEFAULT_JDBC_TYPE;
  private Class<? extends TypeHandler<?>> typeHandler = DEFAULT_TYPE_HANDLER;
  private boolean array = false;
  private boolean collection = false;
  private Class<?> elementType;
  private boolean id = false;
  private boolean joined = false;
  private boolean inverse = false;
  private EntityType joinEntity;
  private boolean tableJoin = false;
  private String joinTable;
  private String foreignColumn;
  private boolean createAt = false;
  private boolean updateAt = false;
  
  public Attribute(EntityType entity, Field field) {
    this.entity   = entity;
    this.field    = field;
    this.property = field.getName();
    this.javaType = field.getType();
  }
  
  public boolean isSingular() {
    return !isPlural();
  }
  
  public boolean isPlural() {
    return array || collection;
  }
}
