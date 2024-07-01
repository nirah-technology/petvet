# **PETVET**
![Static Badge](https://img.shields.io/badge/petvet-grey)
![Static Badge](https://img.shields.io/badge/java-17-c74634)
![Static Badge](https://img.shields.io/badge/maven-005580)
![Static Badge](https://img.shields.io/badge/gradle-005580)
![Static Badge](https://img.shields.io/badge/android-A4C639)
![Static Badge](https://img.shields.io/badge/arduino-0088CC)


## **CONTEXTE**

Le projet PETVET est un système qui a pour but de suivre l'état de santé des animaux de compagnie au sein d'une famille.

Dans son mode de fonctionnement minimal, le système doit être composé de :

- 1 smartphone


Pour un fonctionnement optimal, le système doit être composé de :

- au moins 3 microcontroleur (Arduino, ESP32, Raspberry, etc...)
- au moins 1 smartphone
- au moins 1 puce pour un chat détectable via onde Wifi/Bluetooth

Plus il y aura de microcontroleurs en réseau, plus la précision de géolocalisation des animaux sera précise.


## PROJETS

PETVET est un système composé des logiciels suivants:

| NOM | TECHNOLOGIES | TYPE | DESCRIPTION |
|---|---|---|---|
| [app](./app) | Java, Android, XML | Application Mobile | Logiciel pour téléhpone Android qui permet de faire le suivis de santé des animaux. |
| [petvet-ai](./petvet-ai) | Java, Deap Learning | Bibliothèque | Logiciel qui permet de manipuler une intelligeance artificielle pour la classification des plantes et des animaux. |
| [petvet-argparse](./petvet-argparse) | Java | Bibliothèque | Logiciel qui permet d'analyser les arguments passé en ligne de commande. |
| [petvet-cluster-monitor](./petvet-cluster-monitor) | Java, Swing | Application Lourde | Logiciel qui permet de surveiller les communications des microcontrolleurs du systèmes de localisation de PETVET. |
| [petvet-cluster-simulator](./petvet-cluster-simulator) | Java, Swing | Application Lourde, Application Console | Logiciel qui permet de simuler les microcontrolleurs du système de localisation de PETVET. |
| [petvet-core](./petvet-core) | Java | Bibliothèque | Logiciel qui contient le coeur de métier de PETVET. |
| [petvet-esp32-wroom32](./petvet-esp32-wroom32) | C/C++ | Application Arduino | Logiciel pour microcontrolleur qui permet de détecter la localisation des animaux. |
| [petvet-features](./petvet-features) | Java | Bibliothèque | Logiciel qui définis toutes les fonctionnalités de PETVET. |
| [petvet-geopulsetracker](./petvet-geopulsetracker) | Java, Swing | Application Lourde | Logiciel qui petmet de voir en tant réèl l'emplacement des microcontrolleurs et des animaux. |
| [petvet-messaging](./petvet-messaging) | Java, Message Broker, Multicast | Bibliothèque | Logiciel qui permet de diffuser des messages entre différentes applications sur le réseau PETVET. |
| [petvet-pytotanica](./petvet-pytotanica) | Java | Bibliothèque | Logiciel qui permet de gérer les plantes aromatiques pour la phytothérapie. |


## SCHEMA

![Image](./petvet-softwares-overview.drawio.svg)

