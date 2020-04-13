package com.github.mybatees.metamodel.method;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.MapperMethod;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.LanguageDriver;

/**
 * MapperMethod 基类
 *
 * @author yuechao
 */
public abstract class BaseMapperMethod implements MapperMethod {
  
  protected final EntityType entity;
  protected final Attribute id;
  
  public BaseMapperMethod(EntityType entity) {
    this.entity = entity;
    this.id = entity.getId();
  }
  
  @Override
  public Class<?> getParameterType() {
    return null;
  }
  
  @Override
  public String getResultMap() {
    return null;
  }
  
  @Override
  public Class<?> getResultType() {
    return null;
  }
  
  @Override
  public Integer getFetchSize() {
    return null;
  }
  
  @Override
  public Integer getTimeout() {
    return null;
  }
  
  @Override
  public boolean isFlushCache() {
    return false;
  }
  
  @Override
  public boolean isUseCache() {
    return false;
  }
  
  @Override
  public KeyGenerator getKeyGenerator() {
    return getSqlCommandType() == SqlCommandType.INSERT ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
  }
  
  @Override
  public String getKeyProperty() {
    return null;
  }
  
  @Override
  public String getKeyColumn() {
    return null;
  }
  
  @Override
  public Class<? extends LanguageDriver> getLanguageDriverClass() {
    return null;
  }
  
  @Override
  public String getResultSets() {
    return null;
  }
  
  @Override
  public boolean isBatch() {
    return false;
  }
}
