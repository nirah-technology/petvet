package io.nirahtech.petvet.installer.infrastructure.out.adapters;

import java.io.File;
import java.nio.file.Path;

interface Televerser {
    void televerse(final File file, final Path esp32Path);
}
