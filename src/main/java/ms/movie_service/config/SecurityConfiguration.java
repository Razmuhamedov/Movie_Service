package ms.movie_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService customUserDetailsService;

    private final JwtTokenFilter jwtTokenFilter;

    private final AuthEntryPointJwt authEntryPointJwt;

    public SecurityConfiguration(CustomUserDetailsService customUserDetailsService,
                                 JwtTokenFilter jwtTokenFilter,
                                 AuthEntryPointJwt authEntryPointJwt) {

        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
        this.authEntryPointJwt = authEntryPointJwt;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(customUserDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .antMatchers("/api/v1/users/getByEmail/**").permitAll()
                .antMatchers("/api/v1/users/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/users/secured/**").hasRole("ADMIN")
                .antMatchers("/api/v1/movies/**").hasAnyRole("USER", "ADMIN", "MODERATOR")
                .antMatchers("/api/v1/movies/secured/**").hasAnyRole("ADMIN", "MODERATOR")
                .antMatchers("/api/v1/library/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/v1/library/secured/**").hasRole("ADMIN")
                .anyRequest().permitAll();

        //changed not authorized request
        http.exceptionHandling().authenticationEntryPoint(authEntryPointJwt);

        //add jwt token filter
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    //Details committed for brevity
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
            return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    public PasswordEncoder getPasswordEncoder() {
//        return new PasswordEncoder() {
//            @Override
//            public String encode(CharSequence charSequence) {
//                System.out.println("getPasswordEncoder encode:  " + charSequence);
//                return charSequence.toString();
//            }


}
