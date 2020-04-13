package com.github.mybatees.metamodel.method.helper;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.metamodel.EntityType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 表别名帮助类
 *
 * @author yuechao
 */
public class TableAliasMap {
  
  protected String tableAlias;
  private final Map<Field, String> tableAliasMap = new HashMap<>();
  private final Map<Field, String> joinTableAliasMap = new HashMap<>();
  
  private int counter = 0;
  
  private String generate(String name) {
    return name + counter++;
  }
  
  /**
   * 生成主表别名
   *
   * @param entity 实体信息
   * @return 主表别名
   */
  protected String generateTableAlias(EntityType entity) {
    this.counter = 0;
    String tableAlias = generate(entity.getTable());
    this.tableAlias = tableAlias;
    return tableAlias;
  }
  
  /**
   * 生成关联表别名
   *
   * @param attribute 关联属性信息
   * @return 关联表别名
   */
  protected String generateTableAlias(Attribute attribute) {
    String alias = generate(attribute.getJoinEntity().getTable());
    this.tableAliasMap.put(attribute.getField(), alias);
    return alias;
  }
  
  /**
   * 生成关联关系表别名
   *
   * @param attribute 关联属性信息
   * @return 关联关系表别名
   */
  protected String generateJoinTableAlias(Attribute attribute) {
    String alias = generate(attribute.getJoinTable());
    this.joinTableAliasMap.put(attribute.getField(), alias);
    return alias;
  }
  
  /**
   * 获取主表别名
   */
  public String getTableAlias() {
    return tableAlias;
  }
  
  /**
   * 获取关联表别名
   *
   * @param attribute 关联属性信息
   */
  public String getTableAlias(Attribute attribute) {
    return tableAliasMap.getOrDefault(attribute.getField(), tableAlias);
  }
  
  /**
   * 获取关联关系表别名
   *
   * @param attribute 关联属性信息
   */
  public String getJoinTableAlias(Attribute attribute) {
    return joinTableAliasMap.get(attribute.getField());
  }
}
