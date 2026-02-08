package com.bank.security;

import com.bank.entity.Customer;
import com.bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final CustomerRepository customerRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with username: " + username));
        
        return UserPrincipal.create(customer);
    }
    
    @Transactional
    public UserDetails loadUserById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with id: " + id));
        
        return UserPrincipal.create(customer);
    }
}
