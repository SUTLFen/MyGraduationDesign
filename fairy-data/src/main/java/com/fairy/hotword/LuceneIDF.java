package com.fairy.hotword;

import com.fairy.IDF;
import com.fairy.util.IndexUtil;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

import java.io.IOException;

/**
 * 
 * @author seaboat
 * @date 2017-05-10
 * @version 1.0
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * <p>inverse document frequency by lucene.</p>
 */
public class LuceneIDF implements IDF {

  @Override
  public float getIDF(String term) {
    try {
      IndexReader reader = IndexUtil.getIndexReader();
      float a = reader.numDocs() + 1;
      float b = reader.docFreq(new Term("content", term)) + 1;
      float idf = (float) Math.log(a / b);
      return idf;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return 0;
  }

}
