/* ColumnFilter.java

	Purpose:

	Description:

	History:
		Thu Sep 17 16:18:13 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid.filter;

import java.util.Objects;

import org.zkoss.lang.reflect.Fields;
import org.zkoss.zk.ui.UiException;

/**
 * @author rudyhuang
 */
public class ColumnFilter<T> implements Filter<T> {
	private final String _column;
	private final Filter<T> _filter;

	public ColumnFilter(String column, Filter<T> filter) {
		_column = column;
		_filter = filter;
	}

	@SuppressWarnings("unchecked")
	private T getValue(Object data) throws NoSuchMethodException {
		return (T) Fields.getByCompound(data, _column);
	}

	@Override
	public boolean test(T data) {
		try {
			return _filter.test(getValue(data));
		} catch (NoSuchMethodException e) {
			throw UiException.Aide.wrap(e);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ColumnFilter<?> that = (ColumnFilter<?>) o;
		return Objects.equals(_column, that._column) && Objects.equals(_filter, that._filter);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_column, _filter);
	}
}
