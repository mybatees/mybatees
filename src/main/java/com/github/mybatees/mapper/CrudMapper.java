package com.github.mybatees.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * CRUD Mapper 接口
 *
 * @author yuechao
 */
public interface CrudMapper<T, ID> extends CommonMapper<T> {
  
  /**
   * 保存实体
   *
   * @param entity 实体
   */
  void save(T entity);
  
  /**
   * 批量保存实体
   *
   * @param entities 实体集合
   */
  void saveAll(Collection<T> entities);
  
  /**
   * 查询实体
   *
   * @param id 主键
   * @return 实体
   */
  Optional<T> find(ID id);
  
  /**
   * 查询实体集合
   *
   * @param ids 主键集合
   * @return 实体集合
   */
  List<T> findAll(Collection<ID> ids);
  
  /**
   * 计数
   *
   * @return 实体数量
   */
  long count();
  
  /**
   * 判断实体是否存在
   *
   * @param id 主键
   * @return 实体是否存在
   */
  boolean exists(ID id);
  
  /**
   * 更新实体
   *
   * @param entity 实体
   */
  void update(T entity);
  
  /**
   * 批量更新实体
   *
   * @param entities 实体集合
   */
  void updateAll(Collection<T> entities);
  
  /**
   * 删除实体
   *
   * @param id 主键
   */
  void delete(ID id);
  
  /**
   * 批量删除实体
   *
   * @param ids 主键集合
   */
  void deleteAll(Collection<ID> ids);
}
