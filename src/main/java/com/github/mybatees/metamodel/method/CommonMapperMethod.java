package com.github.mybatees.metamodel.method;

import com.github.mybatees.metamodel.MapperMethod;

import java.util.Collection;
import java.util.List;

/**
 * 通用映射方法信息
 *
 * @author yuechao
 */
public interface CommonMapperMethod extends MapperMethod {
  
  /**
   * 填充参数
   */
  void populateParameters(Collection<?> parameters);
  
  /**
   * 获取前置嵌套 MapperMethod
   */
  List<NestedMapperMethod> getBeforeNestedMapperMethods();
  
  /**
   * 获取后置嵌套 MapperMethod
   */
  List<NestedMapperMethod> getAfterNestedMapperMethods();
}
