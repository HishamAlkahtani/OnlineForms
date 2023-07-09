package online.hk10.OnlineForms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import online.hk10.OnlineForms.database.FormsDAO;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	FormsDAO db;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// init and setup dao authentication provider 
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setPasswordEncoder(passwordEncoder());
		authProvider.setUserDetailsService(db);
		authProvider.setUserDetailsPasswordService(db);

		http.authenticationProvider(authProvider);
		
		
		http.authorizeHttpRequests((request) -> {
			request.requestMatchers("/error", "/", "/index.html", "/form/**", "/getform/**", "/assets/**", "/success.html", "/register", "/login", "/isLoggedIn", "/logout").permitAll();	
			request.requestMatchers("/createForm", "/userDashboard").authenticated();
			request.requestMatchers("/responses/**", "/responsesRaw/**", "/responsesExcel/**").access(authorizationManager()); 
		});
		
		http.formLogin((f) -> {
			f.loginPage("/login")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/userDashboard");
		});
		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return db;
	}
	
	@Bean
	public AuthorizationManager<RequestAuthorizationContext> authorizationManager() {
		return new OnlineFormsAuthorizationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();		
	}
	
	
}
