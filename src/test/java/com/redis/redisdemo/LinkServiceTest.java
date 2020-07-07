package com.redis.redisdemo;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class LinkServiceTest {
	
	private LinkRepository linkRepository = mock(LinkRepository.class);
	private LinkService linkService = new LinkService("http://some-domain.com", linkRepository);
	
	@Before
	public void setup() {
		
		when(linkRepository.save(any())).thenAnswer(new Answer<Mono<Link>>() {

			@Override
			public Mono<Link> answer(InvocationOnMock invocation) throws Throwable {
				
				return Mono.just((Link) invocation.getArguments()[0]);
			}
		});
	}
	
	@Test
	public void shortensLink() {
		
		StepVerifier.create(linkService.shortenLink("https://spring.io"))
					.expectNextMatches(result -> result != null && result.length() > 0
										&& result.startsWith(""))
					.expectComplete()
					.verify();
	}

}
