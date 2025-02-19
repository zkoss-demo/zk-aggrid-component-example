/* Aggridcolumn.ts

	Purpose:

	Description:

	History:
		Fri Jun 19 14:36:45 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
 */
// @ts-ignore
import {ColDef} from '@ag-grid-community/core';

(() => {
aggrid.Aggridcolumn = zk.$extends(zk.Widget, {
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
		let colDef: ColDef = this._generateColDef();
		if (this.nChildren) {
			colDef.children = this.$class.mapToColumnDefs(this.firstChild);
		}
		return colDef;
	},
	_generateColDef(): ColDef {
		let colDef: ColDef = zk.copy({}, this._colDef);
		colDef['_zk_uuid'] = this.uuid;
		delete colDef.children;
		return colDef;
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
}, {
	mapToColumnDefs(column: zk.Widget | null): ColDef[] {
		let columnDefs: ColDef[] = [];
		for (; column; column = column.nextSibling) {
			if (aggrid.Aggridcolumn.isInstance(column) && !aggrid.Aggriddefaultcolumn.isInstance(column))
				columnDefs.push(column.toColDef());
		}
		return columnDefs;
	}
});
})();