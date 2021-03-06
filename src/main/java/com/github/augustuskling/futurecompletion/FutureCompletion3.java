package com.github.augustuskling.futurecompletion;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.annotation.CheckReturnValue;

import com.github.augustuskling.futurecompletion.function.Consumer3;
import com.github.augustuskling.futurecompletion.function.Function3;

public class FutureCompletion3<MostSpecificSupertype, T0, T1, T2> extends
		FutureCompletion<List<MostSpecificSupertype>> {

	public FutureCompletion3(
			CompletionStage<List<MostSpecificSupertype>> delegate) {
		super(delegate);
	}

	@SuppressWarnings("unchecked")
	@CheckReturnValue
	public FutureCompletion<Void> thenAccept(Consumer3<T0, T1, T2> fn) {
		return thenAccept(l -> fn.accept((T0) l.get(0), (T1) l.get(1),
				(T2) l.get(2)));
	}

	@CheckReturnValue
	public <U> FutureCompletion<U> thenApply(
			Function3<T0, T1, T2, ? extends U> fn) {
		return thenApply(apply(fn));
	}

	@CheckReturnValue
	public <U> FutureCompletion<U> thenCompose(
			Function3<T0, T1, T2, ? extends CompletionStage<U>> fn) {
		return thenCompose(apply(fn));
	}

	@SuppressWarnings("unchecked")
	private <U> Function<? super List<MostSpecificSupertype>, ? extends U> apply(
			Function3<T0, T1, T2, ? extends U> fn) {
		return l -> fn.apply((T0) l.get(0), (T1) l.get(1), (T2) l.get(2));
	}
}
