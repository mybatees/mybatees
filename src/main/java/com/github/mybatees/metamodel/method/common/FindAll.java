package com.github.mybatees.metamodel.method.common;

import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.method.helper.EntitySelectHelper;
import com.github.mybatees.util.NamingUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.Collection;

/**
 * 查询实体集合
 *
 * @see com.github.mybatees.mapper.CrudMapper#findAll(Collection)
 * @author yuechao
 */
public class FindAll extends BaseCommonMapperMethod {
  
  public FindAll(EntityType entity) {
    super(entity);
  }
  
  @Override
  public SqlCommandType getSqlCommandType() {
    return SqlCommandType.SELECT;
  }
  
  @Override
  public Class<?> getParameterType() {
    return Collection.class;
  }
  
  @Override
  public SQL getSql() {
    return new EntitySelectHelper(){{
      
      select(entity);
      
      where(byIds());
    }}
    .sql();
  }
  
  @Override
  public String getResultMap() {
    return NamingUtils.resultMap(entity);
  }
}
