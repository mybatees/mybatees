package com.github.mybatees.mapping;

import com.github.mybatees.metamodel.method.CommonMapperMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Collection;

/**
 * 通用 MappedStatement 包装
 *
 * @author yuechao
 */
@RequiredArgsConstructor
public class CommonMappedStatement {
  
  private final CommonMapperMethod method;
  private final @Getter MappedStatement mappedStatement;
  
  public String getId() {
    return mappedStatement.getId();
  }
  
  /**
   * 是否为批量执行方法
   */
  public boolean isBatch() {
    return method.isBatch();
  }
  
  /**
   * 填充参数
   *
   * @param parameters 参数
   */
  public void populateParameters(Collection<?> parameters) {
    method.populateParameters(parameters);
  }
}
