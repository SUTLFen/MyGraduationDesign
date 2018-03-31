$(function(){

    initMapWithGaode();

    //四叉树，分割地图
    drawQuadTreeMap();

    //绘制时序图   参数：.csv文件
    drawTimeView();

    //绘制雷达视图
    d3.json("../data/GridVectorNormal.json", function(data){
        drawRadarView(data);
    });

});