package com.github.mybatees.entity;

import com.github.mybatees.annotation.Column;
import com.github.mybatees.annotation.Entity;
import com.github.mybatees.annotation.Id;
import lombok.*;
import org.apache.ibatis.type.LongTypeHandler;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ForeignOne {
  
  @NonNull
  @Id
  @Column(typeHandler = LongTypeHandler.class)
  private Long foreignOneId;
  
  private String foreignOneName;
}
