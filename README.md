# MilkWay

MilkWay est une application Android simple et gratuite qui permet aux agriculteurs de commander leurs barrières verticales Lely Boetech (ou autres) depuis leur smartphone. Grâce à MilkWay, vous ne perdrez plus de temps à rechercher la télécommande de vos barrières pour diriger les vaches vers le robot de traite.

Contrairement à beaucoup d'applications existantes de commande à distance, celle-ci ne contient pas d'annonces publicitaires et ne collecte pas vos données personnelles.

Voici des captures d'écran de l'application : 

![Logo de MilkWay](images/logo.png "Logo de MilkWay") 
![Application MilkWay](images/application_2portes.jpg "Application MilkWay")
![Application MilkWay](images/application_3portes.jpg "Application MilkWay")

## Matériel nécessaire

Pour fonctionner, MilkWay nécéssite l'installation d'une carte de relais Wifi à brancher en parralèle des boutons poussoirs qui commandent les barrières. Pour le moment, l'application ne supporte que certaines cartes qui doivent ***impérativement*** contenir un ESP8266 (dans un module ESP-01) et un microcontrolleur STM8S103. Elles sont fabriquées par LC Technology (inscrit au verso de la carte) et revendues sous différentes marques comme Dollatek, Daokai ou autres. Selon le nombre de barrières à commander, vous aurez besoin d'une carte avec 2 ou 4 relais. Le prix varie de quelques euros à 25 euros environ. Vous pouvez rechercher ces cartes en tapant "relai ESP8266", "relai wifi 2 canaux", etc sur votre moteur de recherche ou sur le site du revendeur de votre choix. Parmi les plus connus, on peut citer Amazon, Ebay, AliExpress (prix bas mais délai de plusieurs semaines) mais il y en a bien d'autres.

Voici les photos des cartes actuellement supportées : 

![Image relai wifi deux cannaux](images/relai_2_contacts.png "Relai wifi 2 canaux")
![Image relai wifi quatre cannaux](images/relai_4_contacts.png "Relai wifi 4 canaux")

Ces cartes sont disponibles en alimentation 5V ou 12V selon vos préférences. Vous pouvez installer la carte dans une simple boite de dérivation étanche et l'alimenter avec une alimentation de récupération (attention à bien vérifier que les tensions à vide et sous charge soient correctes car certaines alimentations d'entrée de gamme ne sont pas régulées et délivrent une tension trop élevée à faible charge, ce qui risquerait d'endommager la carte). Une alternative un peu plus onéreuse mais plus sérieuse dans un bâtiment d'élevage potentiellement humide et poussiéreux est de faire le montage dans un coffret électrique étanche avec un rail DIN comportant un disjoncteur 2A, un bloc d'alimentation et la carte de relais wifi fixée avec des supports en plastique pour rail DIN. Voici une photo du coffret : 

![Photo d'un coffret électrique étanche cablé](images/coffret.jpg "Exemple de montage")

## Schéma électrique

Ce schéma électrique est donné à titre d'exemple, libre à vous de l'adapter selon vos besoins : 

![Schéma électrique](images/schema_electrique.png "Schéma électrique")


## Description de la carte de relais wifi

La documentation chinoise de la carte est disponible sur le site du fabricant [http://www.lctech.cc](http://www.lctech.cc). Pas très lisible pour nous français, voici une description plus parlante en anglais : 

![Description de la carte de relais wifi](images/relai_details.jpg "Description de la carte de relais wifi")

## Mise en service

### Fonctionnement en réseau wifi indépendant
TO DO 

### Connexion du relai à la box ou routeur wifi
TO DO

## Licence et non-responsabilité
MilkWay est une application open source sous licence [GPL version 3](LICENSE). Cela signifie que vous êtes libre de télécharger, utiliser et modifier le code source de l'application, sous réserve de respecter les termes de la licence.

Veuillez noter que l'application est fournie en l'état, sans garantie d'aucune sorte. Je ne suis en aucun cas responsable des dommages éventuels sur le matériel de l'utilisateur.
