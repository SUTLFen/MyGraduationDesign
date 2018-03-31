var s;
var map;   //leaflet地图

function initMapWithGaode() {
    var normalm = L.tileLayer.chinaProvider('GaoDe.Normal.Map',{maxZoom:18,minZoom:5});
    var normal = L.layerGroup([normalm]);

    map = L.map("map",{
        // center:[30.3, 120.2],
        center:[30.23, 120.2],
        zoom: 11,
        layers:[normal],
        zoomControl:false
    });

    L.control.zoom({
        zoomInTitle: '放大',
        zoomOutTitle: '缩小'
    }).addTo(map);
}
