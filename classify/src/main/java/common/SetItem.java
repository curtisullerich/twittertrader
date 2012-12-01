package common;

public class SetItem<T> {
	public SetItem(T thing, String label2) {
		this.value = thing;
		this.label = label2;
	}

	public T value;
	public String label;
}