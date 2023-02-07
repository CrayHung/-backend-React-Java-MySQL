package com.test.demo.config;

import javax.imageio.spi.IIOServiceProvider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.test.demo.dao.UserDao;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


/**
 * 每次有任何request就要跑一次JwtAuthFilter 
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    
    //private static final String AUTHORIZATION = null;
    //private final String AUTHORIZATION;

    //private static final String AUTHORIZATION = null;

    private final UserDao userDao;

    private final JwtUtils jwtUtils;


    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException, java.io.IOException{

        final String authHeader = request.getHeader("Authorization");
        final String userEmail;
        final String jwtToken;

        /*Bearer Token :  HTTP 的認證「Authorization」方案有許多種格式，而 Bearer 就是其中一種且被定義在 Header 中的驗證方案，通常搭配於 JWT 上 
        要存取某 API 時，若要身份驗證則在 JWT 前面加上字符串 Bearer 再放到 HTTP 請求的 Header 中，這個值就是 Bearer Token*/

        //如果authHeader是空或不是以Bearer開頭,代表沒有JWT 則重複doFilter()
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = authHeader.substring("Bearer ".length());
        //jwtToken = authHeader.split(" ")[1].trim();
        //jwtToken = authHeader.substring(7); //beginIndex:7
        userEmail = jwtUtils.extractUsername(jwtToken);  

        //SecurityContextHolder : 用來儲存驗證過的Authentication ，也就是負責管理SecurityContext
        //UserDetails : 作為封裝使用者資訊的物件，包含使用者名稱、密碼、權限等
        //UserDetailsService : 用來載入使用者資訊，Spring Security 進行安全驗證時收到輸入請求中的使用者名稱，會呼叫userDao 中的findUserByEmail() 方法去尋找相應的使用者資訊，並封裝成UserDetails 物件
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = userDao.findUserByEmail(userEmail);
            
            if(jwtUtils.isTokenValid(jwtToken, userDetails)){
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);

    }
}
