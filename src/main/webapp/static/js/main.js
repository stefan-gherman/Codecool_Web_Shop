

// set current category in localstorage
if (localStorage.getItem("#categoryProducts")) {
    $("#categoryProducts").val(localStorage.getItem("#categoryProducts"));
}

function stopRefreshOption() {

    // change current link when category was pressed
    $(document).ready(function () {
        $("#categoryProducts").change(function () {
            localStorage.setItem("categoryProducts", $("#categoryProducts").val());
            if (this.value === "tablets") {
                window.history.pushState(null, null, "/?productCategory=tablets")
                location.reload();
            } else if (this.value === "living-room-furniture") {
                window.history.pushState(null, null, "/?productCategory=living-room-furniture")
                location.reload();
            } else if(this.value === "cameras"){
                window.history.pushState(null, null, "/?productCategory=cameras")
                location.reload();
            } else if(this.value === "monitors") {
                window.history.pushState(null, null, "/?productCategory=monitors")
                location.reload();
            }
        });
    });
}

stopRefreshOption();
