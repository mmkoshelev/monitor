<%@ page import="ru.monitor.AceType" contentType="text/html;charset=UTF-8" %>
<h5>Исходные ACE</h5>
<table>
    <g:each in="${etalonFile.etalonAce}" var="ace">
        <tr>
            <td class="small-inner-table">${AceType.getByValue(ace.mode).name}</td>
            <td class="small-inner-table">${ace.value}</td>
            <td class="small-inner-table">${ace.trustee ?: "?"}</td>
        </tr>
    </g:each>
    <g:if test="${!etalonFile.etalonAce}">
        <tr>
            <td>Нет данных</td>
        </tr>
    </g:if>
</table>
<br/>
<h5>Изменения ACE</h5>
<table>
    <g:each in="${file.checkAces}" var="ace">
        <tr>
            <td>
                <g:if test="${ace.diff == 1}">
                    <span title="Атрибут добавлен" class="aui-icon aui-icon-small aui-iconfont-add">Add</span>
                </g:if>
                <g:else>
                    <span title="Атрибут удален" class="aui-icon aui-icon-small aui-iconfont-devtools-task-disabled">Delete</span>
                </g:else>
            </td>
            <td class="small-inner-table">${AceType.getByValue(ace.mode).name}</td>
            <td class="small-inner-table">${ace.value}</td>
            <td class="small-inner-table">${ace.trustee ?: "?"}</td>
        </tr>
    </g:each>
    <g:if test="${!file.checkAces}">
        <tr>
            <td>Нет изменений</td>
        </tr>
    </g:if>
</table>