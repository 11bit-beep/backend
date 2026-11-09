package backend11.backend.auth.jwt;

import backend11.backend.user.domain.Role;

public interface TokenProvider {
    String createToken(String username, Role role);

    String getUsername(String token);

    String getRole(String token);

    boolean validateToken(String token);
}
