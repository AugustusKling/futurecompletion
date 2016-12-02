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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.jodah.concurrentunit.Waiter;

import org.junit.Test;

public class AllOfTest {

	@Test
	public void allOf1() throws TimeoutException {
		Waiter waiter = new Waiter();

		FutureCompletion<String> stringFuture = FutureCompletion
				.completedFutureCompletion("test");

		FutureCompletion<List<String>> combi = FutureCompletion
				.allOf(stringFuture);

		combi.thenAccept(list -> {
			waiter.assertNotNull(list);
			waiter.assertEquals(1, list.size());
			waiter.assertEquals("test", list.get(0));
			waiter.resume();
		});

		waiter.await(1, TimeUnit.SECONDS);
	}

	@Test
	public void allOf2() throws TimeoutException {
		Waiter waiter = new Waiter();

		FutureCompletion<Integer> intFuture = FutureCompletion
				.completedFutureCompletion(3);
		FutureCompletion<Long> longFuture = FutureCompletion
				.completedFutureCompletion(7L);

		FutureCompletion2<? extends Number, Integer, Long> combi = FutureCompletion
				.allOf(intFuture, longFuture);

		combi.thenAccept(list -> {
			waiter.assertNotNull(list);
			waiter.assertEquals(2, list.size());
			waiter.assertEquals(3, list.get(0));
			waiter.assertEquals(7L, list.get(1));

			waiter.resume();
		});
		combi.thenAccept((intValue, longValue) -> {
			waiter.assertEquals(3, intValue);
			waiter.assertEquals(7L, longValue);

			waiter.resume();
		});

		FutureCompletion<String> classNames = combi.thenApply((intValue,
				longValue) -> intValue.getClass().getName() + " " + intValue
				+ ", " + longValue.getClass().getName() + " " + longValue);

		classNames.thenAccept(test -> {
			waiter.assertEquals("java.lang.Integer 3, java.lang.Long 7", test);

			waiter.resume();
		});

		waiter.await(1, TimeUnit.SECONDS, 3);
	}

	@Test
	public void emptyList() throws TimeoutException {
		Waiter waiter = new Waiter();

		FutureCompletion<List<String>> transposed = FutureCompletion
				.allOf(Collections.<CompletableFuture<String>> emptyList());

		transposed.thenAccept(list -> {
			assertTrue(list.isEmpty());

			waiter.resume();
		});

		waiter.await(1, TimeUnit.SECONDS);
	}

	@Test
	public void emptySet() throws TimeoutException {
		Waiter waiter = new Waiter();

		FutureCompletion<List<String>> transposed = FutureCompletion
				.allOf(Collections.<CompletableFuture<String>> emptySet());

		transposed.thenAccept(list -> {
			assertTrue(list.isEmpty());

			waiter.resume();
		});

		waiter.await(1, TimeUnit.SECONDS);
	}

	@Test
	public void emptySetCompletionStage() throws TimeoutException {
		Waiter waiter = new Waiter();

		FutureCompletion<List<String>> transposed = FutureCompletion
				.allOf(Collections.<CompletionStage<String>> emptySet());

		transposed.thenAccept(list -> {
			assertTrue(list.isEmpty());

			waiter.resume();
		});

		waiter.await(1, TimeUnit.SECONDS);
	}

	@Test
	public void singleElement() throws TimeoutException {
		Waiter waiter = new Waiter();

		Set<FutureCompletion<String>> stages = Collections
				.singleton(FutureCompletion.completedFutureCompletion("test"));
		FutureCompletion<List<String>> transposed = FutureCompletion
				.allOf(stages);

		transposed.thenAccept(list -> {
			assertEquals(1, list.size());
			assertEquals("test", list.get(0));

			waiter.resume();
		});

		waiter.await(1, TimeUnit.SECONDS);
	}

	@Test
	public void someElements() throws TimeoutException {
		Waiter waiter = new Waiter();

		List<CompletionStage<CharSequence>> stages = Arrays.asList(
				FutureCompletion.completedFutureCompletion("test0"),
				FutureCompletion.completedFutureCompletion("test1"),
				FutureCompletion.completedFutureCompletion("test2"),
				FutureCompletion.completedFutureCompletion("test3"),
				CompletableFuture
						.completedFuture(new StringBuffer("more test")));
		FutureCompletion<List<CharSequence>> transposed = FutureCompletion
				.allOf(stages);

		transposed.thenAccept(list -> {
			assertEquals(5, list.size());
			assertEquals("test0", list.get(0));
			assertEquals("test1", list.get(1));
			assertEquals("test2", list.get(2));
			assertEquals("test3", list.get(3));
			assertEquals("more test", list.get(4).toString());

			waiter.resume();
		});

		waiter.await(1, TimeUnit.SECONDS);
	}

	@Test
	public void failsElements() throws TimeoutException {
		Waiter waiter = new Waiter();

		CompletableFuture<Number> toFail = new CompletableFuture<>();
		List<CompletionStage<? extends Serializable>> stages = Arrays.asList(
				FutureCompletion.completedFutureCompletion("test"), toFail);
		FutureCompletion<List<Serializable>> transposed = FutureCompletion
				.allOf(stages);

		transposed.whenComplete((list, throwable) -> {
			assertNotNull(throwable);
			assertEquals("test failure", throwable.getMessage());

			waiter.resume();
		});
		toFail.completeExceptionally(new Exception("test failure"));

		waiter.await(1, TimeUnit.SECONDS);
	}
}
