package com.github.mybatees.binding;

import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.reader.EntityTypeReader;
import com.github.mybatees.session.MybateesConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;

import java.util.HashMap;
import java.util.Map;

/**
 * 实体注册器
 *
 * @author yuechao
 */
@RequiredArgsConstructor
@Slf4j
public class EntityRegistry {
  
  private final MybateesConfiguration configuration;
  private final Map<Class<?>, EntityType> knownEntities = new HashMap<>();
  
  /**
   * 添加实体
   * @param type 实体类
   */
  public void addEntity(Class<?> type) {
    if (hasEntity(type)) {
      throw new BindingException("Type " + type + " is already known to the EntityRegistry.");
    }
    
    registerEntity(type);
  }
  
  /**
   * 判断实体是否已注册
   * @param type 实体类
   * @return 是否已注册
   */
  public boolean hasEntity(Class<?> type) {
    return knownEntities.containsKey(type);
  }
  
  /**
   * 获取实体信息
   * @param type 实体类
   * @return 实体信息
   */
  public EntityType getEntity(Class<?> type) {
    EntityType entity = knownEntities.get(type);
    if (entity == null) {
      entity = registerEntity(type);
    }
  
    return entity;
  }
  
  /**
   * 注册实体
   * @param type 实体类
   * @return 实体信息
   */
  private EntityType registerEntity(Class<?> type) {
    final EntityTypeReader reader = new EntityTypeReader(configuration, type);
    
    EntityType entity = reader.getEntity();
  
    // 为了解决循环依赖
    // 提前将实体信息写入缓存
    knownEntities.put(type, entity);
    
    // 解析实体
    reader.parse();
    
    return entity;
  }
}
