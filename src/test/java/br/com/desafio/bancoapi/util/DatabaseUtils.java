package br.com.desafio.bancoapi.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseUtils {

  @Autowired
  private DataSource dataSource;

  private Connection connection;

  public void apagarTabelas() {
    try (Connection connection = dataSource.getConnection()) {
      this.connection = connection;

      Statement statement = connection.createStatement();

      statement.addBatch("DELETE FROM CONTA_TRANSACOES;");
      statement.addBatch("DELETE FROM TRANSACAO;");
      statement.addBatch("DELETE FROM CONTA;");
      statement.addBatch("DELETE FROM CLIENTE;");
      statement.addBatch("DELETE FROM AGENCIA;");
      statement.addBatch("DELETE FROM BANCO;");
      statement.addBatch("alter sequence banco_seq restart with 1;");
      statement.addBatch("alter sequence agencia_seq restart with 1;");
      statement.addBatch("alter sequence cliente_seq restart with 1;");
      statement.addBatch("alter sequence conta_seq restart with 1;");
      statement.addBatch("alter sequence transacao_seq restart with 1;");
      statement.executeBatch();


    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      this.connection = null;
    }
  }


}
