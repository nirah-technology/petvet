package io.nirahtech.petvet.argparse;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.nirahtech.argparse.ArgumentParser;

public class ArgumentParserTest {
    
    @Test
    public void validArguments() throws Exception {
        String[] arguments = "-a Ah -b Bé --cc Cé".split(" ");
        final ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.add("aa", "a", null, true, true);
        argumentParser.add("bb", "b", null, true, false);
        argumentParser.add("cc", "c", null, false, false);
        argumentParser.add("dd", "d", null, false, true);
        argumentParser.parse(arguments);
        assertTrue(argumentParser.get("aa").isPresent());
        assertTrue(argumentParser.get("bb").isPresent());
        assertTrue(argumentParser.get("c").isPresent());
        System.out.println(
            argumentParser.getHelp()
        );

    }
}
