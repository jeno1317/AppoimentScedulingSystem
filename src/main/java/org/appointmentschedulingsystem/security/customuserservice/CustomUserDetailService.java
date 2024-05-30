package org.appointmentschedulingsystem.security.customuserservice;

import lombok.AllArgsConstructor;
import org.appointmentschedulingsystem.security.customuser.CustomUserDetail;
import org.appointmentschedulingsystem.entity.User;
import org.appointmentschedulingsystem.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Could User Not Found !!");
        }
        return new CustomUserDetail(user);
    }

}

