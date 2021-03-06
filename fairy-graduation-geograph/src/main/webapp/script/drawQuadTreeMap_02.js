var leaf_threshhold = 50;  // 四叉树分叉阈值

var entropyArray = [-0.25235066,
    -4.5324087,
    -4.477898,
    -4.3169856,
    -3.9614975,
    -4.164416,
    -2.7272558,
    -1.7501979,
    -1.0402491,
    -1.8225399,
    -3.9797168,
    -4.0699706,
    -3.4977307,
    -3.1527863,
    -0.8064474,
    -3.0614681,
    -3.1828613,
    -3.132613,
    -1.50808,
    -3.7723353,
    -2.9235816,
    -4.2844844,
    -3.734143,
    -3.43219,
    -3.1447463];

function drawQuad(sel, proj, kkData){

    var width = d3.select("#map").style("width");
    var height = d3.select("#map").style("height");

    var x_extent = d3.extent(kkData, function(d){
        return d.x;
    });
    var y_extent = d3.extent(kkData, function(d){ 
        return d.y;
    });

    var data = toPoints(kkData);

    var quadtree = d3.geom.quadtree()
        .extent([[x_extent[0] - 1, y_extent[0] - 1], [x_extent[1] + 1, y_extent[1] + 1]])
        (data);


    var color = d3.scale.linear()
        .domain([0, 8])  // max depth of quadtree
        .range(["#efe", "#060"]);

    var entropyScale = d3.scale.linear()
        .domain(d3.extent(entropyArray))
        .range([1, 0]);

    var colorInterpolate = function(index) {
        var color_a = d3.rgb(255, 255, 204);
        var color_b = d3.rgb(116, 169, 207);
        var compute = d3.interpolate(color_a, color_b);
        return compute(index);
    }


    function initAreClickedArry(length){
        var arry = new Array(length);
        for(var i = 0; i < length; i++){
            arry[i] = 0;
        }
        return arry;
    }
    var areaClickedAryy = initAreClickedArry(data.length);

    var rect = sel.selectAll(".node")
        .data(nodes(quadtree))
        .enter()
        .append("rect")
        .attr("class", function(d,i){
            return "node " + "rect_" + i;
        })
        .attr("id", function(d, i){
            return "rect_" + i;
        })
        .attr("x", function(d) { return d.x1; })
        .attr("y", function(d) { return d.y1; })
        .attr("width", function(d) { return d.x2 - d.x1; })
        .attr("height", function(d) { return d.y2 - d.y1; })
        .style("fill", function(d, i){ return colorInterpolate(entropyScale(entropyArray[i]));})
        .style("opacity", 0.3)
        .on("click", function(d, i){
            if(areaClickedAryy[i] % 2 == 0 ){
                d3.select("#radarStroke_" + i)
                    .transition().duration(10)
                    .style("opacity", 0.8);
                d3.selectAll(".radarCircle_" + i)
                    .transition().duration(10)
                    .style("fill-opacity", 0.8);

            }else if(areaClickedAryy[i] % 2 == 1){
                d3.select("#radarStroke_" + i)
                    .transition().duration(10)
                    .style("opacity", 0);
                d3.selectAll(".radarCircle_" + i)
                    .transition().duration(10)
                    .style("fill-opacity", 0);
            }
            areaClickedAryy[i]++;

            //显示词云视图
            showWordCloud(i);
        });

    var point = sel.selectAll(".point")
        .data(data)
        .enter().append("circle")
        .attr("class", "point")
        .attr("cx", function(d) { return d[0]; })
        .attr("cy", function(d) { return d[1]; })
        .attr("r", 3);

    function nodes(quadtree) {
        var nodes = [];
        quadtree.depth = 0; // root
        quadtree.visit(function(node, x1, y1, x2, y2) {
            node.x1 = x1;
            node.y1 = y1;
            node.x2 = x2;
            node.y2 = y2;
            nodes.push(node);
            for (var i=0; i<4; i++) {
                if (node.nodes[i])
                    node.nodes[i].depth = node.depth+1;
            }
        });

        //每棵子树中结点个数，包括根结点
        for(var i = 0 ; i < nodes.length; i++){
            nodes[i].count = countOfSubtree(nodes[i]);
        }

        //广度优先BFS
        var resultNodes = [];
        var queue = new Queue();
        queue.enqueue(quadtree);
        var i = 0;
        while(!queue.isEmpty()){
            var _node = queue.dequeue();
            if(_node.count <= leaf_threshhold){
                _node.leaf = true;
            }
            resultNodes.push(_node);
            if(_node.count > leaf_threshhold){
                var subNodes = _node.nodes;
                for (var i = 0 ; i < subNodes.length; i++){
                    if(subNodes[i] != null){
                        queue.enqueue(subNodes[i]);
                    }
                }
            }
        }

        saveRegins(resultNodes, proj);

        return resultNodes;
    }
}
//获取点
function toPoints(data) {
    var result = [];
    for (var i = 0; i < data.length; i++) {
        var point = [data[i].x, data[i].y];
        result.push(point);
    }
    return result;
}

function countOfSubtree(curNode) {
    if (curNode == null) {
        return 0;
    } else {
        var nodes = curNode.nodes;
        var count = 1;
        for (var i = 0; i < nodes.length; i++) {
            count += countOfSubtree(nodes[i]);
        }
        return count;
    }
}

//将nodes中的叶子结点保存起来；
function saveRegins(nodes, proj){
    var data = new Object();
    var leafNodes = [];
    for(var i = 0 ; i < nodes.length; i++){
        if(nodes[i].leaf == true){

            var latLng_01 = proj.layerPointToLatLng(L.point(nodes[i].x1, nodes[i].y1));
            var latLng_02 = proj.layerPointToLatLng(L.point(nodes[i].x2, nodes[i].y2));

            var leafNode = new Object();
            leafNode.lat01 = latLng_01.lat;
            leafNode.lng01 = latLng_01.lng;
            leafNode.lat02 = latLng_02.lat;
            leafNode.lng02 = latLng_02.lng;
            leafNodes.push(leafNode);
        }
    }

    // data.nodes = JSON.stringify(leafNodes);
    //
    //     $.ajax({
    //         url: "SaveRegionServlet",
    //         type: "post",
    //         data:data,
    //         dataType: "json",
    //         success: function(data){
    //            console.log("sucess");
    //         },
    //         error: function(XMLHttpRequest , textStatus, errorThrown){
    //             // console.log("textStatus : " + textStatus);
    //         }
    //     });
}



