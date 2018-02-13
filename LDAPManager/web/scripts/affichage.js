/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function dataLine(id,valeur){
    var contenu =  "<div class='dataLine'><label class='dataLabel' for='proposition'>"+id+"</label>\n\
        <span class='data'>"+valeur+"</span>\n\
        <input type='text' id='"+id+"' name='"+id+"' class='dataField' placeholder='"+valeur+"'></div><br />";
    document.write(contenu);

}

function securityLine(id,label){
    var contenu =  "<div class='securityLine'><label class='securityLabel' for='proposition'>"+label+"</label><br>\n\
        <input type='text' id='"+id+"' name='"+id+"' class='securityField'></div><br />";
    document.write(contenu);
}

function disableTxt() {
     document.getElementById("modifier").hidden = true; 
     document.getElementById("annuler").hidden = false; 
     var Data = document.getElementsByClassName("data");
     var DataField = document.getElementsByClassName("dataField");
     var DataLabel = document.getElementsByClassName("dataLabel");
     for(var i=0, len=Data.length; i<len; i++)
        {
            Data[i].style.display = "none";
            DataLabel[i].style.display = "none";
            DataField[i].style.display = "inline-block";
        }

}
function undisableTxt() {
     document.getElementById("modifier").hidden = false; 
     document.getElementById("annuler").hidden = true; 
     var Data = document.getElementsByClassName("data");
     var DataField = document.getElementsByClassName("dataField");
     var DataLabel = document.getElementsByClassName("dataLabel");
     for(var i=0, len=Data.length; i<len; i++)
        {
            Data[i].style.display = "inline-block";
            DataLabel[i].style.display = "inline-block";
            DataField[i].style.display = "none";
        }
}

function showSecurity(){
    document.getElementById("securityForm").style.marginLeft = "0px";
}
