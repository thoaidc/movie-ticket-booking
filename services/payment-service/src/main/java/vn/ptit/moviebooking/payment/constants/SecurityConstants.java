package vn.ptit.moviebooking.payment.constants;

/**
 * Security configuration parameters
 * @author thoaidc
 */
@SuppressWarnings("unused")
public interface SecurityConstants {

    // The encryption complexity in PasswordEncoder's algorithm (between 4 and 31)
    // Higher values mean the password is harder to attack, but too high will reduce performance
    int BCRYPT_COST_FACTOR = 12;

    interface TOKEN_PAYLOAD {
        String USER_ID = "userId";
        String USERNAME = "username";
        String AUTHORITIES = "authorities";
    }

    interface CORS {
        String APPLY_FOR = "/**"; // CORS filter is applied to all requests
        String[] ALLOWED_HEADERS = {
            "Content-Type",     // Content format
            "Authorization",    // Authentication token
            "Accept",           // Client-expected content
            "Origin",           // Origin of the request
            "X-CSRF-Token",     // Anti-CSRF token
            "X-Requested-With", // Ajax request markup
            "Access-Control-Allow-Origin", // Server response header
            "X-App-Version",    // Application version (optional)
            "X-Device-ID"       // Device ID (optional)
        };

        String[] ALLOWED_REQUEST_METHODS = {"GET", "PUT", "POST", "DELETE"};
        String[] ALLOWED_ORIGIN_PATTERNS = {"*"}; // The list of domains allowed to access the resources. * means all
        boolean ALLOW_CREDENTIALS = true; // Allow sending cookies or authentication information
    }

    interface COOKIES {
        // The key of the cookie storing the JWT token, which is HTTP-only
        // This cookie is automatically sent with requests by browser but cannot be accessed by JavaScript
        String HTTP_ONLY_COOKIE_ACCESS_TOKEN = "dct_access_token";
    }

    interface HEADER {

        // The request header storing the JWT token, used in cases where the token is not found in the HTTP-only cookies
        String AUTHORIZATION_HEADER = "Authorization";
        String AUTHORIZATION_GATEWAY_HEADER = "Authorization-Gateway";
        String TOKEN_TYPE = "Bearer "; // JWT token type
    }
}
