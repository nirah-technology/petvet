package io.nirahtech.petvet.services.storage;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public interface StorageService {

    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    <T> T load(final File file) throws IOException, ClassNotFoundException;

    /**
     *
     * @param data
     * @param destinationFile
     * @throws IOException
     */
    <T> void save(final T data, final File destinationFile) throws IOException;
}
