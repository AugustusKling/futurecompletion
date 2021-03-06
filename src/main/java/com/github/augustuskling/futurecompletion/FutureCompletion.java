package com.github.augustuskling.futurecompletion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import javax.annotation.CheckReturnValue;

public class FutureCompletion<T> implements CompletionStage<T> {
	private CompletableFuture<T> delegate;

	public FutureCompletion(CompletionStage<T> delegate) {
		this.delegate = delegate.toCompletableFuture();
	}

	@CheckReturnValue
	@Override
	public <U> FutureCompletion<U> thenApply(Function<? super T, ? extends U> fn) {
		return toFutureCompletion(delegate.thenApply(fn));
	}

	@CheckReturnValue
	@Override
	public <U> FutureCompletion<U> thenApplyAsync(
			Function<? super T, ? extends U> fn) {
		return toFutureCompletion(delegate.thenApplyAsync(fn));
	}

	@CheckReturnValue
	@Override
	public <U> FutureCompletion<U> thenApplyAsync(
			Function<? super T, ? extends U> fn, Executor executor) {
		return toFutureCompletion(delegate.thenApplyAsync(fn, executor));
	}

	/**
	 * Ignores the {@link FutureCompletion}'s result and replaces it by the
	 * value returned by the provided {@link Supplier}.
	 * 
	 * @param supplier
	 *            Provides value in case of successful completion. Never called
	 *            in case this {@link FutureCompletion} resolves with an error.
	 * @return Dependent {@link FutureCompletion}.
	 */
	@CheckReturnValue
	public <U> FutureCompletion<U> thenSupply(Supplier<U> supplier) {
		CompletableFuture<U> transformed = delegate.thenApply(v -> supplier
				.get());
		return toFutureCompletion(transformed);
	}

	@Override
	public FutureCompletion<Void> thenAccept(Consumer<? super T> action) {
		return toFutureCompletion(delegate.thenAccept(action));
	}

	@Override
	public FutureCompletion<Void> thenAcceptAsync(Consumer<? super T> action) {
		return toFutureCompletion(delegate.thenAcceptAsync(action));
	}

	@Override
	public FutureCompletion<Void> thenAcceptAsync(Consumer<? super T> action,
			Executor executor) {
		return toFutureCompletion(delegate.thenAcceptAsync(action, executor));
	}

	@Override
	public FutureCompletion<Void> thenRun(Runnable action) {
		return toFutureCompletion(delegate.thenRun(action));
	}

	@Override
	public FutureCompletion<Void> thenRunAsync(Runnable action) {
		return toFutureCompletion(delegate.thenRunAsync(action));
	}

	@Override
	public FutureCompletion<Void> thenRunAsync(Runnable action,
			Executor executor) {
		return toFutureCompletion(delegate.thenRunAsync(action, executor));
	}

	@CheckReturnValue
	@Override
	public <U, V> FutureCompletion<V> thenCombine(
			CompletionStage<? extends U> other,
			BiFunction<? super T, ? super U, ? extends V> fn) {
		return toFutureCompletion(delegate.thenCombine(other, fn));
	}

	@CheckReturnValue
	@Override
	public <U, V> FutureCompletion<V> thenCombineAsync(
			CompletionStage<? extends U> other,
			BiFunction<? super T, ? super U, ? extends V> fn) {
		return toFutureCompletion(delegate.thenCombineAsync(other, fn));
	}

	@CheckReturnValue
	@Override
	public <U, V> FutureCompletion<V> thenCombineAsync(
			CompletionStage<? extends U> other,
			BiFunction<? super T, ? super U, ? extends V> fn, Executor executor) {
		return toFutureCompletion(delegate
				.thenCombineAsync(other, fn, executor));
	}

	@Override
	public <U> FutureCompletion<Void> thenAcceptBoth(
			CompletionStage<? extends U> other,
			BiConsumer<? super T, ? super U> action) {
		return toFutureCompletion(delegate.thenAcceptBoth(other, action));
	}

	@Override
	public <U> FutureCompletion<Void> thenAcceptBothAsync(
			CompletionStage<? extends U> other,
			BiConsumer<? super T, ? super U> action) {
		return toFutureCompletion(delegate.thenAcceptBothAsync(other, action));
	}

