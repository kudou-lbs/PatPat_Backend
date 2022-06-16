package com.games.tap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.tap.domain.UserInfo;
import com.games.tap.service.UserService;
import com.games.tap.util.Echo;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Tag(name = "数据库操作测试")
@RequestMapping("/test")
@RestController
public class DbTestController {
    @Autowired
    private UserService userService;
    private ObjectMapper mapper = new ObjectMapper();

    @Operation(summary = "主页",description = "null")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String hello(){
        return "hello";
    }

    @Operation(summary = "获取所有用户信息" )
    @RequestMapping(value = "/UserInfo/getAll",method = RequestMethod.GET)
    @ResponseBody
    public List<UserInfo> getAllUserInfo(){
        return userService.getAllUserInfo();
    }


    @Operation(summary = "上传图片")
    @RequestMapping("/uploadImg")
    @ResponseBody
    //文件上传
    public String uploadImg(@RequestParam("fileName") MultipartFile file, @RequestParam("nickname") String nickName,HttpServletRequest request) throws IOException {

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
                    int row = saveImg(nickName, relativePath);
                    if(row > 0)
                        result = "图片上传成功";
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

    private int saveImg(String nickName, String path){
        //将图片信息存入数据库
        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(nickName);
        userInfo.setAvatar(path);
        return userService.insertPic(userInfo);
    }

    @Operation(summary = "获取图片路径")
    @RequestMapping("/getImgPath")
    @ResponseBody
    public String getImgPathByOwner(@RequestParam("nickname")String nickname){
        List<UserInfo> imgs= userService.getPicByName(nickname);
        HashMap<String, List> map = new HashMap<>();
        ArrayList<String> paths = new ArrayList<>();
        if(imgs!=null && !imgs.isEmpty()){
            for(UserInfo i:imgs){
                paths.add(i.getAvatar());
            }
        }
        map.put("paths", paths);

        String result;
        try {
            result = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }

        return result;
    }



}
