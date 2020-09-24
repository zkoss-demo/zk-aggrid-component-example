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
			case CONTAINS:
				return filterContains(data);
			case NOT_CONTAINS:
				return !filterContains(data);
			case EQUALS:
				return filterEquals(data);
			case NOT_EQUAL:
				return !filterEquals(data);
			case STARTS_WITH:
				return startsWithIgnoreCase(data, _filter);
			case ENDS_WITH:
				return endsWithIgnoreCase(data, _filter);
			default: // no filter
				return true;
		}
	}

	private boolean filterContains(String data) {
		return containsIgnoreCase(data, _filter);
	}

	private static boolean containsIgnoreCase(String str, String searchStr) {
		if (str == null || searchStr == null)
			return false;

		final int length = searchStr.length();
		for (int i = str.length() - length; i >= 0; i--) {
			if (str.regionMatches(true, i, searchStr, 0, length))
				return true;
		}
		return false;
	}

	private static boolean startsWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null)
			return false;

		final int length = prefix.length();
		if (length > str.length())
			return false;
		return str.regionMatches(true, 0, prefix, 0, length);
	}

	private static boolean endsWithIgnoreCase(String str, String suffix) {
		if (str == null || suffix == null)
			return false;

		final int length = suffix.length();
		final int pos = str.length() - length;
		if (pos < 0)
			return false;
		return str.regionMatches(true, pos, suffix, 0, length);
	}

	private boolean filterEquals(String data) {
		// TODO: implement caseSensitive
		return data != null && data.equalsIgnoreCase(_filter);
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
