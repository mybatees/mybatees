package com.github.mybatees.session;

import com.github.mybatees.builder.CommonMapperBuilder;
import com.github.mybatees.mapper.CommonMapper;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * Mybatees {@link SqlSessionFactoryBuilder}
 *
 * @author yuechao
 */
public class MybateesSqlSessionFactoryBuilder extends SqlSessionFactoryBuilder {
  
  @Override
  public SqlSessionFactory build(Configuration config) {
    
    // 扫描通用 Mapper
    CommonMapperBuilder builder = new CommonMapperBuilder(new MybateesConfiguration(config));
    for (Class<?> mapper : config.getMapperRegistry().getMappers()) {
      if (CommonMapper.class.isAssignableFrom(mapper)) {
        builder.parse(mapper);
      }
    }
    
    return super.build(config);
  }
}
