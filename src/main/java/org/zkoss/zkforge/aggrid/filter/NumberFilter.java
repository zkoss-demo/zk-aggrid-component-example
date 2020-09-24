/* NumberFilter.java

	Purpose:

	Description:

	History:
		Thu Sep 17 11:34:32 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid.filter;

import java.util.Objects;

/**
 * @author rudyhuang
 */
public class NumberFilter implements Filter<Number> {
	private final String _type;
	private final Number _filter;
	private final Number _filterTo;

	public NumberFilter(String type, Number filter) {
		this(type, filter, null);
	}

	public NumberFilter(String type, Number filter, Number filterTo) {
		_type = type;
		_filter = filter;
		_filterTo = filterTo;
	}

	@Override
	public boolean test(Number data) {
		switch (_type) {
			case EQUALS:
				return filterEquals(data);
			case NOT_EQUAL:
				return !filterEquals(data);
			case LESS_THAN:
				return compareTo(_filter, data) > 0;
			case LESS_THAN_OR_EQUAL:
				return compareTo(_filter, data) >= 0;
			case GREATER_THAN:
				return compareTo(_filter, data) < 0;
			case GREATER_THAN_OR_EQUAL:
				return compareTo(_filter, data) <= 0;
			case IN_RANGE: // TODO: implement inRangeInclusive
				return compareTo(_filter, data) < 0 && compareTo(_filterTo, data) > 0;
			default: // no filter
				return true;
		}
	}

	private boolean filterEquals(Number data) {
		return _filter.equals(data);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private int compareTo(Number filter, Number data) {
		if (!(filter instanceof Comparable)) {
			throw new UnsupportedOperationException("filter should be Comparable");
		}
		return ((Comparable) filter).compareTo(data);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NumberFilter that = (NumberFilter) o;
		return Objects.equals(_type, that._type) && Objects.equals(_filter, that._filter)
				&& Objects.equals(_filterTo, that._filterTo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_type, _filter, _filterTo);
	}
}
