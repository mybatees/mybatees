package com.github.mybatees.metamodel.method.common;

import com.github.mybatees.metamodel.EntityType;

import java.util.Collection;

/**
 * 批量删除实体
 *
 * @see com.github.mybatees.mapper.CrudMapper#deleteAll(Collection)
 * @author yuechao
 */
public class DeleteAll extends Delete {
  
  public DeleteAll(EntityType entity) {
    super(entity);
  }
  
  @Override
  public boolean isBatch() {
    return true;
  }
}
