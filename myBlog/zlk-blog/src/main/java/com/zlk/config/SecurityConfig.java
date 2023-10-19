package com.zlk.config;

import com.zlk.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    //匿名访问和无需认证访问区别
//   "匿名访问" 和 "不需要认证访问" 是在访问控制和身份验证领域中使用的两个相关但不完全相同的概念，它们有一些区别：
//
//    匿名访问 (Anonymous Access)：
//
//    匿名访问表示允许未登录或未提供身份信息的用户访问应用程序的某些部分或资源。
//    匿名用户通常被分配一个匿名身份，他们没有提供有效的身份认证信息，但仍然被允许访问一些公开资源，如公共网页、部分API等。
//    这种情况下，虽然用户没有提供有效的身份认证信息，但系统仍然知道他们是匿名用户。
//    不需要认证访问 (No Authentication Required)：
//
//    不需要认证访问表示某些资源或部分应用程序可以被任何用户访问，而无需进行身份验证。
//    这包括匿名用户，但也包括已登录用户，只要他们具有访问这些资源的权限，不需要再次进行身份验证。
//    这意味着即使已登录用户也可以访问这些资源，而无需重新提供身份认证信息。
//    总的来说，匿名访问更关注用户是否已提供身份信息，而不需要认证访问更侧重于资源是否需要身份验证。匿名访问通常是指未登录用户的访问，而不需要认证访问可能包括未登录和已登录用户的访问，只要他们有权访问这些资源。
//
//    在某些情况下，这两个概念可以相互替代，因为匿名用户通常也属于不需要认证的访问范围。然而，具体的实现和用例可能会有所不同，因此在设计访问控制策略时，需要根据应用程序的需求和安全性要求来考虑这些概念。

    //认证失败
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    //授权失败
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/login").anonymous()
//                //指这个接口需要认证才能访问
//                .antMatchers("/logout").authenticated()
//                .antMatchers("/user/userInfo").authenticated()
              /*  //代表以下接口需要认证之后才能访问
                .antMatchers("/link/getAllLink").authenticated()*/
                // 除上面外的所有请求全部不需要认证即可访问
                .anyRequest().permitAll();

        //配置异常处理器
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        //关闭security的默认注销功能，使用自己定义的注销功能，因为它的默认请求路径也是/logout
        http.logout().disable();
        //意思是将jwtAuthenticationTokenFilter过滤器添加在UsernamePasswordAuthenticationFilter过滤器之前
        //实际上重写configure方法时，没有调用父类的方法，所以UsernamePasswordAuthenticationFilter并没有在过滤器链当中
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //允许跨域
        http.cors();
    }

    //密码加密处理
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //重写这个方法可以暴露AuthenticationManager到应用程序上下文中
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
