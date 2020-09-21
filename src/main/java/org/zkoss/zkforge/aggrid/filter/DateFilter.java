/* DateFilter.java

	Purpose:

	Description:

	History:
		Thu Sep 17 12:48:08 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid.filter;

import java.util.Date;
import java.util.Objects;

/**
 * @author rudyhuang
 */
public class DateFilter implements Filter<Date> {
	private final String _type;
	private final Date _from;
	private final Date _to;

	public DateFilter(String type, Date from) {
		this(type, from, null);
	}

	public DateFilter(String type, Date from, Date to) {
		_type = type;
		_from = from;
		_to = to;
	}

	@Override
	public boolean test(Date data) {
		switch (_type) {
			case "equals":
				return _from.equals(data);
			case "notEqual":
				return !_from.equals(data);
			case "lessThan":
				return data != null && data.before(_from);
			case "greaterThan":
				return data != null && data.after(_from);
			case "inRange": // TODO: implement inRangeInclusive
				return data != null && data.compareTo(_from) > 0 && data.compareTo(_to) < 0;
			default: // no filter
				return true;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DateFilter that = (DateFilter) o;
		return Objects.equals(_type, that._type) && Objects.equals(_from, that._from)
				&& Objects.equals(_to, that._to);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_type, _from, _to);
	}
}
