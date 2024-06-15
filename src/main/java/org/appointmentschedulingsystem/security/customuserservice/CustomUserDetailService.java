package org.appointmentschedulingsystem.security.customuserservice;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailService {
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
