package com.oeong.dao;

import com.oeong.entity.UserFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserFileDao {
    List<UserFile> queryByUserId(Integer id, Integer begin, Integer offset);
    UserFile queryByUserFileId(Integer id);
    void save(UserFile userFile);
    void update(UserFile userFile);
    void delete(Integer id);
    int queryFileCounts(Integer id);
}
