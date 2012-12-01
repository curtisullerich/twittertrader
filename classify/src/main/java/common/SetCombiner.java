package common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class SetCombiner<O, I> {

	LinkedList<SetFactory<I>> factories = new LinkedList<SetFactory<I>>();

	public SetCombiner<O, I> add(SetFactory<I> setFactory) {
		factories.add(setFactory);
		return this;
	}

	public abstract O combine(Iterable<I> in);

	public Iterator<O> iterator() {
		return new InternalIterator(
				new ArrayList<SetFactory<I>>(this.factories));
	}

	private class InternalIterator implements Iterator<O> {
		
		
		int[] counts;
		int[] i;

		public InternalIterator(ArrayList<SetFactory<I>> arrayList) {
			counts = new int[arrayList.size()];
			i = new int[arrayList.size()];
			
			
		}

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public O next() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub

		}

	}

}
