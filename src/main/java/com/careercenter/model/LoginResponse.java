package com.careercenter.model;

import lombok.Data;

@Data
public class LoginResponse {
	
    private String token;
    private String tokenType = "Bearer";

    public LoginResponse(String token) {
        this.token = token;
    }

}
