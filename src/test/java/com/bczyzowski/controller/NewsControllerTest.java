package com.bczyzowski.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import javax.inject.Inject;
import java.time.Month;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class NewsControllerTest {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    @Timeout(4)
    @Test
    void fetchingOctoberHeadlinesUsesCache() {
        var request = HttpRequest.GET(UriBuilder.of("/").path(Month.OCTOBER.name()).build());
        var news = client.toBlocking().retrieve(request, News.class);
        var expected = "Micronaut AOP: Awesome flexibility without the complexity";

        assertEquals(Collections.singletonList(expected), news.getHeadlines());

        news = client.toBlocking().retrieve(request, News.class);
        assertEquals(Collections.singletonList(expected), news.getHeadlines());
    }
}