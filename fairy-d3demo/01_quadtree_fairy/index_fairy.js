var width = 960,
    height = 500;

d3.json("data/data.json", function(error, _data){

    var data = jsonData(_data);

    var quadtree = d3.geom.quadtree()
        .extent([[-1, -1], [width + 1, height + 1]])
        (data);

    var color = d3.scale.linear()
        .domain([0, 8])  // max depth of quadtree
        .range(["#efe", "#060"]);

    var svg = d3.select("body").append("svg")
        .attr("width", width)
        .attr("height", height)
        .on("click", function (d) {
            var xy = d3.mouse(d3.selectAll('svg')[0][0]);
            svg.selectAll("#pt")
                .attr("cx", xy[0])
                .attr("cy", xy[1]);
            clicked();
        });

    var rect = svg.selectAll(".node")
        .data(nodes(quadtree))
        .enter()
        .append("rect")
        .attr("class", "node")
        .attr("x", function(d) { return d.x1; })
        .attr("y", function(d) { return d.y1; })
        .attr("width", function(d) { return d.x2 - d.x1; })
        .attr("height", function(d) { return d.y2 - d.y1; });

    var point = svg.selectAll(".point")
        .data(data)
        .enter().append("circle")
        .attr("class", "point")
        .attr("cx", function(d) { return d[0]; })
        .attr("cy", function(d) { return d[1]; })
        .attr("r", 3);

    svg.append("circle")
        .attr("id", "pt")
        .attr("r", 3)
        .attr("cx", width/2)
        .attr("cy", height/2)
        .style("fill", "yellow");


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
                if (node.nodes[i])  //node.nodes: 四个子节点
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
        while(!queue.isEmpty()){
            var _node = queue.dequeue();

            if(_node.count <= 50)
            _node.leaf = true;
            resultNodes.push(_node);
            if(_node.count > 50){
                var subNodes = _node.nodes;
                for (var i = 0 ; i < subNodes.length; i++){
                    queue.enqueue(subNodes[i]);
                }
            }
        }

        saveRegins();

        return resultNodes;
    }

});

function jsonData(data) {
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










