package com.fairy.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

public class LuceneUtil {
    private static LuceneUtil instance = null;
    public static  LuceneUtil getInstance(){
        if(instance == null){
            instance = new LuceneUtil();
        }
        return instance;
    }

    public IndexWriter getAnalyzerIndexWriter(String path) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(path));
        Analyzer ikAnalyzer =  new IKAnalyzer(true);
        IndexWriterConfig config = new IndexWriterConfig(ikAnalyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);
        return iwriter;
    }

    public static IndexReader getIndexReader(String indexPath) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(indexPath));
        IndexReader indexReader = DirectoryReader.open(directory);
        return indexReader;
    }
}
