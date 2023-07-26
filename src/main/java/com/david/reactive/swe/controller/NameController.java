package com.david.reactive.swe.controller;

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

  @PostMapping(value = "/uppercase", consumes = MediaType.TEXT_PLAIN_VALUE)
  Mono<String> uppercase(
      //@RequestBody String reqBody,
      ServerWebExchange ex) throws IOException {
    Assert.isTrue(ex.getRequest().getHeaders().getContentType().getCharset().contains(StandardCharsets.UTF_8));
    return getRequestBody(ex)
        .map(String::toUpperCase);
  }

  private Mono<String> getRequestBody(ServerWebExchange swe) throws IOException {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      return swe.getRequest().getBody()
          .doOnNext(buffer -> copy(buffer.asInputStream(), baos))
          .then(Mono.defer(() -> Mono.just(baos.toString(StandardCharsets.UTF_8))));
    }
  }

  void copy(InputStream is, OutputStream os) {
    try {
        IOUtils.copy(is, os);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
