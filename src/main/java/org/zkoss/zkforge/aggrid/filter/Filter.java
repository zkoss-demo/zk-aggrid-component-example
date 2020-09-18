package org.zkoss.zkforge.aggrid.filter;

import java.util.function.Predicate;

/**
 * @author rudyhuang
 */
public interface Filter<T> extends Predicate<T> {
	/**
	 * To test each data in a model.
	 *
	 * @param data the data
	 * @return true if the data is kept
	 */
	boolean test(T data);
}
