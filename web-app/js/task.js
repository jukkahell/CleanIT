/**
 * Created by IntelliJ IDEA.
 * User: Hell
 * Date: 16.9.2011
 * Time: 0:53
 * To change this template use File | Settings | File Templates.
 */

$(document).ready(function() {
    $(".dayOfMonth").click(function() {
        $(this).toggleClass("activeBox");
        var val = $(this).attr("id").split("-")[1];
        if ($(this).hasClass("activeBox")) {
            $(this).append("<input type='hidden' name='dayOfMonth' value='"+val+"' />");
        } else {
            $(this).html(val);
            $("#dayOfMonth-"+val).remove();
        }
    });

    $(".dayOfWeek").click(function() {
        $(this).toggleClass("activeBox");
        var val = $(this).attr("id").split("-")[1];
        if ($(this).hasClass("activeBox")) {
            $(this).append("<input type='hidden' name='dayOfWeek' value='"+val+"' />");
        } else {
            $(this).html(val);
            $("#dayOfWeek-"+val).remove();
        }
    });

    $(".dayOfYear").click(function() {
        $(this).toggleClass("activeBox");
        var val = $(this).attr("id").split("-")[1];
        if ($(this).hasClass("activeBox")) {
            $(this).append("<input type='hidden' name='dayOfYear' value='"+val+"' />");
        } else {
            $(this).html(val);
            $("#dayOfYear-"+val).remove();
        }
    });

    $(".monthLeft").click(function() {
        if (!$(this).hasClass("disabled")) {
            $("#dayOfYearContainer").animate(
                {
                    left: '+=155'
                }, 300, function() {
                }
            );
        }
    });

    $(".monthRight").click(function() {
        if (!$(this).hasClass("disabled")) {
            $("#dayOfYearContainer").animate(
                {
                    left: '-=155'
                }, 300, function() {
                }
            );
        }
    });

    $(".taskDone").click(function() {
        $(".familyUsers").hide();
        var familyUsers = $(this).find(".familyUsers");
        familyUsers.slideToggle("fast");
    });

    $(".userSelection").click(function() {
        var taskId = $(this).attr("id").split("-")[1];
        var userId = $(this).attr("id").split("-")[2];
        var elementExists = $("#selectedUser-"+taskId+"-"+userId);
        
        if (elementExists.length != 0) {
            elementExists.remove();
            $(this).removeClass("selectedUser")
        } else {
            $(this).addClass("selectedUser");
            $("#taskForm-"+taskId).append("<input id='selectedUser-"+taskId+"-"+userId+"' type='hidden' name='userIds' value='"+userId+"' />");
        }
        return false;
    });

    $(".markTaskDone").click(function() {
        var taskId = $(this).attr("id").split("-")[1];
        $("#taskForm-"+taskId).submit();
        console.log($("#taskForm-"+taskId));
        return false;
    });

    $(document).click(function(evt) {
        if (evt.target.className != "doneBtn" && !$(evt.target).hasClass("userSelection")) {
            $(".familyUsers").hide();
        }
    });

    $( "#dailyTaskList" ).accordion({
        header: 'li.accordable',
        clearStyle: false,
        active: false,
        collapsible: true,
        autoHeight: true
    });
});