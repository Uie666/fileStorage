package com.oeong.controller;

import com.oeong.entity.User;
import com.oeong.entity.UserFile;
import com.oeong.service.UserFileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private UserFileService userFileService;

    @GetMapping("/index")
    public String fileIndex() {
        return "file/list";
    }

    @ResponseBody
    @PostMapping("/all")
    public Map<String, Object> queryAllFile(HttpSession session, HttpServletRequest request) {
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        User user = (User) session.getAttribute("user");
        List<UserFile> files = userFileService.queryByUserId(user.getId(), page, limit);

        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("count", userFileService.queryFileCounts(user.getId()));
        res.put("data", files);
        return res;
    }

    // 上传文件
    @PostMapping("/upload")
    @ResponseBody // 将Java对象转为Json格式的数据
    public Map<String, String> upload(@RequestParam("file") MultipartFile file, HttpSession session) {
        System.out.println("upload trigger");
        Map<String, String> res = new HashMap<>();
        try {
            // 获取用户
            User user = (User) session.getAttribute("user");
            // 获取文件原始名
            String fileName = file.getOriginalFilename();
            // 获取文件后缀
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            // 获取文件大小
            long size = file.getSize();
            // 获取文件类型
            String type = file.getContentType();
            // 根据日期动态生成目录
            String localContainer = "/fileContainer";
            String uploadPath = ResourceUtils.getURL("classpath:").getPath() + localContainer;
            String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            File dateDirPath = new File(uploadPath + File.separator + dateFormat);
            if (!dateDirPath.exists()) {
                dateDirPath.mkdirs();
            }
            System.out.println(dateDirPath);
            // 处理文件上传
            file.transferTo(new File(dateDirPath, fileName));
            // 将文件信息存入数据库中
            UserFile userFile = new UserFile();
            userFile.setFileName(fileName)
                    .setExt("." + extension)
                    .setPath(Paths.get(localContainer, dateFormat, fileName).toString())
                    .setSize(size)
                    .setType(type)
                    .setUserId(user.getId());
            userFileService.save(userFile);
            res.put("code", "0");
            res.put("msg", "上传成功");
            res.put("url", "/fileStorage/file/index");
        } catch (IOException e) {
            e.printStackTrace();
            res.put("code", "-1");
            res.put("msg", "上传失败");
            res.put("url", "/fileStorage/file/index");
        }
        return res;
    }

    /**
     * @param id       : 文件id
     * @param response :
     * @Author: Hongchenglong
     * @Date: 2021/6/23 14:01
     * @Decription: 文件下载
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable("id") Integer id, HttpServletResponse response) {
        String openStyle = "attachment";
        System.out.println("trigger download");
        getFile(openStyle, id, response);
    }

    /**
     * @param id:
     * @param response:
     * @Author: Hongchenglong
     * @Date: 2021/6/23 14:05
     * @Decription: 文件预览
     */
    @GetMapping("/preview/{id}")
    public void preview(@PathVariable("id") Integer id, HttpServletResponse response) {
        String openStyle = "inline";
        System.out.println("trigger download");
        getFile(openStyle, id, response);
    }

    public void getFile(String openStyle, Integer id, HttpServletResponse response) {
        UserFile file = userFileService.queryByUserFileId(id);
        System.out.println("==============getFile=================" + file);
        try {
            // 文件路径
            System.out.println("realPath: ");
            String realPath = ResourceUtils.getURL("classpath:").getPath() + file.getPath();
            System.out.println(realPath);
            // 文件输入流
            FileInputStream is = new FileInputStream(new File(realPath));
            // 附件下载
            response.setHeader("content-disposition", openStyle + "; filename = " + URLEncoder.encode(file.getFileName(), "UTF-8"));
            // 响应输出流
            ServletOutputStream os = response.getOutputStream();
            // 文件拷贝
            IOUtils.copy(is, os);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 更新下载次数
        if (openStyle.equals("attachment")) {
            file.setDownloadCounts(file.getDownloadCounts() + 1);
            userFileService.update(file);
        }
    }

    @GetMapping("/delete/{id}")
    @ResponseBody // ！！！忘记加@ResponseBody注解，返回的页面模板，报错template might not exist
    public Map<String, Object> delete(@PathVariable("id") Integer id) {
        Map<String, Object> map = new HashMap<>();
        UserFile userFile = userFileService.queryByUserFileId(id);
        try {
            String realPath = ResourceUtils.getURL("classpath:").getPath() + userFile.getPath();
            System.out.println("realPath: " + realPath);
            File file = new File(realPath);
            if (file.exists()) {
                file.delete();
            }
            userFileService.delete(id);
            map.put("code", 0);
            map.put("msg", "删除成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            map.put("code", -1);
            map.put("msg", "删除失败");
        }
        return map;
    }
}
