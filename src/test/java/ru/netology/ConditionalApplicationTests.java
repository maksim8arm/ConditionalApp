package ru.netology;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConditionalApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    private static final GenericContainer<?> devappTest = new GenericContainer<>("devapp:ver.1").withExposedPorts(8080);
    private static final GenericContainer<?> prodappTest = new GenericContainer<>("prodapp:ver.1").withExposedPorts(8081);

    @BeforeAll
    public static void setUp() {
        devappTest.start();
        prodappTest.start();
    }

    @Test
    void answerFromDev() {
        Integer devappPort = devappTest.getMappedPort(8080);

        ResponseEntity<String> forEntity1 = restTemplate.getForEntity("http://localhost:" + devappPort + "/profile", String.class);

        Assert.assertEquals("Current profile is dev", forEntity1.getBody());
    }

    @Test
    void answerFromProd() {
        Integer prodappPort = prodappTest.getMappedPort(8081);

        ResponseEntity<String> forEntity2 = restTemplate.getForEntity("http://localhost:" + prodappPort + "/profile", String.class);

        Assert.assertEquals("Current profile is production", forEntity2.getBody());
    }
}