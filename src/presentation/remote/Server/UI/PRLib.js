/**
 * Presentation Remote UI Library
 * https://github.com/amiraslanaslani/presentation-remote
 * @author Amir Aslan Aslani
 */

var uriPrefix = "data:image/gif;base64, ";

function sendKey(keyCode)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", keyCode, false );
    xmlHttp.send( null );
    return xmlHttp.responseText;
}

function setImageAsScreen(id){
    var screenImage = document.getElementById(id);
    setInterval(function () {
        screenImage.src = uriPrefix + sendKey("/screen/base64");
    }, 200);
}
