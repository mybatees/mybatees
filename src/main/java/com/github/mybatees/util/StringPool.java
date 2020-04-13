package com.github.mybatees.util;

/**
 * 常用字符串常量池
 *
 * @author yuechao
 */
public interface StringPool {
  
  String EMPTY = "";
  String SPACE = " ";
  String DOT = ".";
  String UNDERSCORE = "_";
  String COMMA = ",";
  String DOLLAR = "$";
  String HASH = "#";
  String LEFT_BRACKET = "(";
  String RIGHT_BRACKET = ")";
  String LEFT_BRACE = "{";
  String RIGHT_BRACE = "}";
  
  String COMMA_SPACE = COMMA + SPACE;
  String DOLLAR_LEFT_BRACE = DOLLAR + LEFT_BRACE;
  String HASH_LEFT_BRACE = HASH + LEFT_BRACE;
}
