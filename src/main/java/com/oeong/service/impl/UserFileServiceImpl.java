package com.oeong.service.impl;

import com.oeong.dao.UserFileDao;
import com.oeong.entity.UserFile;
import com.oeong.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserFileServiceImpl implements UserFileService {

    @Autowired
    private UserFileDao userFileDao;

    @Override
    public List<UserFile> queryByUserId(Integer id, Integer page, int limit) {
        int begin = (page - 1) * limit;
        int offset = limit;
        return userFileDao.queryByUserId(id, begin, offset);
    }

    @Override
    public UserFile queryByUserFileId(Integer id) {
        return userFileDao.queryByUserFileId(id);
    }

    @Override
    public void save(UserFile userFile) {
        userFile.setDownloadCounts(0).setUploadTime(new Date());
        userFileDao.save(userFile);
    }

    @Override
    public void update(UserFile userFile) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public int queryFileCounts(Integer id) {
        return userFileDao.queryFileCounts(id);
    }
}
