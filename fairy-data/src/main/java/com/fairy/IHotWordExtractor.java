package com.fairy;

import com.fairy.hotword.Result;

import java.util.List;


/**
 * 
 * @author seaboat
 * @date 2017-09-19
 * @version 1.0
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * <p>a implement for hotword extraction.</p>
 */
public interface IHotWordExtractor extends Extractor {

  public List<Result> extract(int id, int topN, boolean useScore);

  public List<Result> extract(int id, int topN);

  public List<Result> extract(int id);

}
