package com.sasaki.cabanas_app.service;

import com.sasaki.cabanas_app.domain.pessoa.Pessoa;
import com.sasaki.cabanas_app.domain.pessoa.PessoaRequestDTO;
import com.sasaki.cabanas_app.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PessoaService {

    @Autowired
    private PessoaRepository repository;

    @Transactional
    public Pessoa salvar(Pessoa pessoa) throws Exception{
        verificaPessoaJaCadastrada(pessoa.getCelular());
        return repository.save(pessoa);
    }

    public Optional<Pessoa> verificaPessoaJaCadastrada(String celular){
        Optional<Pessoa> newPessoa = repository.findByCelular(celular);

        if(newPessoa.isPresent()){
            throw new RuntimeException("Este Cliente ja possui cadastro com esse telefone!");
        }

        return newPessoa;

    }

    public List<Pessoa> buscarPessoas(){
        return repository.findAll();
    }

    public Optional<Pessoa> buscarPessoaId(Long id) throws Exception{
        Optional<Pessoa> pessoa = repository.findById(id);
        if(pessoa.isEmpty()){
            throw new RuntimeException("Cliente não encontrado na base de dados!");
        }
        return pessoa;
    }

    public Optional<Pessoa> updatePessoa(Long id, PessoaRequestDTO pessoaRequestDTO) throws Exception {
        Optional<Pessoa> pessoaNew = buscarPessoaId(id);
        var pessoa = pessoaNew.get();

        Optional<Pessoa> pessoaComMesmoCelular = repository.findByCelular(pessoaRequestDTO.celular());
        if(pessoaComMesmoCelular.isPresent() && pessoaComMesmoCelular.get().getId() != id)
            throw new RuntimeException("O celular fornecido ja esta cadastrado em outra pessoa");

        BeanUtils.copyProperties(pessoaRequestDTO, pessoa);

        return Optional.of(repository.save(pessoa));

    }
}
