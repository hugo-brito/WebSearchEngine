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
            $("#responsesize").html("<p>Your busca for <strong>" + $('#searchbox').val() + "</strong> retrieved <strong>" + data.length + "</strong> results.</p>");
            var buffer = "<ul>\n";
            $.each(data, function(index, value) {
                buffer += "<a href=\"" + value.url + "\">" + value.title + "</a><br>";
            });
            $("#urllist").html(buffer);
        });
    });
});