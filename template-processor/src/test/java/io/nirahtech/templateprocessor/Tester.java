package io.nirahtech.templateprocessor;

public class Tester {
    public static void main(String[] args) {
        String test = "{{ test }}";
        TemplateEngine engine = new JinjaEngine();
        engine.put("test", "Nicolas");
        System.out.println(engine.parse(test));
    }
}
