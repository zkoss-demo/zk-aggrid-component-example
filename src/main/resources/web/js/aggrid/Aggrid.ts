/* Aggrid.ts

	Purpose:

	Description:

	History:
		Fri Jun 19 14:36:45 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
 */
(() => {
// Infinite Scrolling Datasource
interface Datasource {
	// Callback the grid calls that you implement to fetch rows from the server. See below for params.
	getRows(params: GetRowsParams): void;

	// optional destroy method, if your datasource has state it needs to clean up
	destroy?(): void;
}

// Params for the above Datasource.getRows()
interface GetRowsParams {
	// The first row index to get.
	startRow: number;

	// The first row index to NOT get.
	endRow: number;

	// If doing server-side sorting, contains the sort model
	sortModel: any;

	// If doing server-side filtering, contains the filter model
	filterModel: any;

	// The grid context object
	context: any;

	// Callback to call when the request is successful.
	successCallback(rowsThisBlock: any[], lastRow?: number): void;

	// Callback to call when the request fails.
	failCallback(): void;
}

aggrid.Aggrid = zk.$extends(zul.Widget, {
	_theme: 'ag-theme-alpine',

	$init() {
		this.$supers('$init', arguments);
		this._gridOptions = {};
		agGrid.PropertyKeys.ALL_PROPERTIES.forEach((prop) => {
			Object.defineProperty(this, prop,{
				configurable: true, // some properties are multi-type (e.g: string and function), possible duplicated
				set(newValue) { this._gridOptions[prop] = newValue; },
				get() { return this._gridOptions[prop]; }
			});
		});
	},

	$define: {
		theme(v: string) {
			if (this.desktop) {
				jq(this.$n()).removeClass((index, className) => {
					let found = className.match(/ag-theme-[\w-]+/);
					return found ? found[0] : '';
				}).addClass(v);
			}
		}
	},
	bind_() {
		this.$supers(aggrid.Aggrid, 'bind_', arguments);
		let gridOptions = this._gridOptions;
		if (this.nChildren) {
			gridOptions.columnDefs = aggrid.Aggridcolumn.mapToColumnDefs(this.firstChild);
		}
		gridOptions.getRowNodeId = this._getRowUuid;
		this._initRowModel(gridOptions);
		new agGrid.Grid(this.$n(), gridOptions);
		this._registerCallbacks();
	},
	unbind_() {
		this._unregisterCallbacks();
		this.$supers(aggrid.Aggrid, 'unbind_', arguments);
	},

	_initRowModel(gridOptions) {
		gridOptions.rowModelType = 'infinite';
		gridOptions.datasource = this._newDataSource();
	},
	_getRowUuid(item): string {
		return item['_zk_uuid'];
	},
	_registerCallbacks() {
		this._api().addGlobalListener(this.proxy(this._handleEvents));
	},
	_unregisterCallbacks() {
		this._api().removeGlobalListener(this.proxy(this._handleEvents));
	},
	_api() {
		return this._gridOptions.api;
	},
	_handleEvents(name: string, e) {
		let api = e.api;
		switch (name) {
			case 'selectionChanged':
				let selectedIds = api.getSelectedNodes()
					.map(curr => curr.rowIndex) // FIXME: wrong if sorted in clientSide
					.filter(id => id != null);
				this.fire('onSelectionChanged', {selectedIds: selectedIds});
				break;
			default:
				this.fire('on' + name.substring(0, 1).toUpperCase() + name.substring(1), this._filterEvent(e));
		}
	},
	_filterEvent(e): object {
		let keys = ['api', 'columnApi', 'event', 'node', 'column', 'source', 'target'],
			target = {};
		for (let i in e) {
			if (keys.indexOf(i) >= 0) continue;
			if (!Object.prototype.hasOwnProperty.call(e, i)) continue;
			target[i] = e[i];
		}
		return target;
	},

	domClass_(): string {
		return this.$supers('domClass_', arguments) + ' ' + this._theme;
	},
	_pagingBlock(rows, lastRow) {
		let successCallback = this._successCallback;
		if (successCallback) {
			this._successCallback = null;
			successCallback(rows, lastRow);
			this._checkSelected(this._selectedUuids);
		}
	},
	set_refreshInfiniteCache() {
		this._api().refreshInfiniteCache();
	},
	set_selectedUuids(uuids: number[], fromServer?: boolean) {
		this._selectedUuids = uuids;
		this._checkSelected(uuids, fromServer);
	},
	_checkSelected: function (uuids: number[], triggerSelectionChanged = false) {
		let api = this._api();
		if (api) {
			api.deselectAll();
			api.forEachNode(node => {
				if (uuids.indexOf(node.id) !== -1)
					node.setSelected(true, false, triggerSelectionChanged);
			});
		}
	},

	_newDataSource(): Datasource {
		let self = this;
		return new class implements Datasource {
			public getRows(params: GetRowsParams): void {
				let {startRow, endRow, sortModel, filterModel, successCallback, failCallback} = params;
				try {
					self.fire('onPaging', {startRow, endRow, sortModel, filterModel});
					self._successCallback = successCallback;
				} catch (e) {
					failCallback();
				}
			}
		};
	}
});
})();