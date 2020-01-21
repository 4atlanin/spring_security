package by.security.demo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;

public class MyUserDetailService implements UserDetailsService
{
    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException
    {
        UserDetails userDetails = new MyUserDetails();

        return userDetails;
    }

    private class MyUserDetails implements UserDetails
    {
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities()
        {
            return Collections.singletonList( (GrantedAuthority) () -> "ROLE_GUEST" );
        }

        @Override
        public String getPassword()
        {
            return "guest";
        }

        @Override
        public String getUsername()
        {
            return "guest";
        }

        @Override
        public boolean isAccountNonExpired()
        {
            return true;
        }

        @Override
        public boolean isAccountNonLocked()
        {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired()
        {
            return true;
        }

        @Override
        public boolean isEnabled()
        {
            return true;
        }
    }
}
