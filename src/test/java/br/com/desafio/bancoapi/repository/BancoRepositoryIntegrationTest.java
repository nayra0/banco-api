package br.com.desafio.bancoapi.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import br.com.desafio.bancoapi.model.Banco;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class BancoRepositoryIntegrationTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private BancoRepository repository;

  @Test
  public void givenEmployees_whenGetEmployees_thenStatus200() throws Exception {

    createTestBanco("123", "banco de teste");

    mvc.perform(get("/bancos").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect((ResultMatcher) content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect((ResultMatcher) jsonPath("$[0].name", is("123")));
  }

  private void createTestBanco(String codigo, String nome) {
    Banco b = new Banco();
    b.setCodigo(codigo);
    b.setNome(nome);
    repository.save(b);

  }

}
