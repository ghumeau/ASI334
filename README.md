# ASI334

- On souhaite développer une application de type « self service » pour la gestion des comptes utilisateurs d’un annuaire LDAP

- L’accès à cette application sera protégé par un formulaire d’authentification. Si l’utilisateur a oublié son mot de passe, il pourra le réinitialiser en répondant à sa question secrète.

- Une fois authentifié, un utilisateur pourra :
    * Visualiser et mettre à jour ses informations d’identité (nom, prénom, adresse mail)
    * Changer son mot de passe, sa question secrète et la réponse associée
    * Activer ou désactiver l’authentification en 2 étapes avec Google Authenticator. Il faudra bien entendu prévoir le cas d’une perte du téléphone sur lequel est installé Google Authenticator.
