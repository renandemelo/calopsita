var cardsUrl;
function initialize(url) {
    cardsUrl = url;
}

function timeline(daysBetweenTodayAndStartDate, daysBetweenEndDateAndToday,
        daysBetweenEndDateAndStartDate) {
    $('#start_title').html('Start');
    $('#end_title').html('End');
    $('#today_start_today').html('Today');
    if (daysBetweenTodayAndStartDate == 0) {
        $('#start').hide();
        $('#start_today_line').hide();
        $('#today_start_title').html('Start');
    }
    if (daysBetweenEndDateAndToday == 0) {
        $('#end').hide();
        $('#start_end_line').hide();
        $('#today_start_title').html('End');
    }
    if (daysBetweenTodayAndStartDate == 0 && daysBetweenEndDateAndToday == 0) {
        $('#today_start_title').html('Start / End');
    }
    if (!(daysBetweenEndDateAndStartDate > 0)) {
        $('#start_today_line').css( {
            'width' : 300
        });
        $('#start_end_line').css( {
            'width' : 300
        });

        if (isNaN(daysBetweenEndDateAndToday)) {
            $('#end_date').html('?');
        }
        if (isNaN(daysBetweenTodayAndStartDate)) {
            $('#start_date').html('?');
        }
    }
    if (daysBetweenTodayAndStartDate > 0 && !(daysBetweenEndDateAndToday < 0)) {
        if (!isNaN(daysBetweenEndDateAndToday)) {
            $('#start_today_line')
                    .css(
                            {
                                'width' : 600 * ((daysBetweenTodayAndStartDate - 1) / daysBetweenEndDateAndStartDate)
                            });
            $('#start_end_line')
                    .css(
                            {
                                'width' : 600 * ((daysBetweenEndDateAndToday - 1) / daysBetweenEndDateAndStartDate)
                            });
        }
    }
    if (!(daysBetweenTodayAndStartDate > 0)
            && !(daysBetweenEndDateAndToday < 0)) {
        $('#start').css( {
            'float' : 'right'
        });
        $('#end').css( {
            'float' : 'right'
        });
        $('#start_today_line').css( {
            'float' : 'right'
        });
        $('#start_today_line').css( {
            'border-width' : 0
        });
        if (!(isNaN(daysBetweenEndDateAndToday) && isNaN(daysBetweenTodayAndStartDate))) {
            $('#start_today_line')
                    .css(
                            {
                                'width' : -600
                                        * ((daysBetweenTodayAndStartDate + 1) / daysBetweenEndDateAndToday)
                            });
            $('#start_end_line')
                    .css(
                            {
                                'width' : 600 * ((daysBetweenEndDateAndToday - 1) / daysBetweenEndDateAndToday)
                            });
        }
    }
    if (daysBetweenEndDateAndToday < 0) {
        $('#start_today_line').hide();
        $('#today_start').hide();
        $('#today_end').show();
        $('#today_end_line').show();
        $('#start_end_line')
                .css(
                        {
                            'width' : 600 * ((daysBetweenEndDateAndStartDate - 1) / daysBetweenTodayAndStartDate)
                        });
        $('#today_end_line')
                .css(
                        {
                            'width' : -600
                                    * ((daysBetweenEndDateAndToday + 1) / daysBetweenTodayAndStartDate)
                        });
    }
}

function prepare() {
    $('.selectable').selectable( {
        filter : 'li'
    });

    $(".selectable li").selectableAndDraggable();

    $('#todo_cards, #iteration_cards').droppable( {
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
    function fixWidth() {
        var width = $('body').width();
        $('#todo_cards, #iteration_cards').css( {
            width : 0.48 * width,
            'float' : 'left'
        });
        $('#done_cards, #backlog').css( {
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
    $(div + ' .ui-selected').not('.clone').each(function(c, e) {
        params['cards[' + c + '].id'] = $('span', e).text();
        params['cards[' + c + '].status'] = status;
    });
    return params;
}
function modifyCards(div, status, method) {
    var params = get_params(div, status);
    params['_method'] = method;
    $.ajax( {
        type : 'POST',
        url : cardsUrl,
        data : params,
        success : function(data) {
            $('#iteration_cards ol')
                    .html($('#iteration_cards ol', data).html());
            $('#todo_cards ol').html($('#todo_cards ol', data).html());
            $('#done_cards ol').html($('#done_cards ol', data).html());
            $('#backlog ol').html($('#backlog ol', data).html());
            prepare();
        }
    });
}
function todo_cards() {
    modifyCards('.selectable', 'TODO', 'POST');
}
function done_cards() {
    modifyCards('.selectable', 'DONE', 'POST');
}
function remove_cards() {
    modifyCards('.cards', 'TODO', 'DELETE');
}