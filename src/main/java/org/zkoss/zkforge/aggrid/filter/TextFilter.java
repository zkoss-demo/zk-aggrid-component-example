/* TextFilter.java

	Purpose:

	Description:

	History:
		Thu Sep 17 11:24:23 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid.filter;

import java.util.Objects;

/**
 * @author rudyhuang
 */
public class TextFilter implements Filter<String> {
	private final String _type;
	private final String _filter;

	public TextFilter(String type, String filter) {
		_type = type;
		_filter = filter;
	}

	@Override
	public boolean test(String data) {
		switch (_type) {
			case "contains":
				return filterContains(data);
			case "notContains":
				return !filterContains(data);
			case "equals":
				return filterEquals(data);
			case "notEqual":
				return !filterEquals(data);
			case "startsWith":
				return data != null && data.startsWith(_filter);
			case "endsWith":
				return data != null && data.endsWith(_filter);
			default: // no filter
				return true;
		}
	}

	private boolean filterContains(String data) {
		return data != null && data.contains(_filter);
	}

	private boolean filterEquals(String data) {
		return Objects.equals(data, _filter);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TextFilter that = (TextFilter) o;
		return Objects.equals(_type, that._type) && Objects.equals(_filter, that._filter);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_type, _filter);
	}
}
