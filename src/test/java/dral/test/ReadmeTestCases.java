package dral.test;

import static dral.test.Should.*;

import org.junit.Test;

public class ReadmeTestCases {

	@Test
	public void test() {
		should(5, x -> x > 0);
		should(4, 5, (x,y) -> x.compareTo(y) < 0);
		shouldThrow(() -> { throw new RuntimeException(); }, RuntimeException.class);
		shouldThrow(() -> { throw new RuntimeException("Message"); },
				e -> e.getMessage().equals("Message")
			);
		should(() -> 5 > 0);
	}
}
