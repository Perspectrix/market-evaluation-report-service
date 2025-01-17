package com.perspectrix.market.services;

import com.perspectrix.market.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class PersonService {
    private final WebClient webClient;

    public PersonService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Person getPerson(String id) {
        return webClient.get()
                .uri("/api/read/{id}", id)
                .retrieve()
                .bodyToMono(Person.class)
                .block();
    }
}
