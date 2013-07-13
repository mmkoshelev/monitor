<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="IE=EDGE" http-equiv="X-UA-Compatible">

    <title><g:layoutTitle default="Monitor"/></title>

    <link rel="stylesheet" href="${resource(dir: 'aui/css', file: 'aui-all.css')}" media="all">
    <!--[if lt IE 9]><link rel="stylesheet" href="${resource(dir: 'aui/css', file: 'aui-ie.css')}" media="all"><![endif]-->
    <!--[if IE 9]><link rel="stylesheet" href="${resource(dir: 'aui/css', file: 'aui-ie9.css')}" media="all"><![endif]-->
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'monitor.css')}" type="text/css">

    <script src="${resource(dir: 'aui/js', file: 'aui-all.js')}"></script>
    <!--[if lt IE 9]><script src="${resource(dir: 'aui/js', file: 'aui-ie.js')}"></script><![endif]-->

    <g:layoutHead/>
    <r:layoutResources/>
</head>
<body>
    <div id="header">
        <div class="aui-header aui-dropdown2-trigger-group">
            <div class="aui-header-primary">
                <h1 id="logo" class="aui-header-logo aui-header-logo-textonly">
                    <g:link uri="/">
                        <span class="aui-header-logo-device">monitor</span>
                    </g:link>
                </h1>
                <ul class="aui-nav">
                    <li>
                        <a href="#menu1" aria-owns="menu1" aria-haspopup="true"
                           class="aui-dropdown2-trigger">Menu<span class="aui-icon-dropdown"></span></a>
                        <div id="menu1" class="aui-dropdown2 aui-style-default aui-dropdown2-in-header">
                            <ul>
                                <li><a href="#">Item 1</a></li>
                                <li><a href="#">Item 2</a></li>
                                <li><a href="#">Item 3</a></li>
                                <li><a href="#">Item 4</a></li>
                                <li><a href="#">Item 5</a></li>
                            </ul>
                        </div>
                    </li>
                    <li><a href="../sandbox/index.html">Sandbox</a></li>
                </ul>
            </div>
            <div class="aui-header-secondary">
                <ul class="aui-nav">
                    <li>
                        <g:form action="quicksearch" method="post" class="aui-quicksearch">
                            <label for="quicksearch" class="assistive">Поиск</label>
                            <g:textField name="quicksearch" id="quicksearch" placeholder="Поиск" class="search" />
                        </g:form>
                    </li>
                    <li>
                        <a href="#menu2" aria-owns="menu2" aria-haspopup="true" class="aui-dropdown2-trigger">
                            <span class="aui-icon aui-icon-small aui-iconfont-configure">Configure</span>
                            <span class="aui-icon-dropdown"></span>
                        </a>
                        <div id="menu2" class="aui-dropdown2 aui-style-default aui-dropdown2-in-header">
                            <div class="aui-dropdown2-section">
                                <ul>
                                    <li><a href="#">User Info</a></li>
                                    <li><a href="#">Item 2</a></li>
                                </ul>
                            </div>
                            <div class="aui-dropdown2-section">
                                <ul>
                                    <li><a href="#">Log out</a></li>
                                </ul>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <div id="content">
        <m:messages/>
        <g:layoutBody/>
    </div>

    <div id="footer">
        <div class="footer-body">
            <ul>
                <li>Copyright &copy; 2013 Monitor</li>
                <li><a href="https://developer.atlassian.com/display/AUI/License">Apache License v2.0</a></li>
            </ul>
        </div>
    </div>

    <r:layoutResources/>
</body>
</html>