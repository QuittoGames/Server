package com.quitto.server.infrastructure.services.OAuth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.enums.Role;
import com.quitto.server.infrastructure.db.Entity.users.UserEntity;
import com.quitto.server.infrastructure.db.repository.users.JpaUserRepository;

@Service
public class OAuth2UserProvisioningService extends DefaultOAuth2UserService {

    private final JpaUserRepository repo;
    private final UserAuthoritiesMapper mapper;

    public OAuth2UserProvisioningService(JpaUserRepository repo,
                                   UserAuthoritiesMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {

        OAuth2User oauthUser = super.loadUser(request);

        String email = oauthUser.getAttribute("email");

        UserEntity user = repo.findByEmail(email)
            .orElseGet(() -> {
                UserEntity u = new UserEntity();
                u.setEmail(email);
                u.setRole(Role.USER); // default seguro
                return repo.save(u);
            });

        List<GrantedAuthority> authorities = mapper.map(user);

        return new DefaultOAuth2User(
            authorities,
            oauthUser.getAttributes(),
            "email"
        );
    }
}
