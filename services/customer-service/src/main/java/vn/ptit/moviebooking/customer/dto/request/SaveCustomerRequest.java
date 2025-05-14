package vn.ptit.moviebooking.customer.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import vn.ptit.moviebooking.customer.constants.RegexConstants;

public class SaveCustomerRequest {

    @NotNull
    @Min(value = 1, message = "Customer ID invalid")
    private Integer id;

    @NotBlank(message = "Fullname cannot be blank")
    private String fullname;

    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = RegexConstants.EMAIL_PATTERN, message = "Email is invalid format")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = RegexConstants.PHONE_PATTERN, message = "Phone number is invalid format")
    private String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
