package com.github.mybatees.metamodel.method.common;

import com.github.mybatees.metamodel.EntityType;

import java.util.Collection;

/**
 * 批量保存实体
 *
 * @see com.github.mybatees.mapper.CrudMapper#saveAll(Collection)
 * @author yuechao
 */
public class SaveAll extends Save {
  
  public SaveAll(EntityType entity) {
    super(entity);
  }
  
  @Override
  public boolean isBatch() {
    return true;
  }
}
