package com.david.reactive.swe.controller;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class NameController {

  @PostMapping(value = "/uppercase", consumes = TEXT_PLAIN_VALUE, produces = TEXT_PLAIN_VALUE)
  public Mono<String> uppercase(ServerWebExchange serverWebExchange) {

    Assert.isTrue(serverWebExchange.getRequest().getHeaders().getContentType().getCharset()
        .contains(StandardCharsets.UTF_8), "The charset is not utf-8");

    return getRequestBody(serverWebExchange)
        .map(String::toUpperCase);
  }

  private Mono<String> getRequestBody(ServerWebExchange serverWebExchange) {
    try (var baos = new ByteArrayOutputStream()) {

      return serverWebExchange.getRequest().getBody()
          .doOnNext(buffer -> copy(buffer.asInputStream(), baos))
          .collectList()
          .map(it -> baos.toString(StandardCharsets.UTF_8));

    } catch (IOException ex) {
      return Mono.error(ex);
    }
  }

  private void copy(InputStream is, OutputStream os) {
    try {
      IOUtils.copy(is, os);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
