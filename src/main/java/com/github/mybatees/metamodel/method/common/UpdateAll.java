package com.github.mybatees.metamodel.method.common;

import com.github.mybatees.metamodel.EntityType;

/**
 * 批量更新实体
 *
 * @see com.github.mybatees.mapper.CrudMapper#update(Object)
 * @author yuechao
 */
public class UpdateAll extends Update {
  
  public UpdateAll(EntityType entity) {
    super(entity);
  }
  
  @Override
  public boolean isBatch() {
    return true;
  }
}
