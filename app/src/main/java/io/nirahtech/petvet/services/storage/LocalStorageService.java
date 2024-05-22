package io.nirahtech.petvet.services.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.stream.Stream;

public class LocalStorageService implements StorageService {

    @Override
    public <T> T load(File file) throws IOException, ClassNotFoundException {
        if (!file.exists()) {
            throw new IOException(String.format("%s doesn't exists.", file.getAbsolutePath()));
        }
        if (!file.isFile()) {
            throw  new IOException(String.format("%s isn't a valid file.", file.getAbsolutePath()));
        }

        T object;
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        ) {
            object = (T) objectInputStream.readObject();
        }
        return object;
    }

    @Override
    public final <T> void save(T data, File destinationFile) throws IOException {
        final File parent = destinationFile.getParentFile();
        if (!Objects.requireNonNull(parent).exists()) {
            destinationFile.getParentFile().mkdirs();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);) {
            objectOutputStream.writeObject(data);
        }
    }
}
