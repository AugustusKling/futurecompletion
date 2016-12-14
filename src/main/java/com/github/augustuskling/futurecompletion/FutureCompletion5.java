package com.github.augustuskling.futurecompletion;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.annotation.CheckReturnValue;

import com.github.augustuskling.futurecompletion.function.Consumer5;
import com.github.augustuskling.futurecompletion.function.Function5;

public class FutureCompletion5<MostSpecificSupertype, T0, T1, T2, T3, T4>
		extends FutureCompletion<List<MostSpecificSupertype>> {

	public FutureCompletion5(
			CompletionStage<List<MostSpecificSupertype>> delegate) {
		super(delegate);
	}

	@SuppressWarnings("unchecked")
	@CheckReturnValue
	public FutureCompletion<Void> thenAccept(Consumer5<T0, T1, T2, T3, T4> fn) {
		return thenAccept(l -> fn.accept((T0) l.get(0), (T1) l.get(1),
				(T2) l.get(2), (T3) l.get(3), (T4) l.get(4)));
	}

	@CheckReturnValue
	public <U> FutureCompletion<U> thenApply(
			Function5<T0, T1, T2, T3, T4, ? extends U> fn) {
		return thenApply(apply(fn));
	}

	@CheckReturnValue
	public <U> FutureCompletion<U> thenCompose(
			Function5<T0, T1, T2, T3, T4, ? extends CompletionStage<U>> fn) {
		return thenCompose(apply(fn));
	}

	@SuppressWarnings("unchecked")
	private <U> Function<? super List<MostSpecificSupertype>, ? extends U> apply(
			Function5<T0, T1, T2, T3, T4, ? extends U> fn) {
		return l -> fn.apply((T0) l.get(0), (T1) l.get(1), (T2) l.get(2),
				(T3) l.get(3), (T4) l.get(4));
	}
}
