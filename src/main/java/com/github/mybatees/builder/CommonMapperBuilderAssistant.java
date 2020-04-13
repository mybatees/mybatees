package com.github.mybatees.builder;

import com.github.mybatees.mapping.CommonMappedStatement;
import com.github.mybatees.mapping.NestedMappedStatement;
import com.github.mybatees.metamodel.MapperMethod;
import com.github.mybatees.metamodel.method.CommonMapperMethod;
import com.github.mybatees.metamodel.method.NestedMapperMethod;
import com.github.mybatees.session.MybateesConfiguration;
import com.github.mybatees.util.SqlScriptUtils;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;

/**
 * 通用 Mapper 注册助手
 *
 * @author yuechao
 */
public class CommonMapperBuilderAssistant extends MapperBuilderAssistant {
  
  protected final MybateesConfiguration configuration;
  
  public CommonMapperBuilderAssistant(MybateesConfiguration configuration, Class<?> type) {
    super(configuration, type.getName());
    this.configuration = configuration;
    String namespace = type.isInterface() ? type.getName() : type.getPackage().getName();
    setCurrentNamespace(namespace);
  }
  
  public String applyCurrentNamespace(String base) {
    return super.applyCurrentNamespace(base, false);
  }
  
  /**
   * 注册通用 MappedStatement
   *
   * @param method 通用 MapperMethod 信息
   * @return MappedStatement
   */
  public MappedStatement addCommonMappedStatement(CommonMapperMethod method) {
    MappedStatement mappedStatement = addMappedStatement(method);
    CommonMappedStatement commonMappedStatement = new CommonMappedStatement(method, mappedStatement);
    configuration.addCommonMappedStatement(commonMappedStatement);
    return mappedStatement;
  }
  
  /**
   * 注册前置嵌套 MappedStatement
   *
   * @param ms MappedStatement
   * @param method 嵌套 MapperMethod 信息
   */
  public void addBeforeNestedMappedStatement(MappedStatement ms, NestedMapperMethod method) {
    String id = applyCurrentNamespace(method.getId());
    MappedStatement mappedStatement = configuration.hasStatement(id)
      ? configuration.getMappedStatement(id)
      : addMappedStatement(method);
    NestedMappedStatement nestedMappedStatement = new NestedMappedStatement(method, mappedStatement);
    configuration.addBeforeMappedStatement(ms, nestedMappedStatement);
  }
  
  /**
   * 注册后置嵌套 MappedStatement
   *
   * @param ms MappedStatement
   * @param method 嵌套 MapperMethod 信息
   */
  public void addAfterNestedMappedStatement(MappedStatement ms, NestedMapperMethod method) {
    String id = applyCurrentNamespace(method.getId());
    MappedStatement mappedStatement = configuration.hasStatement(id)
      ? configuration.getMappedStatement(id)
      : addMappedStatement(method);
    NestedMappedStatement nestedMappedStatement = new NestedMappedStatement(method, mappedStatement);
    configuration.addAfterMappedStatement(ms, nestedMappedStatement);
  }
  
  /**
   * 注册 MappedStatement
   *
   * @param method MapperMethod 信息
   * @return MappedStatement
   */
  public MappedStatement addMappedStatement(MapperMethod method) {
    Class<?> parameterType = method.getParameterType();
    Class<? extends LanguageDriver> languageDriverClass = method.getLanguageDriverClass();
    LanguageDriver languageDriver = configuration.getLanguageDriver(languageDriverClass);
  
    String script = SqlScriptUtils.script(method.getSql().toString());
    SqlSource sqlSource = languageDriver.createSqlSource(configuration, script, parameterType);
  
    return addMappedStatement(
      method.getId(),
      sqlSource,
      StatementType.PREPARED,
      method.getSqlCommandType(),
      method.getFetchSize(),
      method.getTimeout(),
      // ParameterMapID
      null,
      parameterType,
      method.getResultMap(),
      // ResultType
      method.getResultType(),
      configuration.getDefaultResultSetType(),
      method.isFlushCache(),
      method.isUseCache(),
      // TODO gcode issue #577
      false,
      method.getKeyGenerator(),
      method.getKeyProperty(),
      method.getKeyColumn(),
      // DatabaseID
      null,
      languageDriver,
      method.getResultSets()
    );
  }
}
