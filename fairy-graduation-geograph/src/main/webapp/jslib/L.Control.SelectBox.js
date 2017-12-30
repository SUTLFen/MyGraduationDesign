/**
 * Created by Fairy_LFEn on 2016/4/12/0012.
 */
var startPoint;
var endPoint;
var s;
function pointFlash(a, b, dl_lng, dl_lat){
    s = setInterval(function(){
        var a_random = Math.random() * a;
        var b_random = Math.random() * b;

        var lng_temp = dl_lng + a_random;
        var lat_temp = dl_lat + b_random;

        console.log(lng_temp + "   " + lat_temp);

        var greenIcon = L.icon({
            iconUrl: '../graph/flashingPoint.gif',
            iconSize: [10, 10], // size of the icon
        });

        L.marker([lat_temp, lng_temp], {icon: greenIcon}).addTo(map);
        console.log("add marker ...... ");
    }, 2000);

    // for (var i = 0; i < 20; i++) {
    //     // console.log("i : " + i);
    //     var a_random = Math.random() * a;
    //     var b_random = Math.random() * b;
    //
    //     var lng_temp = dl_lng + a_random;
    //     var lat_temp = dl_lat + b_random;
    //
    //     console.log(lng_temp + "   " + lat_temp);
    //
    //     var greenIcon = L.icon({
    //         iconUrl: '../graph/flashingPoint.gif',
    //         iconSize: [10, 10], // size of the icon
    //     });
    //
    //     L.marker([lat_temp, lng_temp], {icon: greenIcon}).addTo(map);
    //     console.log("add marker ...... ");
    // }
}

