package org.infokin.chatapp;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

  @Bean
  public Faker produceFaker() {
    return new Faker();
  }

}
