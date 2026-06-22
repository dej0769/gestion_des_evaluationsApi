package com.example.Gestion_des_evaluations.Auth.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOtpRequestDTO {
    @NotNull
    private String email;

    @NotNull
    private Integer code;
}