package com.github.augustuskling.futurecompletion;

import java.util.concurrent.CompletableFuture;

import javax.annotation.CheckReturnValue;

public class FutureCompletionPromise<T> {
	private CompletableFuture<T> delegate;

	public FutureCompletionPromise() {
		delegate = new CompletableFuture<>();
	}

	public boolean complete(T value) {
		return delegate.complete(value);
	}

	public boolean completeExceptionally(Throwable ex) {
		return delegate.completeExceptionally(ex);
	}

	@CheckReturnValue
	public FutureCompletion<T> toFutureCompletion() {
		return new FutureCompletion<T>(delegate);
	}
}
