package com.games.tap.service;

import org.springframework.beans.factory.annotation.Value;
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
public class ImageService {

    @Value("${web.upload-path}")
    private String uploadPath;

    public Map<String,String> uploadImage(MultipartFile file){
        String result= "";//上传结果信息
        Map<String,String> map=new HashMap<>();
        if (file.getSize() > 1024*1024*2){
            result="图片大小不能超过2MB";
        }
        else{
            //判断上传文件格式
            String fileType = file.getContentType();
            if (Objects.equals(fileType, "image/png") || Objects.equals(fileType, "image/jpeg")||Objects.equals(fileType, "image/jpg")) {
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
                String projectPath = uploadPath+"/image";
                System.out.println(projectPath);
                if (upload(projectPath, file, fileName)) {
                    //文件存放的相对路径(一般存放在数据库用于img标签的src)
                    String relativePath="/image/"+fileName;
                    map.put("path",relativePath);
                } else{
                    result="图片上传失败";
                }
            } else{
                result="图片格式不正确";
            }
        }
        //结果用json形式返回
        map.put("result",result);
        return map;
    }

    private boolean upload(String realPath, MultipartFile file, String fileName){
        // 将img文件存入本地
        String path = realPath + "/" + fileName;
        System.out.println(path);
        File dest = new File(path);
        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            boolean b = dest.getParentFile().mkdir();
            if(!b)return false;
        }
        //保存文件
        try {
            file.transferTo(dest);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
//
//    private int saveGamePic(String path){
//        //将图片信息存入数据库
//        Game game=new Game();
//        game.setPicture(path);
//        return gameService.insertPic(game);
//    }


    /**
     * 删除图片
     */
    public boolean deleteFiles(String pathName){

        boolean flag = false;
        //根据路径创建文件对象
        File file = new File(uploadPath+pathName);
        //路径是个文件且不为空时删除文件
        if(file.isFile()&&file.exists()){
            flag = file.delete();
        }
        //删除失败时，返回false
        return flag;
    }
}
