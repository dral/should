package dral.test;

import java.util.Objects;

@FunctionalInterface
public interface CodeBlock {
	
	void run() throws Throwable;
	
	default CodeBlock andThen(CodeBlock after) {
		Objects.requireNonNull(after);
		return () -> { run(); after.run(); };
	}
	
	default Throwable getThrows(){
		try {
			run();
		} catch (Throwable e) {
			return e;
		}
		return null;
	}
}