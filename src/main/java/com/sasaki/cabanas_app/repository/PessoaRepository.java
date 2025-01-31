package com.sasaki.cabanas_app.repository;

import com.sasaki.cabanas_app.domain.pessoa.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {


    Optional<Pessoa> findByCelular(String celular);

    @Override
    Optional<Pessoa> findById(Long aLong);
}
