package riggie.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import riggie.common.BaseContext;
import riggie.common.R;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
@Slf4j
public class LoginFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
       HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
       String url= request.getRequestURI();
       log.info("获取url:"+url);
       String[] urls=new String[]{
               "/employee/login",
               "/employee/logout",
               "/backend/**",
               "/front/**",
               "/common/**",
               "/user/sendMsg",
               "/user/login"

       };
       boolean check=check(url,urls);
       if(check){
           log.info("此次请求不需要处理",url);
           filterChain.doFilter(request,response);
           return;

       }
       if(request.getSession().getAttribute("employee")!=null){
           log.info("用户已经登录");
           Long empId=(Long)request.getSession().getAttribute("employee");
           BaseContext.setCurrentId(empId);
           filterChain.doFilter(request,response);
           return;
       }
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }
       log.info("用户未登录");
       response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
       return;
    }
    public boolean check(String url,String[] urls){
        for(String defaultUrl:urls){
            boolean match=PATH_MATCHER.match(defaultUrl,url);
            if(match){
                return  true;
            }

        }
        return false;
    }

}
