package com.example.whatsapp.dtos;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @NotBlank(message = "Email không được rỗng")
    private String email;

    @NotBlank(message = "Password không được rỗng")
    private String password;
}