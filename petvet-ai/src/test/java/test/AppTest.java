package test;

import java.io.File;
import java.util.stream.Stream;

public class AppTest {
    public static void main(String[] args) {
        File file = new File(".");
        Stream.of(file.list()).forEach(f -> System.out.println(f));
    }
}
