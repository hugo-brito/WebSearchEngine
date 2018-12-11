// JavaScript Document

$(document).ready(function() {
    var baseUrl = "http://localhost:8080";

    $("#searchbutton").click(function() {
        console.log("Sending request to server.");
        $.ajax({
            method: "GET",
            url: baseUrl + "/search",
            data: {query: $('#searchbox').val()}
        }).success( function (data) {
            console.log("Received response " + data);

            if ($("#searchbox").val() === "") { // empty query
                $("#responseSize").html("<h4>Please type something to busca for.</h4>");}

            else {

                if (data.length === 0) {
                    $("#responseSize").html("<h4>Your busca for <strong>" + $('#searchbox').val() + "</strong> did not retrieve any results.</h4>"); // no results
                } else {
                    $("#responseSize").html("<h4>Your busca for <strong>" + $('#searchbox').val() + "</strong> retrieved <strong>" + data.length + "</strong> results.</h4>");
                }

                var buffer = "";

//			if data.length>10
                $.each(data, function (index, value) {
                    buffer += "<div class='result'><a href=\'" + value.url + "'><h1>" + value.title + "</h1><h2>" + value.url + "</h2><h3>";
                    var element = 0;
                    var limit = 15;
                    $.each(value.words, function (index, val) {
                        if (element <= limit) {
                            buffer += val + " ";
                            element++;
                        }

                    });
                    buffer += "...</h3></a></div>";
                });
                $("#resultWrapper").html(buffer);
            }
        });
    });
// 	$(document).keypress(function(e) {
//     if(e.which == 13) {
//         document.getElementById("searchbox").addEventListener("change", myFunction);
//     function() {
//         document.getElementById("searchbutton").click();
//     }
//        $("#searchbutton").click(function() {
//        console.log("Sending request to server.");
//        $.ajax({
//            method: "GET",
//            url: baseUrl + "/search",
//            data: {query: $('#searchbox').val()}
//        }).success( function (data) {
//            console.log("Received response " + data);
//
//            $("#responsesize").html("<p>Your busca for <strong>" + $('#searchbox').val() + "</strong> retrieved <strong>" + data.length + "</strong> results.</p>");
//            var buffer = "";
//
////			if data.length>10
//            $.each(data, function(index, value) {
//                buffer += "<p><a href=\"" + value.url + "\">" + value.title + "</a></p>";
//            });
//            $("#urllist").html(buffer);
//        });
//    });
// 		alert('You pressed enter!');
//
//     }
// });
});