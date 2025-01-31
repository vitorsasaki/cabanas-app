package com.sasaki.cabanas_app.service;

import com.sasaki.cabanas_app.domain.pessoa.Pessoa;
import com.sasaki.cabanas_app.domain.pessoa.PessoaRequestDTO;
import com.sasaki.cabanas_app.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository repository;

    private Pessoa pessoa1;
    private Pessoa pessoa2;
    private PessoaRequestDTO pessoaRequestDTO;

    @Captor
    private ArgumentCaptor<Pessoa> pessoaArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pessoa1 = new Pessoa(1, "Naldo", "67991445566");
        pessoa2 = new Pessoa(2, "Rafa Naldo", "67981425698");
        pessoaRequestDTO = new PessoaRequestDTO("Naldo Atualizado", "67999887766");


    }

    @Nested
    class salvarPessoa {

        @Test
        @DisplayName("Testar se esta sendo salvo o objeto Pessoa")
        void testarSalvarPessoa() throws Exception {
            //Arrange

            //Act
            pessoaService.salvar(pessoa1);

            //Assert
            //Verifico se o objeto que esta sendo salvo é o mesmo objeto de retorno
            then(repository).should().save(pessoaArgumentCaptor.capture());
            Pessoa pessoaSalva = pessoaArgumentCaptor.getValue();
            assertEquals(pessoa1, pessoaSalva);
            verify(repository, times(1)).save(pessoa1);
        }

        @Test
        @DisplayName("Testar o metodo que verifica que não tem cadastro pelo celular")
        void verificaPessoaNaoCadastradaTest(){

            when(repository.findByCelular(pessoa1.getCelular())).thenReturn(Optional.empty());

            pessoaService.verificaPessoaJaCadastrada(pessoa1.getCelular());

            verify(repository, times(1)).findByCelular(pessoa1.getCelular());

        }

        @Test
        @DisplayName("Testar o metodo que verifica que ja tem cadastro pelo celular")
        void verificaPessoaJaCadastradaTest(){
            // Arranjo: Simula que a pessoa já está cadastrada
            when(repository.findByCelular(pessoa1.getCelular())).thenReturn(Optional.of(pessoa1));

            // Ação: Chama o método e espera a exceção
            RuntimeException thrown = assertThrows(RuntimeException.class, ()->{pessoaService.verificaPessoaJaCadastrada(pessoa1.getCelular());});

            // Verificação: Verifica se a mensagem da exceção é a esperada
            assertEquals("Este Cliente ja possui cadastro com esse telefone!", thrown.getMessage());

            // Verifica que o método findByCelular foi chamado 1 vez
            verify(repository, times(1)).findByCelular(pessoa1.getCelular());

        }

    }

    @Nested
    class buscarPessoas{

        @Test
        @DisplayName("Testando o retorno de lista de pesssoas")
        void buscarPessoasCadastradasTest(){
            when(repository.findAll()).thenReturn(Arrays.asList(pessoa1, pessoa2));

            List<Pessoa> pessoas = pessoaService.buscarPessoas();

            assertNotNull(pessoas);
            assertEquals(2, pessoas.size());
            assertTrue(pessoas.contains(pessoa1));
            assertTrue(pessoas.contains(pessoa2));

            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("Testando buscar pessoas pelo id")
        void buscarPessoaIdTest() throws Exception {
            Long id = 1L;
            when(repository.findById(id)).thenReturn(Optional.of(pessoa1));

            Optional<Pessoa> result = pessoaService.buscarPessoaId(id);

            assertNotNull(result);
            assertTrue(result.isPresent());
            assertEquals(pessoa1, result.get());

            verify(repository, times(1)).findById(id);
        }

        @Test
        @DisplayName("Testando se o retorno passando id é vazio")
        void buscarPessoaIdTestSemRetorno() {
            Long id = 1L;
            when(repository.findById(id)).thenReturn(Optional.empty());

            RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
                pessoaService.buscarPessoaId(id);
            });

            assertEquals("Cliente não encontrado na base de dados!", thrown.getMessage());

        }
    }

    @Nested
    class updatePessoas{

        @Test
        @DisplayName("Testa a atualização de um pessoa com sucesso")
        void updatePessoaComSucessoTest()throws Exception{
            when(repository.findById(1L)).thenReturn(Optional.of(pessoa1));
            when(repository.findByCelular(pessoaRequestDTO.celular())).thenReturn(Optional.empty());
            when(repository.save(any(Pessoa.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Optional<Pessoa> pessoaAtualizada = pessoaService.updatePessoa(1L, pessoaRequestDTO);

            assertTrue(pessoaAtualizada.isPresent());
            assertEquals(pessoaRequestDTO.nome(), pessoaAtualizada.get().getNome());
            assertEquals(pessoaRequestDTO.celular(), pessoaAtualizada.get().getCelular());

            verify(repository, times(1)).save(pessoa1);
        }

        @Test
        @DisplayName("Testa se o id passado para atualização se encontra no banco de dados")
        void updatePessoaExisteNobanco(){
            when(repository.findById(1L)).thenReturn(Optional.empty());

            Exception exception = assertThrows(Exception.class, ()->{pessoaService.updatePessoa(1L, pessoaRequestDTO);});

            assertEquals("Cliente não encontrado na base de dados!", exception.getMessage());
        }

        @Test
        @DisplayName("Testa se o novo numero alterado não consta na base de dados")
        void updatePessoaVerificaCelularJaExiste(){
            when(repository.findById(1L)).thenReturn(Optional.of(pessoa1));
            when(repository.findByCelular(pessoaRequestDTO.celular())).thenReturn(Optional.of(pessoa2));

            Exception exception = assertThrows(RuntimeException.class, ()->{pessoaService.updatePessoa(1L, pessoaRequestDTO);});

            assertEquals("O celular fornecido ja esta cadastrado em outra pessoa", exception.getMessage());
        }
    }



}