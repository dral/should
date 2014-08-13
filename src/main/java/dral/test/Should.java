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

	/**
	 * Verifies that the given assertion evaluates to true.
	 * @param assertion what should be true
	 * @throws AssertionError if either the assertion
	 * evaluation fails or if it evaluates to false.
	 */
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
	
	/**
	 * <p>Verifies that a predicate holds for a given item
	 * <p>Ex:
	 * <code>should(father, x -> x.hasChildren())</code>
	 * @param item the item to be tested
	 * @param test a test for the item
	 * @throws NullPointerException if the test is null
	 * @throws AssertionError if the test evaluation 
	 * fails or if it evaluates to false.
	 */
	public static <T> void should(T item, Predicate<? super T> test){
		Objects.requireNonNull(test);
		should(() -> test.test(item));
	}

	/**
	 * <p>Verifies that a predicate holds for a couple of
	 * items.
	 * <p>Ex:
	 * <code>should(father, son, (x,y) -> x.getChildren().contains(y))</code>
	 * @param item1 the first item
	 * @param item2 the second item
	 * @param test a binary predicate on both items
	 * @throws NullPointerException if the test is null
	 * @throws AssertionError if the test evaluation 
	 * fails or if it evaluates to false.
	 */
	public static <T, R> void should(T item1, R item2, BiPredicate<? super T, ? super R> test){
		Objects.requireNonNull(test);
		should(() -> test.test(item1, item2));
	}

	/**
	 * Verifies that a given {@link CodeBlock} issues an error
	 * of the given type.
	 * <p>Ex:
	 * <code>shouldThrow(() -> { float i = 1/0 ;}, ArithmeticException.class);</code>
	 * @param codeBlock the code to be run.
	 * @param expected the expected type of the error.
	 * @throws NullPointerException if expected is null.
	 * @throws AssertionError if either no error is thrown
	 * or if the thrown error is not of the expected type.
	 */
	public static void shouldThrow(CodeBlock codeBlock, Class<? extends Throwable> expected){
		Objects.requireNonNull(expected);
		shouldThrow(codeBlock, e -> expected.isInstance(e));
	}
	
	/**
	 * Verifies an arbitrary test on the error issued by the
	 * given {@link CodeBlock}.
	 * @param code the code to be run.
	 * @param errorTest the predicate that the error should verify
	 * @throws AssertionError if either no error is thrown
	 * or if the thrown error doesn't satisfy the given test.
	 */
	public static void shouldThrow(CodeBlock code, Predicate<? super Throwable> errorTest){
		should(code.catchError(), PredicateUtils.and(Objects::nonNull, errorTest));
	}

	/**
	 * Verifies that a predicate holds for all the items issued
	 * by a given {@link Stream}.
	 * @param input A stream of items
	 * @param test a test for the items
	 */
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