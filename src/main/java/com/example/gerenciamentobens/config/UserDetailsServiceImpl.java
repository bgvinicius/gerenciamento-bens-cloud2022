package com.example.gerenciamentobens.config;

import com.example.gerenciamentobens.entity.user.User;
import com.example.gerenciamentobens.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isPresent()){
            return new UserDetailsImpl(
                    user.get().getUsername(),
                    user.get().getPassword(),
                    user.get().getEmail()
            );
        }

        throw new UsernameNotFoundException("Username " + username + " not found");
    }
}
