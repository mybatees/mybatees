package com.github.mybatees.mapping;

import com.github.mybatees.metamodel.method.NestedMapperMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 嵌套 MappedStatement 包装
 *
 * @author yuechao
 */
@EqualsAndHashCode
public class NestedMappedStatement {
  
  private final NestedMapperMethod method;
  private final @Getter MappedStatement mappedStatement;
  
  public NestedMappedStatement(NestedMapperMethod method, MappedStatement mappedStatement) {
    this.method = method;
    this.mappedStatement = mappedStatement;
  }
  
  /**
   * 检验参数
   * 如果参数不合规，则不会执行
   *
   * @param parameter 参数
   */
  public boolean validateParameter(Object parameter) {
    return method.validateParameter(parameter);
  }
}
