package com.github.augustuskling.futurecompletion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TransposeTest {
	@Test
	public void emptyList() throws InterruptedException {
		FutureCompletion<List<String>> transposed = FutureCompletion
				.transpose(Collections.<CompletableFuture<String>> emptyList());
		CountDownLatch latch = new CountDownLatch(1);

		transposed.thenAccept(list -> {
			assertTrue(list.isEmpty());

			latch.countDown();
		});

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}

	@Test
	public void emptySet() throws InterruptedException {
		FutureCompletion<List<String>> transposed = FutureCompletion
				.transpose(Collections.<CompletableFuture<String>> emptySet());
		CountDownLatch latch = new CountDownLatch(1);

		transposed.thenAccept(list -> {
			assertTrue(list.isEmpty());

			latch.countDown();
		});

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}

	@Test
	public void emptySetCompletionStage() throws InterruptedException {
		FutureCompletion<List<String>> transposed = FutureCompletion
				.transpose(Collections.<CompletionStage<String>> emptySet());

		CountDownLatch latch = new CountDownLatch(1);

		transposed.thenAccept(list -> {
			assertTrue(list.isEmpty());

			latch.countDown();
		});

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}

	@Test
	public void singleElement() throws InterruptedException {
		Set<FutureCompletion<String>> stages = Collections
				.singleton(FutureCompletion.completedFutureCompletion("test"));
		FutureCompletion<List<String>> transposed = FutureCompletion
				.transpose(stages);

		CountDownLatch latch = new CountDownLatch(1);

		transposed.thenAccept(list -> {
			assertEquals(1, list.size());
			assertEquals("test", list.get(0));

			latch.countDown();
		});

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}

	@Test
	public void someElements() throws InterruptedException {
		List<CompletionStage<CharSequence>> stages = Arrays.asList(
				FutureCompletion.completedFutureCompletion("test"),
				CompletableFuture
						.completedFuture(new StringBuffer("more test")));
		FutureCompletion<List<CharSequence>> transposed = FutureCompletion
				.transpose(stages);

		CountDownLatch latch = new CountDownLatch(1);

		transposed.thenAccept(list -> {
			assertEquals(2, list.size());
			assertEquals("test", list.get(0));
			assertEquals("more test", list.get(1).toString());

			latch.countDown();
		});

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}

	@Test
	public void failsElements() throws InterruptedException {
		CompletableFuture<Number> toFail = new CompletableFuture<>();
		List<CompletionStage<? extends Serializable>> stages = Arrays.asList(
				FutureCompletion.completedFutureCompletion("test"), toFail);
		FutureCompletion<List<Serializable>> transposed = FutureCompletion
				.transpose(stages);

		CountDownLatch latch = new CountDownLatch(1);

		transposed.whenComplete((list, throwable) -> {
			assertNotNull(throwable);
			assertEquals("test failure", throwable.getMessage());

			latch.countDown();
		});
		toFail.completeExceptionally(new Exception("test failure"));

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}
}
