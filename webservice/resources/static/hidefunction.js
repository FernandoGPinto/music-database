function hide() {
        document.getElementById("query").setAttribute("style","display:none");
        document.getElementById("query").style.display="none";
        document.getElementById("show-button").setAttribute("style","display:inline");
        document.getElementById("show-button").style.display="inline";
        document.getElementById("hide-button").setAttribute("style","display:none");
        document.getElementById("hide-button").style.display="none";
        document.getElementById("table-wrapper").setAttribute("style","max-height:400px");
        document.getElementById("table-wrapper").style.maxHeight="400px";
}
function show() {
        document.getElementById("query").setAttribute("style","display:inline");
        document.getElementById("query").style.display="inline";
        document.getElementById("show-button").setAttribute("style","display:none");
        document.getElementById("show-button").style.display="none";
        document.getElementById("hide-button").setAttribute("style","display:inline");
        document.getElementById("hide-button").style.display="inline";
        document.getElementById("table-wrapper").setAttribute("style","max-height:300px");
        document.getElementById("table-wrapper").style.maxHeight="300px";
}