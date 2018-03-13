package com.fairy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * .config文件util
 */
public class ConfigUtil {

    public static String getValue(String keyName, String configFileName){
        String configPath = ConfigUtil.class.getClassLoader().getResource(configFileName).getPath();
        Properties properties = new Properties();
        InputStream input = null;
        try {

            input = new FileInputStream(new File(configPath));//加载Java项目根路径下的配置文件
            properties.load(input);// 加载属性文件
            return properties.getProperty(keyName);
        } catch (IOException io) {
            io.printStackTrace();
            return null;
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
