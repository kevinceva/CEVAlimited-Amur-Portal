package com.ceva.filters;

import java.io.IOException;
import java.util.Random;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class HttpOnlyCookieFilter implements Filter {
   private FilterConfig filterConfig;
   private Random random = new Random();
   public void init(FilterConfig filterConfig)
      throws ServletException {
      this.filterConfig = filterConfig;
   }
   public void destroy() {
      this.filterConfig = null;
   }

   public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
       HttpServletRequest request = (HttpServletRequest)arg0;
       HttpServletResponse response = (HttpServletResponse)arg1;
       if(!request.getSession().isNew()){
           //long cookie = random.nextLong();
           String str1 = (String)request.getSession().getAttribute("HttPOnlyCookie");
           Cookie[] cookies = request.getCookies();
           for(int i=0;i<cookies.length;i++){
               if(cookies[i].getName().equals("HttPOnlyCookie")){
                   if(!cookies[i].getValue().startsWith(str1)) throw new RuntimeException("Error because of HttpOnly Bi-Pass!!!");
               }
           }
       }
       if(request.getSession().isNew()){
           long str = random.nextLong();
           Cookie cookie = new Cookie("HttPOnlyCookie", (str + "; path=/; HttpOnly"));
           response.addCookie(cookie);
           request.getSession().setAttribute("HttPOnlyCookie", (str + ""));
       }
       arg2.doFilter(arg0, arg1);
    }

}