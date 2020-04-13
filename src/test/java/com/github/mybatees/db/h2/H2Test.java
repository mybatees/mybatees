package com.github.mybatees.db.h2;

import com.github.mybatees.BaseTest;
import com.github.mybatees.session.MybateesSqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;

public class H2Test extends BaseTest {
  
  private static final String H2 = "h2";
  
  @BeforeAll
  static void setupH2() {
    sqlSessionFactory = new MybateesSqlSessionFactoryBuilder().build(configurationReader, H2);
  }
  
  @Override
  protected String getDbType() {
    return H2;
  }
}
