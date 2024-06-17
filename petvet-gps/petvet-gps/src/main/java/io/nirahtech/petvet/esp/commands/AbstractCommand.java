package io.nirahtech.petvet.esp.commands;

import java.io.IOException;

abstract class AbstractCommand implements Command {

    protected AbstractCommand() {
    }

    @Override
    public void execute() throws IOException {
        
    }   

    @Override
    public void execute(String argument) throws IOException {
        
    }
}
