package io.nirahtech.petvet.installer.infrastructure.out.ports;

import java.util.Collection;

public interface USB<T> {
    Collection<T> list();
    void upload();
}
