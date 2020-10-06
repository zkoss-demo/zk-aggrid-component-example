/* AgGridEvent.java

	Purpose:
		
	Description:
		
	History:
		Thu Sep 24 16:20:09 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.zkoss.lang.Generics;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuRequests;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.event.Event;

/**
 * @author rudyhuang
 */
public class AgGridEvent<E> extends Event {
	private Map<String, ?> _data;
	private E _node;
	private Aggridcolumn<E> _column;
	private Set<Aggridcolumn<E>> _columns;

	public AgGridEvent(String name) {
		super(name);
	}

	public AgGridEvent(String name, Component target) {
		super(name, target);
	}

	public AgGridEvent(String name, Component target, Map<String, ?> data) {
		this(name, target, data, null, null, Collections.emptySet());
	}

	public AgGridEvent(String name, Component target, Map<String, ?> data,
	                   E node, Aggridcolumn<E> column, Set<Aggridcolumn<E>> columns) {
		super(name, target, data);
		this._data = data;
		this._node = node;
		this._column = column;
		this._columns = columns;
	}

	public static <E> AgGridEvent<E> getAgGridEvent(AuRequest request, E node) {
		final Desktop desktop = request.getDesktop();
		final Map<String, Object> data = request.getData();
		return new AgGridEvent<>(request.getCommand(), request.getComponent(),
				data, node,
				Generics.cast(desktop.getComponentByUuidIfAny((String) data.get("column"))),
				AuRequests.convertToItems(desktop, Generics.cast(data.get("columns"))));
	}

	public Object get(String key) {
		return _data.get(key);
	}

	public E getNode() {
		return _node;
	}

	public Aggridcolumn<E> getColumn() {
		return _column;
	}

	public Set<Aggridcolumn<E>> getColumns() {
		return _columns;
	}
}
