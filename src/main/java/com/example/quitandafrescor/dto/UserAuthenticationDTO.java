package com.example.quitandafrescor.dto;

import jakarta.validation.constraints.Email;

public record UserAuthenticationDTO(@Email String email, String password) {

}
