AJS.toInit(function() {
  var baseUrl = AJS.$("meta[name='application-base-url']").attr("content");
     
  function populateForm() {
    AJS.$.ajax({
      url: baseUrl + "/rest/openairintegrationplugin-admin/1.0/",
      dataType: "json",
      success: function(config) {
        AJS.$("#url").attr("value", config.url);
        AJS.$("#companyId").attr("value", config.companyId);
        AJS.$("#userId").attr("value", config.userId);
        AJS.$("#password").attr("value", config.password);
      }
    });
  }
  
  function updateConfig() {
	  AJS.$.ajax({
	    url: baseUrl + "/rest/openairintegrationplugin-admin/1.0/",
	    type: "PUT",
	    contentType: "application/json",
	   
	    data: '{  "url": "' + AJS.$("#url").attr("value") + '", "companyId": "' + AJS.$("#companyId").attr("value") + '", "userId": "' +  AJS.$("#userId").attr("value") + '", "password": "' + AJS.$("#password").attr("value") + '" }',
	    processData: false
	  });
	}
     
  populateForm();
  
  AJS.$("#admin").submit(function(e) {
	    e.preventDefault();
	    updateConfig();
	});
});