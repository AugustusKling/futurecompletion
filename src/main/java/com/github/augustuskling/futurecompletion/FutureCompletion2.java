package com.github.augustuskling.futurecompletion;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.CheckReturnValue;

public class FutureCompletion2<MostSpecificSupertype, T0, T1> extends
		FutureCompletion<List<MostSpecificSupertype>> {

	public FutureCompletion2(
			CompletionStage<List<MostSpecificSupertype>> delegate) {
		super(delegate);
	}

	@SuppressWarnings("unchecked")
	@CheckReturnValue
	public FutureCompletion<Void> thenAccept(BiConsumer<T0, T1> fn) {
		return thenAccept(l -> fn.accept((T0) l.get(0), (T1) l.get(1)));
	}

	@CheckReturnValue
	public <U> FutureCompletion<U> thenApply(BiFunction<T0, T1, ? extends U> fn) {
		return thenApply(apply(fn));
	}

	@CheckReturnValue
	public <U> FutureCompletion<U> thenCompose(
			BiFunction<T0, T1, ? extends CompletionStage<U>> fn) {
		return thenCompose(apply(fn));
	}

	@SuppressWarnings("unchecked")
	private <U> Function<? super List<MostSpecificSupertype>, ? extends U> apply(
			BiFunction<T0, T1, ? extends U> fn) {
		return l -> fn.apply((T0) l.get(0), (T1) l.get(1));
	}
}
