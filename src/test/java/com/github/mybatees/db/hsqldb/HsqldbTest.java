package com.github.mybatees.db.hsqldb;

import com.github.mybatees.BaseTest;
import com.github.mybatees.session.MybateesSqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;

public class HsqldbTest extends BaseTest {
  
  private static final String HSQLDB = "hsqldb";
  
  @BeforeAll
  static void setupHsqldb() {
    sqlSessionFactory = new MybateesSqlSessionFactoryBuilder().build(configurationReader, HSQLDB);
  }
  
  @Override
  protected String getDbType() {
    return HSQLDB;
  }
}
