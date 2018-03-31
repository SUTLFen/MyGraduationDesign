/**
 * Created by Fairy_LFen on 2017/4/26.
 */

/**
* 将数据转为RadarView适合的数据。
* */
var DimVector = 20;

var initArray = function (n) {
    var arry = [];
    for(var i = 0; i <n; i ++){
        arry[i] = 0;
    }
    return arry;
};
var calVectorArevageVal = function (datap) {
    var i , j , argVector = initArray(DimVector);
    for(i = 0; i < datap.length; i++){
       var vector = datap[i].vector;
        for(j = 0; j < vector.length; j++){
            argVector[j] += vector[j];
        }
    }
    for(var k = 0; k < argVector.length; k++){
        var tmp = new Number(argVector[k]/datap.length) ;
        argVector[k] =parseFloat(tmp.toFixed(2));
    }
    return argVector;
};
var formatData = function(data, titles){
    var i, j, radarData = [];

    for(i = 0 ; i < data.length; i++){
        var radarDataItem = [];
        // var arg_vector = calVectorArevageVal(data[i]);
        var arg_vector = data[i];

        for(var j = 0; j < arg_vector.length; j++){
            var element = new Object();
            element.axis = titles[j];
            element.value = arg_vector[j];
            radarDataItem.push(element);
        }
        radarData.push(radarDataItem);

    }
    return radarData;
}

var initTitleArray = function () {
    var titles = ["餐饮", "购物", "生活", "体育休闲",
        "医疗保健", "住宿", "风景名胜", "商务住宅", "政府机构",
        "科教文化", "交通设施", "金融保险", "公司企业"];

 // var titles = ["餐饮服务", "购物服务", "生活服务", "体育休闲服务",
 //        "医疗保健服务", "住宿服务", "风景名胜", "商务住宅", "政府机构及社会团体",
 //        "科教文化服务", "交通设施服务", "金融保险服务", "公司企业"];

    return titles;
};
var generateColor = function (data) {
    // var colorInterp = d3.interpolate(d3.rgb(2, 239, 255), d3.rgb(255, 31, 14));
    var colorArry = [];

    var linear = d3.scale.linear()
        .domain([0, data.length-1])
        .range([0, 1]);

    for(var i = 0; i < data.length; i++){
        // colorArry.push(colorInterp(i));
        colorArry.push("hsl(" + linear(i) * 360 + ",100%,50%)");
    }
    var color = d3.scale.ordinal()
        .range(colorArry);

    // for(var i = 0; i < clusters.length; i++){
    //     colorArry.push(d3.rgb(115,115,115));
    // }

    return colorArry;
};
var drawRadarView = function(data){
    //data : 二维数组，每一维包含一个聚簇
    var titles = initTitleArray(),
        radarData = formatData(data, titles),

        // margin = {top: 65, right: 50, bottom: 65, left: 50},
        margin = {top: 35, right: 10, bottom: 30, left: 10},
        width = $(".radar-view-box").width() - margin.left - margin.right,
        height = $(".radar-view-box").height() - margin.bottom - margin.top;


    var color = generateColor(data);

    var radarChartOptions = {
        w: 350,
        h: 235,
        margin: margin,
        maxValue: 1,
        levels: 10,
        roundStrokes: true,
        color: color
    };
    RadarChart(".radar-view-box", radarData, radarChartOptions);
}