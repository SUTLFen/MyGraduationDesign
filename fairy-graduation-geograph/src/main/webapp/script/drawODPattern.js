/**
 * Created by Fairy_LFen on 2017/5/9.
 */
var odPatternData, centroid, odPatternDataTmp, flowWidthScale;
var initArray = function(length){
    var arry = new Array(length);
    for(var i = 0; i< length; i++){
        arry[i] = 0;
    }
    return arry;
};

var calFlowInAndOutDiff = function(){
    var flowIn_Outs = initArray(odPatternData.length);// 每个聚类，流入减去流出  （结果为正  红色表示， 结果为负   蓝色表示）
    var flowInSum = initArray(odPatternData.length);
    var flowOutSum = initArray(odPatternData.length);

    for(var i = 0 ; i < odPatternData.length; i++){
        for(var j = 0 ; j < odPatternData[i].length; j++){
            if(i != j){
                flowInSum[i] += odPatternData[j][i].weight;
                flowOutSum[i] += odPatternData[i][j].weight;
            }
        }
    }
    for(i = 0; i < flowIn_Outs.length; i++){
        flowIn_Outs[i] = flowInSum[i] - flowOutSum[i];
    }
    return flowIn_Outs;
};

//集合中心颜色映射
var centroid_color_Pos = function(index){
    var color_a = d3.rgb(253,141,60);
    var color_b = d3.rgb(189,0,38);
    var compute = d3.interpolate(color_a, color_b);
    return compute(index);
};

var centroid_color_Neg = function(index){
    var color_a = d3.rgb(158, 154, 200);
    var color_b = d3.rgb(84,39,143);
    var compute = d3.interpolate(color_a, color_b);
    return compute(index);
};

//流量移动颜色映射
var flow_color_Pos = function(index){
    var color_a = d3.rgb(252,78,42);
    var color_b = d3.rgb(128,0,38);
    var compute = d3.interpolate(color_a, color_b);
    return compute(index);
};

var flow_color_Neg = function(index){
    var color_a = d3.rgb(128,125,186);
    var color_b = d3.rgb(63,0,125);
    var compute = d3.interpolate(color_a, color_b);
    return compute(index);
};


var collectODPairs = function(index){
    var result = [];
    for(var i = 0; i < odPatternData.length; i++) {
        if (i != index) {
            result.push(odPatternData[i][index]);
            result.push(odPatternData[index][i]);
        }
    }
    return result;
};

var drawClustCenters = function(sel){
    var defs = sel.append("defs");
    var arrowMarker = defs.append("marker")
        .attr("id","arrow")
        .attr("markerUnits","strokeWidth")
        .attr("markerWidth","12")
        .attr("markerHeight","12")
        .attr("viewBox","0 0 12 12")
        .attr("refX","6")
        .attr("refY","6")
        .attr("orient","auto");
    var arrow_path = "M2,2 L10,6 L2,10 L6,6 L2,2";
    arrowMarker.append("path")
        .attr("d",arrow_path)
        .attr("fill","#000");

    var flowIn_Outs = calFlowInAndOutDiff();
    var g_circles = sel.append("g")
        .attr("class", "g_circles");

    var flowExtent = d3.extent(flowIn_Outs);
    var flowScale_Pos = d3.scale.linear()  //几何中心颜色映射
        .domain([0, flowExtent[1]])
        .range([0, 1]);
    var flowScale_Neg = d3.scale.linear()   //几何中心颜色映射
        .domain([0, flowExtent[0]])
        .range([0, 1]);


    g_circles.selectAll("circle")
        .data(centroid)
        .enter()
        .append("circle")
        .attr("class", "clust-centorid")
        .attr("cx", function(d){
            return d.x;
        })
        .attr("cy", function(d){
            // return d[1];
            return d.y;
        })
        .attr("r", 5)
        .style("fill", function(d, i){
            // d3.rgb(237,248,177)
            var flow = flowIn_Outs[i];
            if(flow >= 0){
                return centroid_color_Pos(flowScale_Pos(flow));
            }else{
                return centroid_color_Neg(flowScale_Neg(flow));
            }
        })
        .style("stroke", d3.rgb(254,153,41))
        .style("stroke-width", 2)
        .on("click", function(d, i){

            console.log("clickedArry[" + i + "] : "  + clickedArry[i]);

            if(clickedArry[i]%2 == 0){
                //flow in
                d3.selectAll(".to" + i)
                    .transition().duration(10)
                    .style("stroke",
                        function(d){
                            return flow_color_Pos(flowWidthScale(d.weight))
                        }
                    )
                    .style("opacity", 1);
                //flow out
                d3.selectAll(".from" + i)
                    .transition().duration(10)
                    .style("stroke", function(d){
                        return flow_color_Neg(flowWidthScale(d.weight))
                    })
                    .style("opacity", 1);
            }else if(clickedArry[i]%2 == 1){
                //flow in
                d3.selectAll(".to" + i)
                    .transition().duration(10)
                    .style("stroke", "grey")
                    .style("opacity", 0.7);
                //flow out
                d3.selectAll(".from" + i)
                    .transition().duration(10)
                    .style("stroke", "grey")
                    .style("opacity", 0.7);
            }
            clickedArry[i]++;
        }, true)
        .on("mouseover", function(d, i){
            // d3.select(".from"+i+"to")
            d3.select(this)
                .transition()
                .duration(200)
                .style("stroke", d3.rgb(254,227,145))
                .style("fill-opacity", 0.8);
        })
        .on("mouseout", function(d, i){
            d3.select(this)
                .transition()
                .duration(200)
                .style("stroke", d3.rgb(254,153,41));
        });
};


