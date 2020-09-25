/* AgGridEvent.java

	Purpose:
		
	Description:
		
	History:
		Thu Sep 24 16:20:09 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid;

import java.util.Map;

import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * @author rudyhuang
 */
public class AgGridEvent<E> extends Event {
	private Map<String, ?> _data;
	private E _node;

	public AgGridEvent(String name) {
		super(name);
	}

	public AgGridEvent(String name, Component target) {
		super(name, target);
	}

	public AgGridEvent(String name, Component target, Map<String, ?> data) {
		super(name, target, data);
		this._data = data;
	}

	public AgGridEvent(String name, Component target, Map<String, ?> data, E node) {
		super(name, target, data);
		this._data = data;
		this._node = node;
	}

	public static <E> AgGridEvent<E> getAgGridEvent(AuRequest request, E node) {
		return new AgGridEvent<>(request.getCommand(), request.getComponent(), request.getData(), node);
	}

	public Object get(String key) {
		return _data.get(key);
	}

	public E getNode() {
		return _node;
	}
}
