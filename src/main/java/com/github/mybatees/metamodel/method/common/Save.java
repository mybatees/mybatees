package com.github.mybatees.metamodel.method.common;

import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.method.helper.EntityInsertHelper;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * 保存实体
 *
 * @see com.github.mybatees.mapper.CrudMapper#save(Object)
 * @author yuechao
 */
public class Save extends BaseCommonMapperMethod {
  
  public Save(EntityType entity) {
    super(entity);
  }
  
  @Override
  public SqlCommandType getSqlCommandType() {
    return SqlCommandType.INSERT;
  }
  
  @Override
  public Class<?> getParameterType() {
    return entity.getJavaType();
  }
  
  @Override
  public SQL getSql() {
    return new EntityInsertHelper(){{
      
      insert(entity);
  
      withInsertRelation(afterNestedMapperMethods::add);
    }}
    .sql();
  }
}
