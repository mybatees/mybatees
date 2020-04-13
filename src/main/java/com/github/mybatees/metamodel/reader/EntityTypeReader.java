package com.github.mybatees.metamodel.reader;

import com.github.mybatees.annotation.Entity;
import com.github.mybatees.annotation.Transient;
import com.github.mybatees.session.MybateesConfiguration;
import com.github.mybatees.metamodel.Attribute;
import com.github.mybatees.metamodel.EntityType;
import com.github.mybatees.util.NamingUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 实体信息解析器
 *
 * @author yuechao
 */
@Slf4j
public class EntityTypeReader {
  
  private final MybateesConfiguration configuration;
  private final Class<?> type;
  private final @Getter EntityType entity;
  private final List<AttributeReader> attributeReaders = new ArrayList<>();
  
  public EntityTypeReader(MybateesConfiguration configuration, Class<?> type) {
    this.configuration = configuration;
    this.type = type;
    this.entity = new EntityType(type);
  
    // 解析表名
    parseTable();
  
    // 解析属性
    parseAttributes();
  }
  
  /**
   * 解析实体信息
   */
  public void parse() {
    // 解析属性
    attributeReaders.forEach(AttributeReader::parse);
  }
  
  /**
   * 解析表名
   */
  private void parseTable() {
    String table = null;
  
    final Entity entityAnt = type.getAnnotation(Entity.class);
    if (entityAnt != null) {
      table = entityAnt.table();
    } else {
      log.warn("Managed type: {} should annotated with @Entity", type);
    }
    
    table = NamingUtils.validName(table,
      // 默认根据类名推断表名
      () -> NamingUtils.defaultTable(entity)
    );
    
    // 添加表名前缀
    String tablePrefix = configuration.getTablePrefix(type);
    table = NamingUtils.scalar(tablePrefix, table);
  
    entity.setTable(table);
  }
  
  /**
   * 预解析属性
   */
  private void parseAttributes() {
    final Field[] fields = type.getDeclaredFields();
    
    Stream.of(fields).forEach(this::parseAttribute);
    
    final Attribute id = attributeReaders.stream()
      .map(AttributeReader::getAttribute)
      .filter(Attribute::isId)
      .reduce((o1, o2) -> {
        throw new BindingException("Multiple Ids found in managed type: " + type);
      })
      .orElseThrow(
        () -> new IllegalArgumentException("No Id fount in managed type: " + type)
      );
  
    entity.setId(id);
  }
  
  /**
   * 预解析属性
   *
   * @param field 属性
   */
  private void parseAttribute(Field field) {
    if (!field.isAnnotationPresent(Transient.class)) {
      AttributeReader attributeReader = new AttributeReader(configuration, entity, field);
      entity.addAttribute(attributeReader.getAttribute());
      attributeReaders.add(attributeReader);
    }
  }
}
