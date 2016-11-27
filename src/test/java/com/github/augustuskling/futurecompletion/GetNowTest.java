package com.github.augustuskling.futurecompletion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class GetNowTest {

	@Test
	public void notYetDone() {
		CompletableFuture<String> delegate = new CompletableFuture<>();
		FutureCompletion<String> fc = new FutureCompletion<>(delegate);
		try {
			fc.getNow();
			fail();
		} catch (IllegalStateException e) {
			assertEquals("Computation still ongoning, value not available.",
					e.getMessage());
		}
	}

	@Test
	public void resolved() {
		FutureCompletion<String> fc = FutureCompletion
				.completedFutureCompletion("test ok");
		assertEquals("test ok", fc.getNow());
	}

	@Test
	public void rejected() {
		FutureCompletion<String> fc = FutureCompletion
				.failedFutureCompletion(new Exception("test rejection"));
		try {
			fc.getNow();
			fail();
		} catch (CompletionException e) {
			assertEquals("test rejection", e.getCause().getMessage());
		}
	}

	@Test
	public void computationFails() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		FutureCompletion<?> fc = new FutureCompletion<>(
				CompletableFuture.supplyAsync(() -> {
					throw new RuntimeException("test: failure");
				})).whenComplete((value, th) -> latch.countDown());

		assertTrue(latch.await(1, TimeUnit.SECONDS));

		try {
			fc.getNow();
			fail();
		} catch (CompletionException e) {
			assertEquals("test: failure", e.getCause().getMessage());
		}
	}
}
