package com.example.demo;

import com.example.demo.auth.ApplicationUserService;
import com.example.demo.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //autoryzacja na metodach w userManagementcontroller
public class ApplicationSecurityConfigJWT extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;

            public ApplicationSecurityConfigJWT(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService){
                this.passwordEncoder = passwordEncoder;
                this.applicationUserService = applicationUserService;
            }
    //konfiguracja wyswietla popup z prosba o zalogowanie

    //antMatchers strony zezwolone
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))//disabling security

                .csrf().disable()
                .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager()))
                .authorizeRequests()
                .antMatchers("/", "/index", "/css/*", "/js/*", "/h2-console/**")
                .permitAll()
                .antMatchers("/api/**").hasRole(ApplicationUserRole.GIRL.name())
                .anyRequest()
                .authenticated();

    }


    @Override

    //konfiguracja providera do odczytu uzytkownikow z bazy danych
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setPasswordEncoder(passwordEncoder);
                provider.setUserDetailsService(applicationUserService);
                return provider;
    }
}
