package io.nirahtech.templateprocessor;

public class JinjaEngine extends AbstractTemplateEngine {
    @Override
    protected String beginTokenCharacters() {
        return "\\{\\{";
    }

    @Override
    protected String endTokenCharacters() {
        return "\\}\\}";
    }
}
