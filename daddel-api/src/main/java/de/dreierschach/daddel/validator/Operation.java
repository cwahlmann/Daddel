package de.dreierschach.daddel.validator;

public class Operation<T> implements Validator<T> {
	private Validator<T> u;
	private Validator<T> v;
	private Operator<Boolean> operator;
	
	public Operation(Validator<T> u, Validator<T> v, Operator<Boolean> operator) {
		this.u = u;
		this.v = v;
		this.operator = operator;
	}
	
	@Override
	public boolean validate(T t) {
		return operator.operate(u.validate(t),v.validate(t));
	}
}
