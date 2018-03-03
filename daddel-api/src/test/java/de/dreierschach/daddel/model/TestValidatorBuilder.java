package de.dreierschach.daddel.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.dreierschach.daddel.validator.Validator;
import de.dreierschach.daddel.validator.Expression;

public class TestValidatorBuilder {

	@Test
	public void testOr() {
		Validator<Integer> v = Expression.instance(Integer.class) //
				.with(value -> value == 1) //
				.or(value -> value == 2).create();
		assertFalse(v.validate(0));
		assertTrue(v.validate(1));
		assertTrue(v.validate(2));
		assertFalse(v.validate(3));
	}

	@Test
	public void testAnd() {
		Validator<Integer> v = Expression.instance(Integer.class) //
				.with(value -> value >= 0) //
				.and(value -> value <= 2).create();
		assertFalse(v.validate(-1));
		assertTrue(v.validate(0));
		assertTrue(v.validate(2));
		assertFalse(v.validate(3));
	}

	@Test
	public void testNot() {
		Validator<Integer> v = Expression.instance(Integer.class) //
				.not(value -> value > 0) //
				.create();
		assertFalse(v.validate(1));
		assertTrue(v.validate(0));
		assertTrue(v.validate(-1));
	}
}
