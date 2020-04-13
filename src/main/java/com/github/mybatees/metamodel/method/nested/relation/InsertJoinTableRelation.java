package com.github.mybatees.metamodel.method.nested.relation;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.util.NamingUtils;
import com.github.mybatees.util.SqlScriptUtils;
import com.github.mybatees.util.StringPool;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * 插入关联关系(表关联)
 *
 * @author yuechao
 */
public class InsertJoinTableRelation extends BaseRelationMapperMethod {
  
  public InsertJoinTableRelation(Attribute attribute) {
    super(attribute);
  }
  
  @Override
  public String getId() {
    return NamingUtils.insertRelation(attribute);
  }
  
  @Override
  public SqlCommandType getSqlCommandType() {
    return SqlCommandType.INSERT;
  }
  
  @Override
  public SQL getSql() {
    return new SQL(){{
      
      INSERT_INTO(attribute.getJoinTable());
      
      INTO_COLUMNS(attribute.getColumn(), attribute.getForeignColumn());
      
      INTO_VALUES(values());
    }};
  }
  
  @Override
  protected boolean allowAttributeNullOrEmpty() {
    return false;
  }
  
  private String values() {
    String property = attribute.getProperty();
    
    if (attribute.isSingular()) {
      return inlineValues(property);
    }
  
    String item = SqlScriptUtils.foreachItem(property);
    String values = SqlScriptUtils.foreach(property)
      .item(item)
      .separator(StringPool.COMMA_SPACE)
      .build(StringPool.LEFT_BRACKET + inlineValues(item) + StringPool.RIGHT_BRACKET);
  
    return SqlScriptUtils.trim().prefixOverrides("(").suffixOverrides(")").build(values);
  }
  
  private String inlineValues(String qualifier) {
    String columnParam = SqlScriptUtils.safeParam(
      NamingUtils.qualify(qualifier, joinId.getProperty())
    );
    String foreignColumnParam = SqlScriptUtils.safeParam(id.getProperty());
  
    return columnParam + StringPool.COMMA_SPACE + foreignColumnParam;
  }
}
