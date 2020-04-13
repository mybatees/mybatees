package com.github.mybatees.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Sql Script 工具类
 *
 * @author yuechao
 */
public class SqlScriptUtils implements StringPool {
  
  public static final String PARAM_COLLECTION = "collection";
  public static final String PARAM_LIST = "list";
  public static final String PARAM = "param";
  public static final String ITEM = "item";
  
  public static String safeParam(String param) {
    return HASH_LEFT_BRACE + param + RIGHT_BRACE;
  }
  
  public static String unsafeParam(String param) {
    return DOLLAR_LEFT_BRACE + param + RIGHT_BRACE;
  }
  
  public static String safeParam(int index) {
    return safeParam(PARAM + index);
  }
  
  public static String unsafeParam(int index) {
    return unsafeParam(PARAM + index);
  }
  
  public static String as(String column, String alias) {
    return column + " AS " + alias;
  }
  
  public static String simpleJoin(String tableAlias, String joinTable, String joinTableAlias, String column, String joinColumn) {
    return joinTable + " " + joinTableAlias
      + " ON " +
      joinTableAlias + "." + joinColumn
      + " = " +
      tableAlias + "." + column;
  }
  
  public static String eq(String column, String value) {
    return column + " = " + value;
  }
  
  public static String between(String column, String head, String tail) {
    return column + " BETWEEN " + head + " AND " + tail;
  }
  
  public static String lt(String column, String value) {
    return column + " < " + value;
  }
  
  public static String le(String column, String value) {
    return column + " <= " + value;
  }
  
  public static String gt(String column, String value) {
    return column + " > " + value;
  }
  
  public static String ge(String column, String value) {
    return column + " >= " + value;
  }
  
  public static String isNull(String column) {
    return column + " IS NULL";
  }
  
  public static String notNull(String column) {
    return column + " NOT NULL";
  }
  
  public static String like(String column, String value) {
    return column + " LIKE " + value;
  }
  
  public static String notLike(String column, String value) {
    return column + " NOT LIKE " + value;
  }
  
  public static String not(String column, String value) {
    return column + " <> " + value;
  }
  
  public static String in(String column, String value) {
    return column + " IN (" + value + ")";
  }
  
  public static String notIn(String column, String value) {
    return column + " NOT IN " + value;
  }
  
  public static String isTrue(String column) {
    return column + " = TRUE";
  }
  
  public static String isFalse(String column) {
    return column + " = FALSE";
  }
  
  public static String ignoreCase(String column, String value) {
    return "UPPER(" + column + ") = UPPER(" + value + ")";
  }
  
  public static String safeEq(String column, String property) {
    return eq(column, safeParam(property));
  }
  
  public static String set(String column, String param) {
    return eq(column, param);
  }
  
  public static String safeSet(String column, String property) {
    return safeEq(column, property);
  }
  
  public static String setNull(String column) {
    return column + " = NULL";
  }
  
  public static String test(String test, String content) {
    return "<if test=\"" + test + "\">\n" + content + "\n</if>";
  }
  
  public static String set(String content) {
    return "<set>\n" + content + "\n</set>";
  }
  
  public static String where(String content) {
    return "<where>\n" + content + "\n</where>";
  }
  
  public static TrimBuilder trim() {
    return new TrimBuilder();
  }
  
  public static ForeachBuilder foreach(String collection) {
    return new ForeachBuilder(collection);
  }
  
  public static String foreachItem(String collection) {
    return NamingUtils.scalar(collection, ITEM);
  }
  
  public static String script(String content) {
    return "<script>\n" + content + "\n</script>";
  }
  
  public abstract static class AbstractScriptBuilder<B> {
    
    private final Map<String, String> attrValues = new HashMap<>();
    
    protected abstract B self();
    protected abstract String tag();
    protected abstract String[] attrs();
    
    protected B addAttr(String attr, String attrValue) {
      attrValues.put(attr, attrValue);
      return self();
    }
    
    public String build(String content) {
      StringBuilder script = new StringBuilder();
      script.append("<").append(tag());
      for (String attr : attrs()) {
        String value = attrValues.get(attr);
        if (value != null) {
          script.append(" ").append(attr).append("=\"").append(value).append("\"");
        }
      }
      script.append(">");
      script.append(content);
      script.append("</").append(tag()).append(">");
      
      return script.toString();
    }
  }
  
  public static class TrimBuilder extends AbstractScriptBuilder<TrimBuilder> {
    private static final String TAG_NAME = "trim";
    private static final String ATTR_PREFIX = "prefix";
    private static final String ATTR_PREFIX_OVERRIDES = "prefixOverrides";
    private static final String ATTR_SUFFIX = "suffix";
    private static final String ATTR_SUFFIX_OVERRIDES = "suffixOverrides";
    
    @Override
    protected TrimBuilder self() {
      return this;
    }
    
    @Override
    protected String tag() {
      return TAG_NAME;
    }
    
    @Override
    protected String[] attrs() {
      return new String[]{
        ATTR_PREFIX, ATTR_PREFIX_OVERRIDES, ATTR_SUFFIX, ATTR_SUFFIX_OVERRIDES
      };
    }
    
    public TrimBuilder prefix(String open) {
      return addAttr(ATTR_PREFIX, open);
    }
    
    public TrimBuilder prefixOverrides(String item) {
      return addAttr(ATTR_PREFIX_OVERRIDES, item);
    }
    
    public TrimBuilder suffix(String separator) {
      return addAttr(ATTR_SUFFIX, separator);
    }
    
    public TrimBuilder suffixOverrides(String close) {
      return addAttr(ATTR_SUFFIX_OVERRIDES, close);
    }
  }
  
  public static class ForeachBuilder extends AbstractScriptBuilder<ForeachBuilder> {
    private static final String TAG_NAME = "foreach";
    private static final String ATTR_COLLECTION = "collection";
    private static final String ATTR_OPEN = "open";
    private static final String ATTR_ITEM = "item";
    private static final String ATTR_SEPARATOR = "separator";
    private static final String ATTR_CLOSE = "close";
    
    @Override
    protected ForeachBuilder self() {
      return this;
    }
    
    @Override
    protected String tag() {
      return TAG_NAME;
    }
    
    @Override
    protected String[] attrs() {
      return new String[]{
        ATTR_COLLECTION, ATTR_OPEN, ATTR_ITEM, ATTR_SEPARATOR, ATTR_CLOSE
      };
    }
    
    private ForeachBuilder(String collection) {
      addAttr(ATTR_COLLECTION, collection);
    }
    
    public ForeachBuilder open(String open) {
      return addAttr(ATTR_OPEN, open);
    }
    
    public ForeachBuilder item(String item) {
      return addAttr(ATTR_ITEM, item);
    }
    
    public ForeachBuilder separator(String separator) {
      return addAttr(ATTR_SEPARATOR, separator);
    }
    
    public ForeachBuilder close(String close) {
      return addAttr(ATTR_CLOSE, close);
    }
  }
}
