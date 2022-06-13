package io.lana.libman.core.user.role;

public final class Authorities {
    private Authorities() {
    }

    public static final String ADMIN = "ADMIN";

    public static final String LIBRARIAN = "LIBRARIAN";

    public static final String FORCE = "FORCE";

    public static class User {
        private User() {
        }

        public static final String SYSTEM = "SYSTEM";
    }
}
