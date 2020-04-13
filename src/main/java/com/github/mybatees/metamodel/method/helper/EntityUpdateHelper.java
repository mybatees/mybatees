package com.github.mybatees.metamodel.method.helper;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.method.nested.relation.*;
import com.github.mybatees.util.NamingUtils;
import com.github.mybatees.util.SqlScriptUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.function.Consumer;

/**
 * 实体更新帮助类
 *
 * @author yuechao
 */
public class EntityUpdateHelper {
  
  private SQL sql;
  private EntityType entity;
  private Consumer<BaseRelationMapperMethod> relationDeleteConsumer;
  private Consumer<BaseRelationMapperMethod> relationInsertConsumer;
  private String[] conditions;
  
  /**
   * 更新实体
   */
  public void update(EntityType entity) {
    this.sql = new SQL(){{
      UPDATE(entity.getTable());
    }};
    this.entity = entity;
  }
  
  /**
   * 处理原关联关系删除
   */
  public void withDeleteRelation(Consumer<BaseRelationMapperMethod> relationConsumer) {
    this.relationDeleteConsumer = relationConsumer;
  }
  
  /**
   * 处理新关联关系插入
   */
  public void withInsertRelation(Consumer<BaseRelationMapperMethod> relationConsumer) {
    this.relationInsertConsumer = relationConsumer;
  }
  
  /**
   * 更新条件
   */
  public void where(String... conditions) {
    this.conditions = conditions;
  }
  
  public SQL sql() {
    for (Attribute attribute : entity.getAttributes()) {
      updateAttribute(attribute);
    }
    
    sql.WHERE(conditions);
    
    return sql;
  }
  
  private void updateAttribute(Attribute attribute) {
    if (attribute.isId() || attribute.isCreateAt()) {
      return;
    }
  
    if (attribute.isTableJoin()) {
      consumerRelation(relationDeleteConsumer, new DeleteJoinTableRelation(attribute, conditions));
      consumerRelation(relationInsertConsumer, new InsertJoinTableRelation(attribute));
    }
    else if (attribute.isInverse() || attribute.isPlural()) {
      consumerRelation(relationDeleteConsumer, new DeleteJoinEntityRelation(attribute, conditions));
      consumerRelation(relationInsertConsumer, new InsertJoinEntityRelation(attribute));
    }
    else {
      sql.SET(setAttribute(attribute));
    }
  }
  
  private void consumerRelation(Consumer<BaseRelationMapperMethod> relationConsumer, BaseRelationMapperMethod relation) {
    if (relationConsumer != null) {
      relationConsumer.accept(relation);
    }
  }
  
  private String setAttribute(Attribute attribute) {
    String column = attribute.getColumn();
    String property = attribute.getProperty();
    
    if (attribute.isJoined()) {
      property = NamingUtils.qualify(property, attribute.getJoinEntity().getId().getProperty());
    }
    
    return SqlScriptUtils.safeSet(column, property);
  }
}
