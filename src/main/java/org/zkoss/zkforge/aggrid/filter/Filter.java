package org.zkoss.zkforge.aggrid.filter;

import java.util.function.Predicate;

import org.zkoss.zkforge.aggrid.FilterParams;

/**
 * @author rudyhuang
 */
public interface Filter<T> extends Predicate<T> {
	String EMPTY = "empty";
	String EQUALS = "equals";
	String NOT_EQUAL = "notEqual";
	String LESS_THAN = "lessThan";
	String LESS_THAN_OR_EQUAL = "lessThanOrEqual";
	String GREATER_THAN = "greaterThan";
	String GREATER_THAN_OR_EQUAL = "greaterThanOrEqual";
	String IN_RANGE = "inRange";
	String CONTAINS = "contains";
	String NOT_CONTAINS = "notContains";
	String STARTS_WITH = "startsWith";
	String ENDS_WITH = "endsWith";

	/**
	 * To test each data in a model.
	 *
	 * @param data the data
	 * @return true if the data is kept
	 */
	default boolean test(T data) {
		return test(data, null);
	}

	/**
	 * To test each data in a model.
	 *
	 * @param data the data
	 * @param filterParams the filter setting, might be null
	 * @return true if the data is kept
	 */
	boolean test(T data, FilterParams filterParams);
}
