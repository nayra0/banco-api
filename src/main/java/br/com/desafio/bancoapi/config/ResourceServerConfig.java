package br.com.desafio.bancoapi.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Autowired
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ROLE");
  }

  @Bean
  public UserDetailsService userDetailsService() {
    @SuppressWarnings("deprecation")
    User.UserBuilder builder = User.withDefaultPasswordEncoder();
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(builder.username("admin").password("admin").roles("ROLE").build());
    return manager;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources",
            "/configuration/security", "/swagger-ui.html", "/webjars/**",
            "/swagger-resources/configuration/ui", "/swagger-ui.html",
            "/swagger-resources/configuration/security")
        .permitAll().anyRequest().authenticated().and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.stateless(true);
  }

}
