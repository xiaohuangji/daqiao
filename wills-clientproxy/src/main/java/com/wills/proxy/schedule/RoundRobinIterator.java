package com.wills.proxy.schedule;

import java.util.Iterator;
import java.util.List;
 

/**
 * @author huangsiping
 *
 * @param <T>
 */
public class RoundRobinIterator<T> implements Iterable<T> {
	private List<T> coll;
	private int index = 0;

	public RoundRobinIterator(List<T> coll) {
		this.coll = coll;
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {
			public boolean hasNext() {
				return true;
			}

			public T next() {
				if(coll==null||coll.isEmpty()){
					return null;
				}
				index = (index + 1) % coll.size();
				T res = coll.get(index);
				return res;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}