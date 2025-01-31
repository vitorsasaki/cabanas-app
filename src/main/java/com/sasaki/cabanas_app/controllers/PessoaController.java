package com.sasaki.cabanas_app.controllers;

import com.sasaki.cabanas_app.domain.pessoa.Pessoa;
import com.sasaki.cabanas_app.domain.pessoa.PessoaRequestDTO;
import com.sasaki.cabanas_app.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @PostMapping
    public ResponseEntity salvarPessoa(@RequestBody @Valid PessoaRequestDTO pessoaRequestDTO){
        try{
            Pessoa pessoa = pessoaService.salvar(new Pessoa(pessoaRequestDTO));
            return ResponseEntity.ok().body(pessoa);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> getAllPessoas(){
        return ResponseEntity.status(HttpStatus.OK).body(pessoaService.buscarPessoas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getIdPessoa(@PathVariable(value = "id") Long id){
        try {
            Optional<Pessoa> pessoa = pessoaService.buscarPessoaId(id);
            return ResponseEntity.ok().body(pessoa);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePessoa(@PathVariable(value = "id") Long id , @RequestBody @Valid PessoaRequestDTO pessoaRequestDTO){
        try {
            Optional<Pessoa> pessoa = pessoaService.updatePessoa(id, pessoaRequestDTO);
            return ResponseEntity.ok().body(pessoa);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
