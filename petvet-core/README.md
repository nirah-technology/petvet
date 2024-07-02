# **PetVet-Core**

## **Description**

**PetVet-Core** est une bibliothèque Java complète conçue pour la gestion d'un parc animalier, des soins vétérinaires, des événements liés aux animaux et des contacts d'urgence.

## **Fonctionnalités**
- **Gestion du Parc Animalier**: Classes pour définir les animaux, les races, les genres, les espèces et leurs cycles de vie.

  PetVet-Core permet de définir et de gérer les animaux, leurs races, genres, espèces et cycles de vie. Cela inclut la possibilité de spécifier des informations détaillées sur chaque animal, comme son nom, sa race, son genre et son espèce.

    ### Exemple :

    ```java

    ```

- **Infrastructure de Base**: Comprend des entités telles que des maisons, des fermes, des bibliothèques et des répertoires pour la gestion des animaux de compagnie, des plantes, etc.

  La bibliothèque fournit une infrastructure de base pour les entités diverses telles que les maisons, les fermes, les bibliothèques, et les répertoires. Ces classes sont utiles pour gérer les environnements dans lesquels les animaux vivent et interagissent.

    ### Exemple :

    ```java

    ```

- **Gestion de la Clinique**: Prise en charge des cliniques vétérinaires avec des classes pour les consultations, les maladies, les médicaments (conventionnels et naturels), les chirurgies, les vaccinations et les vaccins.

  PetVet-Core comprend des classes pour gérer les cliniques vétérinaires. Cela inclut la gestion des consultations, des maladies, des médicaments (conventionnels et naturels), des chirurgies, des vaccinations et des vaccins.



    ### Exemple :

    ```java

    ```

- **Gestion des Contacts**: Gère les détails de contact tels que les villes, les pays, les provinces, les rues, les e-mails, les numéros de téléphone et les adresses postales.

  Pour gérer les contacts d'urgence, la bibliothèque fournit des classes pour définir les détails de contact comme les villes, les pays, les provinces, les rues, les e-mails, les numéros de téléphone et les adresses postales.

    ### Exemple :

    ```java

    ```

- **Pharmacie**: Fournit des classes pour la gestion des concoctions spéciales et des élixirs.

  PetVet-Core inclut des classes pour la gestion des concoctions spéciales et des élixirs, ce qui est essentiel pour le traitement des animaux avec des médicaments spécifiques.

    ### Exemple :

    ```java

    ```

- **Planification des Événements**: Inclut la gestion des calendriers, la définition des événements et les services de rappel d'événements pour la programmation des activités liées aux animaux.

  La bibliothèque offre des fonctionnalités pour la planification des événements liés aux animaux, comme la gestion des calendriers, des types d'événements et des rappels d'événements pour une organisation efficace des activités.

    ### Exemple :

    ```java

    ```

- **Utilitaires**: Offre des classes utilitaires pour la gestion des identifiants, des volumes et des poids.

  PetVet-Core propose également des utilitaires comme la gestion des identifiants, des volumes et des poids, ce qui facilite le suivi et la gestion des données spécifiques aux animaux.

    ### Exemple :

    ```java

    ```


## **Prérequis**
- Java 17 ou une version ultérieure.

## **Installation**

Pour utiliser cette bibliothèque dans votre projet, ajoutez simplement les fichiers source à votre répertoire de projet Java. 

### **Maven**

Ajoutez cette dépendance à votre ***pom.xml*** situé à la racine de votre projet Java :

```xml
<dependencies>
    <dependency>
        <groupId>io.nirahtech</groupId>
        <artifactId>petvet-core</artifactId>
        <version>1.0.0</version>
    </dependency>
<dependencies>
```

### **Gradle**
ajoutez cette dépendance à votre ***build.gradle.kts*** situé à la racine de votre projet Java :

```kotlin
dependencies {
    implementation("io.nirahtech:petvet-core:1.0-SNAPSHOT")
}
```

## **Licence**

Ce logiciel logiciel est protégé par une [LICENCE](./LICENSE).