	@Override
	public <U> FutureCompletion<Void> thenAcceptBothAsync(
			CompletionStage<? extends U> other,
			BiConsumer<? super T, ? super U> action, Executor executor) {
		return toFutureCompletion(delegate.thenAcceptBothAsync(other, action,
				executor));
	}

	@Override
	public FutureCompletion<Void> runAfterBoth(CompletionStage<?> other,
			Runnable action) {
		return toFutureCompletion(delegate.runAfterBoth(other, action));
	}

	@Override
	public FutureCompletion<Void> runAfterBothAsync(CompletionStage<?> other,
			Runnable action) {
		return toFutureCompletion(delegate.runAfterBothAsync(other, action));
	}

	@Override
	public FutureCompletion<Void> runAfterBothAsync(CompletionStage<?> other,
			Runnable action, Executor executor) {
		return toFutureCompletion(delegate.runAfterBothAsync(other, action,
				executor));
	}

	@CheckReturnValue
	@Override
	public <U> FutureCompletion<U> applyToEither(
			CompletionStage<? extends T> other, Function<? super T, U> fn) {
		return toFutureCompletion(delegate.applyToEither(other, fn));
	}

	@CheckReturnValue
	@Override
	public <U> FutureCompletion<U> applyToEitherAsync(
			CompletionStage<? extends T> other, Function<? super T, U> fn) {
		return toFutureCompletion(delegate.applyToEitherAsync(other, fn));
	}

	@CheckReturnValue
	@Override
	public <U> FutureCompletion<U> applyToEitherAsync(
			CompletionStage<? extends T> other, Function<? super T, U> fn,
			Executor executor) {
		return toFutureCompletion(delegate.applyToEitherAsync(other, fn,
				executor));
	}

	@Override
	public FutureCompletion<Void> acceptEither(
			CompletionStage<? extends T> other, Consumer<? super T> action) {
		return toFutureCompletion(delegate.acceptEither(other, action));
	}

	@Override
	public FutureCompletion<Void> acceptEitherAsync(
			CompletionStage<? extends T> other, Consumer<? super T> action) {
		return toFutureCompletion(delegate.acceptEitherAsync(other, action));
	}

	@Override
	public FutureCompletion<Void> acceptEitherAsync(
			CompletionStage<? extends T> other, Consumer<? super T> action,
			Executor executor) {
		return toFutureCompletion(delegate.acceptEitherAsync(other, action,
				executor));
	}

	@Override
	public FutureCompletion<Void> runAfterEither(CompletionStage<?> other,
			Runnable action) {
		return toFutureCompletion(delegate.runAfterEither(other, action));
	}

	@Override
	public FutureCompletion<Void> runAfterEitherAsync(CompletionStage<?> other,
			Runnable action) {
		return toFutureCompletion(delegate.runAfterEitherAsync(other, action));
	}

	@Override
	public FutureCompletion<Void> runAfterEitherAsync(CompletionStage<?> other,
			Runnable action, Executor executor) {
		return toFutureCompletion(delegate.runAfterEitherAsync(other, action,
				executor));
	}

	@CheckReturnValue
	@Override
	public <U> FutureCompletion<U> thenCompose(
			Function<? super T, ? extends CompletionStage<U>> fn) {
		return toFutureCompletion(delegate.thenCompose(fn));
	}

	@CheckReturnValue
	@Override
	public <U> FutureCompletion<U> thenComposeAsync(
			Function<? super T, ? extends CompletionStage<U>> fn) {
		return toFutureCompletion(delegate.thenComposeAsync(fn));
	}

	@CheckReturnValue
	@Override
	public <U> FutureCompletion<U> thenComposeAsync(
			Function<? super T, ? extends CompletionStage<U>> fn,
			Executor executor) {
		return toFutureCompletion(delegate.thenComposeAsync(fn, executor));
	}

	@CheckReturnValue
	@Override
	public FutureCompletion<T> exceptionally(Function<Throwable, ? extends T> fn) {
		return toFutureCompletion(delegate.exceptionally(fn));
	}

