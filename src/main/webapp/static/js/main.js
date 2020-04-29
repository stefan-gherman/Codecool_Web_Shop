
// set current category in localstorage
if (localStorage.getItem("#categoryProducts")) {
    $("#categoryProducts").val(localStorage.getItem("#categoryProducts"));
}

function stopRefreshOption() {

    // change current link when category was pressed
    $(document).ready(function () {
        $("#categoryProducts").change(function () {
            localStorage.setItem("categoryProducts", $("#categoryProducts").val());
            if (this.value === "electronic-learning-toys") {
                window.history.pushState("a", "a", "/?productCategory=electronic-learning-toys")
                location.reload();
            }
            if (this.value === "tablet") {
                window.history.pushState("a", "a", "/?productCategory=tablet")
                location.reload();
            }
        });
    });
}

stopRefreshOption();
