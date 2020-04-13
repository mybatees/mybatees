package com.github.mybatees.metamodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体类型信息
 *
 * @author yuechao
 */
@RequiredArgsConstructor
@Getter
@Setter
public class EntityType {
  
  private final Class<?> javaType;
  private String table;
  private Attribute id;
  private final List<Attribute> attributes = new ArrayList<>();
  
  public String getName() {
    return javaType.getSimpleName();
  }
  
  public void addAttribute(Attribute attribute) {
    attributes.add(attribute);
  }
}
