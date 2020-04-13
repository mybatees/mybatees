package com.github.mybatees;

import com.github.mybatees.entity.*;
import com.github.mybatees.mapper.Mapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseTest {
  
  protected static Reader configurationReader;
  protected static SqlSessionFactory sqlSessionFactory;
  
  One one1 = new One(2001L, "one1");
  One one2 = new One(2002L, "one2");
  InverseOne inverseOne1 = new InverseOne(3001L, "inverseOne1");
  InverseOne inverseOne2 = new InverseOne(3002L, "inverseOne2");
  ForeignOne foreignOne1 = new ForeignOne(4001L, "foreignOne1");
  ForeignOne foreignOne2 = new ForeignOne(4002L, "foreignOne2");
  Many many1 = new Many(5001L, "many1");
  Many many2 = new Many(5002L, "many2");
  Many many3 = new Many(5003L, "many3");
  Many many4 = new Many(5004L, "many4");
  ForeignMany foreignMany1 = new ForeignMany(6001L, "foreignMany1");
  ForeignMany foreignMany2 = new ForeignMany(6002L, "foreignMany2");
  ForeignMany foreignMany3 = new ForeignMany(6003L, "foreignMany3");
  ForeignMany foreignMany4 = new ForeignMany(6004L, "foreignMany4");
  
  Main main1 = new Main(
    1001L, "main1", one1, inverseOne1, foreignOne1,
    Arrays.asList(many1, many2), Arrays.asList(foreignMany1, foreignMany2)
  );
  
  Main main2 = new Main(
    1002L, "main2", one2, inverseOne2, foreignOne2,
    Arrays.asList(many3, many4), Arrays.asList(foreignMany3, foreignMany4)
  );
  
  @BeforeAll
  static void setup() throws Exception {
    configurationReader = Resources.getResourceAsReader("com/github/mybatees/mybatis-config.xml");
  }
  
  @BeforeEach
  void init() throws SQLException, IOException {
    Configuration configuration = sqlSessionFactory.getConfiguration();
    
    // populate in-memory database
    DataSource dataSource = configuration.getEnvironment().getDataSource();
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setAutoCommit(true);
    runner.setStopOnError(false);
    runner.setLogWriter(null);
    runner.setErrorLogWriter(new PrintWriter(System.err));
    runner.runScript(Resources.getResourceAsReader("com/github/mybatees/db/" + getDbType() + "/CreateDB.sql"));
  }
  
  protected abstract String getDbType();
  
  @Test
  void testSave() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      
      Main main = new Main(
        "main", one1, inverseOne1, foreignOne1,
        Arrays.asList(many1, many2), Arrays.asList(foreignMany1, foreignMany2)
      );
      
      mapper.save(main);
      
      sqlSession.commit();
      
      Main saved = getMain(mapper, main.getMainId());
      
      assertNotNull(saved);
      assertEquals(main, saved);
      assertNotNull(saved.getCreateAt());
      assertNull(saved.getUpdateAt());
    }
  }
  
  @Test
  void testSaveAll() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      
      Main main1 = new Main(
        "main1", one1, inverseOne1, foreignOne1,
        Arrays.asList(many1, many2), Arrays.asList(foreignMany1, foreignMany2)
      );
      
      Main main2 = new Main(
        "main2", one2, inverseOne2, foreignOne2,
        Arrays.asList(many3, many4), Arrays.asList(foreignMany3, foreignMany4)
      );
      
      mapper.saveAll(Arrays.asList(main1, main2));
      
      sqlSession.commit();
      
      Main saved1 = getMain(mapper, main1.getMainId());
      
      assertNotNull(saved1);
      assertEquals(main1, saved1);
      assertNotNull(saved1.getCreateAt());
      assertNull(saved1.getUpdateAt());
      
      Main saved2 = getMain(mapper, main2.getMainId());
      assertEquals(main2, saved2);
      assertNotNull(saved2);
      assertNotNull(saved2.getCreateAt());
      assertNull(saved2.getUpdateAt());
    }
  }
  
  @Test
  void testFind() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      
      Optional<Main> optionalMain = mapper.find(main1.getMainId());
      
      assertTrue(optionalMain.isPresent());
      
      Main main = optionalMain.get();
      
      assertEquals(main, main1);
    }
  }
  
  @Test
  void testFindAll() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      
      List<Main> mainList = mapper.findAll(Arrays.asList(
        main1.getMainId(), main2.getMainId()
      ));
      
      assertEquals(2, mainList.size());
      assertEquals(main1, mainList.get(0));
      assertEquals(main2, mainList.get(1));
    }
  }
  
  @Test
  void testCount() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      
      assertEquals(2, mapper.count());
    }
  }
  
  @Test
  void testExists() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      
      assertTrue(mapper.exists(main1.getMainId()));
    }
  }
  
  @Test
  void testUpdate() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      
      main1.setMainName("main1_改");
      main1.setOne(one2);
      main1.setInverseOne(inverseOne2);
      main1.setForeignOne(foreignOne2);
      main1.setManyList(Arrays.asList(many3, many4));
      main1.setForeignManyList(Arrays.asList(foreignMany3, foreignMany4));
      
      mapper.update(main1);
      
      sqlSession.commit();
      
      assertEquals(main1, getMain(mapper, main1.getMainId()));
    }
  }
  
  @Test
  void testUpdateAll() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      
      main1.setMainName("main1_改");
      main1.setOne(one2);
      main1.setInverseOne(inverseOne2);
      main1.setForeignOne(foreignOne2);
      main1.setManyList(Arrays.asList(many3, many4));
      main1.setForeignManyList(Arrays.asList(foreignMany3, foreignMany4));
      
      main2.setMainName("main2_改");
      main2.setOne(one1);
      main2.setInverseOne(inverseOne1);
      main2.setForeignOne(foreignOne1);
      main2.setManyList(Arrays.asList(many1, many2));
      main2.setForeignManyList(Arrays.asList(foreignMany1, foreignMany2));
      
      mapper.updateAll(Arrays.asList(main1, main2));
      
      sqlSession.commit();
      
      assertEquals(main1, getMain(mapper, main1.getMainId()));
      assertEquals(main2, getMain(mapper, main2.getMainId()));
    }
  }
  
  @Test
  void testDelete() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      
      mapper.delete(main1.getMainId());
      
      sqlSession.commit();
      
      assertNull(getMain(mapper, main1.getMainId()));
    }
  }
  
  @Test
  void testDeleteAll() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      
      mapper.deleteAll(Arrays.asList(main1.getMainId(), main2.getMainId()));
      
      sqlSession.commit();
      
      assertNull(getMain(mapper, main1.getMainId()));
      assertNull(getMain(mapper, main2.getMainId()));
    }
  }
  
  private Main getMain(Mapper mapper, Long id) {
    Main saved = mapper.findByMainId(id);
    if (saved == null) {
      return null;
    }
    
    saved.setOne(mapper.findOneByMainId(id));
    saved.setInverseOne(mapper.findInverseOneByMainId(id));
    saved.setForeignOne(mapper.findForeignOneByMainId(id));
    saved.setManyList(mapper.findManyByMainId(id));
    saved.setForeignManyList(mapper.findForeignManyByMainId(id));
    return saved;
  }
}
