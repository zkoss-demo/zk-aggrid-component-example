/* CompositeFilter.java

	Purpose:

	Description:

	History:
		Thu Sep 17 16:09:06 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid.filter;

import java.util.Objects;

/**
 * @author rudyhuang
 */
public abstract class CompositeFilter<T> implements Filter<T> {
	protected final Filter<T> _condition1;
	protected final Filter<T> _condition2;

	public CompositeFilter(Filter<T> condition1, Filter<T> condition2) {
		_condition1 = condition1;
		_condition2 = condition2;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CompositeFilter<?> that = (CompositeFilter<?>) o;
		return Objects.equals(_condition1, that._condition1) && Objects.equals(_condition2, that._condition2);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_condition1, _condition2);
	}
}
