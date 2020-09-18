/* OrFilter.java

	Purpose:

	Description:

	History:
		Thu Sep 17 15:29:23 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid.filter;

/**
 * @author rudyhuang
 */
public class OrFilter<T> extends CompositeFilter<T> {
	public OrFilter(Filter<T> condition1, Filter<T> condition2) {
		super(condition1, condition2);
	}

	@Override
	public boolean test(T data) {
		return _condition1.or(_condition1).test(data);
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode() * 31 + "OR".hashCode();
	}
}