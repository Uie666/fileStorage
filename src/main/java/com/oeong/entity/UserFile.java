package com.oeong.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@Data  // @Data : 注解在类上, 为类提供读写属性（提供get/set方法）, 此外还提供了 equals()、hashCode()、toString() 方法
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true) // 链式访问
public class UserFile {
    private Integer id;
    private String fileName;
    private String ext;
    private String path;
    private long size;
    private String type;
    private Integer downloadCounts;
    private Date uploadTime;
    private Integer userId;
}
