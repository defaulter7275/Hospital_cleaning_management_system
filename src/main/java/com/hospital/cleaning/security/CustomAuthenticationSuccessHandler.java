package com.hospital.cleaning.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;
import java.util.Collection;

public class CustomAuthenticationSuccessHandler
        extends SavedRequestAwareAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession();
        String username = authentication.getName();
        session.setAttribute("username", username);

        // Redirect based on role
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/dashboard";

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                redirectUrl = "/admin/dashboard";
                break;
            } else if (authority.getAuthority().equals("ROLE_MANAGER")) {
                redirectUrl = "/manager/dashboard";
                break;
            } else if (authority.getAuthority().equals("ROLE_STAFF")) {
                redirectUrl = "/dashboard";
                break;
            }
        }
        // Clear authentication attributes
        clearAuthenticationAttributes(request);
        // Redirect to appropriate page
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
