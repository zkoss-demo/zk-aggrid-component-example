/* AgGridEvent.java

	Purpose:
		
	Description:
		
	History:
		Thu Sep 24 16:20:09 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid;

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * @author rudyhuang
 */
public class AgGridEvent extends Event {
	private Map<String, ?> _data;

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

	public Object get(String key) {
		return _data.get(key);
	}
}
