package com.github.mybatees.interceptor;

import com.github.mybatees.mapping.NestedMappedStatement;
import com.github.mybatees.session.MybateesConfiguration;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.*;

/**
 * 通用 MappedStatement Executor
 *
 * @author yuechao
 */
public class CommonExecutor implements Executor {
  
  private final MybateesConfiguration configuration;
  private final Executor delegate;
  
  public CommonExecutor(MybateesConfiguration configuration, Executor delegate) {
    this.configuration = configuration;
    this.delegate = delegate;
  }
  
  /**
   * 批量更新
   *
   * @param ms MappedStatement
   * @param parameters 参数
   * @return 更新数量
   * @throws SQLException SQLException
   */
  public int batchUpdate(MappedStatement ms, Collection<?> parameters) throws SQLException {
    BatchExecutor batchExecutor = new BatchExecutor(configuration, delegate.getTransaction());
    
    // 执行前置嵌套 MappedStatement
    updateNested(batchExecutor, configuration.getBeforeMappedStatements(ms), parameters);
    batchExecutor.flushStatements();
  
    for (Object parameter : parameters) {
      batchExecutor.update(ms, parameter);
    }
    List<BatchResult> batchResults = batchExecutor.flushStatements();
  
    // 执行后置嵌套 MappedStatement
    updateNested(batchExecutor, configuration.getAfterMappedStatements(ms), parameters);
    batchExecutor.flushStatements();
    
    return Arrays.stream(batchResults.get(0).getUpdateCounts()).sum();
  }
  
  @Override
  public int update(MappedStatement ms, Object parameter) throws SQLException {
    // 执行前置嵌套 MappedStatement
    updateNested(delegate, configuration.getBeforeMappedStatements(ms), Collections.singleton(parameter));
  
    int result = delegate.update(ms, parameter);
  
    // 执行后置嵌套 MappedStatement
    updateNested(delegate, configuration.getAfterMappedStatements(ms), Collections.singleton(parameter));
  
    return result;
  }
  
  /**
   * 执行嵌套 MappedStatement
   *
   * @param executor Executor
   * @param nestedMappedStatements NestedMappedStatementSet
   * @param parameters 参数
   * @throws SQLException SQLException
   */
  private void updateNested(Executor executor, Set<NestedMappedStatement> nestedMappedStatements, Collection<?> parameters) throws SQLException {
    for (NestedMappedStatement nestedMappedStatement : nestedMappedStatements) {
      for (Object parameter : parameters) {
        // 检验参数
        if (nestedMappedStatement.validateParameter(parameter)) {
          executor.update(nestedMappedStatement.getMappedStatement(), parameter);
        }
      }
    }
  }
  
  @Override
  public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) throws SQLException {
    return delegate.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
  }
  
  @Override
  public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
    return delegate.query(ms, parameter, rowBounds, resultHandler);
  }
  
  @Override
  public <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
    return delegate.queryCursor(ms, parameter, rowBounds);
  }
  
  @Override
  public List<BatchResult> flushStatements() throws SQLException {
    return delegate.flushStatements();
  }
  
  @Override
  public void commit(boolean required) throws SQLException {
    delegate.commit(required);
  }
  
  @Override
  public void rollback(boolean required) throws SQLException {
    delegate.commit(required);
  }
  
  @Override
  public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
    return delegate.createCacheKey(ms, parameterObject, rowBounds, boundSql);
  }
  
  @Override
  public boolean isCached(MappedStatement ms, CacheKey key) {
    return delegate.isCached(ms, key);
  }
  
  @Override
  public void clearLocalCache() {
    delegate.clearLocalCache();
  }
  
  @Override
  public void deferLoad(MappedStatement ms, MetaObject resultObject, String property, CacheKey key, Class<?> targetType) {
    delegate.deferLoad(ms, resultObject, property, key, targetType);
  }
  
  @Override
  public Transaction getTransaction() {
    return delegate.getTransaction();
  }
  
  @Override
  public void close(boolean forceRollback) {
    delegate.close(forceRollback);
  }
  
  @Override
  public boolean isClosed() {
    return delegate.isClosed();
  }
  
  @Override
  public void setExecutorWrapper(Executor executor) {
    delegate.setExecutorWrapper(executor);
  }
}
