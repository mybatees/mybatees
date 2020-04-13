package com.github.mybatees.metamodel.method.helper;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.util.DependencyTree;
import com.github.mybatees.util.NamingUtils;
import com.github.mybatees.util.SqlScriptUtils;
import com.github.mybatees.util.StringPool;
import com.sun.deploy.util.StringUtils;
import org.apache.ibatis.jdbc.SQL;

/**
 * 实体查询帮助类
 *
 * @author yuechao
 */
public class EntitySelectHelper extends TableAliasMap {
  
  private SQL sql;
  private EntityType entity;
  private boolean count;
  private String[] conditions;
  
  /**
   * 查询实体
   */
  public void select(EntityType entity) {
    this.sql = new SQL(){{
      FROM(entity.getTable() + StringPool.SPACE + generateTableAlias(entity));
    }};
    this.entity = entity;
    this.count = false;
  }
  
  /**
   * 统计实体
   */
  public void count(EntityType entity) {
    select(entity);
    this.count = true;
  }
  
  /**
   * 查询条件
   */
  public void where(String... conditions) {
    this.conditions = conditions;
  }
  
  public SQL sql() {
    if (count) {
      sql.SELECT("COUNT(1)");
    } else {
      DependencyTree<EntityType> dependencyTree = DependencyTree.of(entity);
      for (Attribute attribute : entity.getAttributes()) {
        selectAttribute(attribute, tableAlias, StringPool.EMPTY, dependencyTree);
      }
    }
    
    if (conditions != null && conditions.length > 0) {
      sql.WHERE(conditions);
    }
    
    return sql;
  }
  
  private void selectAttribute(Attribute attribute, String tableAlias, String columnPrefix, DependencyTree<EntityType> dependencyTree) {
    if (attribute.isJoined()) {
      selectJoinAttribute(attribute, tableAlias, columnPrefix, dependencyTree);
    }
    else {
      String column = NamingUtils.qualify(tableAlias, attribute.getColumn());
      String columnAlias = columnPrefix + attribute.getProperty();
      
      sql.SELECT(SqlScriptUtils.as(column, columnAlias));
    }
  }
  
  private void selectJoinAttribute(Attribute attribute, String tableAlias, String columnPrefix, DependencyTree<EntityType> dependencyTree) {
    EntityType entity = dependencyTree.current();
    Attribute id = entity.getId();
    EntityType joinEntity = attribute.getJoinEntity();
    Attribute joinId = joinEntity.getId();
    
    if (dependencyTree.hasCircleDependency(joinEntity)) {
      return;
    }
    
    if (attribute.isTableJoin()) {
      selectTableJoinAttribute(entity, tableAlias, attribute);
    }
    else {
      
      String joinEntityTable = joinEntity.getTable();
      String joinEntityTableAlias = generateTableAlias(attribute);
      
      if (attribute.isInverse() || attribute.isPlural()) {
        sql.LEFT_OUTER_JOIN(SqlScriptUtils.simpleJoin(
          tableAlias,
          joinEntityTable, joinEntityTableAlias,
          id.getColumn(),
          attribute.getForeignColumn()
        ));
      } else {
        sql.LEFT_OUTER_JOIN(SqlScriptUtils.simpleJoin(
          tableAlias,
          joinEntityTable, joinEntityTableAlias,
          attribute.getColumn(),
          joinId.getColumn()
        ));
      }
    }
    
    tableAlias = getTableAlias(attribute);
    columnPrefix = NamingUtils.scalar(columnPrefix, attribute.getProperty());
    dependencyTree = dependencyTree.depends(joinEntity);
    for (Attribute joinAttribute : joinEntity.getAttributes()) {
      selectAttribute(joinAttribute, tableAlias, columnPrefix, dependencyTree);
    }
  }
  
  private void selectTableJoinAttribute(EntityType entity, String tableAlias, Attribute attribute) {
    Attribute id = entity.getId();
    EntityType joinEntity = attribute.getJoinEntity();
    Attribute joinId = joinEntity.getId();
    
    String joinTable = attribute.getJoinTable();
    String joinTableAlias = generateJoinTableAlias(attribute);
    
    String joinEntityTable = joinEntity.getTable();
    String joinEntityTableAlias = generateTableAlias(attribute);
  
    sql.LEFT_OUTER_JOIN(SqlScriptUtils.simpleJoin(
      tableAlias,
      joinTable, joinTableAlias,
      id.getColumn(),
      attribute.getForeignColumn()
    ));
  
    sql.LEFT_OUTER_JOIN(SqlScriptUtils.simpleJoin(
      joinTableAlias,
      joinEntityTable, joinEntityTableAlias,
      attribute.getColumn(),
      joinId.getColumn()
    ));
  }
}
