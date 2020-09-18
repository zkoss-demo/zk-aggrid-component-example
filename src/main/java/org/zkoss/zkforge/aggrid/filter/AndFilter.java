/* AndFilter.java

	Purpose:

	Description:

	History:
		Thu Sep 17 15:27:29 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid.filter;

/**
 * @author rudyhuang
 */
public class AndFilter<T> extends CompositeFilter<T> {
	public AndFilter(Filter<T> condition1, Filter<T> condition2) {
		super(condition1, condition2);
	}

	@Override
	public boolean test(T data) {
		return _condition1.and(_condition2).test(data);
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode() * 31 + "AND".hashCode();
	}
}
