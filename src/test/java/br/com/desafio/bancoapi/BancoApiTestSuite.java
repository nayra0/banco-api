package br.com.desafio.bancoapi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AgenciaResourceIT.class, BancoResourceIT.class, ClienteResourceIT.class,
    ContaResourceIT.class, TransacaoResourceIT.class})
public class BancoApiTestSuite {

}
