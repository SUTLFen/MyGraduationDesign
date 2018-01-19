package com.fairy.data.preprocess;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.KK;
import com.fairy.pojo.PrCar;
import com.fairy.pojo.PrCarOld;
import com.fairy.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 预处理卡口车辆数据
 */
public class PrCarPreprocessor {

    private String prCarOldPath = "fairy-data/data/01_PrCar";
    private String prCarPath = "fairy-data/data/01_PrCarNew";

    private String kkOldPath = "fairy-data/data/kk_info.json";

    private FileUtil fileUtil = FileUtil.getInstance();


    public void addLatlngToPrCar() throws IOException, ParseException {
        List<KK> kkList = toKKList(kkOldPath);
        List<PrCarOld> prCarOldList = toPrCarOldList(prCarOldPath);

        List<PrCar> prCarList = new ArrayList<>();

        for (PrCarOld prCarOldTmp : prCarOldList) {
            PrCar prCar = null;
            for (KK kkTmp : kkList) {
                if (prCarOldTmp.getKkid().equals(kkTmp.getKkId())){
                    prCar = new PrCar(prCarOldTmp, kkTmp.getLat(), kkTmp.getLng());
                    break;
                }
            }

            if(prCar != null){
                prCarList.add(prCar);
            }
        }

        String jsonStr = JSON.toJSONString(prCarList, true);
        fileUtil.saveToFile(prCarPath, jsonStr, false);

    }

    private List<PrCarOld> toPrCarOldList(String path) throws IOException {
        String str = fileUtil.readJsonFileToStr(new File(path));
        List<PrCarOld> list = JSON.parseArray(str, PrCarOld.class);
        return list;
    }


    private List<KK> toKKList(String kkOldPath) throws IOException {
        String str = fileUtil.readJsonFileToStr(new File(kkOldPath));
        List<KK> list = JSON.parseArray(str, KK.class);
        return list;
    }

    public static void main(String[] args) throws IOException, ParseException {
        new PrCarPreprocessor().addLatlngToPrCar();
    }

}
