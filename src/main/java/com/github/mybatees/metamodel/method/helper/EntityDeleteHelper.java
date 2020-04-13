package com.github.mybatees.metamodel.method.helper;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.method.nested.relation.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.function.Consumer;

/**
 * 实体删除帮助类
 *
 * @author yuechao
 */
public class EntityDeleteHelper {
  
  private SQL sql;
  private EntityType entity;
  private Consumer<BaseRelationMapperMethod> relationDeleteConsumer;
  private String[] conditions;
  
  /**
   * 删除实体
   */
  public void delete(EntityType entity) {
    this.sql = new SQL(){{
      DELETE_FROM(entity.getTable());
    }};
    this.entity = entity;
  }
  
  /**
   * 处理关联关系删除
   */
  public void withDeleteRelation(Consumer<BaseRelationMapperMethod> relationConsumer) {
    this.relationDeleteConsumer = relationConsumer;
  }
  
  /**
   * 设定删除条件
   */
  public void where(String... conditions) {
    this.conditions = conditions;
  }
  
  public SQL sql() {
    for (Attribute attribute : entity.getAttributes()) {
      deleteRelation(attribute);
    }
    
    sql.WHERE(conditions);
    
    return sql;
  }
  
  private void deleteRelation(Attribute attribute) {
    if (attribute.isTableJoin()) {
      consumerRelation(new DeleteJoinTableRelation(attribute, conditions));
    }
    else if (attribute.isInverse() || attribute.isPlural()) {
      consumerRelation(new DeleteJoinEntityRelation(attribute, conditions));
    }
  }
  
  private void consumerRelation(BaseRelationMapperMethod relation) {
    if (relationDeleteConsumer != null) {
      relationDeleteConsumer.accept(relation);
    }
  }
}
