package com.example.whatsapp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendShipDTO {

    @NotNull(message = "User One ID không được null")
    private Long userOneId;

    @NotNull(message = "User Two ID không được null")
    private Long userTwoId;
}
