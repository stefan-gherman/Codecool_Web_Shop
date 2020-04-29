
$(document).ready(() => {
   console.log("JQuery Loaded");

  const $childreOfSubmitDivs = $('.inner_form').children();

  let i=0;
  for(i=0; i<$childreOfSubmitDivs.length; i++) {

      if($childreOfSubmitDivs.id.search(""))
      $childreOfSubmitDivs[i].on("click", () => {

      })
  }

});