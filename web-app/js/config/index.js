AJS.$(document).ready(function(){
    addTypeDialog();
});

/**
 * Добавление диалога отображения типов проверки
 *
 * @since 0.2
 */
function addTypeDialog() {
    AJS.$(".shw-type-btn").each(function() {
        var $this = AJS.$(this);
        AJS.InlineDialog($this, 1,
            function(content, trigger, showPopup) {
                content.css({"padding":"20px"}).html($this.data("text"));
                showPopup();
                return false;
            }
        );
    });


}