package common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

public abstract class SetCombiner<O, I> implements Iterable<SetItem<O>> {

	LinkedList<SetFactoryWrapper> factories = new LinkedList<SetFactoryWrapper>();

	public SetCombiner<O, I> appendSingle(SetFactory<I> setFactory) {
		factories.add(new SetFactoryWrapper(setFactory, true));
		return this;
	}

	public SetCombiner<O, I> appendAny(SetFactory<I> setFactory) {
		factories.add(new SetFactoryWrapper(setFactory, false));
		return this;
	}

	protected SetItem<O> combine(Iterable<SetItem<I>> in) {
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<I> pipes = new ArrayList<I>();

		for (SetItem<I> i : in) {
			pipes.add(i.value);
			labels.add(i.label);
		}

		return combine(pipes, labels);
	}

	protected SetItem<O> combine(List<I> values, List<String> labels) {
		throw new NotImplementedException();
	}

	public Iterator<SetItem<O>> iterator() {
		return new InternalIterator(new ArrayList<SetFactoryWrapper>(
				this.factories));
	}

	private class SetFactoryWrapper {

		SetFactory<I> factory;
		boolean isSingle;

		public SetFactoryWrapper(SetFactory<I> factory, boolean single) {
			this.factory = factory;
			this.isSingle = single;
		}

	}

	private class InternalIterator implements Iterator<SetItem<O>> {

		ArrayList<SetFactoryWrapper> factories;
		int[] counts;
		int[] i;

		public InternalIterator(ArrayList<SetFactoryWrapper> a) {
			counts = new int[a.size()];
			i = new int[a.size()];

			factories = a;
			for (int j = 0; j < counts.length; j++) {
				if (factories.get(j).isSingle)
					counts[j] = factories.get(j).factory.produce().size();
				else
					counts[j] = (int) Math.pow(2, factories.get(j).factory
							.produce().size());

			}
		}

		@Override
		public boolean hasNext() {
			for (int j = 0; j < counts.length; j++) {
				if (i[j] == counts[j])
					return false;
				if (i[j] < counts[j])
					return true;
			}
			return false;
		}

		@Override
		public SetItem<O> next() {
			if (!hasNext())
				throw new IllegalStateException();

			ArrayList<SetItem<I>> ret = new ArrayList<SetItem<I>>(counts.length);

			for (int j = 0; j < counts.length; j++) {

				if (factories.get(j).isSingle) {
					ret.add(factories.get(j).factory.produce().get(i[j]));
				} else {
					List<SetItem<I>> items = factories.get(j).factory.produce();

					for (int k = 0; k < items.size(); k++) {
						if (((i[j] >> k) & 1) == 1)
							ret.add(items.get(k));
					}
				}
			}

			// Increment
			i[i.length - 1]++;

			for (int j = counts.length - 1; j >= 0; j--) {
				if (i[j] < counts[j])
					break;

				if (j > 0) {
					i[j] = 0;
					i[j - 1]++;
				}
			}

			return combine(ret);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
