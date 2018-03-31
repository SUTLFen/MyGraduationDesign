package com.fairy.data.wordcloud;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.Grid;
import com.fairy.util.JSONUtil;
import com.fairy.utils.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 整理生成word cloud所需的关键词。
 * 指定时间，指定区域，关键词。
 */
public class WordCloudGenInSpecifiedGrid {

    private String dateStr = "2016-01-01";
    private String gridPath = "fairy-data/data/spatial/GridData.json";
    private String keyWordsPath = "fairy-data/data/weibo/0101/keywords0101_selected.json";
    private String ATensorPath = "fairy-data/data/preprocess/0101/ATensor0101.json";

    private FileUtil fileUtil = FileUtil.getInstance();

    /**
     * 将每个区域在seg_01的关键词存于各自的文件，以区域单位
     */
    public void gen() throws IOException {
        List<Grid> gridList = JSONUtil.toGrid(gridPath);
        List<List<Integer>> ATensorMatrix = JSONUtil.toATensor(ATensorPath);
        List<String> keyWordSelectedList = JSONUtil.toKeyWordList(keyWordsPath);

        int[] hours = {6, 7, 8, 9, 10};  //seg_01

        List<Integer> weightListInGrid = null;
        List<CloudWord> resultList = null;
        CloudWord cloudWord = null;

        for (int i = 0; i < ATensorMatrix.size(); i++) { //对于每个Grid
            weightListInGrid = ATensorMatrix.get(i);  //Tensor每一行
            resultList = new ArrayList<>();
            for (int j = 0; j < keyWordSelectedList.size(); j++) {
                int count = calCount(hours, j, weightListInGrid);
                cloudWord =new CloudWord(keyWordSelectedList.get(j), count);
                resultList.add(cloudWord);
            }

            //以区域为单位，存储关键词
            String jsonStr = JSON.toJSONString(resultList, true);
            fileUtil.saveToFile("fairy-data/data/wordcloud/0101_seg_01/"+i + ".json",
                    jsonStr, false);
        }
    }

    private int calCount(int[] hours, int j, List<Integer> weightListInGrid) {
        int count = 0;
        for (int i = 0; i < hours.length; i++) {
            count += weightListInGrid.get(hours[i] * 24 + j);
        }
        return count;
    }

    public static void main(String[] args) throws IOException {
        new WordCloudGenInSpecifiedGrid().gen();
    }

}
