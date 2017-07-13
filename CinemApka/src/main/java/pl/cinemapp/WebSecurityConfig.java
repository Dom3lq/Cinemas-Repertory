package pl.cinemapp;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
			
	@Autowired
	DataSource dataSource;
	

	 @Autowired
	    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
	        auth.jdbcAuthentication().dataSource(dataSource)
	        	.usersByUsernameQuery(
	        			"select name, password, enabled from user where name=?")
	        	.authoritiesByUsernameQuery(
	        			"select username, role from user_role where username=?");
	        
	    }

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login", "/css/", "/js/", "/css/**", "/js/**", "/login?logout", "/logout", "/client/**", "/client").permitAll()
                .anyRequest().authenticated()
                .antMatchers("/admin/**", "/admin").hasAuthority("ROLE_ADMIN")
                .and()
            .formLogin()
                .loginPage("/login")
                .usernameParameter("username").passwordParameter("password")
                .and()
            .logout()
               	.logoutUrl("/logout")
               	.permitAll()
               	.logoutSuccessUrl("/login?logout")               	
                .and()
            .exceptionHandling().accessDeniedPage("/acces_denied")
            	.and()
            .sessionManagement()
            	.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
            	
    }

   }
