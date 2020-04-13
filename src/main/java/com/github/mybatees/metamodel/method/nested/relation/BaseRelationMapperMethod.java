package com.github.mybatees.metamodel.method.nested.relation;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.method.nested.BaseNestedMapperMethod;
import org.apache.ibatis.jdbc.SQL;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.sql.Ref;
import java.util.Collection;

/**
 * 维护关联关系 MapperMethod 基类
 *
 * @author yuechao
 */
public abstract class BaseRelationMapperMethod extends BaseNestedMapperMethod {
  
  protected final EntityType joinEntity;
  protected final Attribute joinId;
  protected final Attribute attribute;
  
  public BaseRelationMapperMethod(Attribute attribute) {
    super(attribute.getEntity());
    
    EntityType joinEntity = attribute.getJoinEntity();
    
    this.joinEntity = joinEntity;
    this.joinId = joinEntity.getId();
    this.attribute = attribute;
  }
  
  /**
   * 是否允许属性值为空
   */
  protected abstract boolean allowAttributeNullOrEmpty();
  
  @Override
  public boolean validateParameter(Object parameter) {
    if (allowAttributeNullOrEmpty()) {
      return true;
    }
    
    if (parameter == null) {
      return false;
    }
    
    Object value = ReflectionUtil.getFieldValue(attribute.getField(), parameter);
    if (value == null) {
      return false;
    }
    
    if (value.getClass().isArray()) {
      return ((Object[]) value).length > 0;
    }
    
    if (value instanceof Collection) {
      return !((Collection<?>) value).isEmpty();
    }
    
    return true;
  }
  
  /**
   * SELECT ID FROM TABLE WHERE ...
   */
  protected String selectEntityIds(String... conditions) {
    return new SQL(){{
  
      SELECT(id.getColumn());
  
      FROM(entity.getTable());
  
      WHERE(conditions);
  
    }}
    .toString();
  }
}
