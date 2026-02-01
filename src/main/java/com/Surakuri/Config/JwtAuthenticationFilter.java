package com.Surakuri.Config;

import com.Surakuri.Service.CustomUserDetailsService;
import com.Surakuri.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A custom Spring Security filter that intercepts every incoming HTTP request to process and validate a JWT token.
 *
 * <p>This filter is the core of the JWT-based authentication mechanism. It extends {@link OncePerRequestFilter}
 * to ensure it is executed exactly once per request. Its primary responsibilities are:</p>
 * <ol>
 *   <li>To check for a JWT in the {@code Authorization} header.</li>
 *   <li>To extract the user's email (username) from the token.</li>
 *   <li>To validate the token's integrity and expiration.</li>
 *   <li>If the token is valid, to load the user's details and set the authentication context,
 *       effectively authenticating the user for the duration of the request.</li>
 * </ol>
 * <p>This filter is registered in the {@link SecurityConfig} to be executed before the standard
 * username/password authentication filter.</p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * The main filtering logic that is executed for every request.
     *
     * @param request The incoming HTTP request.
     * @param response The outgoing HTTP response.
     * @param filterChain The chain of filters to pass the request along to.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extract the "Authorization" header from the request.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Check if the header is missing or doesn't start with "Bearer ".
        // If so, this is not a JWT-authenticated request, so we pass it to the next filter and exit.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract the JWT from the header (by removing the "Bearer " prefix).
        jwt = authHeader.substring(7);

        // 4. Extract the user's email from the token using the JwtService.
        userEmail = jwtService.extractUsername(jwt);

        // 5. Check if we have a user email AND the user is not already authenticated.
        // The second check prevents re-validating on every filter in the chain.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Load the user's details from the database using our custom UserDetailsService.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 7. Validate the token against the user details.
            // This checks if the token is not expired and if the user in the token matches the user from the DB.
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 8. If the token is valid, create an authentication token.
                // This is the object Spring Security uses to represent the authenticated user.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Credentials are not needed as we are using JWT
                        userDetails.getAuthorities()
                );

                // 9. Set additional details about the authentication request.
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 10. Update the SecurityContextHolder with the new authentication token.
                // From this point on, the user is considered authenticated for this request.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 11. Pass the request and response along to the next filter in the chain.
        filterChain.doFilter(request, response);
    }
}