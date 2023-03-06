package ru.netology.conditionalapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConditionalAppApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;
	private final GenericContainer<?> devapp = new GenericContainer<>("devapp").
			withExposedPorts(8080);
	private final GenericContainer<?> prodapp = new GenericContainer<>("prodapp").
			withExposedPorts(8081);

	@BeforeEach
	void setUp() {
		devapp.start();
		prodapp.start();
	}

	@Test
	void contextLoads() {
		Integer devappPort = devapp.getMappedPort(8080);
		Integer prodappPort = prodapp.getMappedPort(8081);

		ResponseEntity<String> responseFromDevApp = restTemplate.getForEntity("http://localhost:" + devappPort + "/profile",
				String.class);
		ResponseEntity<String> responseFromProdApp = restTemplate.getForEntity("http://localhost:" + prodappPort + "/profile",
				String.class);

		System.out.println(devappPort);
		System.out.println(prodappPort);
		System.out.println("devapp: " + responseFromDevApp);
		System.out.println("prodapp: " + responseFromProdApp);

		Assertions.assertEquals("Current profile is dev", responseFromDevApp.getBody());
		Assertions.assertEquals("Current profile is production", responseFromProdApp.getBody());
	}

}