	@CheckReturnValue
	public FutureCompletion<T> exceptionallyCompose(
	// Ideally, fn would be able to map to any CompletionStage but this overly
	// restrictive since Java has no syntax to derive common supertypes which
	// share a bound with an instance-level generic type.
			Function<Throwable, ? extends CompletionStage<T>> fn) {
		CompletableFuture<T> fut = new CompletableFuture<>();
		whenComplete((result, th) -> {
			if (th != null) {
				fn.apply(th).whenComplete((result2, th2) -> {
					if (th2 != null) {
						fut.completeExceptionally(th2);
					} else {
						fut.complete(result2);
					}
				});
			} else {
				fut.complete(result);
			}
		});
		return toFutureCompletion(fut);
	}

	@Override
	public FutureCompletion<T> whenComplete(
			BiConsumer<? super T, ? super Throwable> action) {
		return toFutureCompletion(delegate.whenComplete(action));
	}

	@Override
	public FutureCompletion<T> whenCompleteAsync(
			BiConsumer<? super T, ? super Throwable> action) {
		return toFutureCompletion(delegate.whenCompleteAsync(action));
	}

	@Override
	public FutureCompletion<T> whenCompleteAsync(
			BiConsumer<? super T, ? super Throwable> action, Executor executor) {
		return toFutureCompletion(delegate.whenCompleteAsync(action, executor));
	}

	@CheckReturnValue
	@Override
	public <U> FutureCompletion<U> handle(
			BiFunction<? super T, Throwable, ? extends U> fn) {
		return toFutureCompletion(delegate.handle(fn));
	}

	/**
	 * Replaces the result of this {@link FutureCompletion} with the
	 * result/failure as returned by {@code fn}.
	 * 
	 * @param fn
	 *            Maps result/failure of this {@link FutureCompletion} to an
	 *            {@link CompletionStage}.
	 * @return Stage that is depended on result/failure as returned by the
	 *         mapping function {@code fn}.
	 */
	@CheckReturnValue
	public <U> FutureCompletion<U> handleCompose(
			BiFunction<? super T, Throwable, CompletionStage<U>> fn) {
		CompletableFuture<U> fut = new CompletableFuture<>();
		delegate.whenComplete((result, throwable) -> {
			try {
				fn.apply(result, throwable).whenComplete(
						(resultMapped, throwableMapped) -> {
							if (throwableMapped != null) {
								fut.completeExceptionally(throwableMapped);
							} else {
								fut.complete(resultMapped);
							}
						});
			} catch (Throwable t) {
				fut.completeExceptionally(t);
			}
		});
		return toFutureCompletion(fut);
	}

	@CheckReturnValue
	@Override
	public <U> FutureCompletion<U> handleAsync(
			BiFunction<? super T, Throwable, ? extends U> fn) {
		return toFutureCompletion(delegate.handleAsync(fn));
	}

	@CheckReturnValue
	@Override
	public <U> FutureCompletion<U> handleAsync(
			BiFunction<? super T, Throwable, ? extends U> fn, Executor executor) {
		return toFutureCompletion(delegate.handleAsync(fn, executor));
	}

	/**
	 * @return Value contained in this {@link FutureCompletion}.
	 * @throws IllegalStateException
	 *             if this {@link FutureCompletion} is not yet completed. In
	 *             other words when this method is invoked before this
	 *             {@link FutureCompletion} is cancelled, exceptionally failed
	 *             or resolved with the computation result.
	 * @throws CancellationException
	 *             if the computation was cancelled
	 * @throws CompletionException
	 *             if the computation failed or this {@link FutureCompletion}
	 *             was completed with an exception.
	 */
	@CheckReturnValue
	public T getNow() {
		if (delegate.isDone()) {
			// valueIfAbsent will never be used as delegate is already
			// completed.
			@SuppressWarnings("null")
			T valueIfAbsent = (T) null;
			return delegate.getNow(valueIfAbsent);
		} else {
			throw new IllegalStateException(
					"Computation still ongoning, value not available.");
		}
	}

	@CheckReturnValue
	@Override
	public CompletableFuture<T> toCompletableFuture() {
		// Copy to prevent clients from completing the delegate.
		return delegate.thenApply(x -> x);
	}

