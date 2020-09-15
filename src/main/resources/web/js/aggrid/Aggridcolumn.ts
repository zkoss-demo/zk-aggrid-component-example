/* Aggridcolumn.ts

	Purpose:

	Description:

	History:
		Fri Jun 19 14:36:45 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
 */
(() => {
interface ColumnDef {
	children?: ColumnDef[];
}

aggrid.Aggridcolumn = zk.$extends(zk.Widget, {
	$init() {
		this.$supers('$init', arguments);
		this._colDef = {};
	},
	$define: {
		width(v) {
			this._colDef.width = v;
		}
	},
	// TODO: since I can't allow wgt.key = value to call __set, this should be refined.
	__set(name: string, value) {
		if (agGrid.ColDefUtil.ALL_PROPERTIES.indexOf(name) !== -1)
			this._colDef[name] = value;
		else
			this[name] = value;
	},
	toColDef(): ColumnDef {
		let colDef: ColumnDef = this._generateColDef();
		if (this.nChildren) {
			colDef.children = this.$class.mapToColumnDefs(this.firstChild);
		}
		return colDef;
	},
	_generateColDef(): ColumnDef {
		let colDef: ColumnDef = zk.copy({}, this._colDef);
		delete colDef.children;
		return colDef;
	}
}, {
	mapToColumnDefs(column: zk.Widget | null): ColumnDef[] {
		let columnDefs: ColumnDef[] = [];
		for (; column; column = column.nextSibling) {
			columnDefs.push(column.toColDef());
		}
		return columnDefs;
	}
});
})();