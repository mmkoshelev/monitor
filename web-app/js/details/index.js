AJS.$(document).ready(function(){
    addTypeDialog();
});

/**
 * Добавление диалога сравнения ace
 *
 * @since 0.2
 */
function addTypeDialog() {
    AJS.$("a[id^='cmp-ace']").each(function() {
        var $this = AJS.$(this);
        AJS.InlineDialog($this, 1,
            function(content, trigger, showPopup) {
                var url = $this.data("url");
                var id = $this.data("id");
                AJS.$.post(url, {id: id}, function(data) {
                    content.css({"padding":"10px"}).html(data);
                }).error(function(status) {
                    content.css({"padding":"10px"}).html("Ошибка доступа к сервису: " + status.statusText);
                });
                showPopup();
                return false;
            }
        );
    });
}