package engine.config;

import com.allanditzel.springframework.security.web.csrf.CsrfTokenResponseHeaderBindingFilter;
import engine.authorisation.AuthenticationFailureHandler;
import engine.authorisation.AuthenticationSuccessHandler;
import engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPoint authEntryPoint;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/api/**").authenticated();
        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(authEntryPoint);
        http.formLogin().successHandler(authenticationSuccessHandler);
        http.formLogin().failureHandler(authenticationFailureHandler);
        http.logout().logoutSuccessUrl("/");

        // CSRF tokens handling
//        http.addFilterAfter(new CsrfTokenResponseHeaderBindingFilter(), CsrfFilter.class);
    }

//        http.authorizeRequests().antMatchers("/api/**").authenticated(); // сразу редиректит на login.html
//        http
//                .formLogin()
//                .loginPage("/login.html")
////                .defaultSuccessUrl("/home.html", true)
//                .failureUrl("/login-error.html")
//                .and()
//                .logout()
//
//                .logoutSuccessUrl("/home.html");
//                http.csrf().disable();

        // Use AuthenticationEntryPoint to authenticate user/password
//        http.httpBasic().authenticationEntryPoint(authEntryPoint);
//        http.exceptionHandling(authEntryPoint);


//        http.csrf().disable();
        // For the H2 Console

//        http.headers().frameOptions().disable();

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserService myUserDetailsService;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }
}