package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.example.demo.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

            public ApplicationSecurityConfig(PasswordEncoder passwordEncoder){
                this.passwordEncoder = passwordEncoder;
            }
    //konfiguracja wyswietla popup z prosba o zalogowanie

    //antMatchers strony zezwolone
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()//disabling security
                .disable()//disabling security
                .authorizeRequests()
                .antMatchers("/","/index","/css/*","/js/*")
                .permitAll()
                .antMatchers("/user/**").hasRole(GIRL.name())
                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAnyAuthority(ApplicationUserPermission.GIRL_WRITE.name())
                .antMatchers(HttpMethod.POST, "/management/api/**").hasAnyAuthority(ApplicationUserPermission.GIRL_WRITE.name())
                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAnyAuthority(ApplicationUserPermission.GIRL_WRITE.name())
                .antMatchers("/management/api/**").hasAnyRole(GIRL.name(), ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails aneta= User.builder()
                .username("aneta")
                .password(passwordEncoder.encode("pass"))
//                .roles(GIRL.name())
                .authorities(GIRL.getGrantedAuthorities())
                .build();

        UserDetails jacek = User.builder()
                .username("jacek")
                .password(passwordEncoder.encode("pass"))
//                .roles(ADMIN.name())
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(aneta, jacek);
    }
}
