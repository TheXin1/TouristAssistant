package org.datateam.touristassistant.ai;



import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class VectorStoreTest {
    @Autowired
    private DocumentTransformer tokenTextSplitter;
    @Autowired
    private VectorStore vectorStore;
    @Value("classpath:CV.docx")
    private Resource resource;
    @Test
    public void test1() {
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
        // 将文件中的文本分割为多组Document
        List<Document> fileDocuments = tikaDocumentReader.get();
        // 基于Token将多组Document进行更细化的分割
        List<Document> documents = tokenTextSplitter.apply(fileDocuments);
        // 存储到向量数据库中
        vectorStore.accept(documents);
    }

    @Test
    public void similaritySearch() {
        List<Document> documents = vectorStore.similaritySearch("问题一： TV损失有什么作用？为什么需要它？");
        // 获取每个Document里的content
        List<String> collect = documents.stream().map(Document::getContent).toList();
        collect.forEach(e->{
            System.out.println("-------");
            System.out.println(e);
        });
    }
}
