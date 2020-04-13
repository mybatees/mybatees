package com.github.mybatees.interceptor;

import com.github.mybatees.mapping.CommonMappedStatement;
import com.github.mybatees.session.MybateesConfiguration;
import com.github.mybatees.util.SqlScriptUtils;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

/**
 * Mybatees 拦截器
 *
 * @author yuechao
 */
@Intercepts({
  @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
})
@RequiredArgsConstructor
public class MybateesInterceptor implements Interceptor {
  
  private final MybateesConfiguration configuration;
  
  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    Executor executor = (Executor) invocation.getTarget();
    Method method = invocation.getMethod();
    Object[] args = invocation.getArgs();
    
    MappedStatement ms = (MappedStatement) args[0];
    Object parameter = args[1];
    
    // 拦截通用 MappedStatement
    if (configuration.isCommonMappedStatement(ms)) {
      CommonMappedStatement msWrapper = configuration.getCommonMappedStatement(ms);
      CommonExecutor commonExecutor = new CommonExecutor(configuration, executor);
      
      if (msWrapper.isBatch()) {
        // 批量执行
        // SaveAll, UpdateAll DeleteAll
        DefaultSqlSession.StrictMap<?> parameterMap = (DefaultSqlSession.StrictMap<?>) parameter;
        Collection<?> parameters = (Collection<?>) parameterMap.get(SqlScriptUtils.PARAM_COLLECTION);
        msWrapper.populateParameters(parameters);
        return commonExecutor.batchUpdate(ms, parameters);
      }
  
      msWrapper.populateParameters(Collections.singleton(parameter));
      return commonExecutor.update(ms, parameter);
    }
    
    return invocation.proceed();
  }
}
