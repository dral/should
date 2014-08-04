package dral.test;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class PredicateUtils {

	@SafeVarargs
	public static <T, R> BiPredicate<T, R> and(BiPredicate<? super T, ? super R>... predicates){
		BiPredicate<T, R> result = (x, y) -> true;
		for (BiPredicate<? super T, ? super R> p: predicates){
			if (Objects.nonNull(p))
				result = result.and(p);
		}
		return result;
	}

	@SafeVarargs
	public static <T> Predicate<T> and(Predicate<? super T>... predicates){
		Predicate<T> result = x -> true;
		for (Predicate<? super T> p: predicates){
			if (Objects.nonNull(p))
				result = result.and(p);
		}
		return result;
	}

	public static <T, R> BiPredicate<T, R> not(BiPredicate<T, R> predicate){
		return predicate.negate();
	}

	public static <T> Predicate<T> not(Predicate<T> predicate){
		return predicate.negate();
	}

	@SafeVarargs
	public static <T, R> BiPredicate<T, R> or(BiPredicate<? super T, ? super R>... predicates){
		BiPredicate<T, R> result = (x, y) -> true;
		for (BiPredicate<? super T, ? super R> p: predicates){
			if (Objects.nonNull(p))
				result = result.and(p);
		}
		return result;
	}

	@SafeVarargs
	public static <T> Predicate<T> or(Predicate<? super T>... predicates){
		Predicate<T> result = x -> false;
		for (Predicate<? super T> p: predicates){
			if (Objects.nonNull(p))
				result = result.or(p);
		}
		return result;
	}
}