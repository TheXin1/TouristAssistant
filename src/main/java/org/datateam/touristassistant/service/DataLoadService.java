package org.datateam.touristassistant.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


public interface DataLoadService {

    Object loadData(MultipartFile file);
}
