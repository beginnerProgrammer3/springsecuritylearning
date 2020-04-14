package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.concurrent.TimeUnit;

import static com.example.demo.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //autoryzacja na metodach w userManagementcontroller
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
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))//disabling security

                .csrf()
                .disable()//disabling security
                .authorizeRequests()
                .antMatchers("/","/index","/css/*","/js/*","/h2-console/**")
                .permitAll()

//                .antMatchers("/user/**").hasRole(GIRL.name())
//                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAnyAuthority(ApplicationUserPermission.GIRL_WRITE.getPermission())
//                .antMatchers(HttpMethod.POST, "/management/api/**").hasAnyAuthority(ApplicationUserPermission.GIRL_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAnyAuthority(ApplicationUserPermission.GIRL_WRITE.getPermission())
////       podstawowa metoda autoryzacji         .antMatchers(HttpMethod.PUT, "/management/api/**").hasAnyAuthority(ApplicationUserPermission.GIRL_WRITE.name())
//                .antMatchers("/management/api/**").hasAnyRole(GIRL.name(), ADMIN.name())


                .anyRequest()
                .authenticated()
                .and()
                //.httpBasic(); //podstawowa autentykacja nie uzywajaca sessionID (prompt logowania sie)


                .formLogin() //form autentication z session id(wylogowujaca sie po 10 minutach(okno logowania springboot do zapisu session id najlepiej uzyc postgres lub redis
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/courses",true); //.permitAll(); //dodaje strone do logowania zamiast defaultowej springboot
//                        .passwordParameter("somePasswordName")
//                        .usernameParameter("some username")   niestandardowe nazwy wlasne usera i passworda


//                .and().rememberMe(); // to zapamietuje usera na 2 tyg
//                      .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)).key("something secured"); //
//                            .rememberMeParameter("some rememberName")  zmiana nazwy remember me



//                .and().logout()
//                      .logoutUrl("/logout"); //strona do wylogowania
//                                    UWAGA!!!! to dajemy jesli mamy wylaczone csrf .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")
//                        .clearAuthentication(true)
//                        .invalidateHttpSession(true) //usuwa cookies
//                        .deleteCookies("JSESSIONID","remember-me");
//                        .logoutSuccessUrl("/login");



        http.headers().frameOptions().disable(); //wylacza zabezpieczenia i mozna wejsc do konsoli
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
