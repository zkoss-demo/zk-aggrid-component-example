/* Aggrid.ts

	Purpose:

	Description:

	History:
		Fri Jun 19 14:36:45 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
 */
import {
	ColDef,
	ColumnApi,
	GridApi,
	IDatasource,
	IGetRowsParams
} from '@ag-grid-community/core';

(() => {
aggrid.Aggrid = zk.$extends(zul.Widget, {
	_theme: 'ag-theme-alpine',

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
	_getColDefs(): Array<ColDef> {
		return this.nChildren ? aggrid.Aggridcolumn.mapToColumnDefs(this.firstChild) : [];
	},
	_getDefaultColDef(): ColDef | null {
		for (let child = this.firstChild; child; child = child.nextSibling) {
			if (aggrid.Aggriddefaultcolumn.isInstance(child))
				return child.toColDef();
		}
		return null;
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
			case 'viewportChanged':
				this._checkSelected();
				this._fireEvent(name, e);
				break;
			default:
				this._fireEvent(name, e);
		}
	},
	_fireEvent(name: string, e) {
		this.fire('on' + name.substring(0, 1).toUpperCase() + name.substring(1), this._filterEvent(e));
	},
	_filterEvent(e): object {
		let keys = ['api', 'columnApi', 'afterFloatingFilter', 'event',
				'node', 'nodes', 'overNode', 'data', 'afterFloatingFilter',
				'columnGroup', 'column', 'columns', 'flexColumns', 'source', 'target'],
			target = {agGrid: true};
		for (let i in e) {
			if (keys.indexOf(i) >= 0) continue;
			if (!Object.prototype.hasOwnProperty.call(e, i)) continue;
			target[i] = e[i];
		}
		return target;
	},
	_pagingBlock(rows, lastRow): void {
		let successCallback = this._successCallback;
		if (successCallback) {
			this._successCallback = null;
			this.gridApi().hideOverlay();
			successCallback(rows, lastRow);
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