L.Control.SelectBox = L.Control.extend({
    _active: false,
    _map: null,
    includes: L.Mixin.Events,
    options: {
        position: 'topleft',
        className: 'fa fa-crop',
        modal: false
    },
    _this : this,
    onAdd: function (map) {
        this._map = map;
        this._container = L.DomUtil.create('div', 'leaflet-zoom-box-control leaflet-bar');
        this._container.title = "Zoom to specific area";
        //L.DomUtil: create a DOM element.
        var link = L.DomUtil.create('a', this.options.className, this._container);
        link.href = "#";

        this.selectBox = L.DomUtil.create('div', "select-box", this._map.getContainer());
        if (!this.options.modal) {
            map.on('boxzoomend', this.deactivate, this);
        }
        //L.DomEvent.stop : does stopPropagation and preventDefault at the same time.
        //L.DomEvent : utility function to work with the DOM events
        _this = this;
        L.DomEvent
            .on(this._container, 'dblclick', L.DomEvent.stop)
            .on(this._container, 'click', L.DomEvent.stop)
            .on(this._container, 'click', function () {
                this._active = !this._active;
                if (this._active && map.getZoom() != map.getMaxZoom()) {
                    this.activate();
                }
                else {
                    this.deactivate();
                }
            }, this);
        return this._container;
    },
    activate: function () {
        L.DomUtil.addClass(this._container, 'active');
        this.selectAreaEnable();
    },
    onMouseDown: function(e) {
        mouseDownflag = true;
        startPoint = new L.point(e.clientX, e.clientY);

        _selectBox.css("top", startPoint.y);
        _selectBox.css("left", startPoint.x);
        _selectBox.css("width", 0);
        _selectBox.css("height", 0);

        // _this.printLnglatOnPoint(startPoint, "startPoint");

        // $(".quadtree-rect").parent().remove();

    },
    onMouseMove: function(e){
        var curPointTmp = null;
        if (mouseDownflag && _this._active) {
            var curPoint = new L.point(e.clientX, e.clientY);
            var width = curPoint.x - startPoint.x;
            var height = curPoint.y - startPoint.y;
            if (width >= 0 && height >= 0) {
                _selectBox.css("display", "block");
                _selectBox.css("width", width);
                _selectBox.css("height", height);

                tlPoint_L = startPoint;
                drPoint_L = L.point(startPoint.x + width, startPoint.y + height);
            }
            if (width >= 0 && height <= 0) {
                var width_01 = Math.abs(width);
                var height_01 = Math.abs(height);
                var curPointTmp = new L.point(startPoint.x, startPoint.y - height_01);
                _selectBox.css("top", curPointTmp.y);
                _selectBox.css("left", curPointTmp.x);
                _selectBox.css("width", width_01);
                _selectBox.css("height", height_01);

                tlPoint_L = L.point(startPoint.x, startPoint.y - height_01);
                drPoint_L = L.point(startPoint.x + width_01, startPoint.y);
            }
            if (width <= 0 && height <= 0) {
                var width_02 = Math.abs(width);
                var height_02 = Math.abs(height);
                var curPointTmp = new L.point(startPoint.x - width_02, startPoint.y - height_02);
                _selectBox.css("top", curPointTmp.y);
                _selectBox.css("left", curPointTmp.x);
                _selectBox.css("width", width_02);
                _selectBox.css("height", height_02);

                tlPoint_L = L.point(startPoint.x - width, startPoint.y - height_02);
                drPoint_L = L.point(startPoint.x, startPoint.y);
            }
            if (width <= 0 && height >= 0) {
                var width_03 = Math.abs(width);
                var height_03 = Math.abs(height);
                var curPointTmp = new L.point(startPoint.x - width_03, startPoint.y);
                _selectBox.css("top", curPointTmp.y);
                _selectBox.css("left", curPointTmp.x);
                _selectBox.css("width", width_03);
                _selectBox.css("height", height_03);

                tlPoint_L = L.point(startPoint.x - width_03, startPoint.y);
                drPoint_L = L.point(startPoint.x, startPoint.y + height_03)
            }
        }
    },
    onMouseUp: function(e){

        mouseDownflag = false;

        var tlLatLng_L = map.containerPointToLatLng(tlPoint_L);
        var drLatLng_L = map.containerPointToLatLng(drPoint_L);

        console.log("tlLatLng_L : " + tlLatLng_L);
        console.log("drLatLng_L : " + drLatLng_L);

        var jsonData = new Object();
        endPoint = new L.point(e.clientX, e.clientY);


        jsonData.tl_lat = tlLatLng_L.lat;
        jsonData.tl_lng = tlLatLng_L.lng;

        jsonData.dr_lat = drLatLng_L.lat;
        jsonData.dr_lng = drLatLng_L.lng;

        // jsonData.startTime = startTime.getTime();
        // jsonData.endTime = endTime.getTime();

        //godoor.com
        // _this.showAnimatedPoints(jsonData);
        //方框数据, 将选中的框（top-left point 和 down right point）
        // _this.requestData(jsonData);

    },
    
    // godoor.sun
    showAnimatedPoints: function(jsonData) {
        console.log("showAnimatedPoints");
        var a = jsonData.dr_lng - jsonData.tl_lng;
        var b = jsonData.tl_lat - jsonData.dr_lat;

        var dl_lng = jsonData.tl_lng;
        var dl_lat = jsonData.dr_lat;

        pointFlash(a, b, dl_lng, dl_lat);


    },

    //----------show points
    // requestData: function(data){
    //     $.ajax({
    //         url: "SpatialTempDataProvider",
    //         type: "post",
    //         data:data,
    //         dataType: "json",
    //         success: function(data){
    //             console.log("----something from the sever -----");
    //             // console.log(data);
    //             showSpatialTempPoints(data);
    //         },
    //         error: function(XMLHttpRequest , textStatus, errorThrown){
    //             console.log("textStatus : " + textStatus);
    //         }
    //     });
    // },
    
    //-----------show quadTree hotmap
    requestData: function(data){
        console.log("the lnglat : ");
        console.log(data);

        $.ajax({
            url: "QuadTreeNodesProvider",
            type: "post",
            data:data,
            dataType: "json",
            success: function(data){
                showHotmapByQuadtree(data);
            },
            error: function(XMLHttpRequest , textStatus, errorThrown){
                console.log("show hotmap by quadtree : " + textStatus);
            }
        });
    },

    selectAreaEnable: function () {
        mouseDownflag = null;

        this._map.dragging.disable();
        L.DomUtil.addClass(this._map.getContainer(), 'leaflet-zoom-box-crosshair');

        _selectBox = $('.select-box').appendTo($('body'));
        _mapTmp = $("#map");
        _mapTmp.bind("mousedown", this.onMouseDown);
        _mapTmp.bind("mousemove", this.onMouseMove);
        //_mapTmp.bind("mouseup", this.onMouseUp);

        _selectBox.bind("mousedown", this.onMouseDown);
        _selectBox.bind("mousemove", this.onMouseMove);
        _selectBox.bind("mouseup", this.onMouseUp);
        
        // mapSvg = 
    },
    deactivate: function () {
        L.DomUtil.removeClass(this._container, 'active');
        this._map.dragging.enable();
        //this._map.boxZoom.removeHooks();
        L.DomUtil.removeClass(this._map.getContainer(), 'leaflet-zoom-box-crosshair');
        this._active = false;
        this._map.boxZoom._moved = false; //to get past issue w/ Leaflet locking clicks when moved is true (https://github.com/Leaflet/Leaflet/issues/3026).
        this.selectAreaDisable();

        // $(".quadtree-rect").parent().parent().remove();

    },
    selectAreaDisable: function () {
        _mapTmp.unbind("mousedown", this.onMouseDown);
        _mapTmp.unbind("mousemove", this.onMouseMove);
        //_mapTmp.unbind("mouseup", this.onMouseUp);

        _selectBox.css("dispaly", "none");
        _selectBox.css('width', 0);
        _selectBox.css('height', 0);

        _selectBox.unbind("mousedown", this.onMouseDown);
        _selectBox.unbind("mousemove", this.onMouseMove);
        _selectBox.unbind("mouseup", this.onMouseUp);
    }
});

L.control.selectBox = function (options) {
    return new L.Control.SelectBox(options);
};