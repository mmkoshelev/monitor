<%@ page import="ru.monitor.AceType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Контрольная информация</title>
    <meta name="layout" content="auimain" />
</head>
<body>
<div class="aui-page-header">
    <div class="aui-page-header-inner">
        <div class="aui-page-header-main">
            <h2>Контрольная информация сервера проверки [${server.code}]: ${dir.name}</h2>
        </div>
        <div class="aui-page-header-actions">
            <div class="aui-buttons">
                <g:link action="index" id="${server.code}" class="aui-button">
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
                        <th>Название файла</th>
                        <th>Хэш</th>
                        <th>Размер (байт)</th>
                        <th>ACE</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${files}" var="file" status="status">
                        <tr>
                            <td>${params.int("offset", 0) + status + 1}</td>
                            <td>${file.name}</td>
                            <td>${file.hash}</td>
                            <td>${file.size}</td>
                            <td>
                                <table>
                                    <g:each in="${file.etalonAce}" var="ace">
                                        <tr>
                                            <td class="small-inner-table">${AceType.getByValue(ace.mode).name}</td>
                                            <td class="small-inner-table">${ace.value}</td>
                                            <td class="small-inner-table">${ace.trustee ?: "?"}</td>
                                        </tr>
                                    </g:each>
                                </table>
                            </td>
                        </tr>
                    </g:each>
                    <g:if test="${!files}">
                        <tr>
                            <td colspan="5">Нет данных</td>
                        </tr>
                    </g:if>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${filesCount}" id="${dir.id}" omitNext="true" omitPrev="true" />
                <div>Всего записей: ${filesCount}</div>
            </div>
        </div>
    </div>
</div>
</body>
</html>