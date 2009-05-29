var updateCardsUrl;
var removeCardsUrl;
function initialize(update, remove) {
    updateCardsUrl = update;
    removeCardsUrl = remove;
}

function prepare() {
    $('.selectable').selectable( {
        filter : 'li'
    });

    $(".selectable li").selectableAndDraggable();

    $('#todo_cards').droppable( {
        accept : '.card',
        tolerance : 'pointer',
        drop : todo_cards
    });
    $('#done_cards').droppable( {
        accept : '.card',
        tolerance : 'pointer',
        drop : done_cards
    });
    $('#backlog').droppable( {
        accept : '.card',
        tolerance : 'pointer',
        drop : remove_cards
    });
    $('.dialog').dialog( {
        autoOpen : false,
        bgiframe : true,
        modal : true,
        width : '500px',
        show : 'highlight',
        hide : 'highlight'
    });

    function fixWidth() {
        var width = $('body').width();
        $('#todo_cards').css( {
            width : 0.48 * width,
            'float' : 'left'
        });
        $('#done_cards').css( {
            width : 0.48 * width,
            'float' : 'right'
        });
    }
    fixWidth();
    $(window).resize(fixWidth);
};
$(prepare);
function get_params(div, status) {
    var params = {};
    $(div + ' .ui-selected').not('.clone').each( function(c, e) {
        params['cards[' + c + '].id'] = $('span', e).text();
        params['cards[' + c + '].status'] = status;
    });
    return params;
}
function modifyCards(div, status, logic) {
    var params = get_params(div, status);

    $.ajax( {
        url : logic,
        data : params,
        success : function(data) {
            $('#todo_cards ol').html($('#todo_cards ol', data).html());
            $('#done_cards ol').html($('#done_cards ol', data).html());
            $('#backlog ol').html($('#backlog ol', data).html());
            prepare();
        }
    });
}
function todo_cards() {
    modifyCards('.selectable', 'TODO', updateCardsUrl);
}
function done_cards() {
    modifyCards('.selectable', 'DONE', updateCardsUrl);
}
function remove_cards() {
    modifyCards('.cards', 'TODO', removeCardsUrl);
}
function show_help() {
    $('#help').dialog('open');
    return false;
}
function open_dialog(id) {
    $('#dialog_' + id).dialog('open');
}