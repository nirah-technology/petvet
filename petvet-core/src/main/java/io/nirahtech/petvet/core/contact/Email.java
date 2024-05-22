package io.nirahtech.petvet.core.contact;

import java.util.Objects;

public final class Email {
    private final String username;
    private final String domain;

    public Email(
        final String username,
        final String domain
    ) {
        this.username = Objects.requireNonNull(username, "Username for email is required.");
        this.domain = Objects.requireNonNull(domain, "Domain for email is required.");
    }

    /**
     * @return the username
     */
    public final String getUsername() {
        return this.username;
    }

    /**
     * @return the domain
     */
    public final String getDomain() {
        return this.domain;
    }

    public static final Email of(final String email) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("A valid email address is required.");
        }
        final String[] emailParts = email.split("@", 2);
        return new Email(emailParts[0], emailParts[1]);
    }
}
