<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Контрольная информация</title>
    <meta name="layout" content="auimain" />
</head>
<body>
    <div class="aui-page-header">
        <div class="aui-page-header-inner">
            <div class="aui-page-header-main">
                <h2>Контрольная информация сервера проверки [${params.id}]</h2>
            </div>
            <div class="aui-page-header-actions">
                <div class="aui-buttons">
                    <g:link controller="servers" action="index" class="aui-button">
                        <span class="aui-icon aui-icon-small aui-iconfont-close-dialog">close</span>
                        Закрыть
                    </g:link>
                </div>
            </div>
        </div>
    </div>
    <div class="aui-page-panel">
        <div class="aui-page-panel-inner">
            <div class="aui-page-panel-content">
                <table class="aui">
                    <thead>
                        <tr>
                            <td>Директория</td>
                            <td>Количество файлов</td>
                            <td>&nbsp;</td>
                        </tr>
                    </thead>
                    <tbody>
                        <g:each in="${dirs}" var="dir">
                            <tr>
                                <td>${dir.name}</td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                        </g:each>
                   </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>