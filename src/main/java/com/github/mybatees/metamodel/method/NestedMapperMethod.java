package com.github.mybatees.metamodel.method;

import com.github.mybatees.metamodel.MapperMethod;

/**
 * 嵌套映射方法信息
 *
 * @author yuechao
 */
public interface NestedMapperMethod extends MapperMethod {
  
  /**
   * 检验参数
   */
  boolean validateParameter(Object parameter);
}
