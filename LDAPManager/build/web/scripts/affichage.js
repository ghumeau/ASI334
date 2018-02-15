/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* global getElementById */





function standardLine(label, id, data) {
    var contenu = "<div class='standardLine'>\n\
                    <span class='dataLabel' id='" + id + "Label'>" + label + "</span>\n\
                    <span class='dataColon' id='" + id + "Colon'>:</span>\n\
                    <span class='dataID' id='" + id + "'>" + data + "</span>\n\
                    </div>";
    document.write(contenu);
}

function dataLine(label, id, data, erreur) {
    var contenu = "<div class='dataLine'>\n\
                    <span class='dataLabel' id='" + id + "Label'>" + label + "</span>\n\
                    <span class='dataColon' id='" + id + "Colon'>:</span>\n\
                    <span class='data' id='" + id + "'>" + data + "</span>\n\
                    <input type='text' id='" + id + "Field' name='" + id + "' class='dataField' placeholder='" + data + "'/>\n\
                    <input type='button' value='Modifier' class='modify' id='" + id + "Modify' onclick='modify(\"" + id + "\")'/>\n\
                    <input type='submit' value='Valider' class='validate' id='" + id + "Validate' onclick='validate(\"" + id + "\")'/>\n\
                    <input type='button' value='Annuler' class='cancel' id='" + id + "Cancel' onclick='cancel(\"" + id + "\")'/>\n\
                    <span class='error'>" + erreur + "</span>\n\
                    </div>";
    document.write(contenu);
}

function securityLine(type, label, id, erreur) {
    var contenu = "<div class='securityLine'>\n\
                    <span class='securityLabel' for='proposition'>" + label + "</span>\n\
                    <span class='securitColon' id='" + id + "Colon'>:</span>\n\
                    <input type='" + type + "' id='" + id + "Field' name='" + id + "' class='securityField'>\n\
                    <span class='error'>" + erreur + "</span>\n\
                    </div>";
    document.write(contenu);
}

function modify(id) {
    document.getElementById(id).style.display = "none";
    document.getElementById(id + "Field").style.display = "inline-block";
    document.getElementById(id + "Validate").style.display = "inline-block";
    document.getElementById(id + "Cancel").style.display = "inline-block";

    document.getElementById("modifier").hidden = true;

    var Modify = document.getElementsByClassName("modify");
    for (var i = 0, len = Modify.length; i < len; i++)
    {
        Modify[i].style.display = "none";
    }
}

function cancel(id) {
    document.getElementById(id).style.display = "inline-block";
    document.getElementById(id + "Field").style.display = "none";
    document.getElementById(id + "Validate").style.display = "none";
    document.getElementById(id + "Cancel").style.display = "none";

    document.getElementById("modifier").hidden = false;

    var Modify = document.getElementsByClassName("modify");
    for (var i = 0, len = Modify.length; i < len; i++)
    {
        Modify[i].style.display = "inline-block";
    }
}

function validate(id) {
    cancel(id);
    if (confirm("Confirmez vous ce changement ?")) {
        alert("oui");
    } else {
        alert("non");
    }
}

function allModify() {
    document.getElementById("modifier").hidden = true;
    document.getElementById("annuler").hidden = false;
    document.getElementById("valider").style.display = "inline-block";
    var Data = document.getElementsByClassName("data");
    var DataField = document.getElementsByClassName("dataField");
    var Modify = document.getElementsByClassName("modify");
    var Cancel = document.getElementsByClassName("cancel");
    var Validate = document.getElementsByClassName("validate");
    for (var i = 0, len = Data.length; i < len; i++)
    {
        Data[i].style.display = "none";
        DataField[i].style.display = "inline-block";
        Modify[i].style.display = "none";
        Cancel[i].style.display = "none";
        Validate[i].style.display = "none";
    }

}
function allCancel() {
    document.getElementById("modifier").hidden = false;
    document.getElementById("annuler").hidden = true;
    document.getElementById("valider").style.display = "none";
    var Data = document.getElementsByClassName("data");
    var DataField = document.getElementsByClassName("dataField");
    var Modify = document.getElementsByClassName("modify");
    for (var i = 0, len = Data.length; i < len; i++)
    {
        Data[i].style.display = "inline-block";

        DataField[i].style.display = "none";
        Modify[i].style.display = "inline-block";
    }
}

