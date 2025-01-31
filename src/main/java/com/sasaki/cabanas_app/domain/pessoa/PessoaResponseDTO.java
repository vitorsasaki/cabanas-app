package com.sasaki.cabanas_app.domain.pessoa;

public record PessoaResponseDTO(Long id, String nome, String celular) {

   /* public PessoaResponseDTO(Pessoa pessoa){
        this(pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCelular());
    }*/
}
