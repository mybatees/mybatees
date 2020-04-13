package com.github.mybatees.metamodel.method.common;

import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.method.helper.EntitySelectHelper;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * 判断实体是否存在
 *
 * @see com.github.mybatees.mapper.CrudMapper#exists(Object)
 * @author yuechao
 */
public class Exists extends BaseCommonMapperMethod {
  
  public Exists(EntityType entity) {
    super(entity);
  }
  
  @Override
  public Class<?> getParameterType() {
    return id.getJavaType();
  }
  
  @Override
  public SqlCommandType getSqlCommandType() {
    return SqlCommandType.SELECT;
  }
  
  @Override
  public SQL getSql() {
    return new EntitySelectHelper(){{
      
      count(entity);
      
      where(byId());
    }}
    .sql();
  }
  
  @Override
  public Class<?> getResultType() {
    return Boolean.TYPE;
  }
}