	@CheckReturnValue
	public static <T> FutureCompletion<T> toFutureCompletion(
			CompletionStage<T> delegate) {
		return new FutureCompletion<>(delegate);
	}

	@CheckReturnValue
	public static <T> FutureCompletion<T> completedFutureCompletion(T value) {
		return toFutureCompletion(CompletableFuture.completedFuture(value));
	}

	@CheckReturnValue
	public static <T> FutureCompletion<T> failedFutureCompletion(
			Throwable throwable) {
		CompletableFuture<T> fut = new CompletableFuture<>();
		fut.completeExceptionally(throwable);
		return toFutureCompletion(fut);
	}

	/**
	 * Creates stage that awaits completion of both passed stages and collects
	 * results.
	 * 
	 * @param c0
	 *            A stage to await.
	 * @param c1
	 *            Another stage to await.
	 * @return Stage containing results of passed stages in the same order as
	 *         the given parameter. In case any passed stage completes
	 *         exceptionally, the returned stage completes exceptionally with
	 *         the same exception.
	 */
	@CheckReturnValue
	public static <MostSpecificSupertype, T0 extends MostSpecificSupertype, T1 extends MostSpecificSupertype> FutureCompletion2<MostSpecificSupertype, T0, T1> allOf(
			CompletionStage<T0> c0, CompletionStage<T1> c1) {
		return new FutureCompletion2<>(allOf(Arrays.asList(c0, c1)));
	}

	/**
	 * Creates stage that awaits completion of three passed stages and collects
	 * results.
	 * 
	 * @param c0
	 *            A stage to await.
	 * @param c1
	 *            Another stage to await.
	 * @param c2
	 *            Another stage to await.
	 * @return Stage containing results of passed stages in the same order as
	 *         the given parameter. In case any passed stage completes
	 *         exceptionally, the returned stage completes exceptionally with
	 *         the same exception.
	 */
	@CheckReturnValue
	public static <MostSpecificSupertype, T0 extends MostSpecificSupertype, T1 extends MostSpecificSupertype, T2 extends MostSpecificSupertype> FutureCompletion3<MostSpecificSupertype, T0, T1, T2> allOf(
			CompletionStage<T0> c0, CompletionStage<T1> c1,
			CompletionStage<T2> c2) {
		return new FutureCompletion3<>(allOf(Arrays.asList(c0, c1, c2)));
	}

	/**
	 * Creates stage that awaits completion of four passed stages and collects
	 * results.
	 * 
	 * @param c0
	 *            A stage to await.
	 * @param c1
	 *            Another stage to await.
	 * @param c2
	 *            Another stage to await.
	 * @param c3
	 *            Another stage to await.
	 * @return Stage containing results of passed stages in the same order as
	 *         the given parameter. In case any passed stage completes
	 *         exceptionally, the returned stage completes exceptionally with
	 *         the same exception.
	 */
	@CheckReturnValue
	public static <MostSpecificSupertype, T0 extends MostSpecificSupertype, T1 extends MostSpecificSupertype, T2 extends MostSpecificSupertype, T3 extends MostSpecificSupertype> FutureCompletion4<MostSpecificSupertype, T0, T1, T2, T3> allOf(
			CompletionStage<T0> c0, CompletionStage<T1> c1,
			CompletionStage<T2> c2, CompletionStage<T3> c3) {
		return new FutureCompletion4<>(allOf(Arrays.asList(c0, c1, c2, c3)));
	}

	/**
	 * Creates stage that awaits completion of five passed stages and collects
	 * results.
	 * 
	 * @param c0
	 *            A stage to await.
	 * @param c1
	 *            Another stage to await.
	 * @param c2
	 *            Another stage to await.
	 * @param c3
	 *            Another stage to await.
	 * @param c4
	 *            Another stage to await.
	 * @return Stage containing results of passed stages in the same order as
	 *         the given parameter. In case any passed stage completes
	 *         exceptionally, the returned stage completes exceptionally with
	 *         the same exception.
	 */
	@CheckReturnValue
	public static <MostSpecificSupertype, T0 extends MostSpecificSupertype, T1 extends MostSpecificSupertype, T2 extends MostSpecificSupertype, T3 extends MostSpecificSupertype, T4 extends MostSpecificSupertype> FutureCompletion5<MostSpecificSupertype, T0, T1, T2, T3, T4> allOf(
			CompletionStage<T0> c0, CompletionStage<T1> c1,
			CompletionStage<T2> c2, CompletionStage<T3> c3,
			CompletionStage<T4> c4) {
		return new FutureCompletion5<>(allOf(Arrays.asList(c0, c1, c2, c3, c4)));
	}

