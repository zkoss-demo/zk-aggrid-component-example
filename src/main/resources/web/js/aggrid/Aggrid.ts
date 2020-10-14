/* Aggrid.ts

	Purpose:

	Description:

	History:
		Fri Jun 19 14:36:45 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
 */
import {
	ColDef,
	Column,
	ColumnApi,
	GridApi,
	IDatasource,
	IGetRowsParams
} from '@ag-grid-community/core';

(() => {
aggrid.Aggrid = zk.$extends(zul.Widget, {
	_theme: 'ag-theme-alpine',
	_currentTop: 0,
	_currentPage: 0,

	$init(): void {
		this.$supers('$init', arguments);
		this._gridOptions = {};
		this._selection = {
			'selected': [],
			'unselected': []
		};
		this._selectedUuids = new Set();
		// @ts-ignore
		agGrid.PropertyKeys.ALL_PROPERTIES.forEach(prop => {
			Object.defineProperty(this, prop,{
				configurable: true, // some properties are multi-type (e.g: string and function), possible duplicated
				set(newValue) {
					this._gridOptions[prop] = newValue;
					if (this.desktop) {
						this.rerenderLater_ ? this.rerenderLater_() : this.rerender();
					}
				},
				get() { return this._gridOptions[prop]; }
			});
		});
	},

	$define: {
		model(v: boolean): void {
			if (this.desktop) {
				this.gridApi().setDatasource(v ? this._newDataSource() : this._emptyDataSource());
			}
		},
		theme(v: string): void {
			if (this.desktop) {
				jq(this.$n()).removeClass((index, className) => {
					let found = className.match(/ag-theme-[\w-]+/);
					return found ? found[0] : '';
				}).addClass(v);
			}
		}
	},
	bind_(): void {
		this.$supers(aggrid.Aggrid, 'bind_', arguments);
		let gridOptions = this._gridOptions;
		gridOptions.defaultColDef = this._getDefaultColDef();
		gridOptions.columnDefs = this._getColDefs();
		gridOptions.getRowNodeId = this._getRowUuid;
		gridOptions.rowModelType = 'infinite';
		gridOptions.datasource = this._model ? this._newDataSource() : this._emptyDataSource();
		// @ts-ignore
		new agGrid.Grid(this.$n(), gridOptions);
		this._registerCallbacks();
		zWatch.listen({onResponse: this});
		this._shouldUpdateColDefs = false;
		this._init = false;
	},
	unbind_(): void {
		zWatch.unlisten({onResponse: this});
		this._unregisterCallbacks();
		this.$supers(aggrid.Aggrid, 'unbind_', arguments);
	},
	domClass_(): string {
		return this.$supers('domClass_', arguments) + ' ' + this._theme;
	},
	onChildAdded_(child: zul.Widget): void {
		this.$super('onChildAdded_', arguments);
		this.requestUpdateColDefs_();
	},
	onChildRemoved_(child: zul.Widget): void {
		this.requestUpdateColDefs_();
		this.$super('onChildRemoved_', arguments);
	},
	requestUpdateColDefs_(): void {
		// FIXME: called by client setter will not trigger onResponse
		this._shouldUpdateColDefs = true;
	},
	onResponse(e: zk.Event): void {
		if (this._shouldUpdateColDefs) {
			this._shouldUpdateColDefs = false;
			this._updateColDefs();
		}
	},

	_getRowUuid(item): number {
		return item['_zk_uuid'];
	},
	gridApi(): GridApi {
		return this._gridOptions.api;
	},
	columnApi(): ColumnApi {
		return this._gridOptions.columnApi;
	},
	_getDefaultColDef(): ColDef | null {
		return this._findColumn(c => aggrid.Aggriddefaultcolumn.isInstance(c), c => c.toColDef());
	},
	_findColumn<T>(test: (o: zk.Widget) => any, result: (o: zk.Widget) => T): T | null {
		let stack = [this.firstChild];
		while (stack.length > 0) {
			let column = stack.shift();
			if (column) {
				if (test(column))
					return result(column);
				stack.push(column.firstChild);
				stack.push(column.nextSibling);
			}
		}
		return null;
	},
	_getColDefs(): ColDef[] {
		return aggrid.Aggridcolumn.mapToColumnDefs(this.firstChild);
	},
	_updateColDefs(): void {
		this._gridOptions.defaultColDef = this._getDefaultColDef();
		this._gridOptions.columnDefs = this._getColDefs();
		this.rerender();
	},
	_registerCallbacks(): void {
		this.gridApi().addGlobalListener(this.proxy(this._handleEvents));
	},
	_unregisterCallbacks(): void {
		this.gridApi().removeGlobalListener(this.proxy(this._handleEvents));
	},
	_handleEvents(name: string, e): void {
		let selection = this._selection,
			selectedUuids: Set<number> = this._selectedUuids;
		switch (name) {
			case 'rowSelected':
				let node = e.node,
					uuid = node.id,
					selected = node.selected,
					isInRecord = selectedUuids.has(uuid);
				if (selected != isInRecord) { // only handled if changed
					selection[selected ? 'selected' : 'unselected'].push(e.rowIndex);
					selectedUuids[selected ? 'add' : 'delete'](uuid);
				}
				this._fireEvent(name, e);
				break;
			case 'selectionChanged':
				if (selection['selected'].length || selection['unselected'].length) {
					this.fire('onSelectionChanged', {
						selected: [...selection['selected']],
						unselected: [...selection['unselected']],
					});
					selection['selected'].length = 0;
					selection['unselected'].length = 0;
				}
				break;
			case 'paginationChanged':
				this._currentPage = e.api.paginationGetCurrentPage();
				this._checkSelected();
				this._fireEvent(name, e);
				break;
			case 'viewportChanged':
				this._currentTop = e.firstRow;
				this._checkSelected(); // FIXME: poor performance
				this._fireEvent(name, e);
				break;
			case 'columnResized':
				if (e.column && e.source == 'uiColumnDragged') {
					this.fire('onColumnResized',
						{...this._filterEvent(e), actualWidth: e.column.actualWidth},
						{toServer: true});
				} else {
					this._fireEvent(name, e);
				}
				break;
			case 'columnPinned':
				this._updateColumnDef(e.columns, 'pinned', e.pinned);
				this._fireEvent(name, e);
				break;
			case 'columnVisible':
				this._updateColumnDef(e.columns, 'hide', !e.visible);
				this._fireEvent(name, e);
				break;
			default:
				this._fireEvent(name, e);
		}
	},
	_fireEvent(name: string, e) {
		let eventName = 'on' + name.substring(0, 1).toUpperCase() + name.substring(1);
		if (this.isListen(eventName, {any: true}))
			this.fire(eventName, this._filterEvent(e));
	},
	_filterEvent(e): object {
		let keys = ['api', 'columnApi', 'event',
				'node', 'nodes', 'overNode', 'data', 'afterFloatingFilter',
				'columnGroup', 'column', 'columns', 'flexColumns', 'source', 'target'],
			target = {agGrid: true},
			column: Column | null = e['column'],
			columns: Column[] | null = e['columns'];
		if (column)
			target['column'] = column.getColDef()['_zk_uuid'];
		if (columns)
			target['columns'] = columns.map(c => c.getColDef()['_zk_uuid']);
		for (let i in e) {
			if (!e.hasOwnProperty(i)) continue;
			if (keys.indexOf(i) >= 0) continue;
			target[i] = e[i];
		}
		return target;
	},
	_updateColumnDef(cols: ColDef[] | null, attr: string, value: any): void {
		if (cols) {
			cols.forEach(c => {
				let wgt = zk.$(c.getColDef()['_zk_uuid']);
				if (wgt) wgt._colDef[attr] = value;
			})
		}
	},
	_pagingBlock(rows: any[], lastRow?: number): void {
		let successCallback = this._successCallback;
		if (successCallback) {
			this._successCallback = null;
			this.gridApi().hideOverlay();
			successCallback(rows, lastRow);
			// FIXME: a dirty workaround to keep page/scroll positions
			if (!this._init) {
				this._init = true;
				let api = this.gridApi();
				if (this._currentPage)
					api.paginationGoToPage(this._currentPage);
				if (this._currentTop) {
					api.ensureIndexVisible(this._currentTop, 'top');
				}
			}
		}
	},
	_checkSelected(): void {
		let api = this.gridApi(),
			selectedUuids: Set<number> = this._selectedUuids;
		if (api) {
			api.forEachNode(node => {
				let oldValue = node.selected,
					newValue = selectedUuids.has(node.id);
				if (oldValue != newValue)
					node.setSelected(newValue, false, true);
			});
		}
	},
	set_refreshInfiniteCache(): void {
		this.gridApi().refreshInfiniteCache();
	},
	set_selectedUuids(uuids: number[]): void {
		let selectedUuids: Set<number> = this._selectedUuids;
		selectedUuids.clear();
		uuids.forEach(uuid => selectedUuids.add(uuid));
		this._checkSelected();
	},
	exportDataAsCsv(): void {
		this.gridApi().exportDataAsCsv();
	},

	_newDataSource(): IDatasource {
		let self = this;
		return new class implements IDatasource {
			getRows(params: IGetRowsParams): void {
				let {startRow, endRow, sortModel, filterModel, successCallback, failCallback} = params;
				try {
					self.fire('onPaging', {startRow, endRow, sortModel, filterModel});
					self._successCallback = successCallback;
				} catch (e) {
					failCallback();
				}
			}
		};
	},
	_emptyDataSource(): IDatasource {
		let self = this;
		return new class implements IDatasource {
			getRows(params: IGetRowsParams): void {
				self._selectedUuids.clear();
				self.gridApi().showNoRowsOverlay();
				params.successCallback([], 0);
			}
		};
	}
});
})();