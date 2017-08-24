package com.purplebeen.springblog.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @RequestMapping(value="/image", method= RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> uploadImage(@RequestParam("Filedata") MultipartFile multipartFile, HttpSession httpSession, HttpServletRequest request) throws IOException {

        HashMap<String, Object> fileInfo = new HashMap<String, Object>();
        if(multipartFile != null & !(multipartFile.getOriginalFilename().toString().equals(""))) {
            String originalFilename = multipartFile.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

            if(!(extension.equals("jpg")) || !(extension.equals("gif")) || !(extension.equals("bmp")) || !(extension.equals("png"))) {
                fileInfo.put("result", "-1");
                new ResponseEntity<Object>(fileInfo, HttpStatus.NOT_ACCEPTABLE);
            }

            String defaultPath = httpSession.getServletContext().getRealPath("/");
            String path = defaultPath + "upload/image/";

            File file = new File(path);
            if(!file.exists()) {
                file.mkdirs();
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String today = formatter.format(new Date());
            String modifyName = today + "-" + originalFilename;

            String imageurl = httpSession.getServletContext().getContextPath() + "/upload/image" + modifyName;

            fileInfo.put("imageurl", imageurl);
            fileInfo.put("filename", modifyName);
            fileInfo.put("fileextension", extension);
            fileInfo.put("result", 1);
            System.out.println("image url : " + imageurl);
        }
        return new ResponseEntity<>(fileInfo, HttpStatus.CREATED);

    }
}
