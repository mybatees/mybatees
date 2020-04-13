package com.github.mybatees.metamodel.method.common;

import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.method.helper.EntitySelectHelper;
import com.github.mybatees.util.NamingUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * 查询实体
 *
 * @see com.github.mybatees.mapper.CrudMapper#find(Object)
 * @author yuechao
 */
public class Find extends BaseCommonMapperMethod {
  
  public Find(EntityType entity) {
    super(entity);
  }
  
  @Override
  public SqlCommandType getSqlCommandType() {
    return SqlCommandType.SELECT;
  }
  
  @Override
  public Class<?> getParameterType() {
    return id.getJavaType();
  }
  
  @Override
  public SQL getSql() {
    return new EntitySelectHelper(){{
      
      select(entity);
      
      where(byId());
    }}
    .sql();
  }
  
  @Override
  public String getResultMap() {
    return NamingUtils.resultMap(entity);
  }
}
