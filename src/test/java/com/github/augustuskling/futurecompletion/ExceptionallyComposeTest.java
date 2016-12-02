package com.github.augustuskling.futurecompletion;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.jodah.concurrentunit.Waiter;

import org.junit.Test;

public class ExceptionallyComposeTest {

	@Test
	public void okay() throws TimeoutException {
		Waiter waiter = new Waiter();

		FutureCompletion<String> fc = FutureCompletion
				.completedFutureCompletion("test");
		FutureCompletion<String> res = fc
				.exceptionallyCompose(th -> FutureCompletion
						.completedFutureCompletion("test2"));
		res.thenAccept(resValue -> {
			waiter.assertEquals("test", resValue);
			waiter.resume();
		});

		waiter.await(1, TimeUnit.SECONDS);
	}

	@Test
	public void failToOkay() throws TimeoutException {
		Waiter waiter = new Waiter();

		FutureCompletion<String> fc = FutureCompletion
				.failedFutureCompletion(new Exception("test"));
		FutureCompletion<String> res = fc.exceptionallyCompose(th -> {
			waiter.assertEquals("test", th.getMessage());
			return FutureCompletion.completedFutureCompletion("test2");
		});
		res.thenAccept(resValue -> {
			waiter.assertEquals("test2", resValue);
			waiter.resume();
		});

		waiter.await(1, TimeUnit.SECONDS);
	}

	@Test
	public void wrapFail() throws TimeoutException {
		Waiter waiter = new Waiter();

		FutureCompletion<String> fc = FutureCompletion
				.failedFutureCompletion(new Exception("test"));
		FutureCompletion<String> res = fc.exceptionallyCompose(th -> {
			waiter.assertEquals("test", th.getMessage());
			return FutureCompletion.failedFutureCompletion(new Exception(
					"test2", th));
		});
		res.whenComplete((resValue, th) -> {
			waiter.assertNotNull(th);
			waiter.assertEquals("test2", th.getMessage());
			waiter.assertEquals("test", th.getCause().getMessage());
			waiter.resume();
		});

		waiter.await(1, TimeUnit.SECONDS);
	}

}
