# **PETVET**

## **Contexte**

Le projet PETVET est un système qui a pour but de suivre l'état de santé des animaux de compagnie au sein d'une famille.

Dans son mode de fonctionnement minimal, le système doit être composé de :

- au moins 1 smartphone


Pour un fonctionnement optimal, le système doit être composé de :

- au moins 3 microcontroleur (Arduino, ESP32, Raspberry, etc...)
- au moins 1 smartphone
- au moins 1 puce pour un chat détectable via onde Wifi/Bluetooth

Plus il y aura de microcontroleurs en réseau, plus la précision de géolocalisation des animaux sera précise.

PETVET est un système composé des logiciels suivants:

- app: Application Mobile (android)
- petvet-cluster-simulator
- petvet-cluster-monitor

## Présentation des Projets

### App

Il s'agit d'une application mobile pour la plateforme Android.
Au travers de l'application, il est possible de :
- gérer le parc animal
- suivre l'état de santé des animaux
- gérer une pharmacie de plantes médicinales
- gérer les contacts d'urgences
- géolocaliser les animaux

*Lien du projet: [app](./app/)*

### Petvet-Cluster-Simulator

Il s'agit d'une application qui permet de simuler les microcontroleurs.

Un microcontroleur peut:
- lancer un scan pour detecter les animaux
- diffuser le rapport de scan

*Lien du projet: [petvet-cluster-simulator](./petvet-cluster-simulator/)*

### Petvet-Cluster-Monitor

Il s'agit d'une application qui permet de surveiller le bon fonctionnement des microcontroleur.

Le moniteur peut:

- afficher les messages diffusés au sein du cluster de microcontroleur
- lister les microcontroleurs actifs
- inspecter les rapports de scans diffusés par les microcontroleurs.


*Lien du projet: [petvet-cluster-monitor](./petvet-cluster-monitor/)*