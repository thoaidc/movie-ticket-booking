package vn.ptit.moviebooking.customer.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vn.ptit.moviebooking.customer.constants.RegexConstants;

public class VerifyCustomerRequest {

    @NotNull(message = "BookingID cannot be null")
    @Min(value = 1, message = "Invalid bookingId")
    private Integer bookingId;

    @NotBlank(message = "Fullname cannot be blank")
    @Size(max = 150, message = "Fullname exceeds allowed length")
    private String fullname;

    @NotBlank(message = "Email cannot be blank")
    @Size(max = 100, message = "Email exceeds allowed length")
    @Pattern(regexp = RegexConstants.EMAIL_PATTERN, message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Size(max = 20, message = "Phone number exceeds allowed length")
    @Pattern(regexp = RegexConstants.PHONE_PATTERN, message = "Invalid phone number format")
    private String phone;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
