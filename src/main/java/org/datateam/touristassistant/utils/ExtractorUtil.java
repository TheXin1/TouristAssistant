package org.datateam.touristassistant.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

//文件解析类
public class ExtractorUtil {

    //解析PDF文件
    public static String PDFToString(String filePath) throws IOException {
        PDDocument document = PDDocument.load(new File(filePath));
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        return text;
    }

    //解析word文件
    public static String WordToString(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        XWPFDocument document = new XWPFDocument(fis);
        StringBuilder text = new StringBuilder();

        document.getParagraphs().forEach(paragraph -> text.append(paragraph.getText()).append("\n"));
        fis.close();

        return text.toString();
    }

    //解析Txt文件
    public static String TxtToString(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder text = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            text.append(line).append("\n");
        }
        reader.close();
        return text.toString();
    }




}
