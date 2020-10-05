/* CompositeFilterParams.java

	Purpose:
		
	Description:
		
	History:
		Mon Oct 05 12:25:34 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid.filter;

import java.util.function.Function;

import org.zkoss.zkforge.aggrid.FilterParams;

/**
 * @author rudyhuang
 */
public class CompositeFilterParams extends FilterParams {
	private final FilterParams _defaultParams;
	private final FilterParams _params;

	public CompositeFilterParams(FilterParams defaultParams, FilterParams params) {
		_defaultParams = defaultParams;
		_params = params;
	}

	private <T> T getValue(Function<FilterParams, T> m, T defaultValue) {
		T result = defaultValue;
		if (_defaultParams != null)
			result = m.apply(_defaultParams);
		if (_params != null)
			result = m.apply(_params);
		return result;
	}

	@Override
	public boolean isCaseSensitive() {
		return getValue(FilterParams::isCaseSensitive, false);
	}

	@Override
	public boolean isInRangeInclusive() {
		return getValue(FilterParams::isInRangeInclusive, false);
	}

	@Override
	public boolean isIncludeBlanksInEquals() {
		return getValue(FilterParams::isIncludeBlanksInEquals, false);
	}

	@Override
	public boolean isIncludeBlanksInLessThan() {
		return getValue(FilterParams::isIncludeBlanksInLessThan, false);
	}

	@Override
	public boolean isIncludeBlanksInGreaterThan() {
		return getValue(FilterParams::isIncludeBlanksInGreaterThan, false);
	}
}
