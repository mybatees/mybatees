package com.github.mybatees.builder;

import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.metamodel.MapperInterface;
import com.github.mybatees.metamodel.MapperMethod;
import com.github.mybatees.metamodel.method.CommonMapperMethod;
import com.github.mybatees.metamodel.method.NestedMapperMethod;
import com.github.mybatees.metamodel.reader.MapperInterfaceReader;
import com.github.mybatees.session.MybateesConfiguration;
import com.github.mybatees.util.DependencyTree;
import com.github.mybatees.util.NamingUtils;
import com.github.mybatees.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通用 Mapper Builder
 *
 * @author yuechao
 */
@Slf4j
public class CommonMapperBuilder extends BaseBuilder {
  
  private final MybateesConfiguration configuration;
  private MapperInterface mapper;
  private CommonMapperBuilderAssistant assistant;
  
  public CommonMapperBuilder(MybateesConfiguration configuration) {
    super(configuration);
    this.configuration = configuration;
  }
  
  /**
   * 解析 Mapper
   *
   * @param type Mapper接口类
   */
  public void parse(Class<?> type) {
    MapperInterfaceReader reader = new MapperInterfaceReader(configuration, type);
    this.mapper = reader.getMapper();
    this.assistant = new CommonMapperBuilderAssistant(configuration, type);
    
    // 解析 MapperInterface
    reader.parse();
    
    // 注册 ResultMap
    registerResultMap(mapper.getEntity());
  
    // 注册 MappedStatement
    registerMappedStatements();
  }
  
  /**
   * 注册实体对应 ResultMap
   *
   * @param entity 实体信息
   */
  private void registerResultMap(EntityType entity) {
    String id = NamingUtils.resultMap(entity);
    registerResultMap(id, entity, StringPool.EMPTY, DependencyTree.of(entity.getJavaType()));
  }
  
  /**
   * 注册 ResultMap
   *
   * @param id resultMapId
   * @param entity 实体信息
   * @param columnPrefix 列名别名前缀
   * @param dependencyTree 依赖树
   */
  private void registerResultMap(String id, EntityType entity, String columnPrefix, DependencyTree<Class<?>> dependencyTree) {
    if (configuration.hasResultMap(id)) {
      return;
    }
    
    Class<?> entityType = entity.getJavaType();
    MapperBuilderAssistant assistant = new CommonMapperBuilderAssistant(configuration, entityType);
    
    List<ResultMapping> resultMappings = entity.getAttributes()
        .stream()
        .map(attribute -> assistant.buildResultMapping(
          entityType,
          attribute.getProperty(),
          columnPrefix + attribute.getProperty(),
          attribute.getJavaType(),
          attribute.getJdbcType(),
          // Nested Select
          null,
          // Nested Result Map
          parseNestedResultMap(attribute, columnPrefix, dependencyTree),
          // Not Null Column
          null,
          // Column Prefix
          null,
          attribute.getTypeHandler(),
          parseFlags(attribute),
          // Result Set
          null,
          attribute.getForeignColumn(),
          // Lazy
          false
        ))
        .collect(Collectors.toList());
    
    assistant.addResultMap(id, entityType, null, null, resultMappings, false);
  }
  
  /**
   * 解析 ResultFlag
   *
   * @param attribute 属性
   * @return ResultFlags
   */
  private List<ResultFlag> parseFlags(Attribute attribute) {
    if (attribute.isId()) {
      return Collections.singletonList(ResultFlag.ID);
    }
    
    return new ArrayList<>();
  }
  
  /**
   * 解析嵌套 ResultMap
   *
   * @param attribute 属性信息
   * @param columnPrefix 列名别名前缀
   * @param dependencyTree 依赖树
   * @return 嵌套 ResultMapId
   */
  private String parseNestedResultMap(Attribute attribute, String columnPrefix, DependencyTree<Class<?>> dependencyTree) {
    if (attribute.isJoined()) {
      EntityType joinEntity = attribute.getJoinEntity();
      Class<?> joinEntityType = joinEntity.getJavaType();
      
      columnPrefix = NamingUtils.scalar(columnPrefix, attribute.getProperty());
  
      if (!dependencyTree.hasCircleDependency(joinEntity.getJavaType())) {
        String id = NamingUtils.nestedResultMap(attribute, columnPrefix);
        registerResultMap(id, joinEntity, columnPrefix, dependencyTree.depends(joinEntityType));
        return id;
      }
    }
    
    return null;
  }
  
  /**
   * 注册 MappedStatements
   */
  private void registerMappedStatements() {
    for (MapperMethod method : mapper.getCommonMethods()) {
      registerMappedStatement(method);
    }
  }
  
  /**
   * 注册 MappedStatement
   *
   * @param method Mapper 方法信息
   */
  private void registerMappedStatement(MapperMethod method) {
    String id = assistant.applyCurrentNamespace(method.getId());
    if (configuration.hasStatement(id)) {
      return;
    }
    
    if (method instanceof CommonMapperMethod) {
      CommonMapperMethod commonMethod = (CommonMapperMethod) method;
      
      // 注册 MappedStatement
      MappedStatement ms = assistant.addCommonMappedStatement(commonMethod);
      
      // 注册前置嵌套方法
      for (NestedMapperMethod nested : commonMethod.getBeforeNestedMapperMethods()) {
        assistant.addBeforeNestedMappedStatement(ms, nested);
      }
  
      // 注册后置嵌套方法
      for (NestedMapperMethod nested : commonMethod.getAfterNestedMapperMethods()) {
        assistant.addAfterNestedMappedStatement(ms, nested);
      }
    }
  }
}
