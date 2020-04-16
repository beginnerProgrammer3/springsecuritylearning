package com.example.demo;

import com.example.demo.auth.ApplicationUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //autoryzacja na metodach w userManagementcontroller
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;

            public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService){
                this.passwordEncoder = passwordEncoder;
                this.applicationUserService = applicationUserService;
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
                .antMatchers("/management/api/**").hasAnyRole(GIRL.name(), ADMIN.name())


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

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails aneta= User.builder()
//                .username("aneta")
//                .password(passwordEncoder.encode("pass"))
////                .roles(GIRL.name())
//                .authorities(GIRL.getGrantedAuthorities())
//                .build();
//
//        UserDetails jacek = User.builder()
//                .username("jacek")
//                .password(passwordEncoder.encode("pass"))
////                .roles(ADMIN.name())
//                .authorities(ADMIN.getGrantedAuthorities())
//                .build();
//
//        return new InMemoryUserDetailsManager(aneta, jacek);
//    }

    // autentykacja podstawowa w spring security


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
