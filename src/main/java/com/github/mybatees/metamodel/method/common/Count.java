package com.github.mybatees.metamodel.method.common;

import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.method.helper.EntitySelectHelper;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * 计数
 *
 * @see com.github.mybatees.mapper.CrudMapper#count()
 * @author yuechao
 */
public class Count extends BaseCommonMapperMethod {
  
  public Count(EntityType entity) {
    super(entity);
  }
  
  @Override
  public SqlCommandType getSqlCommandType() {
    return SqlCommandType.SELECT;
  }
  
  @Override
  public SQL getSql() {
    return new EntitySelectHelper(){{
      count(entity);
    }}
    .sql();
  }
  
  @Override
  public Class<?> getResultType() {
    return Long.TYPE;
  }
}
