package com.kellymariejones.activitiesforwellness.models.DTO;

// Our registration form will ask for a username/password pair,
// and then ask the user to confirm the password by typing it in again.
// So the associated DTO can extend LoginFormDTO and add an additional field
// for password verification.
public class RegisterFormDTO extends LoginFormDTO {

    private String verifyPassword;

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

}