package ir.afarinesh.realhope.shares.services;

import ir.afarinesh.realhope.entities.security.User;
import ir.afarinesh.realhope.shares.repositories.UserSpringJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserSpringJpaRepository userSpringJpaRepository;

    public UserDetailServiceImpl(UserSpringJpaRepository userSpringJpaRepository) {
        this.userSpringJpaRepository = userSpringJpaRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userSpringJpaRepository.findUserByUsername(username);
        if (userOptional.isEmpty()) {
            throw  new UsernameNotFoundException(username);
        } else {
            return userOptional.get();
        }
    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    };
}
