/* Aggriddefaultcolumn.ts

	Purpose:
		
	Description:
		
	History:
		Tue Sep 29 16:28:36 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
// @ts-ignore
import {ColDef} from '@ag-grid-community/core';

(() => {
aggrid.Aggriddefaultcolumn = zk.$extends(zk.Widget, {
	$init() {
		this.$supers('$init', arguments);
		this._colDef = {};
		// @ts-ignore
		agGrid.ColDefUtil.ALL_PROPERTIES.forEach(prop => {
			Object.defineProperty(this, prop,{
				configurable: true, // some properties are multi-type (e.g: string and function), possible duplicated
				set(newValue) {
					this._colDef[prop] = newValue;
					if (this.desktop) this._updateColDef();
				},
				get() { return this._colDef[prop]; }
			});
		});
	},
	$define: {
		width(v) {
			this._colDef.width = v;
			if (this.desktop) this._updateColDef();
		}
	},
	redraw(out: zk.Buffer): void {
	},
	toColDef(): ColDef {
		return zk.copy({}, this._colDef);
	},
	_updateColDef(): void {
		let parent = this.parent
		for (; parent; parent = parent.parent) {
			if (parent.$instanceof(aggrid.Aggrid)) {
				parent.requestUpdateColDefs_();
				break;
			}
		}
	}
});
})();