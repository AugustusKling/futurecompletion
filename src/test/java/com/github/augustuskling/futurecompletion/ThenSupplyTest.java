package com.github.augustuskling.futurecompletion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.github.augustuskling.futurecompletion.FutureCompletion;

public class ThenSupplyTest {
	@Test
	public void thenSupplyAlreadyResolved() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		FutureCompletion<String> fc = FutureCompletion
				.completedFutureCompletion("some thing");
		CompletionStage<String> resultFuture = fc.thenSupply(
				() -> "other thing").whenComplete((result, throwable) -> {
			assertEquals("other thing", result);

			latch.countDown();
		});

		assertNotNull(resultFuture);

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}

	@Test
	public void thenSupplyFails() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		FutureCompletion<String> fc = FutureCompletion
				.completedFutureCompletion("some thing");
		CompletionStage<Object> resultFuture = fc.thenSupply(() -> {
			throw new RuntimeException("testfailure");
		}).whenComplete((result, throwable) -> {
			assertTrue(throwable instanceof CompletionException);
			assertEquals("testfailure", throwable.getCause().getMessage());

			latch.countDown();
		});

		assertNotNull(resultFuture);

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}

	@Test
	public void thenSupplyAlreadyFailed() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		CompletableFuture<String> fut = new CompletableFuture<>();
		fut.completeExceptionally(new Exception("rejected"));
		FutureCompletion<String> fc = new FutureCompletion<>(fut);
		CompletionStage<String> resultFuture = fc.thenSupply(() -> {
			fail();
			return null;
		});

		assertNotNull(resultFuture);

		resultFuture.whenComplete((result, throwable) -> {
			assertTrue(throwable instanceof CompletionException);
			assertEquals("rejected", throwable.getCause().getMessage());

			latch.countDown();
		});

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}

	@Test
	public void thenSupplyLaterResolved() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		CompletableFuture<String> fut = new CompletableFuture<>();
		FutureCompletion<String> fc = new FutureCompletion<>(fut);
		CompletionStage<String> resultFuture = fc.thenSupply(
				() -> "other thing").whenComplete((result, throwable) -> {
			assertEquals("other thing", result);

			latch.countDown();
		});

		assertNotNull(resultFuture);

		fut.complete("some thing");

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}
}
