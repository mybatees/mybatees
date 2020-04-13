package com.github.mybatees.metamodel.method.common;

import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.method.helper.EntityUpdateHelper;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * 更新实体
 *
 * @see com.github.mybatees.mapper.CrudMapper#update(Object)
 * @author yuechao
 */
public class Update extends BaseCommonMapperMethod {
  
  public Update(EntityType entity) {
    super(entity);
  }
  
  @Override
  public SqlCommandType getSqlCommandType() {
    return SqlCommandType.UPDATE;
  }
  
  @Override
  public Class<?> getParameterType() {
    return entity.getJavaType();
  }
  
  @Override
  public SQL getSql() {
    return new EntityUpdateHelper(){{
      
      update(entity);
  
      withDeleteRelation(beforeNestedMapperMethods::add);
  
      withInsertRelation(afterNestedMapperMethods::add);
      
      where(byId());
    }}
    .sql();
  }
}
