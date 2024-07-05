package io.nirahtech.petvet.installer.infrastructure.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import io.nirahtech.petvet.installer.domain.ESP32;

// https://arduino.github.io/arduino-cli/1.0/installation/
// https://arduino.github.io/arduino-cli/1.0/getting-started/
public final class ArduinoCli {

    private static final String ARDUINO_CLI_BIN = "/bin/arduino-cli";
    private static final String PROJECT_NAME = "PetVetES32Sketch";

    private static ArduinoCli instance = null;

    public static ArduinoCli newInstance(final ESP32 esp32) throws IOException {
        final InputStream arduinoCliInputStream = ArduinoCli.class.getResourceAsStream(ARDUINO_CLI_BIN);
        if (!Objects.isNull(arduinoCliInputStream)) {
            throw new IOException("Bin file not found in resources: " + ARDUINO_CLI_BIN);
        }
        final File bin = File.createTempFile("PETVET", "TEMP");
        final boolean isCreated = bin.createNewFile();
        if (!isCreated) {
            throw new IOException("Unable to create bin file: " + bin);
        }
        bin.deleteOnExit();
        Files.copy(arduinoCliInputStream, bin.toPath());
        boolean canBeExecuted = bin.setExecutable(true);
        if (!canBeExecuted) {
            throw new IOException("Unable to set bin file executable: " + bin);
        }
        final Path workspace = Files.createTempDirectory("PETVET-ESP32-INSTALLER");
        return new ArduinoCli(bin, workspace.toFile(), esp32);
    }

    private final File executableBinary;
    private final File workspace;
    private final ESP32 esp32;
    
    private ArduinoCli(final File executableBinary, final File workspace, final ESP32 esp32) {
        this.executableBinary = executableBinary;
        this.workspace = workspace;
        this.esp32 = esp32;
    }

    private final Process runCommand(final String... commandWithParameters) throws IOException {
        final ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(this.workspace);
        processBuilder.command(commandWithParameters);
        return processBuilder.start();

    }

    public void createNewSketch() throws IOException {
        final Process process = this.runCommand(this.executableBinary.getAbsolutePath(), "sketch", "new", "PetVetES32Sketch");
        try {
            final int exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    public void updateSketch(final String newSourceCode) throws IOException {

    }

    public void installCoreForESP32() throws IOException {

    }

    public void add3rdPartsCore() throws IOException {

    }

    public void compile() throws IOException {
        final Process process = this.runCommand(this.executableBinary.getAbsolutePath(), "compile", "--fqbn", "arduino:samd:mkr1000", PROJECT_NAME);
        try {
            final int exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    public void upload() throws IOException {
        final Process process = this.runCommand(this.executableBinary.getAbsolutePath(), "upload", "-p", this.esp32.getUsbPort().toString(), "--fqbn", "arduino:samd:mkr1000", PROJECT_NAME);
        try {
            final int exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }
}
