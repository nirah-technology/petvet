# **ARGPARSE**

## **Description**

**ArgParse** est une bibliothèque Java qui permet de parser et de gérer les arguments passés en ligne de commande. Elle facilite l'interprétation des paramètres fournis par l'utilisateur lorsqu'un programme est exécuté via un terminal.

## **Avantages**

- Simplifie le traitement des arguments en ligne de commande.
- Évite les erreurs courantes liées à l'analyse manuelle des arguments.
- Fournit un message d'aide automatique pour les utilisateurs.


## **Définition**

Le parsing (ou analyse syntaxique) des paramètres en ligne de commande consiste à interpréter les arguments fournis à un programme lorsqu'il est exécuté via un terminal. Ces arguments sont des options et des valeurs qui modifient le comportement du programme.

## **Utilité**

Lorsqu'un programme est exécuté depuis la ligne de commande, il peut accepter des paramètres pour personnaliser son comportement. Cela permet aux utilisateurs de spécifier des options sans avoir à modifier le code source du programme.

## **Prérequis**
- Java 17 ou une version ultérieure.

## **Installation**

Pour utiliser cette bibliothèque dans votre projet, ajoutez simplement les fichiers source à votre répertoire de projet Java. Si vous utilisez Maven, ajoutez cette dépendance à votre ***pom.xml*** situé à la racine de votre projet Java :

```xml
<dependency>
  <groupId>io.nirahtech</groupId>
  <artifactId>petvet-argparse</artifactId>
  <version>1.0.0</version>
</dependency>

```

## **Fonctionnement**

Le programme prend les arguments fournis en ligne de commande et les analyse pour extraire les différentes options et leurs valeurs. Cela permet au programme de savoir quelles options ont été spécifiées et d'agir en conséquence. En cas d'erreur (comme des arguments inattendus ou manquants), une exception est levée et un message d'aide est affiché.

## **Utilisation**

### Exemple d'utilisation:

Voici un exemple simple de la façon dont vous pouvez utiliser ArgumentParser pour interpréter les arguments en ligne de commande :

```java
package io.nirahtech;

import io.nirahtech.argparse.ArgumentParser;
import io.nirahtech.argparse.ParseException;

public class Program {

    public static void main(String[] args) {
        ArgumentParser parser = new ArgumentParser();

        // Ajout des arguments attendus par le programme
        parser.add("timeout", "t", "Définir le délai d'attente", true, true);
        parser.add("address", "a", "Définir l'adresse cible", true, true);

        try {
            // Parsing des arguments passés en ligne de commande
            parser.parse(args);

            // Récupération des valeurs des arguments
            String timeout = parser.get("t").orElse("500");
            String address = parser.get("a").orElse("localhost");

            // Affichage des valeurs
            System.out.println("Timeout: " + timeout);
            System.out.println("Address: " + address);
        } catch (ParseException e) {
            // Affichage de l'aide en cas d'erreur
            System.err.println(e.getMessage());
            System.err.println(parser.getHelp());
        }
    }
}
```

## **Méthodes principales**

- **add(String longParameterName, String shortParameterName, String description, boolean isRequired, boolean isValueRequired)**: Ajoute un argument attendu par le programme.
- **parse(String[] commandLineArguments)**: Parse les arguments passés en ligne de commande.
- **get(String shortOrLongParameterName)**: Récupère la valeur associée à un argument.
- **getHelp()**: Génère un message d'aide listant tous les arguments attendus par le programme.

## **Licence**

Ce logiciel logiciel est protégé par une [LICENCE](./LICENSE).