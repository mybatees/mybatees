package com.github.mybatees.mapper;

import com.github.mybatees.entity.*;
import com.github.mybatees.mapper.DefaultMapper;

import java.util.List;

public interface Mapper extends DefaultMapper<Main, Long> {
  
  Main findByMainId(Long id);
  
  One findOneByMainId(Long id);
  
  InverseOne findInverseOneByMainId(Long id);
  
  ForeignOne findForeignOneByMainId(Long id);
  
  List<Many> findManyByMainId(Long id);
  
  List<ForeignMany> findForeignManyByMainId(Long id);
}
