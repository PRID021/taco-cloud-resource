package sia.tacocloud.models;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Data
public class RegistrationForm {
    private String userRegisterName;
    private String userRegisterNameError;
    private String userRegisterPassword;
    private String userRegisterPasswordError;
    private String userRegisterPasswordConfirm;
    private String userRegisterPasswordConfirmError;
    private String fullname;
    private String fullnameError;
    private String street;
    private String streetError;
    private String city;
    private String cityError;
    private String state;
    private String stateError;
    private String zip;
    private String zipError;
    private String phone;
    private String phoneError;

    public TacoUser toUser(PasswordEncoder passwordEncoder) {
        return new TacoUser(
                userRegisterName, passwordEncoder.encode(userRegisterPassword),
                fullname, street, city, state, zip, phone);
    }

}
