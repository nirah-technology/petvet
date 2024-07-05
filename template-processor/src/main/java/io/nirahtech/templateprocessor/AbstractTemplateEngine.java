package io.nirahtech.templateprocessor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.atomic.AtomicReference;

abstract class AbstractTemplateEngine implements TemplateEngine {

    protected final Map<String, Object> tokens = new HashMap<>();

    protected abstract String beginTokenCharacters();
    protected abstract String endTokenCharacters();

    private String getTokenPattern(final String token) {
        return String.format("%s %s %s", this.beginTokenCharacters(), token, this.endTokenCharacters());
    }
    
    @Override
    public void put(String key, Object value) {
        this.tokens.put(key, value);
    }

    @Override
    public Map<String, Object> getTokens() {
        return Collections.unmodifiableMap(this.tokens);
    }

    @Override
    public void clear() {
        this.tokens.clear();
    }


    @Override
    public Set<String> retrieveTokens(String template) {
        Set<String> tokensFound = new HashSet<>();
    
        // Define the regular expression pattern to find values between "{{" and "}}"
        Pattern pattern = Pattern.compile("\\{\\{s*(.*?)s*\\}\\}");
        Matcher matcher = pattern.matcher(template);

        // Find all matches and add them to the set
        while (matcher.find()) {
            final String token = matcher.group(1).trim();
            System.out.println(token);
            tokensFound.add(token);
        }

        return tokensFound;

    }
    

    @Override
    public String parse(String template) {
        AtomicReference<String> content = new AtomicReference<>(template);
        this.tokens.entrySet().forEach(tokenSet -> {
            final String token = this.getTokenPattern(tokenSet.getKey());
            String updatedContent = content.get().replaceAll(token, tokenSet.getValue().toString());
            content.set(updatedContent);
        });
        return content.get();
    }
}
