package dral.test;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Should {

	public static void should(BooleanSupplier assertion){
		Objects.requireNonNull(assertion);
		boolean value = false;
		try {
			value = assertion.getAsBoolean();
		} catch (Throwable e) {
			throw new AssertionError("Error evaluating assertion.", e);
		}
		if (!value){
			throw new AssertionError();
		}
	}
	
	public static <T> void should(T item, Predicate<? super T> test){
		Objects.requireNonNull(test);
		should(() -> test.test(item));
	}

	public static <T, R> void should(T item1, R item2, BiPredicate<? super T, ? super R> test){
		Objects.requireNonNull(test);
		should(() -> test.test(item1, item2));
	}

	public static void shouldThrow(CodeBlock codeBlock, Class<? extends Throwable> expected){
		Objects.requireNonNull(expected);
		shouldThrow(codeBlock, e -> expected.isInstance(e));
	}
	
	public static void shouldThrow(CodeBlock code, Predicate<? super Throwable> errorTest){
		should(code.catchError(), PredicateUtils.and(Objects::nonNull, errorTest));
	}

	public static <T> void shouldAll(Stream<T> input, Predicate<? super T> test){
		Function<T, CodeBlock> check = x -> (() -> should(x, test));
		Function<T, Throwable> getError = x -> check.apply(x).catchError();

		List<String> errors = input.parallel()
				.map(getError)
				.filter(Objects::nonNull)
				.limit(100) // limits only the number of errors.
				.map(e -> e.getMessage())
				.collect(Collectors.toList());

		if (!errors.isEmpty()){
			throw new AssertionError(); // TODO Construct error message from inner errors.
		}
	}
}