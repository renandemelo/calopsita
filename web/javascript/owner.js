function confirmAssignCard(url) {
    var msg = {};
    msg['assign'] = {
        html : 'You already own a card. Would you like to own this card anyway?',
        buttons : { 'Yes' : true, 'No' : false },
        submit : function(confirm) {
            if (confirm) {
				window.location.replace(url);			            
            }
        }
    };
    $.prompt(msg);
}

function assign(url, isOwner) {
	if (isOwner){
		confirmAssignCard(url);
	}
	else {
		window.location.replace(url);
	}
}  