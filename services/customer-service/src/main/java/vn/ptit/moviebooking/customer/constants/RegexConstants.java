package vn.ptit.moviebooking.customer.constants;

public interface RegexConstants {

    // The email address cannot have invalid characters, such as a dot at the beginning or end of the domain name
    String EMAIL_PATTERN = "^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

    // Accept only 10 number character
    // Examples of valid phone numbers:
    // 0355034078
    String PHONE_PATTERN = "^\\d{10}$";
}
