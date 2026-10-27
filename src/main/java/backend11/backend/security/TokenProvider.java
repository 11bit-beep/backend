package backend11.backend.security;

import backend11.backend.domain.Role;

public interface TokenProvider {
    String createToken(String username, Role role);

    String getUsername(String token);

    String getRole(String token);

    boolean validateToken(String token);
}
