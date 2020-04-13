package com.github.mybatees.entity;

import com.github.mybatees.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.LongTypeHandler;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(exclude = { "createAt", "updateAt" })
@Entity
public class Main {
  
  @Id
  @Column(typeHandler = LongTypeHandler.class)
  private Long mainId;
  
  private String mainName;
  
  @Transient
  private String ignore;
  
  @JoinColumn
  private One one;
  
  @JoinColumn(inverse = true)
  private InverseOne inverseOne;
  
  @JoinColumn
  @JoinTable
  private ForeignOne foreignOne;
  
  @JoinColumn
  private List<Many> manyList;
  
  @JoinColumn
  @JoinTable
  private List<ForeignMany> foreignManyList;
  
  @CreateAt
  private Date createAt;
  
  @UpdateAt
  private Date updateAt;
  
  public Main(Long id, String name, One one, InverseOne inverseOne, ForeignOne foreignOne, List<Many> manyList, List<ForeignMany> foreignManyList) {
    this.mainId = id;
    this.mainName = name;
    this.one = one;
    this.inverseOne = inverseOne;
    this.foreignOne = foreignOne;
    this.manyList = manyList;
    this.foreignManyList = foreignManyList;
  }
  
  public Main(String name, One one, InverseOne inverseOne, ForeignOne foreignOne, List<Many> manyList, List<ForeignMany> foreignManyList) {
    this.mainName = name;
    this.one = one;
    this.inverseOne = inverseOne;
    this.foreignOne = foreignOne;
    this.manyList = manyList;
    this.foreignManyList = foreignManyList;
  }
}
