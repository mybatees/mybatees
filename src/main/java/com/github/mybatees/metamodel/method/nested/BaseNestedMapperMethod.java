package com.github.mybatees.metamodel.method.nested;

import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.method.BaseMapperMethod;
import com.github.mybatees.metamodel.method.NestedMapperMethod;

/**
 * 嵌套 MapperMethod 基类
 *
 * @author yuechao
 */
public abstract class BaseNestedMapperMethod extends BaseMapperMethod implements NestedMapperMethod {
  
  public BaseNestedMapperMethod(EntityType entity) {
    super(entity);
  }
}
