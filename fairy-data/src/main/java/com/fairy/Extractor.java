package com.fairy;

import java.util.List;

/**
 * 
 * @author seaboat
 * @date 2017-05-09
 * @version 1.0
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * <p>Extractor interface for all extractors.</p>
 */
public interface Extractor {

  public List<String> extract(String text);

}
