/* AggridDefaultColumn.java

	Purpose:
		
	Description:
		
	History:
		Tue Sep 29 16:15:51 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid;

import java.util.Comparator;

import org.zkoss.zkforge.aggrid.filter.ColumnFilters;

/**
 * @author rudyhuang
 */
public class AggridDefaultColumn extends Aggridcolumn<Object> {
	@Override
	public void setSortAscending(Comparator<Object> sorter) {
		throw new UnsupportedOperationException("not supported in default column definition");
	}

	@Override
	public void setSortDescending(Comparator<Object> sorter) {
		throw new UnsupportedOperationException("not supported in default column definition");
	}

	@Override
	protected void initSortComparator() {
		// do nothing
	}

	@Override
	protected void initFilterParams() {
		ColumnFilters.putFilterParams(AggridDefaultColumn.class.getName(), getFilterParams());
	}

	@Override
	protected boolean isChildable() {
		return false;
	}
}
