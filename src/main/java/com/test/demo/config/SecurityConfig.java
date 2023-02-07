/*
 * jakarta.servlet.Filter 已更新
 * 如要新增使用者 , 在List<UserDetails>中新增
 */
package com.test.demo.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.test.demo.dao.UserDao;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;


    /*參考SpringBootWebSecurityConfiguration 中的 SecurityFilterChain 
     * 1. 自定義一個securityFilterChain
     * 2. 將@Order(SecurityProperties.BASIC_AUTH_ORDER) basic的AUTH移除 , 自己重寫一個security
     * 3. .authenticated();後面加上and() .httpBasic();
     * 4. 移除http.formLogin();	http.httpBasic();
     * 
     * Httpbasic()的帳號:user  密碼:看下方terminal跳出的隨機密碼
    */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDao userDao;

    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
            .cors().and()
            .csrf().disable()
            .securityMatcher("/*/auth/*")
            .authorizeHttpRequests(
                (requests) -> requests
                //.requestMatchers(HttpMethod.POST,"/**/auth/**").permitAll()
                .anyRequest()
                .permitAll()
            )//定義匹配到"/**/auth/**" 的url 都不需要驗證
            // .requestMatchers("/crayhung168").hasRole("ADMIN")
            // .requestMatchers("/user").hasRole("USER")
            //.and()     

            .sessionManagement()//避免第一次登入後 , session一直持有authenticate state (STATELESS不使用cookie)
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider())//指定使用自己寫的userDetailsService  
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);//在認證user password之前先執行jwtAuthFilter
            
           // httpBasic();

			return http.build();
	}


    // @Bean
	// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //         http
    //         .csrf().disable()
    //         .authorizeHttpRequests()
    //         // .requestMatchers(HttpMethod.POST,"/*/auth/*").permitAll()
    //         .requestMatchers("/*auth/**")
    //         .permitAll()
    //         .anyRequest()
    //         .authenticated()
    //         .and()
    //         .sessionManagement()//避免第一次登入後 , session一直持有authenticate state (STATELESS不使用cookie)
    //         .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //         .and()
    //         .authenticationProvider(authenticationProvider())//指定使用自己寫的userDetailsService  
    //         .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);//在認證user password之前先執行jwtAuthFilter
            
    //        // httpBasic();

	// 		return http.build();
	// }


        
    /*authenticationProvider : 指定使用自己寫的userDetailsService */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());        //如果不用psssword encoder , setPasswordEncoder可免
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // @Bean
    // @Override
    //  public AuthenticationManager authenticationManagerBean() throws Exception {
    //     AuthenticationManager manager = super.authenticationManagerBean();
    //       return manager;
    // }  


    // @Bean
    // public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    // return http.getSharedObject(AuthenticationManagerBuilder.class)
    //         .build();
    // }

    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    

    @Bean
    public PasswordEncoder PasswordEncoder() {
        //return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();

    }
    
    /*userDetailsService : 比對username的email是否相同 */
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
                return userDao.findUserByEmail(email);
            }
        };
    }

    

}
