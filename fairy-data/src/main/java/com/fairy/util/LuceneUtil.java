package com.fairy.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LuceneUtil {
    private static LuceneUtil instance = null;

    public static  LuceneUtil getInstance(){
        if(instance == null){
            instance = new LuceneUtil();
        }
        return instance;
    }

    public IndexWriter getAnalyzerIndexWriter(String parentPath, String file ) throws IOException {
        Path path = Paths.get(parentPath, file);
        File fileTemp = path.toFile();
        if(!fileTemp.exists()) fileTemp.mkdir();

        Directory directory = FSDirectory.open(Paths.get(fileTemp.getAbsolutePath()));
        Analyzer ikAnalyzer =  new IKAnalyzer(true);
        IndexWriterConfig iwc = new IndexWriterConfig(ikAnalyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        iwc.setRAMBufferSizeMB(100);
        IndexWriter iwriter = new IndexWriter(directory, iwc);
        return iwriter;
    }

    public static IndexReader getIndexReader(String indexPath) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(indexPath));
        IndexReader indexReader = DirectoryReader.open(directory);
        return indexReader;
    }
    public static IndexSearcher getIndexSearcher(String indexPath) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(indexPath));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(indexReader);
        return isearcher;
    }

}
