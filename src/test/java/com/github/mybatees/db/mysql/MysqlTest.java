package com.github.mybatees.db.mysql;

import com.github.mybatees.BaseTest;
import com.github.mybatees.session.MybateesSqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;

public class MysqlTest extends BaseTest {
  
  private static final String MYSQL = "mysql";
  
  @BeforeAll
  static void setupMysql() {
    sqlSessionFactory = new MybateesSqlSessionFactoryBuilder().build(configurationReader, MYSQL);
  }
  
  @Override
  protected String getDbType() {
    return MYSQL;
  }
}
