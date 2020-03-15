package br.com.desafio.bancoapi.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig {

  @Bean
  public Docket bancoApi() {
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage("br.com.desafio.bancoapi"))
        .paths(PathSelectors.any()).build().apiInfo(metaInfo());
  }

  private ApiInfo metaInfo() {
    ApiInfo apiInfo = new ApiInfo("Banco API REST", "API criada para o desafio Infoway", "1.0",
        "Termos de Serviço", new Contact("Nayra Oliveira", "url", "nayrabiaoliveira@gmail.com"),
        "License of API", "API license URL", Collections.emptyList());
    return apiInfo;
  }

}
