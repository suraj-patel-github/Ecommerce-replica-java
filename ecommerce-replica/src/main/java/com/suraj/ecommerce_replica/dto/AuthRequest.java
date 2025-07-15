package com.suraj.ecommerce.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AuthRequest {
    private String username;
    private String password;
    private String email;
    private String role;
}
