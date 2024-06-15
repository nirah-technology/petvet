package io.nirahtech.petvet.esp.commands;

import java.io.IOException;
import java.net.InetAddress;

abstract class AbstractCommand implements Command {
    protected final InetAddress group;
    protected final int port;

    protected AbstractCommand(final InetAddress group, final int port) {
        this.group = group;
        this.port = port;
    }

    @Override
    public void execute() throws IOException {
        
    }   

    @Override
    public void execute(String argument) throws IOException {
        
    }
}
