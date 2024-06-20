#ifndef SCANNER_H
#define SCANNER_H

#include <Arduino.h>
#include "Microship.h" // Inclure votre classe Device

class Scanner {
public:
    virtual Microship* scan() = 0; // Définition de la méthode scan comme une méthode pure virtuelle
};

#endif // SCANNER_H
