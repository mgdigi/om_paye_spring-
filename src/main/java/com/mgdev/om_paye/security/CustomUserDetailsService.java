package com.mgdev.om_paye.security;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mgdev.om_paye.entity.User;
import com.mgdev.om_paye.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Recherche d'utilisateur avec username: {}", username);

        // Essayer d'abord par numéro de téléphone, puis par email
        User user = userRepository.findByPhoneNumber(username)
                .orElse(userRepository.findByEmail(username).orElse(null));

        if (user == null) {
            logger.error("Utilisateur non trouvé avec l'identifiant: {}", username);
            throw new UsernameNotFoundException("Utilisateur non trouvé avec l'identifiant: " + username);
        }

        logger.debug("Utilisateur trouvé: {} avec rôle: {}", user.getEmail(), user.getRole());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getPhoneNumber()) // Utiliser le numéro de téléphone comme username
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}