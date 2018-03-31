package com.fairy.data.weibo;

import com.fairy.pojo.WeiboFields;
import com.fairy.util.LuceneUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.File;
import java.io.IOException;

/**
 * 处理微博content字段，将其中的url和表情去掉。
 */
@Deprecated
public class UpdateWeiboIndexCore {


    private String indexParentPath = "D:\\Users\\liuf\\03_Data\\weibo\\weibo_index";

    public void updateIndex() throws IOException {

        File rawFile = new File(indexParentPath);
        File[] files = rawFile.listFiles();

        File indexFile = null;
        for (int i = 0; i < files.length; i++) {
            indexFile = files[i];
//            IndexWriter indexWriter = LuceneUtil.getAnalyzerIndexWriter(indexFile.getPath());
            //处理每个文件
            updateSingleFile(indexFile);
        }

        System.out.println("update end !!!");
    }

    private void updateSingleFile(File indexFile) throws IOException {
        System.out.println(indexFile.getPath());
        IndexReader indexReader = LuceneUtil.getIndexReader(indexFile.getPath());
        IndexWriter indexWriter = LuceneUtil.getAnalyzerIndexWriter(indexFile.getPath());

        int maxDoc = indexReader.maxDoc();

        Document doc = null;
        String contentStr = null;

        String regexStr = "(http?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        for (int docId = 0; docId < maxDoc; docId++) {
            doc = indexReader.document(docId);
            contentStr = doc.getField(WeiboFields.content).stringValue();

            contentStr = contentStr.replaceAll(regexStr + "|" + "\\[.*]", "");
            contentStr = contentStr.trim();

            System.out.println(contentStr);

            Term updateTerm = new Term("Content", contentStr);
            indexWriter.updateDocument(updateTerm, doc);
        }

    }

    public static void main(String[] args) throws IOException {
        new UpdateWeiboIndexCore().updateIndex();
    }
}
