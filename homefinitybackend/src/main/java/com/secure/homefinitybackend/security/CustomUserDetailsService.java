package com.secure.homefinitybackend.security;

import com.secure.homefinitybackend.models.User;
import com.secure.homefinitybackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: " + email));

        boolean isAccountNonExpired = user.isAccountNonExpired()
                && (user.getAccountExpiryDate() == null
                    || user.getAccountExpiryDate().isAfter(LocalDate.now()));

        boolean isCredentialsNonExpired = user.isCredentialsNonExpired()
                && (user.getCredentialsExpiryDate() == null
                    || user.getCredentialsExpiryDate().isAfter(LocalDate.now()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                isAccountNonExpired,
                isCredentialsNonExpired,
                user.isAccountNonLocked(),
                Collections.singletonList(
                        new SimpleGrantedAuthority(user.getRole().getRoleName().name()))
        );
    }
}
