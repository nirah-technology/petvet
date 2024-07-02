# **PetVet-Phytotanica**

## **Description**

Phytotanica est une bibliothèque Java conçue pour charger et gérer des informations sur les plantes à partir de fichiers de ressources, ainsi que pour maintenir une collection de plantes dans un jardin virtuel.

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
        <artifactId>petvet-phytotanica</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
<dependencies>
```

### **Gradle**
ajoutez cette dépendance à votre ***build.gradle.kts*** situé à la racine de votre projet Java :

```kotlin
dependencies {
    implementation("io.nirahtech:petvet-phytotanica:1.0-SNAPSHOT")
}
```

## **Fonctionnalités**

### Chargement de Plantes à partir de Répertoires de Fichiers

Phytotanica permet de charger plusieurs plantes à partir d'un répertoire contenant plusieurs fichiers de propriétés ResourceBundle (.properties). Chaque fichier représente une plante distincte, permettant ainsi de créer une collection de plantes. Chaque plante est chargée avec des détails tels que son nom scientifique, nom commun, description, période de culture, taille, habitat, récolte, usage et une image associée.

Exemple d'utilisation :

```java
Set<Plant> plants = DefaultEmbededPlantLoader.loadPlantsFromResources();
System.out.println("Nombre de plantes chargées : " + plants.size());
```

### Représentation des Périodes de Culture, Habitats et Tailles

Phytotanica utilise des enregistrements Java (records) pour représenter des informations spécifiques aux plantes comme la période de culture, l'habitat et la taille. Ces enregistrements offrent une manière concise et immuable de définir et manipuler ces données.

Exemple de définition :

```java
CultivationPeriod period = new CultivationPeriod(Month.MARCH, Month.NOVEMBER);
Height height = new Height(100, 500);
Habitat habitat = new Habitat("Forêt tropicale", "Environnement humide et chaud");
```

### Gestion d'un Jardin Virtuel de Plantes

Phytotanica inclut une classe VegetableGarden pour maintenir une collection de plantes. Cette classe permet d'ajouter, de supprimer, de vider et de vérifier la présence de plantes dans le jardin virtuel.

Exemple d'utilisation :

```java
VegetableGarden garden = VegetableGarden.getInstance();
garden.addPlant(plant);
System.out.println("Nombre de plantes dans le jardin : " + garden.size());
```

## **Licence**

Ce logiciel logiciel est protégé par une [LICENCE](./LICENSE).