Wraps `java.util.concurrent.CompletableFuture` to prevent instance receivers from resolving/rejecting the `CompletableFuture`. Has `@CheckReturnValue` on several methods so that static analyzers can detect unused references.

Some methods are added for convenience, namely:
* Static `FutureCompletion.allOf(...)` and `FutureCompletion.anyOf(...)` that take collections and arrays.
* Static `FutureCompletion.allOf(...)` collects results from multiple stages as single stage containing result list.
* Static `FutureCompletion.allOf(...)` for up to 5 parameters returns a subclass for easier access to awaited stage values.
* Static `FutureCompletion.completedFutureCompletion(T)` to create an already resolved `CompletionStage`/`FutureCompletion`.
* Static `FutureCompletion.failedFutureCompletion(Throwable)` to create an already rejected `CompletionStage`/`FutureCompletion`.
* `FutureCompletion.thenSupply(Supplier<U>)` to replace the value of the current `CompletionStage` when it completes successfully.
* `FutureCompletion.handleCompose(BiFunction<? super T, Throwable, CompletionStage<U>>)` to replace the `CompletionStage` with another `CompletionStage` when the former completes.
* `FutureCompletion.getNow()` to acquire to value or immediately fail with an exception if the value is not or not yet known.

See the unit tests for usage examples.
