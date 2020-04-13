package com.github.mybatees.util;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.metamodel.EntityType;
import lombok.NonNull;

import java.util.function.Supplier;

/**
 * 名称工具类
 *
 * @author yuechao
 */
public class NamingUtils {
  
  private static final String WATERMARK = " (mybatees)";
  
  /**
   * 使用 `.` 修饰名称
   *
   * @param qualifier 修饰词
   * @param name 名称
   * @return 修饰名称
   */
  public static String qualify(String qualifier, String name) {
    return join(qualifier, StringPool.DOT, name);
  }
  
  /**
   * 使用 `_` 连接名称
   *
   * @param prefix 前缀
   * @param name 名称
   * @return 修饰名称
   */
  public static String scalar(String prefix, String name) {
    return join(prefix, StringPool.UNDERSCORE, name);
  }
  
  /**
   * 生成默认表名
   * 生成规则：将驼峰式实体类名转为大写下划线连接方式
   *
   * @param entity 实体信息
   * @return 默认表名
   */
  public static String defaultTable(EntityType entity) {
    return camelToUnderscore(entity.getName());
  }
  
  /**
   * 生成默认列名
   * 生成规则：将驼峰式字段名转为大写下划线连接方式
   *
   * @param attribute 属性信息
   * @return 默认列名
   */
  public static String defaultColumn(Attribute attribute) {
    return camelToUnderscore(attribute.getField().getName());
  }
  
  /**
   * 生成默认关联表名
   * 生成规则：表名_字段默认列名
   *
   * @param attribute 属性信息
   * @return 默认关联表名
   */
  public static String defaultJoinTable(Attribute attribute) {
    return scalar(attribute.getEntity().getTable(), defaultColumn(attribute));
  }
  
  /**
   * 生成默认关联列名
   * 生成规则：关联表名_关联表主键列名
   *
   * @param attribute 属性信息
   * @return 默认关联列名
   */
  public static String defaultJoinColumn(Attribute attribute) {
    EntityType joinEntity = attribute.getJoinEntity();
    return scalar(joinEntity.getTable(), joinEntity.getId().getColumn());
  }
  
  /**
   * 生成默认外键列名
   * 生成规则：表名_主键列名
   *
   * @return 默认外键列名
   */
  public static String defaultForeignColumn(Attribute attribute) {
    EntityType entity = attribute.getEntity();
    return scalar(entity.getTable(), entity.getId().getColumn());
  }
  
  public static String commonMethod(Class<?> type) {
    return lowerCaseFirst(type.getSimpleName());
  }
  
  public static String resultMap(EntityType entity) {
    return entity.getJavaType().getName() + WATERMARK;
  }
  
  public static String nestedResultMap(Attribute attribute, String alias) {
    return attribute.getEntity().getName() + "$Nested-" + alias + WATERMARK;
  }
  
  public static String insertRelation(Attribute attribute) {
    return "Insert-Relation$" + attribute.getProperty() + WATERMARK;
  }
  
  public static String deleteRelation(Attribute attribute) {
    return "Delete-Relation$" + attribute.getProperty() + WATERMARK;
  }
  
  public static String validName(String name, Supplier<String> defaultName) {
    return isValid(name) ? name.trim() : defaultName.get();
  }
  
  public static boolean isValid(String name) {
    return !isInvalid(name);
  }
  
  public static boolean isInvalid(String name) {
    return name == null || name.trim().isEmpty();
  }
  
  private static String join(String prefix, String delimiter, String suffix) {
    if (isInvalid(prefix)) {
      return suffix;
    }
  
    if (isInvalid(suffix)) {
      return prefix;
    }
  
    return prefix + delimiter + suffix;
  }
  
  /**
   * 驼峰转下划线
   */
  private static String camelToUnderscore(@NonNull String name) {
    StringBuilder buf = new StringBuilder(name.replace('.', '_'));
    for (int i = 1; i < buf.length() - 1; i++) {
      if (Character.isLowerCase(buf.charAt(i - 1)) &&
        Character.isUpperCase(buf.charAt(i)) &&
        Character.isLowerCase(buf.charAt(i + 1))
      ) {
        buf.insert(i++, '_');
      }
    }
    return buf.toString().toUpperCase();
  }
  
  /**
   * 小写第一个字符
   */
  private static String lowerCaseFirst(@NonNull String name) {
    return Character.toLowerCase(name.charAt(0)) + name.substring(1);
  }
}
