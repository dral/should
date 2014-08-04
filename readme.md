# should

This is a simple functional style library for testing assertions.

## How to use

### Add to your project using Ant/Maven/Gradle/...

- group: `com.github.dral`
- name: `test-should`
- version: `0.1.0`

### Import
```
import static dral.test.Should.*;
```

### Test any unary or binary predicate:

> Informally:
> the given `item(s)` should satisfy `a condition`.

```
should(
	5,        	// item
	x -> x > 0	// a condition
);

should(
	4, 5,                      	// items
	(x,y) -> x.compareTo(y) < 0	// a condition
);
```

### Test that a piece of code throws something:

Either of a specific type

> Informally:
> `some piece of code` should throw `a specific exception type`.

```
shouldThrow(
	() -> { throw new RuntimeException(); },	// some piece of code
	 RuntimeException.class                 	// a specific exception type
);
```

Or satisfying a given condition:

> Informally:
> `some piece of code` should throw something satisfying `a given condition`.

```
shouldThrow(
	() -> { throw new RuntimeException("Message"); },	// some piece of code
	e -> e.getMessage().equals("Message")            	// a given condition
);
```

### Or any arbitrary Boolean supplier:

```
should(() -> 5 > 0);
```

## Missing features (to come...)

- Produce informative error messages for failing tests.
- Collections / Stream testing support.
- API documentation
