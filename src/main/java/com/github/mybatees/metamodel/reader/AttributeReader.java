package com.github.mybatees.metamodel.reader;

import com.github.mybatees.annotation.*;
import com.github.mybatees.session.MybateesConfiguration;
import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.util.NamingUtils;
import lombok.Getter;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.reflection.TypeParameterResolver;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;

/**
 * 属性信息解析器
 *
 * @author yuechao
 */
public class AttributeReader {
  
  private final MybateesConfiguration configuration;
  private final EntityType entity;
  private final Field field;
  private final @Getter Attribute attribute;
  
  public AttributeReader(MybateesConfiguration configuration, EntityType entity, Field field) {
    this.configuration = configuration;
    this.entity = entity;
    this.field = field;
    this.attribute = new Attribute(entity, field);
    
    // 解析修饰注解
    parseQualifyAnnotations();
    
    // 解析值类型
    parseElementType();
  }
  
  /**
   * 解析属性信息
   */
  public void parse() {
    // ID 已经优先解析过了
    if (attribute.isId()) {
      return;
    }
    
    JoinColumn joinColumnAnt = field.getAnnotation(JoinColumn.class);
    if (joinColumnAnt == null) {
      parseColumnAnnotation();
    } else {
      parseJoinAnnotations(joinColumnAnt);
    }
  }
  
  /**
   * 解析修饰注解
   */
  private void parseQualifyAnnotations() {
    if (field.isAnnotationPresent(Id.class)) {
      parseIdAttribute();
    }
    else if (field.isAnnotationPresent(CreateAt.class)) {
      parseCreateAtAttribute();
    }
    else if (field.isAnnotationPresent(UpdateAt.class)) {
      parseUpdateAtAttribute();
    }
  }
  
  /**
   * 解析值类型
   */
  private void parseElementType() {
    Class<?> fieldType = attribute.getJavaType();
  
    if (fieldType.isArray()) {
      attribute.setArray(true);
      attribute.setElementType(fieldType.getComponentType());
    }
    else if (Collection.class.isAssignableFrom(fieldType)) {
      attribute.setCollection(true);
      attribute.setElementType(parseCollectionElementType());
    } else {
      attribute.setElementType(fieldType);
    }
  }
  
  /**
   * 解析 {@link Id}
   */
  private void parseIdAttribute() {
    attribute.setId(true);
  
    // 优先解析 Id 字段属性
    parseColumnAnnotation();
  }
  
  /**
   * 解析 {@link CreateAt}
   */
  private void parseCreateAtAttribute() {
    if (isInvalidFieldType(Date.class)) {
      throw new BindingException("CreateAt field: " + field.getName() + " should be Date type");
    }
  
    attribute.setCreateAt(true);
  }
  
  /**
   * 解析 {@link UpdateAt}
   */
  private void parseUpdateAtAttribute() {
    if (isInvalidFieldType(Date.class)) {
      throw new BindingException("UpdateAt field: " + field.getName() + " should be Date type");
    }
  
    attribute.setUpdateAt(true);
  }
  
  /**
   * 解析 {@link Column}
   */
  @SuppressWarnings("unchecked")
  private void parseColumnAnnotation() {
    String column = null;
    
    Column columnAnt = field.getAnnotation(Column.class);
    if (columnAnt != null) {
      column = columnAnt.name();
      attribute.setJdbcType(columnAnt.jdbcType());
      attribute.setTypeHandler((Class<? extends TypeHandler<?>>) columnAnt.typeHandler());
    }
    
    column = NamingUtils.validName(column,
      // 默认根据字段名推断列名
      () -> NamingUtils.defaultColumn(attribute)
    );
  
    attribute.setColumn(column);
  }
  
  /**
   * 解析 {@link JoinColumn}, {@link JoinTable}
   */
  @SuppressWarnings("unchecked")
  private void parseJoinAnnotations(JoinColumn joinColumnAnt) {
    Class<?> joinEntityType = attribute.isSingular()
      ? attribute.getJavaType()
      : attribute.getElementType();

    attribute.setJoinEntity(configuration.getEntity(joinEntityType));
  
    attribute.setColumn(NamingUtils.validName(joinColumnAnt.name(),
      // 默认根据关联表名和关联表主键列名
      () -> NamingUtils.defaultJoinColumn(attribute)
    ));
    attribute.setJdbcType(joinColumnAnt.jdbcType());
    attribute.setTypeHandler((Class<? extends TypeHandler<?>>) joinColumnAnt.typeHandler());
    attribute.setInverse(joinColumnAnt.inverse());
    
    String foreignColumn = null;
    
    JoinTable joinTableAnt = field.getAnnotation(JoinTable.class);
    if (joinTableAnt != null) {
      attribute.setTableJoin(true);
      
      String joinTable = joinTableAnt.name().trim();
      if (NamingUtils.isValid(joinTable)) {
        // 添加表名前缀
        String tablePrefix = configuration.getTablePrefix(entity.getJavaType());
        joinTable = NamingUtils.scalar(tablePrefix, joinTable);
      } else {
        // 默认根据表名和字段名推断关联表名
        joinTable = NamingUtils.defaultJoinTable(attribute);
      }
      attribute.setJoinTable(joinTable);
      
      foreignColumn = joinTableAnt.foreignColumn();
    }
  
    attribute.setForeignColumn(NamingUtils.validName(foreignColumn,
      // 默认根据表名和主键列名推断外键列名
      () -> NamingUtils.defaultForeignColumn(attribute)
    ));
    
    attribute.setJoined(true);
  }
  
  /**
   * 校验属性类型是否预期
   * @param supperClasses 期望熟悉类型
   * @return 是否符合
   */
  private boolean isInvalidFieldType(Class<?>... supperClasses) {
    Class<?> fieldType = field.getType();
    
    for (Class<?> supperClass : supperClasses) {
      if (!supperClass.isAssignableFrom(fieldType)) {
        return true;
      }
    }
    
    return false;
  }
  
  /**
   * 解析集合元素类型
   *
   * @return 集合元素类型
   */
  private Class<?> parseCollectionElementType() {
    Type type = TypeParameterResolver.resolveFieldType(field, null);
    if (type instanceof ParameterizedType) {
      Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
      if (arguments.length == 1) {
        return (Class<?>) arguments[0];
      }
    }
  
    throw new IllegalArgumentException("Could not resolve collection element type of " + field);
  }
}
