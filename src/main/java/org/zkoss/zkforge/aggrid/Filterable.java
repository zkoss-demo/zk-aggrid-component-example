package org.zkoss.zkforge.aggrid;

import java.util.Collection;

import org.zkoss.zkforge.aggrid.filter.Filter;

/**
 * @author rudyhuang
 */
public interface Filterable<T> {
	boolean applyFilter(Filter<T> filter);

	default void applyFilters(Collection<Filter<T>> filters) {
		if (filters != null) {
			filters.forEach(this::applyFilter);
		}
	}

	boolean removeFilter(Filter<T> filter);

	Collection<Filter<T>> getFilters();

	void removeAllFilters();
}
