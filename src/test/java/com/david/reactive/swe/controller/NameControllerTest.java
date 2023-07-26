package com.david.reactive.swe.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class NameControllerTest {


  @Autowired
  WebTestClient client;

  @Test
  void testUpperCase() {
    client
        .post()
        .uri(URI.create("/uppercase"))
        .bodyValue("david")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE+"; charset=utf-8")
        .header(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN_VALUE)
        .header(HttpHeaders.ACCEPT_CHARSET, "utf-8")
        .exchange()
        .expectBody(String.class)
        .consumeWith(ent ->
            assertThat(ent.getResponseBody()).isEqualTo("DAVID"));
  }

}
