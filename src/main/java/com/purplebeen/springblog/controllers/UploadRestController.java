package com.purplebeen.springblog.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/upload")
public class UploadRestController {

    @RequestMapping(value="/image",  method= RequestMethod.POST)
    public String uploadImage(@RequestParam("multiFile") MultipartFile multipartFile, HttpSession httpSession, Model model) throws IOException {
        if(multipartFile != null & !(multipartFile.getOriginalFilename().toString().equals(""))) {
            String originalFilename = multipartFile.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")+1).toLowerCase();

//            if(!(extension.equals("jpg")) || !(extension.equals("gif")) || !(extension.equals("bmp")) || !(extension.equals("png"))) {
//                return "Error : file 확장자가 올바르지 않습니다";
//            }

            String defaultPath = httpSession.getServletContext().getRealPath("/");
            String path = defaultPath + "upload/image/";

            File destinationFile;
            String destinationFileName;
            do {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                String today = formatter.format(new Date());
                destinationFileName = today + "-" + originalFilename;
                destinationFile = new File(path + destinationFileName);
            } while (destinationFile.exists());

            destinationFile.getParentFile().mkdirs();
            multipartFile.transferTo(destinationFile);

            String imageurl = "/upload/image/" + destinationFileName;
            return imageurl;
        } else {
            return "Error : 파일이 비어있습니다";
        }
    }
}
