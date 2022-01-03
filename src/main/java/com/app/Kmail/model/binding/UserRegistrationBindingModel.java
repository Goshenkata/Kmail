package com.app.Kmail.model.binding;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRegistrationBindingModel {
    @NotNull
    @Size(min = 5, max = 20)
    private String username;
    @NotNull
    @Size(min = 8, max = 32)
    private String password;
    private String confirmPassword;
    @NotNull
    @Size(min = 3, max = 32)
    private String firstName;
    @NotNull @Size(min = 3, max = 32) String lastName;

    public UserRegistrationBindingModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserRegistrationBindingModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserRegistrationBindingModel setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public UserRegistrationBindingModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserRegistrationBindingModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public String toString() {
        return "UserRegistrationBindingModel{" + "username='" + username + '\'' + ", password='" + password + '\'' + ", confirmPassword='" + confirmPassword + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + '}';
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


}
