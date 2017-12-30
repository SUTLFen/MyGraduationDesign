<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>MobilityPattern</title>
    <%@ include file="WEB-INF/jsp/common_css.jsp"%>
    <%@ include file="WEB-INF/jsp/common_js.jsp"%>

</head>
</head>

<body>
<%--页面上半部分：地图（左边）、雷达图（右边）--%>
<div class="top-container">
    <div class="left-container">
        <div id="map"></div>
    </div>
    <div class="right-container">

        <%--<div class="title"><label>Color Encode:</label></div>--%>
        <%--<div class="color-descriptor-box">--%>
        <%--<div id="region_color_gradient" class="color-grad-box">--%>
        <%--<label>Region : </label>--%>
        <%--<div id="region_grad_left" class="color-grad-item"></div>--%>
        <%--</div>--%>

        <%--<div id="centorid_color_gradient" class="color-grad-box">--%>
        <%--<label class="color-grad-title" id="title_centorid">Centorid : </label>--%>
        <%--<div id="centorid_grad_left" class="color-grad-item"></div>--%>
        <%--<div id="centorid_grad_right" class="color-grad-item"></div>--%>
        <%--</div>--%>

        <%--<div id="od_color_gradient" class="color-grad-box">--%>
        <%--<label class="color-grad-title" id="title_od">OD : </label>--%>
        <%--<div id="od_grad_left" class="color-grad-item"></div>--%>
        <%--<div id="od_grad_right" class="color-grad-item"></div>--%>
        <%--</div>--%>
        <%--</div>--%>

        <%--<div class="title title_v2"><label>Region Feature: </label></div>--%>
        <%--<div class="radar-view-box"></div>--%>

    </div>
</div>
<%--页面下半部分：时间序列视图--%>
<div class="bottom-container">
    <div class="time-box"></div>
</div>

</body>
</html>