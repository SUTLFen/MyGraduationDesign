function drawQuadTreeMap(){

    var d3Overlay = L.d3SvgOverlay(function(sel, proj) {
        d3.json("../data/allKKInfo_gps.json", function(error, data){
            var kkData = updateKKData(data, proj);
            //绘制四叉树
            drawQuad(sel, proj, kkData);

        });
    });
    d3Overlay.addTo(map);
}


//lat|lng ： 字符串型转数字
function updateKKData(data, proj){
    var lat, lng;
    for(var i = 0 ; i < data.length ; i++){
        lat = parseFloat(data[i].lat);
        lng = parseFloat(data[i].lng);
        data[i].lat = lat;
        data[i].lng = lng;
        data[i].latLng = L.latLng(lat,lng);
        data[i].x = proj.latLngToLayerPoint(data[i].latLng).x;
        data[i].y = proj.latLngToLayerPoint(data[i].latLng).y;
    }
    return data;
}