package com.oeong.service;

import com.oeong.entity.UserFile;

import java.util.List;

public interface UserFileService {
    List<UserFile> queryByUserId(Integer id, Integer page, int limit);
    UserFile queryByUserFileId(Integer id);
    void save(UserFile userFile);
    void update(UserFile userFile);
    void delete(Integer id);
    int queryFileCounts(Integer id);
}
