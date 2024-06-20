package io.nirahtech.petvet.simulator.electronicalcard.commands;

import java.io.IOException;

public interface Command  {
    void execute() throws IOException;
    void execute(final String argument) throws IOException;
}
