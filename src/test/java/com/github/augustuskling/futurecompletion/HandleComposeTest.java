package com.github.augustuskling.futurecompletion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.jodah.concurrentunit.Waiter;

import org.junit.Test;

public class HandleComposeTest {

	@Test
	public void handleComposeResolvedSuccessToSuccess()
			throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		FutureCompletion<String> fc = FutureCompletion
				.completedFutureCompletion("test");
		FutureCompletion<String> changed = fc.handleCompose((result, th) -> {
			assertEquals("test", result);
			return CompletableFuture.completedFuture("test changed");
		});

		changed.thenAccept(value -> {
			assertEquals("test changed", value);
			latch.countDown();
		});

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}

	@Test
	public void handleComposeLaterResolvedSuccessToSuccess()
			throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		CompletableFuture<String> fut = new CompletableFuture<>();
		FutureCompletion<String> fc = FutureCompletion.toFutureCompletion(fut);
		FutureCompletion<String> changed = fc.handleCompose((result, th) -> {
			assertEquals("test", result);
			return CompletableFuture.completedFuture("test changed");
		});

		changed.thenAccept(value -> {
			assertEquals("test changed", value);
			latch.countDown();
		});

		fut.complete("test");

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}

	@Test
	public void handleComposeLaterResolvedSuccessToFailure()
			throws TimeoutException {
		Waiter waiter = new Waiter();

		CompletableFuture<String> fut = new CompletableFuture<>();
		FutureCompletion<String> fc = FutureCompletion.toFutureCompletion(fut);
		FutureCompletion<String> changed = fc.handleCompose((result, th) -> {
			waiter.assertEquals("test", result);
			waiter.resume();
			return FutureCompletion.failedFutureCompletion(new Exception(
					"testfailure"));
		});

		changed.thenAccept(value -> {
			waiter.fail();
		});
		changed.whenComplete((value, throwable) -> {
			waiter.assertNull(value);
			waiter.assertEquals("testfailure", throwable.getMessage());
			waiter.resume();
		});

		fut.complete("test");

		waiter.await(1, TimeUnit.SECONDS, 2);
	}

	@Test
	public void handleComposeLaterResolvedFailueToSuccess()
			throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		CompletableFuture<String> fut = new CompletableFuture<>();
		FutureCompletion<String> fc = FutureCompletion.toFutureCompletion(fut);
		FutureCompletion<String> changed = fc.handleCompose((result, th) -> {
			assertNull(result);
			assertEquals("fail 1", th.getMessage());
			return CompletableFuture.completedFuture("test ok");
		});

		changed.whenComplete((value, throwable) -> {
			assertNull(throwable);
			assertEquals("test ok", value);
			latch.countDown();
		});

		fut.completeExceptionally(new Exception("fail 1"));

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}

	@Test
	public void handleComposeLaterResolvedFailueToFailure()
			throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		CompletableFuture<String> fut = new CompletableFuture<>();
		FutureCompletion<String> fc = FutureCompletion.toFutureCompletion(fut);
		FutureCompletion<String> changed = fc.handleCompose((result, th) -> {
			assertNull(result);
			assertEquals("fail 1", th.getMessage());
			return FutureCompletion.failedFutureCompletion(new Exception(
					"fail 2"));
		});

		changed.thenAccept(value -> {
			fail();
		});
		changed.whenComplete((value, throwable) -> {
			assertNull(value);
			assertEquals("fail 2", throwable.getMessage());
			latch.countDown();
		});

		fut.completeExceptionally(new Exception("fail 1"));

		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}
}
