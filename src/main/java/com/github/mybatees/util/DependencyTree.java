package com.github.mybatees.util;

import java.util.Objects;

/**
 * 依赖树
 * @param <T> 依赖管理值类型
 */
public class DependencyTree<T> {
  
  private final DependencyTree<T> dependency;
  
  private final T value;
  
  private DependencyTree(T value) {
    this(null, value);
  }
  
  private DependencyTree(DependencyTree<T> dependency, T value) {
    this.dependency = dependency;
    this.value = value;
  }
  
  public static <T> DependencyTree<T> of(T value) {
    return new DependencyTree<>(value);
  }
  
  public T current() {
    return value;
  }
  
  public DependencyTree<T> depends(T value) {
    return new DependencyTree<>(this, value);
  }
  
  public boolean hasCircleDependency(T value) {
    DependencyTree<T> parent = dependency;
    while (parent != null) {
      if (Objects.equals(parent.value, value)) {
        return true;
      }
      
      parent = parent.dependency;
    }
    
    return false;
  }
}
