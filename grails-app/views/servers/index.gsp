<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Сервера проверки</title>
    <meta name="layout" content="auimain">
    <g:set var="hasquicksearch" value="${true}" scope="request" />
    <script src="${resource(dir: 'js/servers', file: 'index.js')}"></script>
</head>
<body>
    <div class="aui-page-header">
        <div class="aui-page-header-inner">
            <div class="aui-page-header-main">
                <h2>Сервера проверки</h2>
            </div>
            <div class="aui-page-header-actions">
                <div class="aui-buttons">
                    <button id="new-server-btn" type="button" class="aui-button">
                        <span class="aui-icon aui-icon-small aui-iconfont-add">add</span>
                        Новый сервер
                    </button>
                    <button id="import-db-btn" type="button" class="aui-button">
                        <span class="aui-icon aui-icon-small aui-iconfont-share">import</span>
                        Импорт базы
                    </button>
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
                        <g:sortableColumn property="name" title="Наименование" />
                        <g:sortableColumn property="code" title="Код компонента" />
                        <th>Дата последней проверки</th>
                        <th>&nbsp;</th>
                    </tr>
                    </thead>
                    <tbody>
                        <g:each in="${servers}" status="i" var="server">
                            <tr>
                                <td>${server.name}</td>
                                <td>${server.code}</td>
                                <td>${lastChecks[server.code]?.format("dd.MM.yyyy HH:mm:ss")}</td>
                                <td class="right">
                                    <g:form action="delete" method="post">
                                        <g:hiddenField name="id" value="${server.id}" />
                                        <button type="submit" class="aui-button" title="Удалить"
                                                onclick="return confirm('Удалить сервер и все данные?');">
                                            <span class="aui-icon aui-icon-small aui-iconfont-remove">remove</span>
                                        </button>
                                    </g:form>
                                </td>
                            </tr>
                        </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="hidden">
        <div id="new-server-panel">
            <g:form name="new-server-form" action="newserver" method="post" class="aui">
                <fieldset>
                    <div class="field-group">
                        <label for="new-server-name">Имя сервера:<span class="aui-icon icon-required"> required</span></label>
                        <g:textField class="text" name="name" id="new-server-name" title="Имя сервера" />
                    </div>
                    <div class="field-group">
                        <label for="new-server-name">Код сервера:<span class="aui-icon icon-required"> required</span></label>
                        <g:textField class="text" name="code" id="new-server-code" title="Код сервера" />
                    </div>
                </fieldset>
            </g:form>
        </div>
        <div id="import-db-panel">
            <g:uploadForm name="import-db-form" action="importdb" class="aui">
                <fieldset>
                    <div class="field-group">
                        <label for="import-db-server">Сервер:</label>
                        <g:select class="select" id="import-db-server" name="server"
                                  from="${servers}" optionKey="code" optionValue="name"/>
                    </div>
                    <div class="field-group">
                        <label>Файл базы:</label>
                        <input class="upfile" type="file" name="dbfile" />
                    </div>
                </fieldset>
            </g:uploadForm>
        </div>
    </div>
</body>
</html>