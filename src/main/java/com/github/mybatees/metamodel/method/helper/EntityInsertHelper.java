package com.github.mybatees.metamodel.method.helper;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.method.nested.relation.InsertJoinEntityRelation;
import com.github.mybatees.metamodel.method.nested.relation.InsertJoinTableRelation;
import com.github.mybatees.metamodel.method.nested.relation.BaseRelationMapperMethod;
import com.github.mybatees.util.NamingUtils;
import com.github.mybatees.util.SqlScriptUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.function.Consumer;

/**
 * 实体插入帮助类
 *
 * @author yuechao
 */
public class EntityInsertHelper {
  
  private SQL sql;
  private EntityType entity;
  private Consumer<BaseRelationMapperMethod> relationInsertConsumer;
  
  /**
   * 插入实体
   */
  public void insert(EntityType entity) {
    this.sql = new SQL(){{
      INSERT_INTO(entity.getTable());
    }};
    this.entity = entity;
  }
  
  /**
   * 处理关联关系插入
   */
  public void withInsertRelation(Consumer<BaseRelationMapperMethod> relationConsumer) {
    this.relationInsertConsumer = relationConsumer;
  }
  
  public SQL sql() {
    for (Attribute attribute : entity.getAttributes()) {
      insertAttribute(attribute);
    }
    
    return sql;
  }
  
  private void insertAttribute(Attribute attribute) {
    if (attribute.isUpdateAt()) {
      return;
    }
    
    if (attribute.isTableJoin()) {
      consumerRelation(new InsertJoinTableRelation(attribute));
    }
    else if (attribute.isInverse() || attribute.isPlural()) {
      consumerRelation(new InsertJoinEntityRelation(attribute));
    }
    else {
      sql.VALUES(attribute.getColumn(), attributeValue(attribute));
    }
  }
  
  private void consumerRelation(BaseRelationMapperMethod relation) {
    if (relationInsertConsumer != null) {
      relationInsertConsumer.accept(relation);
    }
  }
  
  private String attributeValue(Attribute attribute) {
    String property = attribute.getProperty();
    
    if (attribute.isJoined()) {
      property = NamingUtils.qualify(property, attribute.getJoinEntity().getId().getProperty());
    }
    
    return SqlScriptUtils.safeParam(property);
  }
}
