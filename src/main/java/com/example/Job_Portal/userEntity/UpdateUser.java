package com.example.Job_Portal.userEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class UpdateUser {
    private String username;
    private String updatename;
    private String email;
    private String password;
}
