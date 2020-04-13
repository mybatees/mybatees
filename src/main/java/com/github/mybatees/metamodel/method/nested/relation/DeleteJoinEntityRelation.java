package com.github.mybatees.metamodel.method.nested.relation;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.util.NamingUtils;
import com.github.mybatees.util.SqlScriptUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * 删除关联关系(键关联)
 *
 * @author yuechao
 */
public class DeleteJoinEntityRelation extends BaseRelationMapperMethod {
  
  private final String[] conditions;
  
  public DeleteJoinEntityRelation(Attribute attribute, String... conditions) {
    super(attribute);
    this.conditions = conditions;
  }
  
  @Override
  public String getId() {
    return NamingUtils.deleteRelation(attribute);
  }
  
  @Override
  public SqlCommandType getSqlCommandType() {
    return SqlCommandType.UPDATE;
  }
  
  @Override
  public SQL getSql() {
    String foreignColumn = attribute.getForeignColumn();
    
    return new SQL(){{
    
      UPDATE(joinEntity.getTable());
      
      SET(SqlScriptUtils.setNull(foreignColumn));
  
      WHERE(SqlScriptUtils.in(foreignColumn, selectEntityIds(conditions)));
    }};
  }
  
  @Override
  protected boolean allowAttributeNullOrEmpty() {
    return true;
  }
}
