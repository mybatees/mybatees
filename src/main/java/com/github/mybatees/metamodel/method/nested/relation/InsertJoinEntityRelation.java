package com.github.mybatees.metamodel.method.nested.relation;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.util.NamingUtils;
import com.github.mybatees.util.SqlScriptUtils;
import com.github.mybatees.util.StringPool;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * 插入关联关系(键关联)
 *
 * @author yuechao
 */
public class InsertJoinEntityRelation extends BaseRelationMapperMethod {
  
  public InsertJoinEntityRelation(Attribute attribute) {
    super(attribute);
  }
  
  @Override
  public String getId() {
    return NamingUtils.insertRelation(attribute);
  }
  
  @Override
  public SqlCommandType getSqlCommandType() {
    return SqlCommandType.UPDATE;
  }
  
  @Override
  public SQL getSql() {
    return new SQL(){{
    
      UPDATE(joinEntity.getTable());
      
      SET(SqlScriptUtils.safeSet(attribute.getForeignColumn(), id.getProperty()));
  
      WHERE(condition());
    }};
  }
  
  @Override
  protected boolean allowAttributeNullOrEmpty() {
    return false;
  }
  
  private String condition() {
    String property = attribute.getProperty();
    
    if (attribute.isSingular()) {
      return SqlScriptUtils.eq(joinId.getColumn(), parameter(property));
    }
  
    String item = SqlScriptUtils.foreachItem(property);
  
    return SqlScriptUtils.in(joinId.getColumn(),
      SqlScriptUtils.foreach(property)
        .item(item)
        .separator(StringPool.COMMA_SPACE)
        .build(parameter(item))
    );
  }
  
  private String parameter(String qualifier) {
    return SqlScriptUtils.safeParam(NamingUtils.qualify(qualifier, joinId.getProperty()));
  }
}
