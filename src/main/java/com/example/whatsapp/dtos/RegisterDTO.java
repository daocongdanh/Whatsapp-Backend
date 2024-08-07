package com.example.whatsapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

    @NotBlank(message = "FullName không được rỗng")
    private String fullName;

    @NotBlank(message = "Email không được rỗng")
    private String email;

    @NotBlank(message = "Password không được rỗng")
    private String password;

    @NotBlank(message = "Avatar không được rỗng")
    private String avatar;
}
