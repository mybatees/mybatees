package com.github.mybatees.metamodel;

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.LanguageDriver;

/**
 * 映射方法信息
 *
 * @author yuechao
 */
public interface MapperMethod {
  
  /**
   * 获取 Id
   */
  String getId();
  
  /**
   * 获取 sqlCommandType
   */
  SqlCommandType getSqlCommandType();
  
  /**
   * 获取 parameterType
   */
  Class<?> getParameterType();
  
  /**
   * 获取 sql
   */
  SQL getSql();
  
  /**
   * 获取 resultMap
   */
  String getResultMap();
  
  /**
   * 获取 resultType
   */
  Class<?> getResultType();
  
  /**
   * 获取 fetchSize
   */
  Integer getFetchSize();
  
  /**
   * 获取 timeout
   */
  Integer getTimeout();
  
  /**
   * 获取 isFlushCache
   */
  boolean isFlushCache();
  
  /**
   * 获取 isUseCache
   */
  boolean isUseCache();
  
  /**
   * 获取 keyGenerator
   */
  KeyGenerator getKeyGenerator();
  
  /**
   * 获取 keyProperty
   */
  String getKeyProperty();
  
  /**
   * 获取 keyColumn
   */
  String getKeyColumn();
  
  /**
   * 获取 languageDriverClass
   */
  Class<? extends LanguageDriver> getLanguageDriverClass();
  
  /**
   * 获取 resultSets
   */
  String getResultSets();
  
  
  /**
   * 获取 isBatch
   */
  boolean isBatch();
}
