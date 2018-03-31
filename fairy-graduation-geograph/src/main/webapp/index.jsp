<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>

    <meta http-equiv="Content-Type" content="word/html; charset=UTF-8">
    <title>MobilityPattern</title>
    <%@ include file="WEB-INF/jsp/common_css.jsp"%>
    <%@ include file="WEB-INF/jsp/common_js.jsp"%>

</head>

<body>
<%--页面上半部分：地图（左边）、雷达图（右边）--%>
<div class="top-container">
    <div class="left-container">
        <div id="map"></div>
    </div>

    <%--页面上半部分  右边部分  雷达视图、词云视图、微博列表--%>
    <div class="right-container">
        <%--雷达视图--%>


        <ul id="accordion" class="accordion keyword-weibo">
            <li class="open">
                <div class="link">
                    <i class="fa fa-globe"></i>
                    Region Attribute
                    <i class="fa fa-chevron-down"></i>
                </div>
                <ul class="submenu radar-submenu">
                    <li><div class="radar-view-box"></div></li>
                </ul>
            </li>

            <li class="open">
                <div class="link"><i class="fa fa-code"></i>Word Cloud<i class="fa fa-chevron-down"></i></div>
                <ul class="submenu cloud-submenu">
                    <li><div class="cloud-view-div"></div></li>
                </ul>
            </li>
            <li>
                <div class="link"><i class="fa fa-mobile"></i>Weibo Content<i class="fa fa-chevron-down"></i></div>
                <ul class="submenu weibo-content-submenu">
                    <li><a href="#">Javascript</a></li>
                    <li><a href="#">jQuery</a></li>
                    <li><a href="#">Frameworks javascript</a></li>
                </ul>
            </li>
        </ul>
    </div>

</div>

<%--页面下半部分：时间序列视图--%>
<div class="bottom-container">
    <div class="time-box"></div>
</div>

</body>
</html>