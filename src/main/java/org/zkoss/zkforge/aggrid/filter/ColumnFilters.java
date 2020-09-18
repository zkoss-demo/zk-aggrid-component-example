/* ColumnFilters.java

	Purpose:

	Description:

	History:
		Thu Sep 17 16:41:56 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid.filter;

import org.zkoss.json.JSONObject;

/**
 * @author rudyhuang
 */
public class ColumnFilters {
	public static Filter<?> build(String column, JSONObject config) {
		Filter<?> filter;
		String operator = (String) config.get("operator");
		if (operator == null) {
			filter = buildFilter(config);
		} else {
			Filter<?> condition1 = buildFilter((JSONObject) config.get("condition1"));
			Filter<?> condition2 = buildFilter((JSONObject) config.get("condition2"));
			filter = "AND".equals(operator)
					? new AndFilter(condition1, condition2)
					: new OrFilter(condition1, condition2);
		}
		return new ColumnFilter(column, filter);
	}

	private static Filter<?> buildFilter(JSONObject config) {
		String filterType = (String) config.get("filterType");
		String type = (String) config.get("type");
		Object filter = config.get("filter");
		Object filterTo = config.get("filterTo");
		switch (filterType) {
			case "number":
				return new NumberFilter(type, (Number) filter, (Number) filterTo);
			case "text":
				return new TextFilter(type, (String) filter);
//			case "date": // TODO
//				return new DateFilter(type);
			default:
				throw new UnsupportedOperationException("filter type not supported: " + filterType);
		}
	}
}
