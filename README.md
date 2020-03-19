# Projeto criado para o processo seletivo de desenvolvedor Java.

1. **Rodar aplicação:**
./mvnw spring-boot:run

2. **Testes**
Executar a suite de testes: BancoApiTestSuite

3. **Documentação dos métodos disponiveis:**
http://localhost:8080/swagger-ui.html

4. **OAuth 2**

Autenticação:
Usuário: admin
Senha: admin

**Cliente rest:**

client: cliente
password: cliente

**Exemplo para obter token:**

curl --location --request POST 'localhost:8080/oauth/token' \
--header 'Authorization: Basic Y2xpZW50ZTpjbGllbnRl' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client=cliente' \
--data-urlencode 'username=admin' \
--data-urlencode 'password=admin' \
--data-urlencode 'grant_type=password'

Cada requisição necessita que seja enviado o token Authorization no header
key: Authorization 
value: Bearer (token)

**Sacar de uma conta:**

Método: POST
URL:localhost:8080/transacoes/contas/{id}/sacar
Body (JSON) - Exemplo
{
"valor" : 100.0
}

**Depositar uma conta:**

Método: POST
URL:localhost:8080/transacoes/contas/{id}/depositar
Body (JSON) - Exemplo
```{
"valor" : 100.0
}
```

**Transferir entre contas:**

Método: POST
URL:localhost:8080/transacoes/contas/{idContaOrigem}/transferir/{idContaDestino}
Body (JSON) - Exemplo
```{
"valor" : 100.0
}
```

**Extrato de uma conta:**

Método: POST
URL:localhost:8080/transacaoes/contas/{idConta}/extrato


