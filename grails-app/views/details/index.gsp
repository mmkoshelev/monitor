<%@ page import="ru.monitor.CheckStatusType; ru.monitor.AceType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Информация о проведенной проверке</title>
    <meta name="layout" content="auimain" />
</head>
<body>
<div class="aui-page-header">
    <div class="aui-page-header-inner">
        <div class="aui-page-header-main">
            <h2>Информация о проведенной проверке для сервера [${params.id}] от ${checkRun.runDate?.format("dd.MM.yyyy HH:mm:ss")}</h2>
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
                    <th>№</th>
                    <th>Директория</th>
                    <th>Название файла</th>
                    <th>Статус</th>
                    <th>ACE</th>
                    <th>&nbsp;</th>
                </tr>
                </thead>
                <tbody>
                <g:each in="${files}" var="file" status="status">
                    <tr>
                        <td>${status + 1}</td>
                        <td>${file.etalonDir.name}</td>
                        <td>${file.name}</td>
                        <td>${CheckStatusType.getByValueSet(file.status).name.join("<br/>")}</td>
                        <td>
                            <table>
                                <g:each in="${file.checkAces}" var="ace">
                                    <tr>
                                        %{--<td style="border: 0;">--}%
                                            %{--<c:choose>--}%
                                                %{--<c:when test="${ace.diff == 1}">--}%
                                                    %{--<img src="img/add.png" alt="Атрибут добален" title="Атрибут добален" />--}%
                                                %{--</c:when>--}%
                                                %{--<c:when test="${ace.diff == 2}">--}%
                                                    %{--<img src="img/remove.png" alt="Атрибут удален" title="Атрибут удален" />--}%
                                                %{--</c:when>--}%
                                            %{--</c:choose>--}%
                                        %{--</td>--}%
                                        <td class="small-inner-table">${AceType.getByValue(ace.mode).name}</td>
                                        <td class="small-inner-table">${ace.value}</td>
                                        <td class="small-inner-table">${ace.trustee ?: "?"}</td>
                                    </tr>
                                </g:each>
                            </table>
                        </td>
                        <td>
                            <a href="#cmpaces" id="cmp-ace-${i}" data-id="${file.id}" data-code="${code}">Сравнить</a>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>