package org.datateam.touristassistant.controller;

import org.datateam.touristassistant.service.DataLoadService;
import org.datateam.touristassistant.service.impl.DataLoadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/file")
@RestController
public class FileController {

    @Autowired
    private DataLoadServiceImpl dataLoadService;

    @PostMapping("/uploadDatabase")
    public Object uploadFile(MultipartFile file){
            return dataLoadService.loadData(file);
    }
}
