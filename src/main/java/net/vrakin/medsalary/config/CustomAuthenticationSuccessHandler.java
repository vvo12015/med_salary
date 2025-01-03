package net.vrakin.medsalary.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

/**
 * Кастомний обробник успішної аутентифікації.
 *
 * В залежності від ролі користувача, перенаправляє його на відповідну сторінку після успішного входу.
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private RequestCache requestCache;

    public CustomAuthenticationSuccessHandler(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl;

        if (savedRequest != null) {
            targetUrl = savedRequest.getRedirectUrl();
        } else if (request.getParameter("fromIndex") != null) {
            targetUrl = "/home";
        } else {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            targetUrl = roles.contains("ROLE_ADMIN") ? "/security-user" : "/";
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(targetUrl);
        response.getWriter().flush();
    }
}
