# **PETVET Electronical Cards Cluster Simulator**
# *Simulateur d'un Cluster de Microcontroleurs*

## **Description du Projet**

Le projet PETVET Electronical Cards Cluster Simulator permet de simuler des microcontrôleurs (MC) en réseau. Il peut démarrer un nombre personnalisable de microcontrôleurs, qui communiqueront sur un réseau multicast en envoyant et en lisant des messages.

Un microcontrôleur peut devenir "orchestrateur" et aura pour responsabilité de synchroniser les scans pour détecter la présence d'autres microcontrôleurs émettant également un signal, qui représentent les animaux à détecter par les MC du cluster.

Le but des MC du cluster est de détecter et de localiser la présence d'animaux sur un périmètre défini.




## **Fonctionnalités**

- **Affichage des Messages Multicast** : Permet de visualiser tous les messages envoyés via le réseau multicast.

- **Détection Dynamique des Microcontrôleurs** : Identifie automatiquement et liste les microcontrôleurs actifs sur le réseau.

- **Détection des Microcontrôleurs Muets** : Notifie lorsque des microcontrôleurs cessent d'émettre des messages sur le réseau.

- **État de Santé des Microcontrôleurs** : Présente de manière détaillée les informations essentielles sur chaque microcontrôleur connecté.

- **Rapports de Scans Réseau** : Affiche tous les rapports de scans réseau générés par les microcontrôleurs.



## **Actions d'un microcontrôleur (MC)**

Un microcontrôleur peut :

- Demander la disponibilité d'un orchestrateur dans le cluster.
- Demander l'élection d'un orchestrateur.
- Devenir orchestrateur.
- Informer qu'il est orchestrateur.
- Ordonner l'exécution d'un scan.
- Diffuser un heartbeat.





## **Configuration**

Voici un exemple de fichier de configuration pour le simulateur :

```properties
# Configuration des nodes
cluster.size=1

# Configuration du réseau
network.ip.filter=192.168.0.0
network.multicast.group=224.0.1.128
network.multicast.port=44666

# Configuration du node
node.mode=NATIVE_NODE

# Intervalles de temps (en millisecondes)
node.interval.scan=10000
node.interval.orchestrator.request=5000
node.interval.heartbeat=2000

```

## **Technologies Utilisées**

- Java
- Multicast Networking


## **Utilisation**

### Pré-requis

- Java 17 ou version ultérieure

### Compilation et exécution

1. Cloner le dépot:

```bash
git clone https://github.com/votre-utilisateur/petvet-electronical-cards-cluster-simulator.git
cd petvet-electronical-cards-cluster-simulator
```

2. Compilez le projet avec Maven:

```bash
mvn package
```

3. Exécutez le simulateur:
```bash
java -jar target/petvet-electronical-cards-cluster-simulator.jar
```


## **Développement**

Pour développer ou contribuer au projet, suivez ces étapes :

1. Clonez le dépôt Git :

```git
git clone https://github.com/votre-utilisateur/superviser-cluster.
```
2. Importez le projet dans votre IDE Java préféré.
3. Explorez le code source et les tests pour comprendre le fonctionnement de l'application.
4. Apportez vos modifications et testez-les localement.
5. Soumettez une pull request avec une description détaillée des changements apportés.



## **Auteurs**
- Nicolas METIVIER


## **Contributions**

Les contributions au projet sont les bienvenues ! N'hésitez pas à ouvrir une issue pour signaler un bug ou à soumettre une pull request pour ajouter de nouvelles fonctionnalités.

Veuillez suivre les étapes suivantes pour contribuer au projet :

1. Fork le dépôt
2. Créez votre branche feature (git checkout -b feature/ma-feature)
3. Commitez vos changements (git commit -am 'Ajout de ma feature')
4. Poussez votre branche (git push origin feature/ma-feature)
4. Ouvrez une Pull Request



## **Licence**

Ce projet est sous licence [CC BY-NC 4.0](./LICENSE).