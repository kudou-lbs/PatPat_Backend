package com.games.tap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.tap.domain.Game;
import com.games.tap.domain.UserInfo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class UploadImg {

    private UserService userService;
    private GameService gameService;
    private ObjectMapper mapper = new ObjectMapper();

    public UploadImg(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public String uploadAvatar(MultipartFile file) throws IOException {
        String result;//上传结果信息
        Map<String,Object> map=new HashMap<>();
        if (file.getSize() / 1000 > 100){
            result="图片大小不能超过100KB";
        }
        else{
            //判断上传文件格式
            String fileType = file.getContentType();
            if (Objects.equals(fileType, "image/png") || Objects.equals(fileType, "image/jpeg")) {
                //获取文件名
                String fileName = file.getOriginalFilename();
                //获取文件后缀名
                assert fileName != null;
                int index = fileName.lastIndexOf(".");
                String suffixName;
                if(index > 0) {
                    suffixName = fileName.substring(fileName.lastIndexOf("."));
                }else{
                    suffixName = ".png";
                }
                //重新生成文件名
                fileName = UUID.randomUUID()+suffixName;
                //获取项目路径
                Resource resource = new ClassPathResource("");
                String projectPath = resource.getFile().getAbsolutePath()+ "\\static\\img";
                System.out.println(projectPath);
                if (upload(projectPath, file, fileName)) {
                    //文件存放的相对路径(一般存放在数据库用于img标签的src)
                    String relativePath="img/"+fileName;
                    int row = saveAvatar(relativePath);
                    if(row > 0)
                        result = "图片上传成功,图片路径为："+relativePath;
                    else
                        result = "图片上传数据库失败";
                }
                else{
                    result="图片上传失败";
                }
            }
            else{
                result="图片格式不正确";
            }
        }
        //结果用json形式返回
        map.put("result",result);
        String resultJson;
        try {
            resultJson = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
        System.out.println(resultJson);
        return resultJson;
    }

    public String uploadBack(MultipartFile file) throws IOException {
        String result;//上传结果信息
        Map<String,Object> map=new HashMap<>();
        if (file.getSize() / 1000 > 100){
            result="图片大小不能超过100KB";
        }
        else{
            //判断上传文件格式
            String fileType = file.getContentType();
            if (Objects.equals(fileType, "image/png") || Objects.equals(fileType, "image/jpeg")) {
                //获取文件名
                String fileName = file.getOriginalFilename();
                //获取文件后缀名
                assert fileName != null;
                int index = fileName.lastIndexOf(".");
                String suffixName;
                if(index > 0) {
                    suffixName = fileName.substring(fileName.lastIndexOf("."));
                }else{
                    suffixName = ".png";
                }
                //重新生成文件名
                fileName = UUID.randomUUID()+suffixName;
                //获取项目路径
                Resource resource = new ClassPathResource("");
                String projectPath = resource.getFile().getAbsolutePath()+ "\\static\\img";
                System.out.println(projectPath);
                if (upload(projectPath, file, fileName)) {
                    //文件存放的相对路径(一般存放在数据库用于img标签的src)
                    String relativePath="img/"+fileName;
                    int row = saveBack(relativePath);
                    if(row > 0)
                        result = "图片上传成功,图片路径为："+relativePath;
                    else
                        result = "图片上传数据库失败";
                }
                else{
                    result="图片上传失败";
                }
            }
            else{
                result="图片格式不正确";
            }
        }
        //结果用json形式返回
        map.put("result",result);
        String resultJson;
        try {
            resultJson = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
        System.out.println(resultJson);
        return resultJson;
    }

    public String uploadGamePic(MultipartFile file) throws IOException {
        String result;//上传结果信息
        Map<String,Object> map=new HashMap<>();
        if (file.getSize() / 1000 > 100){
            result="图片大小不能超过100KB";
        }
        else{
            //判断上传文件格式
            String fileType = file.getContentType();
            if (Objects.equals(fileType, "image/png") || Objects.equals(fileType, "image/jpeg")) {
                //获取文件名
                String fileName = file.getOriginalFilename();
                //获取文件后缀名
                assert fileName != null;
                int index = fileName.lastIndexOf(".");
                String suffixName;
                if(index > 0) {
                    suffixName = fileName.substring(fileName.lastIndexOf("."));
                }else{
                    suffixName = ".png";
                }
                //重新生成文件名
                fileName = UUID.randomUUID()+suffixName;
                //获取项目路径
                Resource resource = new ClassPathResource("");
                String projectPath = resource.getFile().getAbsolutePath()+ "\\static\\img";
                System.out.println(projectPath);
                if (upload(projectPath, file, fileName)) {
                    //文件存放的相对路径(一般存放在数据库用于img标签的src)
                    String relativePath="img/"+fileName;
                    int row = saveGamePic(relativePath);
                    if(row > 0)
                        result = "图片上传成功,图片路径为："+relativePath;
                    else
                        result = "图片上传数据库失败";
                }
                else{
                    result="图片上传失败";
                }
            }
            else{
                result="图片格式不正确";
            }
        }
        //结果用json形式返回
        map.put("result",result);
        String resultJson;
        try {
            resultJson = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
        System.out.println(resultJson);
        return resultJson;
    }

    private boolean upload(String realPath, MultipartFile file, String fileName){
        // 将img文件存入本地
        String path = realPath + "\\" + fileName;
        System.out.println(path);
        File dest = new File(path);
        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            boolean b = dest.getParentFile().mkdir();
            if(!b){
                return b;
            }
        }
        //保存文件
        try {
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int saveAvatar(String path){
        //将图片信息存入数据库
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar(path);
        return userService.insertAvatar(userInfo);
    }

    private int saveBack(String path){
        //将图片信息存入数据库
        UserInfo userInfo = new UserInfo();
        userInfo.setBackground(path);
        return userService.insertBackground(userInfo);
    }

    private int saveGamePic(String path){
        //将图片信息存入数据库
        Game game=new Game();
        game.setPicture(path);
        return gameService.insertPic(game);
    }
}
