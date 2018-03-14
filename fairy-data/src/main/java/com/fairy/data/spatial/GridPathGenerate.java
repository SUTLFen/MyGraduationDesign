package com.fairy.data.spatial;

import com.fairy.pojo.Grid;
import com.fairy.util.JSONUtil;

import java.io.IOException;
import java.util.List;

public class GridPathGenerate {

    private String gridPath = "GridData.json";
    private String gridPathPath = "GridPathData.json";

    public void generate() throws IOException {

        List<Grid> gridList = JSONUtil.toGrid(gridPath);

        Grid gridCur = null;
        String pathStr = null;

    }

    public static void main(String[] args) throws IOException {
        new GridPathGenerate().generate();
    }

}
