 $(document).ready(function() {
	 

	 var allListlabels = $( ".ui-widget-content table tbody td > label");

     var allListtd = $( ".ui-widget-content table tbody td label").parent().next();


	 for(i=0;i<=allListlabels.length;i++){

	  $(allListlabels[i]).after(allListtd[i]);

	 }


 });