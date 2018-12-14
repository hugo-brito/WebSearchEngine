// JavaScript Document

var baseUrl = "http://localhost:8080";

$(document).ready(function() {
    // var baseUrl = "http://localhost:8080";

    $("#searchbutton").click(function () {
        console.log("Sending request to server.");
        $.ajax({
            method: "GET",
            url: baseUrl + "/search",
            data: {query: $('#searchbox').val()}
        }).success(function (data) {
            console.log("Received response " + data);

            if ($("#searchbox").val() === "") { // empty query
                $("#responseSize").html("<h4>Please type something to busca for.</h4>");
                
            } else {
                if (data.length === 0) {
                    $("#responseSize").html("<h4>Your busca for <strong>" + $('#searchbox').val() + "</strong> did not retrieve any results.</h4>"); // no results
                } else {
                    $("#responseSize").html("<h4>Your busca for <strong>" + $('#searchbox').val() + "</strong> retrieved <strong>" + data.length + "</strong> results.</h4>");
                }

                var buffer = "";

//			if data.length>10
                $.each(data, function (index, value) {
                    buffer += "<div class='result'><a href=\'" + value.url + "' target=\"_blank\"><h1>" + value.title + "</h1><h2>" + value.url + "</h2><h3>";
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
});

 $(function() {
    // var baseUrl = "http://localhost:8080";
    var wordArray;
        $.ajax({
            method: "GET",
            url: baseUrl + "/words",
            success: function (data) {
                wordArray = data;
                wordArray.sort();
                $("#searchbox").autocomplete({
                    source: function(request, response) {
                        //var results = $.ui.autocomplete.filter(wordArray, request.term);
                        var term = $.ui.autocomplete.escapeRegex(request.term)
                            , startsWithMatcher = new RegExp("^" + term, "i")
                            , startsWith = $.grep(wordArray, function(value) {
                            return startsWithMatcher.test(value.label || value.value || value);
                        })
                            , containsMatcher = new RegExp(term, "i")
                            , contains = $.grep(wordArray, function (value) {
                            return $.inArray(value, startsWith) < 0 &&
                                containsMatcher.test(value.label || value.value || value);
                        });
                        response(startsWith.concat(contains).slice(0, 8));
                    },
                    minLength: 3,
                    width: 200
                })
            }
        })
     // $.ui.autocomplete.css('width', $('#searchbox').width());
 });