package com.github.mybatees.metamodel.method.common;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.method.BaseMapperMethod;
import com.github.mybatees.metamodel.method.CommonMapperMethod;
import com.github.mybatees.metamodel.method.NestedMapperMethod;
import com.github.mybatees.util.NamingUtils;
import com.github.mybatees.util.SqlScriptUtils;
import com.github.mybatees.util.StringPool;
import lombok.Getter;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 通用 MapperMethod 基类
 *
 * @author yuechao
 */
public abstract class BaseCommonMapperMethod extends BaseMapperMethod implements CommonMapperMethod {
  
  protected final @Getter List<NestedMapperMethod> beforeNestedMapperMethods = new ArrayList<>();
  protected final @Getter List<NestedMapperMethod> afterNestedMapperMethods = new ArrayList<>();
  
  public BaseCommonMapperMethod(EntityType entity) {
    super(entity);
  }
  
  @Override
  public String getId() {
    return NamingUtils.commonMethod(getClass());
  }
  
  @Override
  public String getKeyProperty() {
    return id.getProperty();
  }
  
  @Override
  public String getKeyColumn() {
    return id.getColumn();
  }
  
  @Override
  public void populateParameters(Collection<?> parameters) {
    Date now = new Date();
  
    for (Attribute attribute : entity.getAttributes()) {
      if (attribute.isCreateAt() && getSqlCommandType() == SqlCommandType.INSERT) {
        // 填充 CreateAt
        populateParameters(attribute, parameters, now);
      }
      else if (attribute.isUpdateAt() && getSqlCommandType() == SqlCommandType.UPDATE) {
        // 填充 UpdateAt
        populateParameters(attribute, parameters, now);
      }
    }
  }
  
  /**
   * 填充属性
   * @param attribute 属性信息
   * @param parameters 参数
   * @param value 属性值
   */
  private void populateParameters(Attribute attribute, Collection<?> parameters, Object value) {
    for (Object parameter : parameters) {
      ReflectionUtil.setFieldValue(attribute.getField(), parameter, value);
    }
  }
  
  /**
   * ID = #{id}
   */
  protected String byId() {
    return SqlScriptUtils.safeEq(id.getColumn(), id.getProperty());
  }
  
  /**
   * ID IN (<foreach collection="collection" item=“id”>#{id}</foreach>)
   */
  protected String byIds() {
    String item = id.getProperty();
    
    String ids = SqlScriptUtils.foreach(SqlScriptUtils.PARAM_COLLECTION)
      .item(item)
      .separator(StringPool.COMMA_SPACE)
      .build(SqlScriptUtils.safeParam(item));
      
    return SqlScriptUtils.in(id.getColumn(), ids);
  }
}
