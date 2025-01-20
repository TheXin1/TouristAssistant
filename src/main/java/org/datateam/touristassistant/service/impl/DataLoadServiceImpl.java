package org.datateam.touristassistant.service.impl;

import org.datateam.touristassistant.service.DataLoadService;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//加载数据到向量数据库中
@Service
public class DataLoadServiceImpl implements DataLoadService {
    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private DocumentTransformer documentTransformer;


    @Override
    public Object loadData(MultipartFile file){
        try {
            Resource resource = file.getResource();
            TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
            List<Document> fileDocuments = tikaDocumentReader.get();
            List<Document> documents = documentTransformer.apply(fileDocuments);
            vectorStore.accept(documents);
            return "上传成功";
        }catch (Exception e){
            return e.getCause();
        }
    }

}
