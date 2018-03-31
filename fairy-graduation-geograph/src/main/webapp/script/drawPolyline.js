var timeSvg

var initSvgConfig = function () {
    margin_01 = {top: 10, right: 10, bottom: 20, left: 65};
    margin_02 = {top: 0, right: 10, bottom: 20, left: 65};

    timeSvg = d3.select(".time-box").append("svg")
        .attr("width", $(".time-box").width()*0.988)
        .attr("height", $(".time-box").height())
        .attr("class", "timeSvg");

    var svgBoxWidth = $(".time-box").width();
    var svgBoxHeight = $(".time-box").height();



    width_01 = svgBoxWidth - margin_01.left - margin_01.right;
    height_01 = svgBoxHeight*0.7 - margin_01.top - margin_01.bottom;

    width_02 = svgBoxWidth - margin_02.left - margin_02.right;
    height_02 = svgBoxHeight*0.3 - margin_02.top - margin_02.bottom;

};
var drawTimeSvg = function(data) {
    scaleX_01 = d3.time.scale().range([0, width_01]);
    scaleY_01 = d3.scale.linear().range([height_01, 0]);

    scaleX_02 = d3.time.scale().range([0, width_02]);
    scaleY_02 = d3.scale.linear().range([height_02, 0]);

    var linePath_01 = d3.svg.line()
        .x(function (d) {
            return scaleX_01(d.hourStr);
        })
        .y(function (d) {
            return scaleY_01(d.count);
        });

    var linePath_02 = d3.svg.line()
        .x(function (d) {
            return scaleX_02(d.hourStr);
        })
        .y(function (d) {
            return scaleY_02(d.count);
        });

    timeSvg.append("defs").append("clipPath")
        .attr("id", "clip")
        .append("rect")
        .attr("width", width_01)
        .attr("height", height_01);

    var focus = timeSvg.append("g")
        .attr("class", "focus")
        .attr("transform", "translate(" + margin_01.left + "," + margin_01.top + ")");//右上角

    var context = timeSvg.append("g")
        .attr("class", "context")
        .attr("transform", "translate(" + margin_02.left + "," + (margin_01.top + height_01 + margin_01.bottom) + ")");


    var xAxis_01 = d3.svg.axis().scale(scaleX_01).orient("bottom")
            .ticks(d3.time.day, 1).tickFormat(d3.time.format("%m/%d")),
        yAxis_01 = d3.svg.axis().scale(scaleY_01).orient("left"),

        xAxis_02 = d3.svg.axis().scale(scaleX_02).orient("bottom")
            .ticks(d3.time.day, 1).tickFormat(d3.time.format("%m/%d")),
        yAxis_02 = d3.svg.axis().scale(scaleY_02).orient("left");

    var brush_02 = d3.svg.brush()
        .x(scaleX_02)
        .on("brush", brushing);

    var brush_01 = d3.svg.brush()
        .x(scaleX_01)
        .on("brushend", brushEnd_01);

    if (data.length > 0) {
        scaleX_01.domain(d3.extent(data.map(function (d) {
            return d.hourStr;
        })));
        scaleY_01.domain([0, d3.max(data.map(function (d) {
            return d.count;
        }))]);

        scaleX_02.domain(scaleX_01.domain()).ticks(d3.time.day, 1);
        scaleY_02.domain(scaleY_01.domain());

        //1. 画area， 2.画x坐标轴， 3. 画y坐标轴
        focus.append("path")
            .datum(data)
            .attr("class", "polyline")
            .attr("d", linePath_01);

        focus.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height_01 + ")")
            .call(xAxis_01);

        focus.append("g")
            .attr("class", "y axis")
            .call(yAxis_01);

        focus.append("g")
            .attr("class", "x brush")
            .call(brush_01)
            .selectAll("rect")
            .attr("y", -6)
            .attr("height", height_01 + 3);

        context.append("path")
            .datum(data)
            .attr("class", "polyline")
            .attr("d", linePath_02);

        context.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height_02 + ")")
            .call(xAxis_02);


        //1. 创建刷子，2. 绑定刷子。
        //把区域与刷子对象绑定好
        context.append("g")
            .attr("class", "x brush")
            .call(brush_02)
            .selectAll("rect")
            .attr("y", -6)
            .attr("height", height_02 + 5);

    }
    //当brush的extend发生变化时，出发brushed函数
    function brushing() {
        scaleX_01.domain(brush_02.empty() ? scaleX_02.domain() : brush_02.extent());
        focus.select(".polyline").attr("d", linePath_01);
        focus.select(".x.axis").call(xAxis_01);
        xAxis_01.ticks(d3.time.hour, 1).tickFormat(d3.time.format("%H:%M"));
    }

    function brushEnd_01(){
        var brushExtent = brush_01.extent();
        timeSeleted = true;

        startTime = new Date(brushExtent[0]);
        endTime = new Date(brushExtent[1]);

        //---------------------绘制OD视图-----------------
        // d3.json("../data/od/ODPairIn0104_seg_01.json", function(data){ //工作日01-04早高峰
        // d3.json("../data/od/ODPairIn0104_seg_03.json", function(data){   //工作日01-04晚高峰

        d3.json("../data/od/ODPairIn0101_seg_01.json", function(data){ //节假日01-01早高峰
        // d3.json("../data/od/ODPairIn0101_seg_03.json", function(data){   //节假日01-01晚高峰
            drawODPattern(data);
        });


        console.log(startTime.toLocaleDateString());
        console.log(endTime.toLocaleDateString());


    //    绘制词云，所有区域的此阶段

    }
}
function addFocusLine(data) {
    //垂直于X轴的对齐线
    var vLine = timeSvg.append("line")
        .attr("class", "focusLine")
        .style("display", "none");

    timeSvg.append("rect")
        .attr("class","overlayRect")
        .attr("x", margin_01.left)
        .attr("y", margin_01.top)
        .attr("width",width_01)
        .attr("height",height_01)
        .on("mouseover", function() {
            console.log("mouseover");
            vLine.style("display",null);
        })
        .on("mouseout", function() {
            console.log("mouseout");
            vLine.style("display","none");
        })
        .on("mousemove", mousemove(data));
};

