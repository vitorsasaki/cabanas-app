package com.sasaki.cabanas_app.domain.pessoa;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "pessoa")
@Entity(name = "pessoa")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;
    private String celular;

    public Pessoa(PessoaRequestDTO data){
        this.nome = data.nome();
        this.celular = data.celular();
    }

    public Pessoa(){

    }
}
