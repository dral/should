package dral.test;

import static dral.test.Should.*;

import java.util.Objects;
import java.util.function.BooleanSupplier;

import org.junit.Test;

public class ShouldTest {

	@Test
	public void shouldPassOnTrueAssertions() {
		should(() -> true);
	}

	@Test
	public void shouldFailOnFalseAssertionsWithNoCause() {
		BooleanSupplier falseAssertion =
				() -> false;

		shouldThrow(() -> should(falseAssertion),
				AssertionError.class);

		shouldThrow(() -> should(falseAssertion),
				e -> Objects.isNull(e.getCause()));
	}

	@Test
	public void shouldFailOnEvaluationErrors() {
		BooleanSupplier evaluationProblem =
				() -> { throw new RuntimeException(); };

		shouldThrow(() -> should(evaluationProblem),
				AssertionError.class);

		shouldThrow(() -> should(evaluationProblem),
				e -> Objects.nonNull(e.getCause()));
	}

	@Test
	public void shouldNotAcceptNullAssertion(){
		shouldThrow(() -> should(null), NullPointerException.class);
	}

	@Test
	public void shouldThrowFailsIfNothingThrown(){
		shouldThrow(
				() -> shouldThrow(
						() -> { },
						RuntimeException.class ),
				AssertionError.class);
	}

	@Test
	public void shouldThrowPassesIfCorrectErrorThrown(){
		shouldThrow(
				() -> { throw new RuntimeException(); },
				RuntimeException.class
				);
	}

	@Test
	public void shouldThrowFailsIfWrongErrorThrown(){
		shouldThrow(
				() -> shouldThrow(
						() -> { throw new Exception(); },
						RuntimeException.class ),
				AssertionError.class);
	}

	@Test
	public void shouldPassOnTruePredicate(){
		Object item1 = new Object();
		Object item2 = new Object();

		should(item1, x -> true);
		should(item1, item2, (x, y) -> true);
	}

	@Test
	public void shouldFailOnFalsePredicate(){
		Object item1 = new Object();
		Object item2 = new Object();

		shouldThrow(() -> should(item1, x -> false), AssertionError.class);
		shouldThrow(() -> should(item1, item2, (x, y) -> false), AssertionError.class);
	}

	@Test
	public void shouldEncapsulateEvaluationErrors(){
		Object item1 = new Object();
		Object item2 = new Object();

		shouldThrow(() -> 
			should(
					item1,
					x -> { throw new RuntimeException(); }
				),
			AssertionError.class);

		shouldThrow(() ->
			should(
					item1, item2,
					(x, y) -> { throw new RuntimeException(); }
				),
			AssertionError.class);
	}

	@Test
	public void shouldNotAcceptNullPredicates(){
		Object item1 = new Object();
		Object item2 = new Object();

		shouldThrow(() -> should(item1, null), NullPointerException.class);
		shouldThrow(() -> should(item1, item2, null), NullPointerException.class);
	}
}