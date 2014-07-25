<%@ page import="ru.monitor.PattType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Конфигурация сервера проверки</title>
    <meta name="layout" content="auimain" />
    <script src="${resource(dir: 'js/config', file: 'index.js')}"></script>
</head>
<body>
    <div class="aui-page-header">
        <div class="aui-page-header-inner">
            <div class="aui-page-header-main">
                <h2>Конфигурация сервера проверки [${params.id}]</h2>
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
                            <th>Группа</th>
                            <th style="width: 600px">Директории</th>
                            <th>Правила проверки</th>
                        </tr>
                    </thead>
                    <tbody>
                       <g:each in="${groups}" var="group" status="status">
                          <tr>
                              <td>${status + 1}</td>
                              <td>${group.name}</td>
                              <g:if test="${group.groups.size() > 0}">
                                  <td colspan="2">
                                      Включает элементы групп:
                                      <g:join in="${group.groups.name}" delimiter=", " />
                                  </td>
                              </g:if>
                              <g:else>
                                  <td colspan="2">
                                      <table>
                                          <g:each in="${group.monitorItems}" var="item">
                                              <tr>
                                                  <td style="width: 620px;">${item.path}</td>
                                                  <td>
                                                      <g:each in="${item.patts}" var="patt">
                                                          <a href="javascript:return false;" class="shw-type-btn"
                                                             data-text='<ul style="list-style-type: circle;"><li>${PattType.getByValueSet(patt.flags).name.join("</li><li>")}</li></ul>'>
                                                              <span class="aui-icon aui-icon-small aui-iconfont-share">show</span> ${patt.pattern}</a>
                                                          <br/>
                                                      </g:each>
                                                  </td>
                                              </tr>
                                          </g:each>
                                      </table>
                                  </td>
                              </g:else>
                          </tr>
                       </g:each>
                       <g:if test="${!groups}">
                           <tr>
                               <td colspan="4">Нет данных</td>
                           </tr>
                       </g:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>