var drawODLines = function(sel){
    var defs = sel.append("defs");
    var arrowMarker = defs.append("marker")
        .attr("id","arrow")
        .attr("markerUnits","strokeWidth")
        .attr("markerWidth","12")
        .attr("markerHeight","12")
        .attr("viewBox","0 0 12 12")
        .attr("refX","6")
        .attr("refY","6")
        .attr("orient","auto");
    var arrow_path = "M2,2 L10,6 L2,10 L6,6 L2,2";
    arrowMarker.append("path")
        .attr("d",arrow_path)
        .attr("fill","#000");

    var weightScaleExtent = d3.extent(odPatternDataTmp, function (d) {
        return d.weight;
    });

    flowWidthScale = d3.scale.linear()
        .domain(weightScaleExtent)
        .range([0, 1]);

    var g_lines = sel.append("g")
        .attr("class", "lines");

    g_lines.selectAll("path")
        .data(odPatternDataTmp)
        .enter()
        .append("path")
        .attr("class", function(d){
            return "odLink " + "from" + d.o_regin_index +" to" + d.d_region_index;
        })
        .attr("id", function(d){
            return "odLink" + "from" + d.o_regin_index +"to" + d.d_region_index;
        })
        .attr("d", function(d){
            if(d.weight > 0){
                var source = centroid[d.o_regin_index],
                    target = centroid[d.d_region_index],

                    // sx = source[0], sy = source[1],
                    // tx = target[0], ty = target[1],

                    sx = source.x, sy = source.y,
                    tx = target.x, ty = target.y,

                    dx = tx - sx, dy = ty - sy,
                    dr = 2 * Math.sqrt(dx * dx + dy * dy);

                return "M " + sx + "," + sy + "A" + dr + "," + dr + " 0,0,1 " + tx + "," + ty;
            }else{
                return null;
            }
        })
        .style("stroke", "#6E6E6E")
        .style("fill", "none")
        .style("stroke-width", function(d){
            return 15 * flowWidthScale(d.weight);
        })
        .style("opacity", 0.7);

};
var drawCenterText = function(sel){
    var g_center_text = sel.append("g")
        .attr("class", "center-text");

    g_center_text.selectAll("text")
        .data(centroid)
        .enter()
        .append("text")
        .text(function(d, i) {
            return i;
        })
        .attr("x", function(d, i) {
            return d[0];
        })
        .attr("y", function(d, i) {
            return d[1] ;
        })
        .style("font-family", "sans-serif")
        .style("font-size", "11px")
        .style("text-anchor", "middle")
        .style("dominant-baseline", "central")
        .style("stroke", "black");
        // .on("style", );;

};

var reduceDimension = function(dataArry){
    var result = [];
    for(var i = 0; i < dataArry.length; i++){
        for(var j = 0 ; j < dataArry.length; j++){
            if(i != j){
                result.push(dataArry[i][j]);
            }
        }
    }
    return result;
};
var clickedArry;
var initClickedArry = function(length){
    var arry = new Array(length);
    for(var i = 0; i < length; i++){
        arry[i] = 0;
    }
    return arry;
};

var drawODPattern = function(odData){

    d3.json("../data/GridData.json", function(data){
        odPatternData = odData;
        // centroid = data;

        odPatternDataTmp = reduceDimension(odPatternData);
        clickedArry = initClickedArry(data.length);

        var d3Overlay = L.d3SvgOverlay(function (sel, proj) {
            sel.selectAll("path")
                .style("opacity", 0.2);
            sel.select("#grects")
                .style("opacity", 0.2);

            centroid = formatCentroids(data, proj);

            drawODLines(sel);
            drawClustCenters(sel);
            // drawCenterText(sel);

        }).addTo(map);
    });
};

function formatCentroids(gridData, proj){

    // data[i].x = proj.latLngToLayerPoint(data[i].latLng).x;

    console.log(gridData);

    var result = [];
    var _grid;

    var gridCur;
    for(var i = 0; i < gridData.length; i++){
        gridCur = gridData[i];
        var centerLat = (gridCur.lat01 + gridCur.lat02) / 2;
        var centerLng = (gridCur.lng01 + gridCur.lng02) / 2;
        var center = new L.latLng(centerLat, centerLng);
        // console.log(center);

        _grid = new Object();
        _grid.x = proj.latLngToLayerPoint(center).x;
        _grid.y = proj.latLngToLayerPoint(center).y;
        result.push(_grid);
    }
    return result;
}













