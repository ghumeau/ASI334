TODO :

A	--> DoubleLoginView	: formulaire avec code (6 chiffres) à saisir ;

A	--> SecurityView	: afficher QRCode ("urlQRcode") ;
				  afficher résultat de la modification ("resultat") ;
				  valeur de la checkbox "on" si "totpFlag" est TRUE ;

A,G	--> QuestionView	: pas d'affichage question/réponse ;

PY, G	--> Data/Security	: pas de persistance des données (VM de tests pas à jour ?) ;


En cours :
--> QuestionView
--> DataView
--> SecurityView


Non testé :
--> DoubleLoginView
--> NewPassView


Validé :
--> LoginView
--> UIDView
--> LoginView
