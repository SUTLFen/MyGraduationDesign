function drawTimeView(){
    //时间视图
    d3.json("../data/hourly_flow.json", function(data){
        drawPolyline(data);
    });
}