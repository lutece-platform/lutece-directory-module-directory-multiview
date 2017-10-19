$(function(){ $("#info_user").popover(
    { 
      html: true, 
      animation:false,
      content: function() {
    		return $("#template-user-infos").html();
  		}
    }
  )
});

