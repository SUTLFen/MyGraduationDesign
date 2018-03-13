var leaf_threshhold = 50;  // 四叉树分叉阈值

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

    var rect = sel.selectAll(".node")
        .data(nodes(quadtree))
        .enter()
        .append("rect")
        .attr("class", "node")
        .attr("x", function(d) { return d.x1; })
        .attr("y", function(d) { return d.y1; })
        .attr("width", function(d) { return d.x2 - d.x1; })
        .attr("height", function(d) { return d.y2 - d.y1; });

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
            console.log(leafNode.lat01+","+ leafNode.lng01+","+ leafNode.lat02+","+ leafNode.lng02);
        }
    }
    data.nodes = JSON.stringify(leafNodes);

        $.ajax({
            url: "SaveRegionServlet",
            type: "post",
            data:data,
            dataType: "json",
            success: function(data){
               console.log("sucess");
            },
            error: function(XMLHttpRequest , textStatus, errorThrown){
                // console.log("textStatus : " + textStatus);
            }
        });
}



