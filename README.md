# MilkWay

![Logo de MilkWay](images/logo.png "Logo de MilkWay") 

MilkWay est une application Android simple et gratuite qui permet aux agriculteurs de commander leurs barrières verticales Lely Boetech (ou autres) depuis leur smartphone. Grâce à MilkWay, vous ne perdrez plus de temps à rechercher la télécommande de vos barrières pour diriger les vaches vers le robot de traite.

Contrairement à beaucoup d'applications existantes de commande à distance, cette application ne contient pas d'annonces publicitaires et ne collecte pas vos données personnelles.

![Application MilkWay](images/application_2portes.jpg "Application MilkWay")

## Matériel nécessaire

Pour fonctionner, cette application nécéssite l'installation d'une carte de relais Wifi à brancher en parralèle des boutons poussoirs qui commandent les barrières. Cette carte est disponible sur des sites tels que Amazon, Ebay, Aliexpress (prix bas mais délai de plusieurs semaines) et bien d'autres. Elle est fabriquée par LC Technology et revendue sous différentes marques comme Dollatek, Daokai. Selon le nombre de barrières à commander, vous aurez besoin d'une carte avec 2 ou 4 relais (courrament appelés relai wifi 2 ou 4 canaux sur les sites marchands). Le prix varie de quelques euros à 25 euros environ. Vous pouvez rechercher ces cartes en tapant "relai ESP8266", "relai wifi 2 canaux", etc sur votre moteur de recherche ou sur le site du revendeur de votre choix. 

Pour l'instant, l'application MilkWay ne fonctionne qu'avec certaines cartes qui doivent absolument contenir un ESP8266 (dans un module ESP-01) et un microcontrolleur STM8S103. Voici les photos des cartes actuellement supportées : 

![Image relai wifi deux cannaux](images/relai_2_contacts.png "Relai wifi 2 canaux")
![Image relai wifi quatre cannaux](images/relai_4_contacts.png "Relai wifi 4 canaux")

Ces cartes sont disponibles en alimentation 5V ou 12V selon vos préférences. Vous pouvez installer la carte dans une simple boite de dérivation étanche et l'alimenter avec une alimentation de récupération (attention à bien vérifier que les tensions à vide et sous charge soient correctes car certaines alimentations d'entrée de gamme ne sont pas régulées et délivrent une tension trop élevée à faible charge, ce qui risquerait d'endommager la carte). Personnellement, j'ai installé le tout dans petit coffret électrique étanche avec un rail DIN comportant un disjoncteur 2A, un bloc d'alimentation et la carte de relais wifi fixée avec des supports en plastique pour rail DIN. Voici une photo du coffret : 

![Photo d'un coffret électrique étanche cablé](images/coffret.jpg "Exemple de montage")

## Schéma électrique

![Schéma électrique](images/schema_electrique.png "Schéma électrique")


## Description de la carte de relais wifi

![Description de la carte de relais wifi](images/relai_details.jpg "Description de la carte de relais wifi")

## Mise en service

### Fonctionnement en réseau wifi indépendant
TO DO 

### Connexion du relai à la box ou routeur wifi
TO DO

## Licence et non-responsabilité
MilkWay est une application open source sous licence [GPL version 3](LICENSE). Cela signifie que vous êtes libre de télécharger, utiliser et modifier le code source de l'application, sous réserve de respecter les termes de la licence.

Veuillez noter que l'application est fournie en l'état, sans garantie d'aucune sorte. Je ne suis en aucun cas responsable des dommages éventuels sur le matériel de l'utilisateur.
