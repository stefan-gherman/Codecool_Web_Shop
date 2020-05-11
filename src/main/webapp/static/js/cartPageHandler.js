$(document).ready(() => {
    console.log("JQuery Loaded");

    //console.log($("form > .inner_form > input[name='quantity']"));
    $("form > .inner_form > input[name='quantity']").on("focus", (event) => {
        $("form > .inner_form > input[type='submit']").hide();
        let submitButton = $(event.target).parent().find(".btn-primary");
        submitButton.on("click", (event) => {
            event.stopPropagation();
        })
        submitButton.show();
    });
    $(document).on("click", (event) => {
        if ($(event.target).closest('.inner_form').length === 0) {
            $("form > .inner_form > input[type='submit']").hide();
        }
    });
});