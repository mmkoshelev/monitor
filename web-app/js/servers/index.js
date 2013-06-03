AJS.$(document).ready(function() {
    addNewServer();
    addImportDb();
});

/**
 * Добаление диалога нового сервера
 *
 * @since 0.1
 */
function addNewServer() {
    var dialog = new AJS.Dialog({
        id: "new-server-dlg",
        width: 470,
        height: 300,
        closeOnOutsideClick: true
    });
    dialog.addHeader("Новый сервер проверки");
    dialog.addPanel("Panel 1", AJS.$("#new-server-panel"), "singlePanel");
    dialog.addButton("Добавить", function () {
        AJS.$("#new-server-form").submit();
    });
    dialog.addLink("Отменить", function (dialog) {
        dialog.hide();
    }, "#");

    AJS.$("#new-server-btn").click(function () {
        dialog.show();
    });
}

/**
 * Добавление диалога импорта файла
 *
 * @since 0.1
 */
function addImportDb() {
    var dialog = new AJS.Dialog({
        id: "import-db-dlg",
        width: 470,
        height: 300,
        closeOnOutsideClick: true
    });
    dialog.addHeader("Импорт базы и синхронизация данных");
    dialog.addPanel("Panel 1", AJS.$("#import-db-panel"), "singlePanel");
    dialog.addButton("Импорт", function () {
        AJS.$("#import-db-form").submit();
    });
    dialog.addLink("Отменить", function (dialog) {
        dialog.hide();
    }, "#");

    AJS.$("#import-db-btn").click(function () {
        if (AJS.$("#import-db-server").find("option").length == 0) {
            alert("Не зарегистрировано ни одного сервера проверки");
            return;
        }
        dialog.show();
    });
}
