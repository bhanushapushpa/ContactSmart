package com.springBoot.Bean.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class myConfig {

	
	@Autowired
	userDetailsServiceImp userDetailService;
	
	@Bean
	UserDetailsService userDetail() {
		return userDetailService;
	}
	


	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// 16-03-2024

		http.csrf(csrf-> csrf.disable())
		.authorizeHttpRequests(config -> {
			config.requestMatchers("/admin/**").hasRole("ADMIN");
			config.requestMatchers("/user/**").hasRole("USER");
			config.requestMatchers("/**").permitAll();
			config.anyRequest().authenticated();
		})
		.formLogin(form -> {form
			.loginPage("/login").permitAll()
			.defaultSuccessUrl("/user/index");
		});

		return http.build();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provide = new DaoAuthenticationProvider();
		provide.setUserDetailsService(userDetailService);
		provide.setPasswordEncoder(passwordEncoder());

		return provide;
	}
	
	

	/*
	 * @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	 * 
	 * http .authorizeHttpRequests( request -> request
	 * .requestMatchers("/admin/**").hasRole("ADMIN")
	 * .requestMatchers("/normal/**").hasRole("USER")
	 * .requestMatchers("/**").permitAll() .anyRequest().authenticated())
	 * .formLogin(l -> l.loginPage("/login") .defaultSuccessUrl("/about",true))
	 * .logout((lo)-> lo.permitAll() )
	 * 
	 * 
	 * .logout(logout -> logout .logoutUrl("/logout") // Specify the logout URL
	 * .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
	 * .invalidateHttpSession(true) // Invalidate session
	 * .deleteCookies("JSESSIONID") // Remove session cookie )
	 * 
	 * 
	 * 
	 * http.authorizeHttpRequests(result ->
	 * result.requestMatchers("/normal/**").hasAuthority("USER")) .formLogin(login
	 * -> login.loginPage("/login").defaultSuccessUrl("/about"));
	 * 
	 * 
	 * http.authorizeHttpRequests(request ->
	 * request.requestMatchers("/normal/**").hasRole("USER")) .formLogin(login ->
	 * login.loginPage("/login").defaultSuccessUrl("/about"));
	 * 
	 * http.authorizeHttpRequests(result ->
	 * result.requestMatchers("/**").permitAll());
	 * 
	 * 
	 * 
	 * http.authorizeHttpRequests(request ->
	 * request.requestMatchers("/admin/**").hasRole("ADMIN")) .formLogin(login ->
	 * login.loginPage("/login").defaultSuccessUrl("/")) .logout(logout ->
	 * logout.logoutSuccessUrl("/login"));
	 * 
	 * 
	 * http.authorizeHttpRequests(request ->
	 * request.requestMatchers("/normal/**").hasRole("USER")) .formLogin(login ->
	 * login.loginPage("/login").defaultSuccessUrl("/")) .logout(logout ->
	 * logout.logoutSuccessUrl("/login"));
	 * 
	 * 
	 * 
	 * 
	 * http.authorizeHttpRequests(authorize ->
	 * authorize.requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().denyAll(
	 * ));
	 * 
	 * 
	 * 
	 * http.authorizeHttpRequests(result ->
	 * result.requestMatchers("/**").permitAll());
	 * 
	 * 
	 * http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/",
	 * "/about", "/signup", "/login")
	 * .permitAll().requestMatchers("/user/**").hasRole("USER").requestMatchers(
	 * "/admin/**").hasRole("ADMIN")) .formLogin(withDefaults());
	 * 
	 * 
	 * // http.httpBasic(withDefaults());
	 * 
	 * http.authorizeHttpRequests( authorize ->
	 * authorize.requestMatchers("/admin/**").hasAuthority("ADMIN").anyRequest().
	 * authenticated()) .formLogin(withDefaults());
	 * 
	 * 
	 * //16-03-2024
	 * 
	 * http.authorizeHttpRequests(configure -> { configure
	 * .requestMatchers("/user/**").hasRole("ADMIN")
	 * .requestMatchers("/**").permitAll() .anyRequest().authenticated();
	 * }).formLogin(form -> { form.defaultSuccessUrl("/"); });
	 * 
	 * return http.build(); }
	 * 
	 * 
	 * 
	 * @Bean JdbcUserDetailsManager userDetailsCheck(DataSource dataSource) {
	 * JdbcUserDetailsManager jdbcUserDetailsManager = new
	 * JdbcUserDetailsManager(dataSource);
	 * 
	 * 
	 * jdbcUserDetailsManager.
	 * setUsersByUsernameQuery("Select * from user where id=?");
	 * 
	 * jdbcUserDetailsManager.
	 * setAuthoritiesByUsernameQuery("Select * from user where id=?");
	 * 
	 * 
	 * return jdbcUserDetailsManager; }
	 * 
	 * 
	 * 
	 * @Bean public DaoAuthenticationProvider authenticationManager1() {
	 * DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	 * provider.setPasswordEncoder(passwordEncoder());
	 * provider.setUserDetailsService(userDetail()); return provider; }
	 * 
	 * 
	 * 
	 * @Bean public void configure(AuthenticationManagerBuilder auth) throws
	 * Exception { auth.authenticationProvider(authenticationManager); }
	 * 
	 * 
	 * 
	 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity
	 * httpSecurity) {
	 * 
	 * httpSecurity.authorizeHttpRequests(request -> request
	 * .requestMatchers("/**").permitAll()
	 * .requestMatchers("/admin/**").hasRole("ADMIN")
	 * .requestMatchers("/user/**").hasRole("USER") .anyRequest().authenticated())
	 * 
	 * .formLogin()) .httpBasic(withDefaults(); return httpSecurity.build();
	 * 
	 * }
	 */

}
