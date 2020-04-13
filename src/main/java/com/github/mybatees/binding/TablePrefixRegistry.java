package com.github.mybatees.binding;

import com.github.mybatees.session.MybateesConfiguration;
import com.github.mybatees.util.StringPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 表名前缀注册器
 *
 * @author yuechao
 */
public class TablePrefixRegistry {
  
  private static final String PROPERTY_TABLE_PREFIX = "mybatees.table.prefix";
  
  private final MybateesConfiguration configuration;
  private final Map<String, String> tablePrefixes = new HashMap<>();
  
  public TablePrefixRegistry(MybateesConfiguration configuration) {
    this.configuration = configuration;
  }
  
  /**
   * 添加表名前缀
   * @param packageName 包名
   * @param tablePrefix 表名前缀
   */
  public void addTablePrefix(String packageName, String tablePrefix) {
    tablePrefixes.put(packageName, tablePrefix);
  }
  
  /**
   * 获取表名前缀
   * @param type Java 类
   * @return 表名前缀
   */
  public String getTablePrefix(Class<?> type) {
    String namespace = type.getPackage().getName();
    String prefix;
    while ((prefix = tablePrefixes.get(namespace)) == null) {
      int dotIndex = namespace.lastIndexOf(".");
      namespace = dotIndex > 0 ? namespace.substring(0, dotIndex) : "";
    }
  
    return prefix;
  }
  
  /**
   * 注册在 mybatis-config.xml 中配置的表名前缀
   *
   * <pre>
   *   1. 配置默认表名前缀
   *   &lt;property name="mybatees.table.prefix" value="T" /&gt;
   *
   *   2. 配置指定包名的表名前缀
   *   &lt;property name="mybatees.table.prefix.com.github.mybatees" value="MYBATEES" /&gt;
   * </pre>
   */
  public void doRegisterFromConfigurationProperties() {
    final Properties variables = configuration.getVariables();
  
    // 配置默认表名前缀
    addTablePrefix(StringPool.EMPTY, variables.getProperty(PROPERTY_TABLE_PREFIX, StringPool.EMPTY));
    // 配置指定包名的表名前缀
    final String keyPrefix = PROPERTY_TABLE_PREFIX + StringPool.DOT;
    variables.forEach((key, val) -> {
      if (key == null || val == null) {
        return;
      }
    
      String packageName = String.valueOf(key);
      String tablePrefix = String.valueOf(val);
      if (packageName.startsWith(keyPrefix)) {
        addTablePrefix(packageName.substring(keyPrefix.length()), tablePrefix);
      }
    });
  }
}
