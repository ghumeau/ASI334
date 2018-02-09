/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function ligne(id,valeur){
    var contenu =  "<div class='ligne'><label for='proposition'>"+id+"</label>\n\
        <span>:  </span><span class='data'>"+valeur+"</span>\n\
        <input type='text' id='"+id+"' name='"+id+"' class='dataForm' placeholder='"+valeur+"'></div><br />";
    document.write(contenu);

}


function disableTxt() {
     document.getElementById("modifier").hidden = true; 
     document.getElementById("annuler").hidden = false; 
     var Data = document.getElementsByClassName("data");
     var DataForm = document.getElementsByClassName("dataForm");
     for(var i=0, len=Data.length; i<len; i++)
        {
            Data[i].style.display = "none";
            DataForm[i].style.display = "inline-block";
        }

}
function undisableTxt() {
     document.getElementById("modifier").hidden = false; 
     document.getElementById("annuler").hidden = true; 
     var Data = document.getElementsByClassName("data");
     var DataForm = document.getElementsByClassName("dataForm");
     for(var i=0, len=Data.length; i<len; i++)
        {
            Data[i].style.display = "inline-block";
            DataForm[i].style.display = "none";
        }
}




