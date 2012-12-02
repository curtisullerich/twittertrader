package common;

import java.util.ArrayList;
import java.util.List;

public abstract class SetFactory<T> {

	private boolean single;

	public SetFactory() {
		this(false);
	}

	public SetFactory(boolean single) {
		this.single = single;
	}

	private List<SetItem<T>> retCache;

	public List<SetItem<T>> produce() {
		retCache = new ArrayList<SetItem<T>>();
		build();
		return retCache;
	}

	protected abstract void build();

	protected void add(T thing, String label) {
		retCache.add(new SetItem<T>(thing, label));
	}

	public boolean isSingle() {
		return single;
	}
}
