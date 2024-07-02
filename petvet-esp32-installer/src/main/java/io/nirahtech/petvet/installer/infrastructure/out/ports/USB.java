package io.nirahtech.petvet.installer.infrastructure.out.ports;

import java.nio.file.Path;
import java.util.Set;

public interface USB {
    Set<Path> list();
    void upload();
}
