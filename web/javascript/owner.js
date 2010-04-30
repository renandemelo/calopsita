$(document).ready(function() {
	var newDiv = document.createElement("div");
	$("#main").append('<div id="dialog-confirm" title="Deseja ser o respons&aacute;vel deste cart&atilde;o?" style="display:none;">'+
						'	<p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Você já é responsável por outros cartões ainda não finalizados. Deseja ser responsável por este cartão mesmo assim?</p>' +
						'</div>');
});

function confirmation(project_id,iteration_id,card_id) {
	$.ajax( {
        type : 'POST',
        url : '/calopsita/projects/' + project_id + '/iterations/' + iteration_id + '/card/' + card_id + '/cardOwner/',
        data : {'card.id': card_id,
				'iteration.id': iteration_id,
				'project_id': project_id},
		dataType: 'json',
        success: function(data) {
			if (data.boolean){
	        	$("#dialog-confirm").dialog({
	        		resizable: false,
	        		height:230,
	        		width:400,
	        		modal: true,
	        		buttons: {
	        			Ok: function() {
	        				location.href = '/calopsita/projects/' + project_id + '/iterations/' + iteration_id + '/card/' + card_id + '/cardOwner/';
	        			},
	        			Cancel: function() {
	        				$(this).dialog('close');
	        			}
	        		}
	        	});
			}
			else {
				location.href = '/calopsita/projects/' + project_id + '/iterations/' + iteration_id + '/card/' + card_id + '/cardOwner/';
			}
        },
        error : function() {
            showDialog("Error", "An error occurred");
        }
    });
}	  