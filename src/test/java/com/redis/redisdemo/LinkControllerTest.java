package com.redis.redisdemo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = LinkController.class)
public class LinkControllerTest {

	@Autowired
	private WebTestClient webTestClient;
	
	@MockBean
	private LinkService linkService;
	
	@SuppressWarnings("deprecation")
	@Test
	public void shortensLink() {
		
		when(linkService.shortenLink("https://spring.io")).thenReturn(Mono.just("http://localhost:8080/aass2211"));
		
		webTestClient.post()
					.uri("/link")
					.contentType(MediaType.APPLICATION_JSON)
					.syncBody("{\"link\" : \"https://spring.io\"}")
					.exchange()
					.expectStatus()
					.is2xxSuccessful()
					.expectBody()
					.jsonPath("$.shortnedLink")
					.value(val -> assertThat(val).isEqualTo("http://localhost:8080/aass2211"));
	}	
	
	@Test
	public void redirectToOriginalLink() {
		
		when(linkService.getOriginalLink("aaa21123")).thenReturn(Mono.just(new Link("http://spring.io", "aaa21123")));
		
		webTestClient.get()
					.uri("/aaa21123")
					.exchange()
					.expectStatus()
					.isPermanentRedirect()
					.expectHeader()
					.value("Location", location -> assertThat(location).isEqualTo("http://spring.io"));
	}

}
