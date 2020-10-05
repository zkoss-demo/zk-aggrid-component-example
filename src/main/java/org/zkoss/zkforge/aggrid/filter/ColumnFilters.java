/* ColumnFilters.java

	Purpose:

	Description:

	History:
		Thu Sep 17 16:41:56 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.zkoss.json.JSONObject;
import org.zkoss.util.CacheMap;
import org.zkoss.zkforge.aggrid.AggridDefaultColumn;
import org.zkoss.zkforge.aggrid.FilterParams;

/**
 * @author rudyhuang
 */
public class ColumnFilters {
	private static final Map<String, FilterParams> FILTER_PARAMS_CACHE = new CacheMap<>();

	private ColumnFilters() {
	}

	public static FilterParams getFilterParams(String column) {
		final FilterParams defaultFilterParams = FILTER_PARAMS_CACHE.get(AggridDefaultColumn.class.getName());
		final FilterParams params = FILTER_PARAMS_CACHE.get(column);
		return defaultFilterParams != null
				? new CompositeFilterParams(defaultFilterParams, params)
				: params;
	}

	public static void putFilterParams(String column, FilterParams filterParams) {
		FILTER_PARAMS_CACHE.put(column, filterParams);
	}

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
			case "date":
				try {
					return new DateFilter(type, toDate(config.get("dateFrom")), toDate(config.get("dateTo")));
				} catch (ParseException e) {
					throw new IllegalArgumentException("Can't create a DateFilter: ", e);
				}
			default:
				throw new UnsupportedOperationException("filter type not supported: " + filterType);
		}
	}

	private static Date toDate(Object date) throws ParseException {
		return date == null ? null : new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse((String) date);
	}
}
