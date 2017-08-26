package com.purplebeen.springblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/upload")
public class UploadRestController {
    @Autowired
    ServletContext servletContext;
    @RequestMapping(value="/image",  method= RequestMethod.POST)
    public String uploadImage(@RequestParam("multiFile") MultipartFile multipartFile, HttpSession httpSession, Model model) throws IOException {
        if(multipartFile != null & !(multipartFile.getOriginalFilename().toString().equals(""))) {
            String originalFilename = multipartFile.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")+1).toLowerCase();


            String webappRoot = servletContext.getRealPath("/");
            String relativeFolder = "resources" + File.separator
                    + "images" + File.separator;
            String path = webappRoot + relativeFolder;

            File file = new File(path);
            if(!file.exists()) {
                file.mkdirs();
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String today = formatter.format(new Date());
            String modifyName = today + "-" + originalFilename;
            String destinationFileName = path + modifyName;
            File destinationFile = new File(destinationFileName);

            multipartFile.transferTo(destinationFile);
            String imageurl = destinationFileName;
            return imageurl;
        } else {
            return "Error : 파일이 비어있습니다";
        }
    }
}
