package com.github.mybatees.metamodel;

import com.github.mybatees.metamodel.method.CommonMapperMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 映射接口信息
 *
 * @author yuechao
 */
@Getter
@Setter
public class MapperInterface {
  
  private final Class<?> javaType;
  private EntityType entity;
  private final List<CommonMapperMethod> commonMethods = new ArrayList<>();
  
  public MapperInterface(Class<?> type) {
    this.javaType = type;
  }
  
  public void addCommonMethod(CommonMapperMethod method) {
    commonMethods.add(method);
  }
}
