$(document).ready(() => {
    console.log("JQuery Loaded");

    console.log($("form > .inner_form > input[name='quantity']"));
    const $childrenOfSubmitDivs = $('.inner_form').children();
    const $holderDiv = $('.inner_form');


    let i = 0;
    let j = 0;
    let buttonsArray = [];
    for (i = 0; i < $childrenOfSubmitDivs.length; i++) {
        if ($childrenOfSubmitDivs[i].id.search("buttonSubmit") === 0) {
            buttonsArray[j] = $childrenOfSubmitDivs[i];
            j++;
        }
    }
    console.log(buttonsArray);
    for (i = 0; i < $childrenOfSubmitDivs.length; i++) {

        if ($childrenOfSubmitDivs[i].id.search("inputSubmit") === 0) {

            const buttonId = $childrenOfSubmitDivs[i].id.replace("inputSubmit", "");


            $("#inputSubmit" + buttonId).on("focus", () => {
                $("#buttonSubmit" + buttonId).show();
                console.log($("#buttonSubmit" + buttonId).attr("id"));
                for (let j = 0; j < buttonsArray.length; j++) {
                    if (buttonsArray[j].id !== $("#buttonSubmit" + buttonId).attr("id")) {
                        console.log($("#buttonSubmit" + buttonId));
                        console.log(`Button from array ${buttonsArray[j].id} - Button from page${$("#buttonSubmit" + buttonId).attr("id")}`);
                        buttonsArray[j].setAttribute("style", "display: none");
                    }
                    $(document).on("click")
                }

            });


        }

    }

});