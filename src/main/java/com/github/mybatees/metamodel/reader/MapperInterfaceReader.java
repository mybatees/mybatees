package com.github.mybatees.metamodel.reader;

import com.github.mybatees.mapper.CommonMapper;
import com.github.mybatees.mapper.CrudMapper;
import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.MapperInterface;
import com.github.mybatees.metamodel.method.CommonMapperMethod;
import com.github.mybatees.metamodel.method.common.*;
import com.github.mybatees.session.MybateesConfiguration;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Mapper 接口解析器
 *
 * @author yuechao
 */
public class MapperInterfaceReader {
  
  @SuppressWarnings("rawtypes")
  private static final Map<Class<? extends CommonMapper>, List<Function<EntityType, CommonMapperMethod>>> COMMON_MAPPER_METHODS = new HashMap<>();
  static {
    COMMON_MAPPER_METHODS.put(CrudMapper.class, Arrays.asList(
      Save::new, SaveAll::new,
      Find::new, FindAll::new,
      Exists::new, Count::new,
      Update::new, UpdateAll::new,
      Delete::new, DeleteAll::new
    ));
  }
  
  private final MybateesConfiguration configuration;
  private final Class<?> type;
  private final @Getter MapperInterface mapper;
  
  public MapperInterfaceReader(MybateesConfiguration configuration, Class<?> type) {
    this.configuration = configuration;
    this.type = type;
    this.mapper = new MapperInterface(type);
  }
  
  /**
   * 解析 Mapper
   */
  public void parse() {
    // 解析 Mapper 对应的实体类
    EntityType entity = parseManagedEntity();
    
    mapper.setEntity(entity);
    
    COMMON_MAPPER_METHODS.forEach((mapperInterface, mapperMethods) -> {
      if (mapperInterface.isAssignableFrom(type)) {
        mapperMethods.forEach(method -> mapper.addCommonMethod(method.apply(entity)));
      }
    });
  }
  
  /**
   * 解析 Mapper 管理的实体
   */
  private EntityType parseManagedEntity() {
    Type[] interfaces = type.getGenericInterfaces();
    for (Type type : interfaces) {
      if (type instanceof ParameterizedType) {
        Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
        if (arguments != null) {
          Class<?> parameter = (Class<?>) arguments[0];
          
          if (parameter != null) {
            return configuration.getEntity(parameter);
          }
        }
      }
    }
    
    throw new IllegalArgumentException("Cannot resolve entity type of mapper: " + type);
  }
}
