package common;


public abstract class SetFactory<T> {
	
	public abstract Iterable<T> build();
}
