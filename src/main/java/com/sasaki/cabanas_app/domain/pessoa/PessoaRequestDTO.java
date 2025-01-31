package com.sasaki.cabanas_app.domain.pessoa;


import jakarta.validation.constraints.NotBlank;

public record PessoaRequestDTO(
        @NotBlank
        String nome,
        @NotBlank
        String celular
) {
}
