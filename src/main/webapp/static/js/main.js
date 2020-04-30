$(document).ready(function(){
    let selectedCategory = sessionStorage.getItem("category");
    let selectedSupplier = sessionStorage.getItem("supplier");

    if (selectedCategory !== undefined) {
        $("#categoryProducts").find("option").each(function () {
            if ($(this).val() === selectedCategory) {
                $(this).attr("selected", true);
            }
        });
    } else if (selectedSupplier !== undefined){
        $("#supplier").find("option").each(function () {
            if ($(this).val() === selectedCategory) {
                $(this).attr("selected", true);
            }
        });
    }

    let category;

    $('#categoryProducts').change(function () {
        sessionStorage.setItem("category", $("#categoryProducts").first().val());
        if(selectedSupplier === null){
            selectedSupplier = "supplier=all";
        }
        window.history.pushState("", "", this.value + "&" + selectedSupplier)
        location.reload();
    });



    $('#supplier').change(function () {
        sessionStorage.setItem("supplier", $("#supplier").first().val());
        if(selectedCategory === null){
            selectedCategory = "/?productCategory=tablets"
        }
        window.history.pushState(null, null, selectedCategory + "&" + this.value)
        location.reload();
    });
})