package com.sasaki.cabanas_app.repository;

import com.sasaki.cabanas_app.domain.pessoa.Pessoa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PessoaRepositoryTest {

    @Autowired
    PessoaRepository pessoaRepository;

    @Test
    @DisplayName("Verifica se existe uma pessoa no banco de dados")
    void findByCelularCase1() {
        String celular = "67991025863";

        Optional<Pessoa> result = this.pessoaRepository.findByCelular(celular);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Verifica se não da erro ao retornar vazio")
    void findByCelularCase2() {
        String celular = "99999999999";

        Optional<Pessoa> result = this.pessoaRepository.findByCelular(celular);

        assertThat(result.isEmpty()).isTrue();
    }


}