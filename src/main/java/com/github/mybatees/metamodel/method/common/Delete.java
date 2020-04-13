package com.github.mybatees.metamodel.method.common;

import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.method.helper.EntityDeleteHelper;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * 删除实体
 *
 * @see com.github.mybatees.mapper.CrudMapper#delete(Object)
 * @author yuechao
 */
public class Delete extends BaseCommonMapperMethod {
  
  public Delete(EntityType entity) {
    super(entity);
  }
  
  @Override
  public SqlCommandType getSqlCommandType() {
    return SqlCommandType.DELETE;
  }
  
  @Override
  public Class<?> getParameterType() {
    return id.getJavaType();
  }
  
  @Override
  public SQL getSql() {
    return new EntityDeleteHelper(){{
      
      delete(entity);
  
      withDeleteRelation(beforeNestedMapperMethods::add);
      
      where(byId());
    }}
    .sql();
  }
}
