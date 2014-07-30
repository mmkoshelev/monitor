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
                        <span class="aui-icon aui-icon-small aui-iconfont-devtools-clone">import</span>
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
                        <th>№</th>
                        <g:sortableColumn property="name" title="Наименование" />
                        <g:sortableColumn property="code" title="Код компонента" />
                        <th>Дата последней проверки</th>
                        <th>&nbsp;</th>
                    </tr>
                    </thead>
                    <tbody>
                        <g:each in="${servers}" status="status" var="server">
                            <tr>
                                <td>${status + 1}</td>
                                <td>${server.name}</td>
                                <td>${server.code}</td>
                                <td>
                                    <span class="aui-lozenge aui-lozenge-${serverStatuses[server.code] ? 'success' : 'error'}">
                                        ${lastChecks[server.code]?.format("dd.MM.yyyy HH:mm:ss") ?: "Не было проверки"}
                                    </span>
                                </td>
                                <td class="right">
                                    <g:form action="delete" method="post" class="noblock margin-right5">
                                        <g:hiddenField name="id" value="${server.id}" />
                                        <button type="submit" class="aui-button aui-button-link" title="Удалить"
                                                onclick="return confirm('Удалить сервер и все данные?');">
                                            <span class="aui-icon aui-icon-small aui-iconfont-remove">remove</span>
                                        </button>
                                    </g:form>
                                    <g:link controller="config" id="${server.code}" class="aui-button aui-button-link margin-right5" title="Конфигурация">
                                        <span class="aui-icon aui-icon-small aui-iconfont-configure"></span></g:link>
                                    <g:link controller="dirs" id="${server.code}" class="aui-button aui-button-link margin-right5" title="Контрольная информация">
                                        <span class="aui-icon aui-icon-small aui-iconfont-devtools-folder-open"></span></g:link>
                                    <g:link controller="details" id="${server.code}" class="aui-button aui-button-link margin-right5" title="Просмотр">
                                        <span class="aui-icon aui-icon-small aui-iconfont-share"></span></g:link>
                                </td>
                            </tr>
                        </g:each>
                        <g:if test="${!servers}">
                            <tr>
                                <td colspan="5">Нет данных</td>
                            </tr>
                        </g:if>
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