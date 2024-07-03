package io.nirahtech.templateprocessor;

import java.util.Map;
import java.util.Set;

/**
 * TemplateProcessor
 */
public interface TemplateEngine {
    void put(final String key, final Object value);
    Map<String, Object> getTokens();
    void clear();
    String parse(final String template);
    Set<String> retrieveTokens(final String template);
}