	@SafeVarargs
	@CheckReturnValue
	public static <ValueType, AnyStage extends CompletionStage<? extends ValueType>> FutureCompletion<List<ValueType>> allOf(
			AnyStage... cfs) {
		return allOf(Arrays.asList(cfs));
	}

	/**
	 * Creates stage that awaits completion of all passed stages and collects
	 * results.
	 * 
	 * @param stages
	 *            Result providers.
	 * @return Stage containing results of passed stages in the same order as
	 *         parameter {@code stages}. In case any passed stage completes
	 *         exceptionally, the returned stage completes exceptionally with
	 *         the same exception.
	 */
	@CheckReturnValue
	public static <ValueType, AnyStage extends CompletionStage<? extends ValueType>> FutureCompletion<List<ValueType>> allOf(
			Collection<AnyStage> stages) {
		if (stages.isEmpty()) {
			return FutureCompletion.completedFutureCompletion(Collections
					.emptyList());
		}

		int size = stages.size();
		AtomicInteger unresolved = new AtomicInteger(size);
		@SuppressWarnings("null")
		List<ValueType> results = new ArrayList<>(Collections.nCopies(size,
				(ValueType) null));
		FutureCompletionPromise<List<ValueType>> p = new FutureCompletionPromise<>();

		IntFunction<BiConsumer<ValueType, Throwable>> generateResolver = i -> (
				value, throwable) -> {
			if (throwable != null) {
				p.completeExceptionally(throwable);
			} else {
				results.set(i, value);
				if (unresolved.decrementAndGet() == 0) {
					p.complete(results);
				}
			}
		};
		int i = 0;
		Iterator<AnyStage> iter = stages.iterator();
		while (iter.hasNext()) {
			iter.next().whenComplete(generateResolver.apply(i));
			i++;
		}

		return p.toFutureCompletion();
	}

	@CheckReturnValue
	public static FutureCompletion<Object> anyOf(CompletableFuture<?>... cfs) {
		return toFutureCompletion(CompletableFuture.anyOf(cfs));
	}

	@CheckReturnValue
	public static FutureCompletion<Object> anyOf(CompletionStage<?>... cfs) {
		return toFutureCompletion(CompletableFuture
				.anyOf(toCompletableFutureArray(cfs)));
	}

	@CheckReturnValue
	private static CompletableFuture<?>[] toCompletableFutureArray(
			CompletionStage<?>[] cfs) {
		return Arrays.stream(cfs).map(CompletionStage::toCompletableFuture)
				.toArray(CompletableFuture[]::new);
	}

	@CheckReturnValue
	public static FutureCompletion<Object> anyOf(
			Collection<CompletableFuture<?>> cfs) {
		return anyOf(cfs.toArray(new CompletableFuture[cfs.size()]));
	}

	public static FutureCompletion<Void> runAsync(Runnable runnable) {
		return toFutureCompletion(CompletableFuture.runAsync(runnable));
	}

	public static FutureCompletion<Void> runAsync(Runnable runnable,
			Executor executor) {
		return toFutureCompletion(CompletableFuture
				.runAsync(runnable, executor));
	}

	@CheckReturnValue
	public static <U> FutureCompletion<U> supplyAsync(Supplier<U> supplier) {
		return toFutureCompletion(CompletableFuture.supplyAsync(supplier));
	}

	@CheckReturnValue
	public static <U> FutureCompletion<U> supplyAsync(Supplier<U> supplier,
			Executor executor) {
		return toFutureCompletion(CompletableFuture.supplyAsync(supplier,
				executor));
	}
}
