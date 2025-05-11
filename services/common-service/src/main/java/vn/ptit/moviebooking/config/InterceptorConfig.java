package vn.ptit.moviebooking.config;

import vn.ptit.moviebooking.constants.SecurityConstants;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class InterceptorConfig {

    /**
     * Configures the CORS (Cross-Origin Resource Sharing) filter in the application <p>
     * CORS is a security mechanism that allows or denies requests between different origins <p>
     * View the details of the permissions or restrictions in {@link SecurityConstants.CORS}
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(SecurityConstants.CORS.ALLOWED_ORIGIN_PATTERNS));
        config.setAllowedHeaders(List.of(SecurityConstants.CORS.ALLOWED_HEADERS));
        config.setAllowedMethods(List.of(SecurityConstants.CORS.ALLOWED_REQUEST_METHODS));
        config.setAllowCredentials(SecurityConstants.CORS.ALLOW_CREDENTIALS);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(SecurityConstants.CORS.APPLY_FOR, config); // Apply CORS to all endpoints

        return new CorsFilter(source);
    }
}