function mousemove(data){
    console.log("mousemove")
    //获取鼠标相对于透明矩形左上角的坐标，左上角坐标为(0,0)
    var mouseX = d3.mouse(this)[0] - margin_01.left;
    var mouseY = d3.mouse(this)[1] - margin_01.top;

    //通过比例尺的反函数计算原数据中的值，例如x0为某个年份，y0为GDP值
    //scale.invert() : 屏幕坐标值--->比例尺的值
    var x0 = scaleX_01.invert( mouseX );
    var y0 = scaleY_01.invert( mouseY );

    //对x0四舍五入，如果x0是2005.6，则返回2006；如果是2005.2，则返回2005
    x0 = Math.round(x0);

    //查找在原数组中x0的值，并返回索引号
    var bisect = d3.bisector( function(d) { return d[0]; }).left;
    var index = bisect(data, x0) ;

    //获取垂直对齐线的x坐标
    var vlx = scaleX_01(data[index][0]) + margin_01.left;

    //设定垂直对齐线的起点和终点
    vLine.attr("x1", vlx)
        .attr("y1",margin_01.top)
        .attr("x2",vlx)
        .attr("y2",height_01 - margin_01.bottom);
}

var startTime, endTime, timeSeleted = false;
var drawPolyline = function(_data){

    initSvgConfig();
    var parseTime = d3.time.format("%Y-%m-%d %H:%M:%S").parse;
    var data = $.map(_data, function(d){
        d.count += d.count;
        d.hourStr =parseTime(d.hourStr);
        return d;
    });

    drawTimeSvg(data);
    //addFocusLine(data);
};