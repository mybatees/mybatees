package com.github.mybatees.metamodel.method.nested.relation;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.util.NamingUtils;
import com.github.mybatees.util.SqlScriptUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * 删除关联关系(表关联)
 *
 * @author yuechao
 */
public class DeleteJoinTableRelation extends BaseRelationMapperMethod {
  
  private String[] conditions;
  
  public DeleteJoinTableRelation(Attribute attribute, String... conditions) {
    super(attribute);
    this.conditions = conditions;
  }
  
  @Override
  public String getId() {
    return NamingUtils.deleteRelation(attribute);
  }
  
  @Override
  public SqlCommandType getSqlCommandType() {
    return SqlCommandType.DELETE;
  }
  
  @Override
  public SQL getSql() {
    return new SQL(){{
    
      DELETE_FROM(attribute.getJoinTable());
      
      WHERE(SqlScriptUtils.in(attribute.getForeignColumn(), selectEntityIds(conditions)));
    }};
  }
  
  @Override
  protected boolean allowAttributeNullOrEmpty() {
    return true;
  }
}
