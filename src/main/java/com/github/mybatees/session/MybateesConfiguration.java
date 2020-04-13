package com.github.mybatees.session;

import com.github.mybatees.interceptor.MybateesInterceptor;
import com.github.mybatees.mapping.CommonMappedStatement;
import com.github.mybatees.mapping.NestedMappedStatement;
import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.binding.EntityRegistry;
import com.github.mybatees.binding.TablePrefixRegistry;
import com.github.mybatees.util.StringPool;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Mybatees {@link Configuration}
 *
 * @author yuechao
 */
public class MybateesConfiguration extends Configuration {
  
  private final Map<String, CommonMappedStatement> commonMappedStatements = new HashMap<>();
  private final Map<String, Set<NestedMappedStatement>> beforeNestedMappedStatements = new HashMap<>();
  private final Map<String, Set<NestedMappedStatement>> afterNestedMappedStatements = new HashMap<>();
  
  private final TablePrefixRegistry tablePrefixRegistry = new TablePrefixRegistry(this);
  private final EntityRegistry entityRegistry = new EntityRegistry(this);
  
  public MybateesConfiguration(Configuration configuration) {
    // 复制 Configuration
    for (Field field : Configuration.class.getDeclaredFields()) {
      Object value = ReflectionUtil.getFieldValue(field, configuration);
      ReflectionUtil.setFieldValue(field, this, value);
    }
  
    // 根据属性配置注册表名前缀
    tablePrefixRegistry.doRegisterFromConfigurationProperties();
    
    addInterceptor(new MybateesInterceptor(this));
  }
  
  public void setTablePrefix(String tablePrefix) {
    tablePrefixRegistry.addTablePrefix(StringPool.EMPTY, tablePrefix);
  }
  
  public void addTablePrefix(String packageName, String tablePrefix) {
    tablePrefixRegistry.addTablePrefix(packageName, tablePrefix);
  }
  
  public String getTablePrefix(Class<?> type) {
    return tablePrefixRegistry.getTablePrefix(type);
  }
  
  public void addEntity(Class<?> type) {
    entityRegistry.addEntity(type);
  }
  
  public EntityType getEntity(Class<?> type) {
    return entityRegistry.getEntity(type);
  }
  
  public void addCommonMappedStatement(CommonMappedStatement ms) {
    commonMappedStatements.put(ms.getId(), ms);
  }
  
  public boolean isCommonMappedStatement(MappedStatement ms) {
    return commonMappedStatements.containsKey(ms.getId());
  }
  
  public CommonMappedStatement getCommonMappedStatement(MappedStatement ms) {
    return commonMappedStatements.get(ms.getId());
  }
  
  public void addBeforeMappedStatement(MappedStatement ms, NestedMappedStatement nested) {
    beforeNestedMappedStatements
      .computeIfAbsent(ms.getId(), id -> new HashSet<>())
      .add(nested);
  }
  
  public Set<NestedMappedStatement> getBeforeMappedStatements(MappedStatement ms) {
    return beforeNestedMappedStatements.getOrDefault(ms.getId(), new HashSet<>());
  }
  
  public void addAfterMappedStatement(MappedStatement ms, NestedMappedStatement nested) {
    afterNestedMappedStatements
      .computeIfAbsent(ms.getId(), id -> new HashSet<>())
      .add(nested);
  }
  
  public Set<NestedMappedStatement> getAfterMappedStatements(MappedStatement ms) {
    return afterNestedMappedStatements.getOrDefault(ms.getId(), new HashSet<>());
  }
}
