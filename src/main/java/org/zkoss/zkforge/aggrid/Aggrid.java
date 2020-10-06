/* Aggrid.java

	Purpose:

	Description:

	History:
		Fri Jun 19 14:30:11 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.lang.Generics;
import org.zkoss.lang.Objects;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuRequests;
import org.zkoss.zk.au.out.AuInvoke;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zkforge.aggrid.filter.ColumnFilters;
import org.zkoss.zkforge.aggrid.filter.Filter;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.event.ListDataListener;
import org.zkoss.zul.ext.Selectable;
import org.zkoss.zul.ext.Sortable;
import org.zkoss.zul.impl.XulElement;

/**
 * Ag-Grid component.
 * Browser support: Internet Explorer 11, Edge, Firefox, Chrome and Safari.
 *
 * Supports {@link ListModel}.
 *
 * @author rudyhuang
 */
public class Aggrid<E> extends XulElement {
	static {
		addClientEvent(Aggrid.class, "onPaging", CE_IMPORTANT | CE_DUPLICATE_IGNORE | CE_NON_DEFERRABLE);
		// Selection
		addClientEvent(Aggrid.class, "onCellClicked", 0);
		addClientEvent(Aggrid.class, "onCellDoubleClicked", 0);
		addClientEvent(Aggrid.class, "onCellFocused", 0);
		addClientEvent(Aggrid.class, "onCellMouseOver", 0);
		addClientEvent(Aggrid.class, "onCellMouseOut", 0);
		addClientEvent(Aggrid.class, "onCellMouseDown", 0);
		addClientEvent(Aggrid.class, "onRowClicked", 0);
		addClientEvent(Aggrid.class, "onRowDoubleClicked", 0);
		addClientEvent(Aggrid.class, "onRowSelected", 0);
		addClientEvent(Aggrid.class, "onSelectionChanged", CE_IMPORTANT);
		addClientEvent(Aggrid.class, "onCellContextMenu", 0);
		addClientEvent(Aggrid.class, "onRangeSelectionChanged", 0);
		// Editing
		addClientEvent(Aggrid.class, "onCellValueChanged", 0);
		addClientEvent(Aggrid.class, "onRowValueChanged", 0);
		addClientEvent(Aggrid.class, "onCellEditingStarted", 0);
		addClientEvent(Aggrid.class, "onCellEditingStopped", 0);
		addClientEvent(Aggrid.class, "onRowEditingStarted", 0);
		addClientEvent(Aggrid.class, "onRowEditingStopped", 0);
		addClientEvent(Aggrid.class, "onPasteStart", 0);
		addClientEvent(Aggrid.class, "onPasteEnd", 0);
		// Sort and Filter
		addClientEvent(Aggrid.class, "onSortChanged", 0);
		addClientEvent(Aggrid.class, "onFilterChanged", 0);
		addClientEvent(Aggrid.class, "onFilterModified", 0);
		// Row Drag and Drop
		addClientEvent(Aggrid.class, "onRowDragEnter", 0);
		addClientEvent(Aggrid.class, "onRowDragMove", 0);
		addClientEvent(Aggrid.class, "onRowDragLeave", 0);
		addClientEvent(Aggrid.class, "onRowDragEnd", 0);
		// Columns
		addClientEvent(Aggrid.class, "onColumnVisible", CE_IMPORTANT | CE_DUPLICATE_IGNORE);
		addClientEvent(Aggrid.class, "onColumnPinned", CE_IMPORTANT | CE_DUPLICATE_IGNORE);
		addClientEvent(Aggrid.class, "onColumnResized", CE_IMPORTANT | CE_DUPLICATE_IGNORE);
		addClientEvent(Aggrid.class, "onColumnMoved", CE_IMPORTANT | CE_DUPLICATE_IGNORE);
		addClientEvent(Aggrid.class, "onColumnRowGroupChanged", 0);
		addClientEvent(Aggrid.class, "onColumnValueChanged", 0);
		addClientEvent(Aggrid.class, "onColumnPivotModeChanged", 0);
		addClientEvent(Aggrid.class, "onColumnPivotChanged", 0);
		addClientEvent(Aggrid.class, "onColumnGroupOpened", 0);
		addClientEvent(Aggrid.class, "onNewColumnsLoaded", 0);
		addClientEvent(Aggrid.class, "onGridColumnsChanged", 0);
		addClientEvent(Aggrid.class, "onDisplayedColumnsChanged", 0);
		addClientEvent(Aggrid.class, "onVirtualColumnsChanged", 0);
		addClientEvent(Aggrid.class, "onColumnEverythingChanged", 0);
		// Miscellaneous
		addClientEvent(Aggrid.class, "onGridReady", 0);
		addClientEvent(Aggrid.class, "onGridSizeChanged", 0);
		addClientEvent(Aggrid.class, "onModelUpdated", 0);
		addClientEvent(Aggrid.class, "onFirstDataRendered", 0);
		addClientEvent(Aggrid.class, "onRowGroupOpened", 0);
		addClientEvent(Aggrid.class, "onExpandOrCollapseAll", 0);
		addClientEvent(Aggrid.class, "onPaginationChanged", 0);
		addClientEvent(Aggrid.class, "onPinnedRowDataChanged", 0);
		addClientEvent(Aggrid.class, "onVirtualRowRemoved", 0);
		addClientEvent(Aggrid.class, "onViewportChanged", 0);
		addClientEvent(Aggrid.class, "onBodyScroll", 0);
		addClientEvent(Aggrid.class, "onDragStarted", 0);
		addClientEvent(Aggrid.class, "onDragStopped", 0);
		addClientEvent(Aggrid.class, "onRowDataChanged", 0);
		addClientEvent(Aggrid.class, "onRowDataUpdated", 0);
		addClientEvent(Aggrid.class, "onToolPanelVisibleChanged", 0);
		addClientEvent(Aggrid.class, "onAnimationQueueEmpty", 0);
		addClientEvent(Aggrid.class, "onCellKeyDown", 0);
		addClientEvent(Aggrid.class, "onCellKeyPress", 0);
	}

	private AuxInfo _auxinf;
	private String _theme = "ag-theme-alpine";
	private ListModel<E> _model;
	/** whether to ignore ListDataEvent.SELECTION_CHANGED */
	private transient boolean _ignoreDataSelectionEvent;
	private final ListDataListener _modelListDataListener = this::onListDataChange;
	private AggridDefaultColumn _defaultColDef;

	//#region properties
	public boolean isSuppressAutoSize() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSAUTOSIZE);
	}

	public void setSuppressAutoSize(boolean suppressAutoSize) {
		if (isSuppressAutoSize() != suppressAutoSize) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSAUTOSIZE, suppressAutoSize);
			smartUpdate("suppressAutoSize", suppressAutoSize);
		}
	}

	public int getAutoSizePadding() {
		return _auxinf != null ? _auxinf.autoSizePadding : 4;
	}

	public void setAutoSizePadding(int autoSizePadding) {
		if (getAutoSizePadding() != autoSizePadding) {
			initAuxInfo().autoSizePadding = autoSizePadding;
			smartUpdate("autoSizePadding", autoSizePadding);
		}
	}

	public boolean isSkipHeaderOnAutoSize() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SKIPHEADERONAUTOSIZE);
	}

	public void setSkipHeaderOnAutoSize(boolean skipHeaderOnAutoSize) {
		if (isSkipHeaderOnAutoSize() != skipHeaderOnAutoSize) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SKIPHEADERONAUTOSIZE, skipHeaderOnAutoSize);
			smartUpdate("skipHeaderOnAutoSize", skipHeaderOnAutoSize);
		}
	}

	public boolean isSuppressColumnMoveAnimation() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSCOLUMNMOVEANIMATION);
	}

	public void setSuppressColumnMoveAnimation(boolean suppressColumnMoveAnimation) {
		if (isSuppressColumnMoveAnimation() != suppressColumnMoveAnimation) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSCOLUMNMOVEANIMATION, suppressColumnMoveAnimation);
			smartUpdate("suppressColumnMoveAnimation", suppressColumnMoveAnimation);
		}
	}

	public boolean isSuppressMovableColumns() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSMOVABLECOLUMNS);
	}

	public void setSuppressMovableColumns(boolean suppressMovableColumns) {
		if (isSuppressMovableColumns() != suppressMovableColumns) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSMOVABLECOLUMNS, suppressMovableColumns);
			smartUpdate("suppressMovableColumns", suppressMovableColumns);
		}
	}

	public boolean isSuppressFieldDotNotation() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSFIELDDOTNOTATION);
	}

	public void setSuppressFieldDotNotation(boolean suppressFieldDotNotation) {
		if (isSuppressFieldDotNotation() != suppressFieldDotNotation) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSFIELDDOTNOTATION, suppressFieldDotNotation);
			smartUpdate("suppressFieldDotNotation", suppressFieldDotNotation);
		}
	}

	public boolean isUnSortIcon() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.UNSORTICON);
	}

	public void setUnSortIcon(boolean unSortIcon) {
		if (isUnSortIcon() != unSortIcon) {
			initAuxInfo().setBoolean(AuxInfo.Attr.UNSORTICON, unSortIcon);
			smartUpdate("unSortIcon", unSortIcon);
		}
	}

	public boolean isSuppressMultiSort() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSMULTISORT);
	}

	public void setSuppressMultiSort(boolean suppressMultiSort) {
		if (isSuppressMultiSort() != suppressMultiSort) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSMULTISORT, suppressMultiSort);
			smartUpdate("suppressMultiSort", suppressMultiSort);
		}
	}

	public boolean isSuppressMenuHide() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSMENUHIDE);
	}

	public void setSuppressMenuHide(boolean suppressMenuHide) {
		if (isSuppressMenuHide() != suppressMenuHide) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSMENUHIDE, suppressMenuHide);
			smartUpdate("suppressMenuHide", suppressMenuHide);
		}
	}

	public boolean isSuppressSetColumnStateEvents() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSSETCOLUMNSTATEEVENTS);
	}

	public void setSuppressSetColumnStateEvents(boolean suppressSetColumnStateEvents) {
		if (isSuppressSetColumnStateEvents() != suppressSetColumnStateEvents) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSSETCOLUMNSTATEEVENTS, suppressSetColumnStateEvents);
			smartUpdate("suppressSetColumnStateEvents", suppressSetColumnStateEvents);
		}
	}

	public boolean isAllowDragFromColumnsToolPanel() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ALLOWDRAGFROMCOLUMNSTOOLPANEL);
	}

	public void setAllowDragFromColumnsToolPanel(boolean allowDragFromColumnsToolPanel) {
		if (isAllowDragFromColumnsToolPanel() != allowDragFromColumnsToolPanel) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ALLOWDRAGFROMCOLUMNSTOOLPANEL, allowDragFromColumnsToolPanel);
			smartUpdate("allowDragFromColumnsToolPanel", allowDragFromColumnsToolPanel);
		}
	}

	public boolean isImmutableColumns() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.IMMUTABLECOLUMNS);
	}

	public void setImmutableColumns(boolean immutableColumns) {
		if (isImmutableColumns() != immutableColumns) {
			initAuxInfo().setBoolean(AuxInfo.Attr.IMMUTABLECOLUMNS, immutableColumns);
			smartUpdate("immutableColumns", immutableColumns);
		}
	}

	public boolean isCacheQuickFilter() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.CACHEQUICKFILTER);
	}

	public void setCacheQuickFilter(boolean cacheQuickFilter) {
		if (isCacheQuickFilter() != cacheQuickFilter) {
			initAuxInfo().setBoolean(AuxInfo.Attr.CACHEQUICKFILTER, cacheQuickFilter);
			smartUpdate("cacheQuickFilter", cacheQuickFilter);
		}
	}

	public boolean isAccentedSort() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ACCENTEDSORT);
	}

	public void setAccentedSort(boolean accentedSort) {
		if (isAccentedSort() != accentedSort) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ACCENTEDSORT, accentedSort);
			smartUpdate("accentedSort", accentedSort);
		}
	}

	public boolean isSuppressMaintainUnsortedOrder() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSMAINTAINUNSORTEDORDER);
	}

	public void setSuppressMaintainUnsortedOrder(boolean suppressMaintainUnsortedOrder) {
		if (isSuppressMaintainUnsortedOrder() != suppressMaintainUnsortedOrder) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSMAINTAINUNSORTEDORDER, suppressMaintainUnsortedOrder);
			smartUpdate("suppressMaintainUnsortedOrder", suppressMaintainUnsortedOrder);
		}
	}

	public boolean isExcludeChildrenWhenTreeDataFiltering() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.EXCLUDECHILDRENWHENTREEDATAFILTERING);
	}

	public void setExcludeChildrenWhenTreeDataFiltering(boolean excludeChildrenWhenTreeDataFiltering) {
		if (isExcludeChildrenWhenTreeDataFiltering() != excludeChildrenWhenTreeDataFiltering) {
			initAuxInfo().setBoolean(AuxInfo.Attr.EXCLUDECHILDRENWHENTREEDATAFILTERING, excludeChildrenWhenTreeDataFiltering);
			smartUpdate("excludeChildrenWhenTreeDataFiltering", excludeChildrenWhenTreeDataFiltering);
		}
	}

	public String getRowSelection() {
		return _auxinf != null ? _auxinf.rowSelection : null;
	}

	public void setRowSelection(String rowSelection) {
		boolean isMultiple = "multiple".equals(rowSelection);
		if (rowSelection != null && !"single".equals(rowSelection) && !isMultiple)
			throw new WrongValueException("expected null, single or multiple: " + rowSelection);
		if (!Objects.equals(getRowSelection(), rowSelection)) {
			initAuxInfo().rowSelection = rowSelection;
			smartUpdate("rowSelection", rowSelection);
			if (_model != null)
				getSelectableModel().setMultiple(isMultiple);
		}
	}

	public boolean isRowMultiSelectWithClick() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ROWMULTISELECTWITHCLICK);
	}

	public void setRowMultiSelectWithClick(boolean rowMultiSelectWithClick) {
		if (isRowMultiSelectWithClick() != rowMultiSelectWithClick) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ROWMULTISELECTWITHCLICK, rowMultiSelectWithClick);
			smartUpdate("rowMultiSelectWithClick", rowMultiSelectWithClick);
		}
	}

	public boolean isRowDeselection() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ROWDESELECTION);
	}

	public void setRowDeselection(boolean rowDeselection) {
		if (isRowDeselection() != rowDeselection) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ROWDESELECTION, rowDeselection);
			smartUpdate("rowDeselection", rowDeselection);
		}
	}

	public boolean isSuppressRowClickSelection() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSROWCLICKSELECTION);
	}

	public void setSuppressRowClickSelection(boolean suppressRowClickSelection) {
		if (isSuppressRowClickSelection() != suppressRowClickSelection) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSROWCLICKSELECTION, suppressRowClickSelection);
			smartUpdate("suppressRowClickSelection", suppressRowClickSelection);
		}
	}

	public boolean isSuppressCellSelection() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSCELLSELECTION);
	}

	public void setSuppressCellSelection(boolean suppressCellSelection) {
		if (isSuppressCellSelection() != suppressCellSelection) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSCELLSELECTION, suppressCellSelection);
			smartUpdate("suppressCellSelection", suppressCellSelection);
		}
	}

	public boolean isEnableRangeSelection() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENABLERANGESELECTION);
	}

	public void setEnableRangeSelection(boolean enableRangeSelection) {
		if (isEnableRangeSelection() != enableRangeSelection) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ENABLERANGESELECTION, enableRangeSelection);
			smartUpdate("enableRangeSelection", enableRangeSelection);
		}
	}

	public boolean isRowDragManaged() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ROWDRAGMANAGED);
	}

	public void setRowDragManaged(boolean rowDragManaged) {
		if (isRowDragManaged() != rowDragManaged) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ROWDRAGMANAGED, rowDragManaged);
			smartUpdate("rowDragManaged", rowDragManaged);
		}
	}

	public boolean isSuppressRowDrag() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSROWDRAG);
	}

	public void setSuppressRowDrag(boolean suppressRowDrag) {
		if (isSuppressRowDrag() != suppressRowDrag) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSROWDRAG, suppressRowDrag);
			smartUpdate("suppressRowDrag", suppressRowDrag);
		}
	}

	public boolean isSuppressMoveWhenRowDragging() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSMOVEWHENROWDRAGGING);
	}

	public void setSuppressMoveWhenRowDragging(boolean suppressMoveWhenRowDragging) {
		if (isSuppressMoveWhenRowDragging() != suppressMoveWhenRowDragging) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSMOVEWHENROWDRAGGING, suppressMoveWhenRowDragging);
			smartUpdate("suppressMoveWhenRowDragging", suppressMoveWhenRowDragging);
		}
	}

	public boolean isSingleClickEdit() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SINGLECLICKEDIT);
	}

	public void setSingleClickEdit(boolean singleClickEdit) {
		if (isSingleClickEdit() != singleClickEdit) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SINGLECLICKEDIT, singleClickEdit);
			smartUpdate("singleClickEdit", singleClickEdit);
		}
	}

	public boolean isSuppressClickEdit() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSCLICKEDIT);
	}

	public void setSuppressClickEdit(boolean suppressClickEdit) {
		if (isSuppressClickEdit() != suppressClickEdit) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSCLICKEDIT, suppressClickEdit);
			smartUpdate("suppressClickEdit", suppressClickEdit);
		}
	}

	public String getEditType() {
		return _auxinf != null ? _auxinf.editType : null;
	}

	public void setEditType(String editType) {
		if (!"fullRow".equals(editType) && editType != null) {
			throw new WrongValueException("expected null or fullRow: " + editType);
		}
		if (!Objects.equals(getEditType(), editType)) {
			initAuxInfo().editType = editType;
			smartUpdate("editType", editType);
		}
	}

	public boolean isEnableCellChangeFlash() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENABLECELLCHANGEFLASH);
	}

	public void setEnableCellChangeFlash(boolean enableCellChangeFlash) {
		if (isEnableCellChangeFlash() != enableCellChangeFlash) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ENABLECELLCHANGEFLASH, enableCellChangeFlash);
			smartUpdate("enableCellChangeFlash", enableCellChangeFlash);
		}
	}

	public int getCellFlashDelay() {
		return _auxinf != null ? _auxinf.cellFlashDelay : 500;
	}

	public void setCellFlashDelay(int cellFlashDelay) {
		if (getCellFlashDelay() != cellFlashDelay) {
			initAuxInfo().cellFlashDelay = cellFlashDelay;
			smartUpdate("cellFlashDelay", cellFlashDelay);
		}
	}

	public int getCellFadeDelay() {
		return _auxinf != null ? _auxinf.cellFadeDelay : 1000;
	}

	public void setCellFadeDelay(int cellFadeDelay) {
		if (getCellFadeDelay() != cellFadeDelay) {
			initAuxInfo().cellFadeDelay = cellFadeDelay;
			smartUpdate("cellFadeDelay", cellFadeDelay);
		}
	}

	public boolean isAllowShowChangeAfterFilter() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ALLOWSHOWCHANGEAFTERFILTER);
	}

	public void setAllowShowChangeAfterFilter(boolean allowShowChangeAfterFilter) {
		if (isAllowShowChangeAfterFilter() != allowShowChangeAfterFilter) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ALLOWSHOWCHANGEAFTERFILTER, allowShowChangeAfterFilter);
			smartUpdate("allowShowChangeAfterFilter", allowShowChangeAfterFilter);
		}
	}

	public boolean isStopEditingWhenGridLosesFocus() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.STOPEDITINGWHENGRIDLOSESFOCUS);
	}

	public void setStopEditingWhenGridLosesFocus(boolean stopEditingWhenGridLosesFocus) {
		if (isStopEditingWhenGridLosesFocus() != stopEditingWhenGridLosesFocus) {
			initAuxInfo().setBoolean(AuxInfo.Attr.STOPEDITINGWHENGRIDLOSESFOCUS, stopEditingWhenGridLosesFocus);
			smartUpdate("stopEditingWhenGridLosesFocus", stopEditingWhenGridLosesFocus);
		}
	}

	public boolean isEnterMovesDown() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENTERMOVESDOWN);
	}

	public void setEnterMovesDown(boolean enterMovesDown) {
		if (isEnterMovesDown() != enterMovesDown) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ENTERMOVESDOWN, enterMovesDown);
			smartUpdate("enterMovesDown", enterMovesDown);
		}
	}

	public boolean isEnterMovesDownAfterEdit() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENTERMOVESDOWNAFTEREDIT);
	}

	public void setEnterMovesDownAfterEdit(boolean enterMovesDownAfterEdit) {
		if (isEnterMovesDownAfterEdit() != enterMovesDownAfterEdit) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ENTERMOVESDOWNAFTEREDIT, enterMovesDownAfterEdit);
			smartUpdate("enterMovesDownAfterEdit", enterMovesDownAfterEdit);
		}
	}

	public int getHeaderHeight() {
		return _auxinf != null ? _auxinf.headerHeight : 25;
	}

	public void setHeaderHeight(int headerHeight) {
		if (getHeaderHeight() != headerHeight) {
			initAuxInfo().headerHeight = headerHeight;
			smartUpdate("headerHeight", headerHeight);
		}
	}

	public int getGroupHeaderHeight() {
		return _auxinf != null ? _auxinf.groupHeaderHeight : 0;
	}

	public void setGroupHeaderHeight(int groupHeaderHeight) {
		if (getGroupHeaderHeight() != groupHeaderHeight) {
			initAuxInfo().groupHeaderHeight = groupHeaderHeight;
			smartUpdate("groupHeaderHeight", groupHeaderHeight);
		}
	}

	public int getFloatingFiltersHeight() {
		return _auxinf != null ? _auxinf.floatingFiltersHeight : 20;
	}

	public void setFloatingFiltersHeight(int floatingFiltersHeight) {
		if (getFloatingFiltersHeight() != floatingFiltersHeight) {
			initAuxInfo().floatingFiltersHeight = floatingFiltersHeight;
			smartUpdate("floatingFiltersHeight", floatingFiltersHeight);
		}
	}

	public int getPivotHeaderHeight() {
		return _auxinf != null ? _auxinf.pivotHeaderHeight : 0;
	}

	public void setPivotHeaderHeight(int pivotHeaderHeight) {
		if (getPivotHeaderHeight() != pivotHeaderHeight) {
			initAuxInfo().pivotHeaderHeight = pivotHeaderHeight;
			smartUpdate("pivotHeaderHeight", pivotHeaderHeight);
		}
	}

	public int getPivotGroupHeaderHeight() {
		return _auxinf != null ? _auxinf.pivotGroupHeaderHeight : 0;
	}

	public void setPivotGroupHeaderHeight(int pivotGroupHeaderHeight) {
		if (getPivotGroupHeaderHeight() != pivotGroupHeaderHeight) {
			initAuxInfo().pivotGroupHeaderHeight = pivotGroupHeaderHeight;
			smartUpdate("pivotGroupHeaderHeight", pivotGroupHeaderHeight);
		}
	}

	public boolean isGroupUseEntireRow() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.GROUPUSEENTIREROW);
	}

	public void setGroupUseEntireRow(boolean groupUseEntireRow) {
		if (isGroupUseEntireRow() != groupUseEntireRow) {
			initAuxInfo().setBoolean(AuxInfo.Attr.GROUPUSEENTIREROW, groupUseEntireRow);
			smartUpdate("groupUseEntireRow", groupUseEntireRow);
		}
	}

	public int getGroupDefaultExpanded() {
		return _auxinf != null ? _auxinf.groupDefaultExpanded : 0;
	}

	public void setGroupDefaultExpanded(int groupDefaultExpanded) {
		if (getGroupDefaultExpanded() != groupDefaultExpanded) {
			initAuxInfo().groupDefaultExpanded = groupDefaultExpanded;
			smartUpdate("groupDefaultExpanded", groupDefaultExpanded);
		}
	}

	public boolean isGroupSuppressAutoColumn() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.GROUPSUPPRESSAUTOCOLUMN);
	}

	public void setGroupSuppressAutoColumn(boolean groupSuppressAutoColumn) {
		if (isGroupSuppressAutoColumn() != groupSuppressAutoColumn) {
			initAuxInfo().setBoolean(AuxInfo.Attr.GROUPSUPPRESSAUTOCOLUMN, groupSuppressAutoColumn);
			smartUpdate("groupSuppressAutoColumn", groupSuppressAutoColumn);
		}
	}

	public boolean isGroupMultiAutoColumn() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.GROUPMULTIAUTOCOLUMN);
	}

	public void setGroupMultiAutoColumn(boolean groupMultiAutoColumn) {
		if (isGroupMultiAutoColumn() != groupMultiAutoColumn) {
			initAuxInfo().setBoolean(AuxInfo.Attr.GROUPMULTIAUTOCOLUMN, groupMultiAutoColumn);
			smartUpdate("groupMultiAutoColumn", groupMultiAutoColumn);
		}
	}

	public boolean isGroupSuppressRow() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.GROUPSUPPRESSROW);
	}

	public void setGroupSuppressRow(boolean groupSuppressRow) {
		if (isGroupSuppressRow() != groupSuppressRow) {
			initAuxInfo().setBoolean(AuxInfo.Attr.GROUPSUPPRESSROW, groupSuppressRow);
			smartUpdate("groupSuppressRow", groupSuppressRow);
		}
	}

	public boolean isGroupSelectsChildren() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.GROUPSELECTSCHILDREN);
	}

	public void setGroupSelectsChildren(boolean groupSelectsChildren) {
		if (isGroupSelectsChildren() != groupSelectsChildren) {
			initAuxInfo().setBoolean(AuxInfo.Attr.GROUPSELECTSCHILDREN, groupSelectsChildren);
			smartUpdate("groupSelectsChildren", groupSelectsChildren);
		}
	}

	public boolean isGroupIncludeFooter() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.GROUPINCLUDEFOOTER);
	}

	public void setGroupIncludeFooter(boolean groupIncludeFooter) {
		if (isGroupIncludeFooter() != groupIncludeFooter) {
			initAuxInfo().setBoolean(AuxInfo.Attr.GROUPINCLUDEFOOTER, groupIncludeFooter);
			smartUpdate("groupIncludeFooter", groupIncludeFooter);
		}
	}

	public boolean isGroupIncludeTotalFooter() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.GROUPINCLUDETOTALFOOTER);
	}

	public void setGroupIncludeTotalFooter(boolean groupIncludeTotalFooter) {
		if (isGroupIncludeTotalFooter() != groupIncludeTotalFooter) {
			initAuxInfo().setBoolean(AuxInfo.Attr.GROUPINCLUDETOTALFOOTER, groupIncludeTotalFooter);
			smartUpdate("groupIncludeTotalFooter", groupIncludeTotalFooter);
		}
	}

	public boolean isGroupSuppressBlankHeader() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.GROUPSUPPRESSBLANKHEADER);
	}

	public void setGroupSuppressBlankHeader(boolean groupSuppressBlankHeader) {
		if (isGroupSuppressBlankHeader() != groupSuppressBlankHeader) {
			initAuxInfo().setBoolean(AuxInfo.Attr.GROUPSUPPRESSBLANKHEADER, groupSuppressBlankHeader);
			smartUpdate("groupSuppressBlankHeader", groupSuppressBlankHeader);
		}
	}

	public boolean isGroupSelectsFiltered() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.GROUPSELECTSFILTERED);
	}

	public void setGroupSelectsFiltered(boolean groupSelectsFiltered) {
		if (isGroupSelectsFiltered() != groupSelectsFiltered) {
			initAuxInfo().setBoolean(AuxInfo.Attr.GROUPSELECTSFILTERED, groupSelectsFiltered);
			smartUpdate("groupSelectsFiltered", groupSelectsFiltered);
		}
	}

	public boolean isGroupRemoveSingleChildren() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.GROUPREMOVESINGLECHILDREN);
	}

	public void setGroupRemoveSingleChildren(boolean groupRemoveSingleChildren) {
		if (isGroupRemoveSingleChildren() != groupRemoveSingleChildren) {
			initAuxInfo().setBoolean(AuxInfo.Attr.GROUPREMOVESINGLECHILDREN, groupRemoveSingleChildren);
			smartUpdate("groupRemoveSingleChildren", groupRemoveSingleChildren);
		}
	}

	public boolean isGroupRemoveLowestSingleChildren() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.GROUPREMOVELOWESTSINGLECHILDREN);
	}

	public void setGroupRemoveLowestSingleChildren(boolean groupRemoveLowestSingleChildren) {
		if (isGroupRemoveLowestSingleChildren() != groupRemoveLowestSingleChildren) {
			initAuxInfo().setBoolean(AuxInfo.Attr.GROUPREMOVELOWESTSINGLECHILDREN, groupRemoveLowestSingleChildren);
			smartUpdate("groupRemoveLowestSingleChildren", groupRemoveLowestSingleChildren);
		}
	}

	public boolean isGroupHideOpenParents() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.GROUPHIDEOPENPARENTS);
	}

	public void setGroupHideOpenParents(boolean groupHideOpenParents) {
		if (isGroupHideOpenParents() != groupHideOpenParents) {
			initAuxInfo().setBoolean(AuxInfo.Attr.GROUPHIDEOPENPARENTS, groupHideOpenParents);
			smartUpdate("groupHideOpenParents", groupHideOpenParents);
		}
	}

	public String getRowGroupPanelShow() {
		return _auxinf != null ? _auxinf.rowGroupPanelShow : "never";
	}

	public void setRowGroupPanelShow(String rowGroupPanelShow) {
		if (!"never".equals(rowGroupPanelShow) && !"always".equals(rowGroupPanelShow)
				&& !"onlyWhenGrouping".equals(rowGroupPanelShow))
			throw new WrongValueException("expected never, always, or onlyWhenGrouping: " + rowGroupPanelShow);
		if (!Objects.equals(getRowGroupPanelShow(), rowGroupPanelShow)) {
			initAuxInfo().rowGroupPanelShow = rowGroupPanelShow;
			smartUpdate("rowGroupPanelShow", rowGroupPanelShow);
		}
	}

	public boolean isPivotMode() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.PIVOTMODE);
	}

	public void setPivotMode(boolean pivotMode) {
		if (isPivotMode() != pivotMode) {
			initAuxInfo().setBoolean(AuxInfo.Attr.PIVOTMODE, pivotMode);
			smartUpdate("pivotMode", pivotMode);
		}
	}

	public String getPivotPanelShow() {
		return _auxinf != null ? _auxinf.pivotPanelShow : "never";
	}

	public void setPivotPanelShow(String pivotPanelShow) {
		if (!"never".equals(pivotPanelShow) && !"always".equals(pivotPanelShow)
				&& !"onlyWhenPivoting".equals(pivotPanelShow))
			throw new WrongValueException("expected never, always, or onlyWhenPivoting: " + pivotPanelShow);
		if (!Objects.equals(getPivotPanelShow(), pivotPanelShow)) {
			initAuxInfo().pivotPanelShow = pivotPanelShow;
			smartUpdate("pivotPanelShow", pivotPanelShow);
		}
	}

	public boolean isRememberGroupStateWhenNewData() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.REMEMBERGROUPSTATEWHENNEWDATA);
	}

	public void setRememberGroupStateWhenNewData(boolean rememberGroupStateWhenNewData) {
		if (isRememberGroupStateWhenNewData() != rememberGroupStateWhenNewData) {
			initAuxInfo().setBoolean(AuxInfo.Attr.REMEMBERGROUPSTATEWHENNEWDATA, rememberGroupStateWhenNewData);
			smartUpdate("rememberGroupStateWhenNewData", rememberGroupStateWhenNewData);
		}
	}

	public boolean isSuppressAggFuncInHeader() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSAGGFUNCINHEADER);
	}

	public void setSuppressAggFuncInHeader(boolean suppressAggFuncInHeader) {
		if (isSuppressAggFuncInHeader() != suppressAggFuncInHeader) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSAGGFUNCINHEADER, suppressAggFuncInHeader);
			smartUpdate("suppressAggFuncInHeader", suppressAggFuncInHeader);
		}
	}

	public boolean isSuppressAggAtRootLevel() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSAGGATROOTLEVEL);
	}

	public void setSuppressAggAtRootLevel(boolean suppressAggAtRootLevel) {
		if (isSuppressAggAtRootLevel() != suppressAggAtRootLevel) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSAGGATROOTLEVEL, suppressAggAtRootLevel);
			smartUpdate("suppressAggAtRootLevel", suppressAggAtRootLevel);
		}
	}

	public boolean isAggregateOnlyChangedColumns() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.AGGREGATEONLYCHANGEDCOLUMNS);
	}

	public void setAggregateOnlyChangedColumns(boolean aggregateOnlyChangedColumns) {
		if (isAggregateOnlyChangedColumns() != aggregateOnlyChangedColumns) {
			initAuxInfo().setBoolean(AuxInfo.Attr.AGGREGATEONLYCHANGEDCOLUMNS, aggregateOnlyChangedColumns);
			smartUpdate("aggregateOnlyChangedColumns", aggregateOnlyChangedColumns);
		}
	}

	public boolean isFunctionsReadOnly() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.FUNCTIONSREADONLY);
	}

	public void setFunctionsReadOnly(boolean functionsReadOnly) {
		if (isFunctionsReadOnly() != functionsReadOnly) {
			initAuxInfo().setBoolean(AuxInfo.Attr.FUNCTIONSREADONLY, functionsReadOnly);
			smartUpdate("functionsReadOnly", functionsReadOnly);
		}
	}

	public boolean isSuppressMakeVisibleAfterUnGroup() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSMAKEVISIBLEAFTERUNGROUP);
	}

	public void setSuppressMakeVisibleAfterUnGroup(boolean suppressMakeVisibleAfterUnGroup) {
		if (isSuppressMakeVisibleAfterUnGroup() != suppressMakeVisibleAfterUnGroup) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSMAKEVISIBLEAFTERUNGROUP, suppressMakeVisibleAfterUnGroup);
			smartUpdate("suppressMakeVisibleAfterUnGroup", suppressMakeVisibleAfterUnGroup);
		}
	}

	public String getPivotColumnGroupTotals() {
		return _auxinf != null ? _auxinf.pivotColumnGroupTotals : null;
	}

	public void setPivotColumnGroupTotals(String pivotColumnGroupTotals) {
		if (pivotColumnGroupTotals != null && !"before".equals(pivotColumnGroupTotals)
				&& !"after".equals(pivotColumnGroupTotals))
			throw new WrongValueException("expected null, before, or after: " + pivotColumnGroupTotals);
		if (!Objects.equals(getPivotColumnGroupTotals(), pivotColumnGroupTotals)) {
			initAuxInfo().pivotColumnGroupTotals = pivotColumnGroupTotals;
			smartUpdate("pivotColumnGroupTotals", pivotColumnGroupTotals);
		}
	}

	public String getPivotRowTotals() {
		return _auxinf != null ? _auxinf.pivotRowTotals : null;
	}

	public void setPivotRowTotals(String pivotRowTotals) {
		if (pivotRowTotals != null && !"before".equals(pivotRowTotals)
				&& !"after".equals(pivotRowTotals))
			throw new WrongValueException("expected null, before, or after: " + pivotRowTotals);
		if (!Objects.equals(getPivotRowTotals(), pivotRowTotals)) {
			initAuxInfo().pivotRowTotals = pivotRowTotals;
			smartUpdate("pivotRowTotals", pivotRowTotals);
		}
	}

	public boolean isPivotSuppressAutoColumn() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.PIVOTSUPPRESSAUTOCOLUMN);
	}

	public void setPivotSuppressAutoColumn(boolean pivotSuppressAutoColumn) {
		if (isPivotSuppressAutoColumn() != pivotSuppressAutoColumn) {
			initAuxInfo().setBoolean(AuxInfo.Attr.PIVOTSUPPRESSAUTOCOLUMN, pivotSuppressAutoColumn);
			smartUpdate("pivotSuppressAutoColumn", pivotSuppressAutoColumn);
		}
	}

	public int getCacheBlockSize() {
		return _auxinf != null ? _auxinf.cacheBlockSize : 100;
	}

	public void setCacheBlockSize(int cacheBlockSize) {
		if (getCacheBlockSize() != cacheBlockSize) {
			initAuxInfo().cacheBlockSize = cacheBlockSize;
			smartUpdate("cacheBlockSize", cacheBlockSize);
		}
	}

	public int getMaxBlocksInCache() {
		return _auxinf != null ? _auxinf.maxBlocksInCache : 0;
	}

	public void setMaxBlocksInCache(int maxBlocksInCache) {
		if (getMaxBlocksInCache() != maxBlocksInCache) {
			initAuxInfo().maxBlocksInCache = maxBlocksInCache;
			smartUpdate("maxBlocksInCache", maxBlocksInCache);
		}
	}

	public int getCacheOverflowSize() {
		return _auxinf != null ? _auxinf.cacheOverflowSize : 1;
	}

	public void setCacheOverflowSize(int cacheOverflowSize) {
		if (getCacheOverflowSize() != cacheOverflowSize) {
			initAuxInfo().cacheOverflowSize = cacheOverflowSize;
			smartUpdate("cacheOverflowSize", cacheOverflowSize);
		}
	}

	public int getMaxConcurrentDatasourceRequests() {
		return _auxinf != null ? _auxinf.maxConcurrentDatasourceRequests : 1;
	}

	public void setMaxConcurrentDatasourceRequests(int maxConcurrentDatasourceRequests) {
		if (getMaxConcurrentDatasourceRequests() != maxConcurrentDatasourceRequests) {
			initAuxInfo().maxConcurrentDatasourceRequests = maxConcurrentDatasourceRequests;
			smartUpdate("maxConcurrentDatasourceRequests", maxConcurrentDatasourceRequests);
		}
	}

	public int getBlockLoadDebounceMillis() {
		return _auxinf != null ? _auxinf.blockLoadDebounceMillis : 0;
	}

	public void setBlockLoadDebounceMillis(int blockLoadDebounceMillis) {
		if (getBlockLoadDebounceMillis() != blockLoadDebounceMillis) {
			initAuxInfo().blockLoadDebounceMillis = blockLoadDebounceMillis;
			smartUpdate("blockLoadDebounceMillis", blockLoadDebounceMillis);
		}
	}

	public int getInfiniteInitialRowCount() {
		return _auxinf != null ? _auxinf.infiniteInitialRowCount : 0;
	}

	public void setInfiniteInitialRowCount(int infiniteInitialRowCount) {
		if (getInfiniteInitialRowCount() != infiniteInitialRowCount) {
			initAuxInfo().infiniteInitialRowCount = infiniteInitialRowCount;
			smartUpdate("infiniteInitialRowCount", infiniteInitialRowCount);
		}
	}

	public boolean isPurgeClosedRowNodes() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.PURGECLOSEDROWNODES);
	}

	public void setPurgeClosedRowNodes(boolean purgeClosedRowNodes) {
		if (isPurgeClosedRowNodes() != purgeClosedRowNodes) {
			initAuxInfo().setBoolean(AuxInfo.Attr.PURGECLOSEDROWNODES, purgeClosedRowNodes);
			smartUpdate("purgeClosedRowNodes", purgeClosedRowNodes);
		}
	}

	public boolean isServerSideSortingAlwaysResets() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SERVERSIDESORTINGALWAYSRESETS);
	}

	public void setServerSideSortingAlwaysResets(boolean serverSideSortingAlwaysResets) {
		if (isServerSideSortingAlwaysResets() != serverSideSortingAlwaysResets) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SERVERSIDESORTINGALWAYSRESETS, serverSideSortingAlwaysResets);
			smartUpdate("serverSideSortingAlwaysResets", serverSideSortingAlwaysResets);
		}
	}

	public int getViewportRowModelPageSize() {
		return _auxinf != null ? _auxinf.viewportRowModelPageSize : 0;
	}

	public void setViewportRowModelPageSize(int viewportRowModelPageSize) {
		if (getViewportRowModelPageSize() != viewportRowModelPageSize) {
			initAuxInfo().viewportRowModelPageSize = viewportRowModelPageSize;
			smartUpdate("viewportRowModelPageSize", viewportRowModelPageSize);
		}
	}

	public int getViewportRowModelBufferSize() {
		return _auxinf != null ? _auxinf.viewportRowModelBufferSize : 0;
	}

	public void setViewportRowModelBufferSize(int viewportRowModelBufferSize) {
		if (getViewportRowModelBufferSize() != viewportRowModelBufferSize) {
			initAuxInfo().viewportRowModelBufferSize = viewportRowModelBufferSize;
			smartUpdate("viewportRowModelBufferSize", viewportRowModelBufferSize);
		}
	}

	public boolean isAlwaysShowVerticalScroll() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ALWAYSSHOWVERTICALSCROLL);
	}

	public void setAlwaysShowVerticalScroll(boolean alwaysShowVerticalScroll) {
		if (isAlwaysShowVerticalScroll() != alwaysShowVerticalScroll) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ALWAYSSHOWVERTICALSCROLL, alwaysShowVerticalScroll);
			smartUpdate("alwaysShowVerticalScroll", alwaysShowVerticalScroll);
		}
	}

	public boolean isSuppressHorizontalScroll() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSHORIZONTALSCROLL);
	}

	public void setSuppressHorizontalScroll(boolean suppressHorizontalScroll) {
		if (isSuppressHorizontalScroll() != suppressHorizontalScroll) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSHORIZONTALSCROLL, suppressHorizontalScroll);
			smartUpdate("suppressHorizontalScroll", suppressHorizontalScroll);
		}
	}

	public boolean isSuppressColumnVirtualisation() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSCOLUMNVIRTUALISATION);
	}

	public void setSuppressColumnVirtualisation(boolean suppressColumnVirtualisation) {
		if (isSuppressColumnVirtualisation() != suppressColumnVirtualisation) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSCOLUMNVIRTUALISATION, suppressColumnVirtualisation);
			smartUpdate("suppressColumnVirtualisation", suppressColumnVirtualisation);
		}
	}

	public boolean isSuppressMaxRenderedRowRestriction() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSMAXRENDEREDROWRESTRICTION);
	}

	public void setSuppressMaxRenderedRowRestriction(boolean suppressMaxRenderedRowRestriction) {
		if (isSuppressMaxRenderedRowRestriction() != suppressMaxRenderedRowRestriction) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSMAXRENDEREDROWRESTRICTION, suppressMaxRenderedRowRestriction);
			smartUpdate("suppressMaxRenderedRowRestriction", suppressMaxRenderedRowRestriction);
		}
	}

	public boolean isSuppressScrollOnNewData() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSSCROLLONNEWDATA);
	}

	public void setSuppressScrollOnNewData(boolean suppressScrollOnNewData) {
		if (isSuppressScrollOnNewData() != suppressScrollOnNewData) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSSCROLLONNEWDATA, suppressScrollOnNewData);
			smartUpdate("suppressScrollOnNewData", suppressScrollOnNewData);
		}
	}

	public boolean isSuppressAnimationFrame() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSANIMATIONFRAME);
	}

	public void setSuppressAnimationFrame(boolean suppressAnimationFrame) {
		if (isSuppressAnimationFrame() != suppressAnimationFrame) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSANIMATIONFRAME, suppressAnimationFrame);
			smartUpdate("suppressAnimationFrame", suppressAnimationFrame);
		}
	}

	public boolean isPagination() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.PAGINATION);
	}

	public void setPagination(boolean pagination) {
		if (isPagination() != pagination) {
			initAuxInfo().setBoolean(AuxInfo.Attr.PAGINATION, pagination);
			smartUpdate("pagination", pagination);
		}
	}

	public int getPaginationPageSize() {
		return _auxinf != null ? _auxinf.paginationPageSize : 100;
	}

	public void setPaginationPageSize(int paginationPageSize) {
		if (getPaginationPageSize() != paginationPageSize) {
			initAuxInfo().paginationPageSize = paginationPageSize;
			smartUpdate("paginationPageSize", paginationPageSize);
		}
	}

	public boolean isPaginationAutoPageSize() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.PAGINATIONAUTOPAGESIZE);
	}

	public void setPaginationAutoPageSize(boolean paginationAutoPageSize) {
		if (isPaginationAutoPageSize() != paginationAutoPageSize) {
			initAuxInfo().setBoolean(AuxInfo.Attr.PAGINATIONAUTOPAGESIZE, paginationAutoPageSize);
			smartUpdate("paginationAutoPageSize", paginationAutoPageSize);
		}
	}

	public boolean isSuppressPaginationPanel() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSPAGINATIONPANEL);
	}

	public void setSuppressPaginationPanel(boolean suppressPaginationPanel) {
		if (isSuppressPaginationPanel() != suppressPaginationPanel) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSPAGINATIONPANEL, suppressPaginationPanel);
			smartUpdate("suppressPaginationPanel", suppressPaginationPanel);
		}
	}

	public boolean isPaginateChildRows() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.PAGINATECHILDROWS);
	}

	public void setPaginateChildRows(boolean paginateChildRows) {
		if (isPaginateChildRows() != paginateChildRows) {
			initAuxInfo().setBoolean(AuxInfo.Attr.PAGINATECHILDROWS, paginateChildRows);
			smartUpdate("paginateChildRows", paginateChildRows);
		}
	}

	public boolean isMasterDetail() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.MASTERDETAIL);
	}

	public void setMasterDetail(boolean masterDetail) {
		if (isMasterDetail() != masterDetail) {
			initAuxInfo().setBoolean(AuxInfo.Attr.MASTERDETAIL, masterDetail);
			smartUpdate("masterDetail", masterDetail);
		}
	}

	public boolean isKeepDetailRows() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.KEEPDETAILROWS);
	}

	public void setKeepDetailRows(boolean keepDetailRows) {
		if (isKeepDetailRows() != keepDetailRows) {
			initAuxInfo().setBoolean(AuxInfo.Attr.KEEPDETAILROWS, keepDetailRows);
			smartUpdate("keepDetailRows", keepDetailRows);
		}
	}

	public int getKeepDetailRowsCount() {
		return _auxinf != null ? _auxinf.keepDetailRowsCount : 10;
	}

	public void setKeepDetailRowsCount(int keepDetailRowsCount) {
		if (getKeepDetailRowsCount() != keepDetailRowsCount) {
			initAuxInfo().keepDetailRowsCount = keepDetailRowsCount;
			smartUpdate("keepDetailRowsCount", keepDetailRowsCount);
		}
	}

	public int getDetailRowHeight() {
		return _auxinf != null ? _auxinf.detailRowHeight : 0;
	}

	public void setDetailRowHeight(int detailRowHeight) {
		if (getDetailRowHeight() != detailRowHeight) {
			initAuxInfo().detailRowHeight = detailRowHeight;
			smartUpdate("detailRowHeight", detailRowHeight);
		}
	}

	public boolean isAutoHeight() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.AUTOHEIGHT);
	}

	public void setAutoHeight(boolean autoHeight) {
		if (isAutoHeight() != autoHeight) {
			initAuxInfo().setBoolean(AuxInfo.Attr.AUTOHEIGHT, autoHeight);
			smartUpdate("autoHeight", autoHeight);
		}
	}

	public int getRowHeight() {
		return _auxinf != null ? _auxinf.rowHeight : 25;
	}

	public void setRowHeight(int rowHeight) {
		if (getRowHeight() != rowHeight) {
			initAuxInfo().rowHeight = rowHeight;
			smartUpdate("rowHeight", rowHeight);
		}
	}

	public boolean isAnimateRows() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ANIMATEROWS);
	}

	public void setAnimateRows(boolean animateRows) {
		if (isAnimateRows() != animateRows) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ANIMATEROWS, animateRows);
			smartUpdate("animateRows", animateRows);
		}
	}

	public String getRowClass() {
		return _auxinf != null ? _auxinf.rowClass : null;
	}

	public void setRowClass(String rowClass) {
		if (!Objects.equals(getRowClass(), rowClass)) {
			initAuxInfo().rowClass = rowClass;
			smartUpdate("rowClass", rowClass);
		}
	}

	public int getScrollbarWidth() {
		return _auxinf != null ? _auxinf.scrollbarWidth : 0;
	}

	public void setScrollbarWidth(int scrollbarWidth) {
		if (getScrollbarWidth() != scrollbarWidth) {
			initAuxInfo().scrollbarWidth = scrollbarWidth;
			smartUpdate("scrollbarWidth", scrollbarWidth);
		}
	}

	public boolean isSuppressRowHoverHighlight() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSROWHOVERHIGHLIGHT);
	}

	public void setSuppressRowHoverHighlight(boolean suppressRowHoverHighlight) {
		if (isSuppressRowHoverHighlight() != suppressRowHoverHighlight) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSROWHOVERHIGHLIGHT, suppressRowHoverHighlight);
			smartUpdate("suppressRowHoverHighlight", suppressRowHoverHighlight);
		}
	}

	public boolean isSuppressCopyRowsToClipboard() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSCOPYROWSTOCLIPBOARD);
	}

	public void setSuppressCopyRowsToClipboard(boolean suppressCopyRowsToClipboard) {
		if (isSuppressCopyRowsToClipboard() != suppressCopyRowsToClipboard) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSCOPYROWSTOCLIPBOARD, suppressCopyRowsToClipboard);
			smartUpdate("suppressCopyRowsToClipboard", suppressCopyRowsToClipboard);
		}
	}

	public boolean isCopyHeadersToClipboard() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.COPYHEADERSTOCLIPBOARD);
	}

	public void setCopyHeadersToClipboard(boolean copyHeadersToClipboard) {
		if (isCopyHeadersToClipboard() != copyHeadersToClipboard) {
			initAuxInfo().setBoolean(AuxInfo.Attr.COPYHEADERSTOCLIPBOARD, copyHeadersToClipboard);
			smartUpdate("copyHeadersToClipboard", copyHeadersToClipboard);
		}
	}

	public String getClipboardDeliminator() {
		return _auxinf != null ? _auxinf.clipboardDeliminator : "\t";
	}

	public void setClipboardDeliminator(String clipboardDeliminator) {
		if (!Objects.equals(getClipboardDeliminator(), clipboardDeliminator)) {
			initAuxInfo().clipboardDeliminator = clipboardDeliminator;
			smartUpdate("clipboardDeliminator", clipboardDeliminator);
		}
	}

	public boolean isSuppressFocusAfterRefresh() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSFOCUSAFTERREFRESH);
	}

	public void setSuppressFocusAfterRefresh(boolean suppressFocusAfterRefresh) {
		if (isSuppressFocusAfterRefresh() != suppressFocusAfterRefresh) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSFOCUSAFTERREFRESH, suppressFocusAfterRefresh);
			smartUpdate("suppressFocusAfterRefresh", suppressFocusAfterRefresh);
		}
	}

	public boolean isSuppressLastEmptyLineOnPaste() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSLASTEMPTYLINEONPASTE);
	}

	public void setSuppressLastEmptyLineOnPaste(boolean suppressLastEmptyLineOnPaste) {
		if (isSuppressLastEmptyLineOnPaste() != suppressLastEmptyLineOnPaste) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSLASTEMPTYLINEONPASTE, suppressLastEmptyLineOnPaste);
			smartUpdate("suppressLastEmptyLineOnPaste", suppressLastEmptyLineOnPaste);
		}
	}

	public boolean isEnableCellTextSelection() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENABLECELLTEXTSELECTION);
	}

	public void setEnableCellTextSelection(boolean enableCellTextSelection) {
		if (isEnableCellTextSelection() != enableCellTextSelection) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ENABLECELLTEXTSELECTION, enableCellTextSelection);
			smartUpdate("enableCellTextSelection", enableCellTextSelection);
		}
	}

	public boolean isSuppressLoadingOverlay() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSLOADINGOVERLAY);
	}

	public void setSuppressLoadingOverlay(boolean suppressLoadingOverlay) {
		if (isSuppressLoadingOverlay() != suppressLoadingOverlay) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSLOADINGOVERLAY, suppressLoadingOverlay);
			smartUpdate("suppressLoadingOverlay", suppressLoadingOverlay);
		}
	}

	public boolean isSuppressNoRowsOverlay() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSNOROWSOVERLAY);
	}

	public void setSuppressNoRowsOverlay(boolean suppressNoRowsOverlay) {
		if (isSuppressNoRowsOverlay() != suppressNoRowsOverlay) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSNOROWSOVERLAY, suppressNoRowsOverlay);
			smartUpdate("suppressNoRowsOverlay", suppressNoRowsOverlay);
		}
	}

	public boolean isEnableCharts() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENABLECHARTS);
	}

	public void setEnableCharts(boolean enableCharts) {
		if (isEnableCharts() != enableCharts) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ENABLECHARTS, enableCharts);
			smartUpdate("enableCharts", enableCharts);
		}
	}

	public boolean isValueCache() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.VALUECACHE);
	}

	public void setValueCache(boolean valueCache) {
		if (isValueCache() != valueCache) {
			initAuxInfo().setBoolean(AuxInfo.Attr.VALUECACHE, valueCache);
			smartUpdate("valueCache", valueCache);
		}
	}

	public boolean isValueCacheNeverExpires() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.VALUECACHENEVEREXPIRES);
	}

	public void setValueCacheNeverExpires(boolean valueCacheNeverExpires) {
		if (isValueCacheNeverExpires() != valueCacheNeverExpires) {
			initAuxInfo().setBoolean(AuxInfo.Attr.VALUECACHENEVEREXPIRES, valueCacheNeverExpires);
			smartUpdate("valueCacheNeverExpires", valueCacheNeverExpires);
		}
	}

	public boolean isSuppressMiddleClickScrolls() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSMIDDLECLICKSCROLLS);
	}

	public void setSuppressMiddleClickScrolls(boolean suppressMiddleClickScrolls) {
		if (isSuppressMiddleClickScrolls() != suppressMiddleClickScrolls) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSMIDDLECLICKSCROLLS, suppressMiddleClickScrolls);
			smartUpdate("suppressMiddleClickScrolls", suppressMiddleClickScrolls);
		}
	}

	public boolean isSuppressPreventDefaultOnMouseWheel() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSPREVENTDEFAULTONMOUSEWHEEL);
	}

	public void setSuppressPreventDefaultOnMouseWheel(boolean suppressPreventDefaultOnMouseWheel) {
		if (isSuppressPreventDefaultOnMouseWheel() != suppressPreventDefaultOnMouseWheel) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSPREVENTDEFAULTONMOUSEWHEEL, suppressPreventDefaultOnMouseWheel);
			smartUpdate("suppressPreventDefaultOnMouseWheel", suppressPreventDefaultOnMouseWheel);
		}
	}

	public boolean isEnableBrowserTooltips() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENABLEBROWSERTOOLTIPS);
	}

	public void setEnableBrowserTooltips(boolean enableBrowserTooltips) {
		if (isEnableBrowserTooltips() != enableBrowserTooltips) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ENABLEBROWSERTOOLTIPS, enableBrowserTooltips);
			smartUpdate("enableBrowserTooltips", enableBrowserTooltips);
		}
	}

	public int getTooltipShowDelay() {
		return _auxinf != null ? _auxinf.tooltipShowDelay : 2000;
	}

	public void setTooltipShowDelay(int tooltipShowDelay) {
		if (getTooltipShowDelay() != tooltipShowDelay) {
			initAuxInfo().tooltipShowDelay = tooltipShowDelay;
			smartUpdate("tooltipShowDelay", tooltipShowDelay);
		}
	}

	public boolean isTooltipMouseTrack() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.TOOLTIPMOUSETRACK);
	}

	public void setTooltipMouseTrack(boolean tooltipMouseTrack) {
		if (isTooltipMouseTrack() != tooltipMouseTrack) {
			initAuxInfo().setBoolean(AuxInfo.Attr.TOOLTIPMOUSETRACK, tooltipMouseTrack);
			smartUpdate("tooltipMouseTrack", tooltipMouseTrack);
		}
	}

	public boolean isEnableCellExpressions() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENABLECELLEXPRESSIONS);
	}

	public void setEnableCellExpressions(boolean enableCellExpressions) {
		if (isEnableCellExpressions() != enableCellExpressions) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ENABLECELLEXPRESSIONS, enableCellExpressions);
			smartUpdate("enableCellExpressions", enableCellExpressions);
		}
	}

	public String getDomLayout() {
		return _auxinf != null ? _auxinf.domLayout : "normal";
	}

	public void setDomLayout(String domLayout) {
		if (!"normal".equals(domLayout) && !"autoHeight".equals(domLayout) && !"print".equals(domLayout))
			throw new WrongValueException("expected normal, autoHeight, or print: " + domLayout);
		if (!Objects.equals(getDomLayout(), domLayout)) {
			initAuxInfo().domLayout = domLayout;
			smartUpdate("domLayout", domLayout);
		}
	}

	public boolean isEnsureDomOrder() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENSUREDOMORDER);
	}

	public void setEnsureDomOrder(boolean ensureDomOrder) {
		if (isEnsureDomOrder() != ensureDomOrder) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ENSUREDOMORDER, ensureDomOrder);
			smartUpdate("ensureDomOrder", ensureDomOrder);
		}
	}

	public int getRowBuffer() {
		return _auxinf != null ? _auxinf.rowBuffer : 20;
	}

	public void setRowBuffer(int rowBuffer) {
		if (getRowBuffer() != rowBuffer) {
			initAuxInfo().rowBuffer = rowBuffer;
			smartUpdate("rowBuffer", rowBuffer);
		}
	}

	public boolean isSuppressParentsInRowNodes() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSPARENTSINROWNODES);
	}

	public void setSuppressParentsInRowNodes(boolean suppressParentsInRowNodes) {
		if (isSuppressParentsInRowNodes() != suppressParentsInRowNodes) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSPARENTSINROWNODES, suppressParentsInRowNodes);
			smartUpdate("suppressParentsInRowNodes", suppressParentsInRowNodes);
		}
	}

	public boolean isSuppressDragLeaveHidesColumns() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSDRAGLEAVEHIDESCOLUMNS);
	}

	public void setSuppressDragLeaveHidesColumns(boolean suppressDragLeaveHidesColumns) {
		if (isSuppressDragLeaveHidesColumns() != suppressDragLeaveHidesColumns) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSDRAGLEAVEHIDESCOLUMNS, suppressDragLeaveHidesColumns);
			smartUpdate("suppressDragLeaveHidesColumns", suppressDragLeaveHidesColumns);
		}
	}

	public int getLayoutInterval() {
		return _auxinf != null ? _auxinf.layoutInterval : 500;
	}

	public void setLayoutInterval(int layoutInterval) {
		if (getLayoutInterval() != layoutInterval) {
			initAuxInfo().layoutInterval = layoutInterval;
			smartUpdate("layoutInterval", layoutInterval);
		}
	}

	public boolean isEnableRtl() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENABLERTL);
	}

	public void setEnableRtl(boolean enableRtl) {
		if (isEnableRtl() != enableRtl) {
			initAuxInfo().setBoolean(AuxInfo.Attr.ENABLERTL, enableRtl);
			smartUpdate("enableRtl", enableRtl);
		}
	}

	public boolean isDebug() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.DEBUG);
	}

	public void setDebug(boolean debug) {
		if (isDebug() != debug) {
			initAuxInfo().setBoolean(AuxInfo.Attr.DEBUG, debug);
			smartUpdate("debug", debug);
		}
	}

	public boolean isSuppressContextMenu() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSCONTEXTMENU);
	}

	public void setSuppressContextMenu(boolean suppressContextMenu) {
		if (isSuppressContextMenu() != suppressContextMenu) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSCONTEXTMENU, suppressContextMenu);
			smartUpdate("suppressContextMenu", suppressContextMenu);
		}
	}

	public boolean isPreventDefaultOnContextMenu() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.PREVENTDEFAULTONCONTEXTMENU);
	}

	public void setPreventDefaultOnContextMenu(boolean preventDefaultOnContextMenu) {
		if (isPreventDefaultOnContextMenu() != preventDefaultOnContextMenu) {
			initAuxInfo().setBoolean(AuxInfo.Attr.PREVENTDEFAULTONCONTEXTMENU, preventDefaultOnContextMenu);
			smartUpdate("preventDefaultOnContextMenu", preventDefaultOnContextMenu);
		}
	}

	public boolean isSuppressTouch() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSTOUCH);
	}

	public void setSuppressTouch(boolean suppressTouch) {
		if (isSuppressTouch() != suppressTouch) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSTOUCH, suppressTouch);
			smartUpdate("suppressTouch", suppressTouch);
		}
	}

	public boolean isSuppressAsyncEvents() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSASYNCEVENTS);
	}

	public void setSuppressAsyncEvents(boolean suppressAsyncEvents) {
		if (isSuppressAsyncEvents() != suppressAsyncEvents) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSASYNCEVENTS, suppressAsyncEvents);
			smartUpdate("suppressAsyncEvents", suppressAsyncEvents);
		}
	}

	public boolean isSuppressCsvExport() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSCSVEXPORT);
	}

	public void setSuppressCsvExport(boolean suppressCsvExport) {
		if (isSuppressCsvExport() != suppressCsvExport) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSCSVEXPORT, suppressCsvExport);
			smartUpdate("suppressCsvExport", suppressCsvExport);
		}
	}

	public boolean isSuppressExcelExport() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSEXCELEXPORT);
	}

	public void setSuppressExcelExport(boolean suppressExcelExport) {
		if (isSuppressExcelExport() != suppressExcelExport) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSEXCELEXPORT, suppressExcelExport);
			smartUpdate("suppressExcelExport", suppressExcelExport);
		}
	}

	public int getAsyncTransactionWaitMillis() {
		return _auxinf != null ? _auxinf.asyncTransactionWaitMillis : 50;
	}

	public void setAsyncTransactionWaitMillis(int asyncTransactionWaitMillis) {
		if (getAsyncTransactionWaitMillis() != asyncTransactionWaitMillis) {
			initAuxInfo().asyncTransactionWaitMillis = asyncTransactionWaitMillis;
			smartUpdate("asyncTransactionWaitMillis", asyncTransactionWaitMillis);
		}
	}

	public boolean isSuppressPropertyNamesCheck() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSPROPERTYNAMESCHECK);
	}

	public void setSuppressPropertyNamesCheck(boolean suppressPropertyNamesCheck) {
		if (isSuppressPropertyNamesCheck() != suppressPropertyNamesCheck) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSPROPERTYNAMESCHECK, suppressPropertyNamesCheck);
			smartUpdate("suppressPropertyNamesCheck", suppressPropertyNamesCheck);
		}
	}

	public boolean isSuppressRowTransform() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSROWTRANSFORM);
	}

	public void setSuppressRowTransform(boolean suppressRowTransform) {
		if (isSuppressRowTransform() != suppressRowTransform) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSROWTRANSFORM, suppressRowTransform);
			smartUpdate("suppressRowTransform", suppressRowTransform);
		}
	}

	public boolean isSuppressBrowserResizeObserver() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSBROWSERRESIZEOBSERVER);
	}

	public void setSuppressBrowserResizeObserver(boolean suppressBrowserResizeObserver) {
		if (isSuppressBrowserResizeObserver() != suppressBrowserResizeObserver) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSBROWSERRESIZEOBSERVER, suppressBrowserResizeObserver);
			smartUpdate("suppressBrowserResizeObserver", suppressBrowserResizeObserver);
		}
	}

	public String getTheme() {
		return _theme;
	}

	public void setTheme(String theme) {
		if (!Objects.equals(_theme, theme)) {
			_theme = theme;
			smartUpdate("theme", theme);
		}
	}

	public boolean isUndoRedoCellEditing() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.UNDOREDOCELLEDITING);
	}

	public void setUndoRedoCellEditing(boolean undoRedoCellEditing) {
		if (isUndoRedoCellEditing() != undoRedoCellEditing) {
			initAuxInfo().setBoolean(AuxInfo.Attr.UNDOREDOCELLEDITING, undoRedoCellEditing);
			smartUpdate("undoRedoCellEditing", undoRedoCellEditing);
		}
	}

	public int getUndoRedoCellEditingLimit() {
		return _auxinf != null ? _auxinf.undoRedoCellEditingLimit : 10;
	}

	public void setUndoRedoCellEditingLimit(int undoRedoCellEditingLimit) {
		if (getUndoRedoCellEditingLimit() != undoRedoCellEditingLimit) {
			initAuxInfo().undoRedoCellEditingLimit = undoRedoCellEditingLimit;
			smartUpdate("undoRedoCellEditingLimit", undoRedoCellEditingLimit);
		}
	}
	//#endregion

	public ListModel<E> getModel() {
		return _model;
	}

	@SuppressWarnings("unchecked")
	private Selectable<E> getSelectableModel() {
		return (Selectable<E>) _model;
	}

	@SuppressWarnings("unchecked")
	private Sortable<E> getSortableModel() {
		return (Sortable<E>) _model;
	}

	public void setModel(ListModel<E> model) {
		if (_model != model) {
			if (model != null && !(model instanceof Selectable))
				throw new UiException(model.getClass() + " must implement " + Selectable.class);
			if (_model != null) {
				_model.removeListDataListener(_modelListDataListener);
			}

			_model = model;
			boolean isModelPresented = _model != null;
			smartUpdate("model", isModelPresented);
			if (isModelPresented) {
				setRowSelection(getSelectableModel().isMultiple() ? "multiple" : "single");
				model.addListDataListener(_modelListDataListener);
				smartUpdate("_selectedUuids", getSelectedUuids());
			}
		}
	}

	public void exportDataAsCsv() {
		response("exportDataAsCsv", new AuInvoke(this, "exportDataAsCsv"));
	}

	private void onListDataChange(ListDataEvent event) {
		final int eventType = event.getType();
		switch (eventType) {
			case ListDataEvent.INTERVAL_ADDED:
			case ListDataEvent.INTERVAL_REMOVED:
			case ListDataEvent.CONTENTS_CHANGED:
				smartUpdate("_refreshInfiniteCache", true);
				break;
			case ListDataEvent.SELECTION_CHANGED:
				if (!_ignoreDataSelectionEvent) {
					smartUpdate("_selectedUuids", getSelectedUuids());
				}
				break;
			case ListDataEvent.MULTIPLE_CHANGED:
				setRowSelection(getSelectableModel().isMultiple() ? "multiple" : "single");
				break;
			default:
		}
	}

	@Override
	protected void renderProperties(ContentRenderer renderer) throws IOException {
		super.renderProperties(renderer);
		if (!"ag-theme-alpine".equals(_theme))
			render(renderer, "theme", _theme);
		render(renderer, "model", _model != null);
		if (_model != null && !getSelectableModel().isSelectionEmpty()) {
			render(renderer, "_selectedUuids", getSelectedUuids());
		}
		if (_auxinf != null) {
			render(renderer, "suppressAutoSize", isSuppressAutoSize());
			if (_auxinf.autoSizePadding != 4)
				render(renderer, "autoSizePadding", _auxinf.autoSizePadding);
			render(renderer, "skipHeaderOnAutoSize", isSkipHeaderOnAutoSize());
			render(renderer, "suppressColumnMoveAnimation", isSuppressColumnMoveAnimation());
			render(renderer, "suppressMovableColumns", isSuppressMovableColumns());
			render(renderer, "suppressFieldDotNotation", isSuppressFieldDotNotation());
			render(renderer, "unSortIcon", isUnSortIcon());
			render(renderer, "suppressMultiSort", isSuppressMultiSort());
			render(renderer, "suppressMenuHide", isSuppressMenuHide());
			render(renderer, "suppressSetColumnStateEvents", isSuppressSetColumnStateEvents());
			render(renderer, "allowDragFromColumnsToolPanel", isAllowDragFromColumnsToolPanel());
			render(renderer, "immutableColumns", isImmutableColumns());
			render(renderer, "cacheQuickFilter", isCacheQuickFilter());
			render(renderer, "accentedSort", isAccentedSort());
			render(renderer, "suppressMaintainUnsortedOrder", isSuppressMaintainUnsortedOrder());
			render(renderer, "excludeChildrenWhenTreeDataFiltering", isExcludeChildrenWhenTreeDataFiltering());
			render(renderer, "rowSelection", getRowSelection());
			render(renderer, "rowMultiSelectWithClick", isRowMultiSelectWithClick());
			render(renderer, "rowDeselection", isRowDeselection());
			render(renderer, "suppressRowClickSelection", isSuppressRowClickSelection());
			render(renderer, "suppressCellSelection", isSuppressCellSelection());
			render(renderer, "enableRangeSelection", isEnableRangeSelection());
			render(renderer, "rowDragManaged", isRowDragManaged());
			render(renderer, "suppressRowDrag", isSuppressRowDrag());
			render(renderer, "suppressMoveWhenRowDragging", isSuppressMoveWhenRowDragging());
			render(renderer, "singleClickEdit", isSingleClickEdit());
			render(renderer, "suppressClickEdit", isSuppressClickEdit());
			render(renderer, "editType", getEditType());
			render(renderer, "enableCellChangeFlash", isEnableCellChangeFlash());
			if (_auxinf.cellFlashDelay != 500)
				render(renderer, "cellFlashDelay", _auxinf.cellFlashDelay);
			if (_auxinf.cellFadeDelay != 1000)
				render(renderer, "cellFadeDelay", _auxinf.cellFadeDelay);
			render(renderer, "allowShowChangeAfterFilter", isAllowShowChangeAfterFilter());
			render(renderer, "stopEditingWhenGridLosesFocus", isStopEditingWhenGridLosesFocus());
			render(renderer, "enterMovesDown", isEnterMovesDown());
			render(renderer, "enterMovesDownAfterEdit", isEnterMovesDownAfterEdit());
			if (_auxinf.headerHeight != 25)
				render(renderer, "headerHeight", _auxinf.headerHeight);
			if (_auxinf.groupHeaderHeight > 0)
				render(renderer, "groupHeaderHeight", _auxinf.groupHeaderHeight);
			if (_auxinf.floatingFiltersHeight != 20)
				render(renderer, "floatingFiltersHeight", _auxinf.floatingFiltersHeight);
			if (_auxinf.pivotHeaderHeight > 0)
				render(renderer, "pivotHeaderHeight", _auxinf.pivotHeaderHeight);
			if (_auxinf.pivotGroupHeaderHeight > 0)
				render(renderer, "pivotGroupHeaderHeight", _auxinf.pivotGroupHeaderHeight);
			render(renderer, "groupUseEntireRow", isGroupUseEntireRow());
			if (_auxinf.groupDefaultExpanded != 0)
				render(renderer, "groupDefaultExpanded", _auxinf.groupDefaultExpanded);
			render(renderer, "groupSuppressAutoColumn", isGroupSuppressAutoColumn());
			render(renderer, "groupMultiAutoColumn", isGroupMultiAutoColumn());
			render(renderer, "groupSuppressRow", isGroupSuppressRow());
			render(renderer, "groupSelectsChildren", isGroupSelectsChildren());
			render(renderer, "groupIncludeFooter", isGroupIncludeFooter());
			render(renderer, "groupIncludeTotalFooter", isGroupIncludeTotalFooter());
			render(renderer, "groupSuppressBlankHeader", isGroupSuppressBlankHeader());
			render(renderer, "groupSelectsFiltered", isGroupSelectsFiltered());
			render(renderer, "groupRemoveSingleChildren", isGroupRemoveSingleChildren());
			render(renderer, "groupRemoveLowestSingleChildren", isGroupRemoveLowestSingleChildren());
			render(renderer, "groupHideOpenParents", isGroupHideOpenParents());
			if (!"never".equals(_auxinf.rowGroupPanelShow))
				render(renderer, "rowGroupPanelShow", _auxinf.rowGroupPanelShow);
			render(renderer, "pivotMode", isPivotMode());
			if (!"never".equals(_auxinf.pivotPanelShow))
				render(renderer, "pivotPanelShow", _auxinf.pivotPanelShow);
			render(renderer, "rememberGroupStateWhenNewData", isRememberGroupStateWhenNewData());
			render(renderer, "suppressAggFuncInHeader", isSuppressAggFuncInHeader());
			render(renderer, "suppressAggAtRootLevel", isSuppressAggAtRootLevel());
			render(renderer, "aggregateOnlyChangedColumns", isAggregateOnlyChangedColumns());
			render(renderer, "functionsReadOnly", isFunctionsReadOnly());
			render(renderer, "suppressMakeVisibleAfterUnGroup", isSuppressMakeVisibleAfterUnGroup());
			render(renderer, "pivotColumnGroupTotals", getPivotColumnGroupTotals());
			render(renderer, "pivotRowTotals", getPivotRowTotals());
			render(renderer, "pivotSuppressAutoColumn", isPivotSuppressAutoColumn());
			if (_auxinf.cacheBlockSize != 100)
				render(renderer, "cacheBlockSize", _auxinf.cacheBlockSize);
			if (_auxinf.maxBlocksInCache > 0)
				render(renderer, "maxBlocksInCache", _auxinf.maxBlocksInCache);
			if (_auxinf.cacheOverflowSize != 1)
				render(renderer, "cacheOverflowSize", _auxinf.cacheOverflowSize);
			if (_auxinf.maxConcurrentDatasourceRequests != 1)
				render(renderer, "maxConcurrentDatasourceRequests", _auxinf.maxConcurrentDatasourceRequests);
			if (_auxinf.blockLoadDebounceMillis > 0)
				render(renderer, "blockLoadDebounceMillis", _auxinf.blockLoadDebounceMillis);
			if (_auxinf.infiniteInitialRowCount > 0)
				render(renderer, "infiniteInitialRowCount", _auxinf.infiniteInitialRowCount);
			render(renderer, "purgeClosedRowNodes", isPurgeClosedRowNodes());
			render(renderer, "serverSideSortingAlwaysResets", isServerSideSortingAlwaysResets());
			if (_auxinf.viewportRowModelPageSize > 0)
				render(renderer, "viewportRowModelPageSize", _auxinf.viewportRowModelPageSize);
			if (_auxinf.viewportRowModelBufferSize > 0)
				render(renderer, "viewportRowModelBufferSize", _auxinf.viewportRowModelBufferSize);
			render(renderer, "alwaysShowVerticalScroll", isAlwaysShowVerticalScroll());
			render(renderer, "suppressHorizontalScroll", isSuppressHorizontalScroll());
			render(renderer, "suppressColumnVirtualisation", isSuppressColumnVirtualisation());
			render(renderer, "suppressMaxRenderedRowRestriction", isSuppressMaxRenderedRowRestriction());
			render(renderer, "suppressScrollOnNewData", isSuppressScrollOnNewData());
			render(renderer, "suppressAnimationFrame", isSuppressAnimationFrame());
			render(renderer, "pagination", isPagination());
			if (_auxinf.paginationPageSize != 100)
				render(renderer, "paginationPageSize", _auxinf.paginationPageSize);
			render(renderer, "paginationAutoPageSize", isPaginationAutoPageSize());
			render(renderer, "suppressPaginationPanel", isSuppressPaginationPanel());
			render(renderer, "paginateChildRows", isPaginateChildRows());
			render(renderer, "masterDetail", isMasterDetail());
			render(renderer, "keepDetailRows", isKeepDetailRows());
			if (_auxinf.keepDetailRowsCount != 10)
				render(renderer, "keepDetailRowsCount", _auxinf.keepDetailRowsCount);
			if (_auxinf.detailRowHeight > 0)
				render(renderer, "detailRowHeight", _auxinf.detailRowHeight);
			render(renderer, "autoHeight", isAutoHeight());
			if (_auxinf.rowHeight != 25)
				render(renderer, "rowHeight", _auxinf.rowHeight);
			render(renderer, "animateRows", isAnimateRows());
			render(renderer, "rowClass", getRowClass());
			if (_auxinf.scrollbarWidth > 0)
				render(renderer, "scrollbarWidth", _auxinf.scrollbarWidth);
			render(renderer, "suppressRowHoverHighlight", isSuppressRowHoverHighlight());
			render(renderer, "suppressCopyRowsToClipboard", isSuppressCopyRowsToClipboard());
			render(renderer, "copyHeadersToClipboard", isCopyHeadersToClipboard());
			if (!"\t".equals(_auxinf.clipboardDeliminator))
				render(renderer, "clipboardDeliminator", _auxinf.clipboardDeliminator);
			render(renderer, "suppressFocusAfterRefresh", isSuppressFocusAfterRefresh());
			render(renderer, "suppressLastEmptyLineOnPaste", isSuppressLastEmptyLineOnPaste());
			render(renderer, "enableCellTextSelection", isEnableCellTextSelection());
			render(renderer, "suppressLoadingOverlay", isSuppressLoadingOverlay());
			render(renderer, "suppressNoRowsOverlay", isSuppressNoRowsOverlay());
			render(renderer, "enableCharts", isEnableCharts());
			render(renderer, "valueCache", isValueCache());
			render(renderer, "valueCacheNeverExpires", isValueCacheNeverExpires());
			render(renderer, "suppressMiddleClickScrolls", isSuppressMiddleClickScrolls());
			render(renderer, "suppressPreventDefaultOnMouseWheel", isSuppressPreventDefaultOnMouseWheel());
			render(renderer, "enableBrowserTooltips", isEnableBrowserTooltips());
			if (_auxinf.tooltipShowDelay != 2000)
				render(renderer, "tooltipShowDelay", _auxinf.tooltipShowDelay);
			render(renderer, "tooltipMouseTrack", isTooltipMouseTrack());
			render(renderer, "enableCellExpressions", isEnableCellExpressions());
			if (!"normal".equals(_auxinf.domLayout))
				render(renderer, "domLayout", _auxinf.domLayout);
			render(renderer, "ensureDomOrder", isEnsureDomOrder());
			if (_auxinf.rowBuffer != 20)
				render(renderer, "rowBuffer", _auxinf.rowBuffer);
			render(renderer, "suppressParentsInRowNodes", isSuppressParentsInRowNodes());
			render(renderer, "suppressDragLeaveHidesColumns", isSuppressDragLeaveHidesColumns());
			if (_auxinf.layoutInterval != 500)
				render(renderer, "layoutInterval", _auxinf.layoutInterval);
			render(renderer, "enableRtl", isEnableRtl());
			render(renderer, "debug", isDebug());
			render(renderer, "suppressContextMenu", isSuppressContextMenu());
			render(renderer, "preventDefaultOnContextMenu", isPreventDefaultOnContextMenu());
			render(renderer, "suppressTouch", isSuppressTouch());
			render(renderer, "suppressAsyncEvents", isSuppressAsyncEvents());
			render(renderer, "suppressCsvExport", isSuppressCsvExport());
			render(renderer, "suppressExcelExport", isSuppressExcelExport());
			if (_auxinf.asyncTransactionWaitMillis != 50)
				render(renderer, "asyncTransactionWaitMillis", _auxinf.asyncTransactionWaitMillis);
			render(renderer, "suppressPropertyNamesCheck", isSuppressPropertyNamesCheck());
			render(renderer, "suppressRowTransform", isSuppressRowTransform());
			render(renderer, "serverSideSortingAlwaysResets", isServerSideSortingAlwaysResets());
			render(renderer, "suppressBrowserResizeObserver", isSuppressBrowserResizeObserver());
			render(renderer, "undoRedoCellEditing", isUndoRedoCellEditing());
			if (_auxinf.undoRedoCellEditingLimit != 10)
				render(renderer, "undoRedoCellEditingLimit", _auxinf.undoRedoCellEditingLimit);
		}
	}

	private List<Integer> getSelectedUuids() {
		return getSelectableModel().getSelection()
				.stream()
				.map(Objects::hashCode)
				.collect(toList());
	}

	private JSONArray generateRowData(int start, int end) {
		if (_model != null) {
			JSONArray result = new JSONArray();
			for (int i = start; i < end; i++) {
				try {
					E element = _model.getElementAt(i);
					JSONObject object = new JSONObject();
					// TODO: replace BeanUtils (Object to Map(JSONObject))
					object.putAll(BeanUtils.describe(element));
					object.put("_zk_uuid", Objects.hashCode(element));
					result.add(object);
				} catch (Exception e) {
					throw UiException.Aide.wrap(e);
				}
			}
			return result;
		}
		return null;
	}

	@Override
	public void service(AuRequest request, boolean everError) {
		String command = request.getCommand();
		Map<String, Object> data = request.getData();
		AgGridEvent<E> agGridEvent = null;
		if (data.containsKey("agGrid")) {
			data.remove("agGrid");
			Integer rowIndex = (Integer) data.get("rowIndex");
			E node = null;
			if (_model != null && rowIndex != null)
				node = _model.getElementAt(rowIndex);
			agGridEvent = AgGridEvent.getAgGridEvent(request, node);
		}
		switch (command) {
			case "onPaging":
				if (_model != null) {
					handleModelFilter((JSONObject) data.get("filterModel"));
					handleModelSort((JSONArray) data.get("sortModel"));
					int start = (int) data.get("startRow");
					int end = (int) data.get("endRow");
					int modelSize = _model.getSize();
					if (modelSize < end) end = modelSize;
					response("pagingBlock",
						new AuInvoke(this, "_pagingBlock", generateRowData(start, end), modelSize)
					);
				}
				break;
			case "onSelectionChanged":
				Set<E> selectedObjects = null;
				Set<E> prevSeldObjects = null;
				if (_model != null) {
					Selectable<E> smodel = getSelectableModel();
					prevSeldObjects = new LinkedHashSet<>(smodel.getSelection());
					List<Integer> selectedIds = Generics.cast(data.get("selected"));
					List<Integer> unselectedIds = Generics.cast(data.get("unselected"));
					selectedObjects = selectedIds.stream()
							.map(_model::getElementAt)
							.collect(toSet());
					final boolean oldIDSE = _ignoreDataSelectionEvent;
					try {
						_ignoreDataSelectionEvent = true;
						if (smodel.isMultiple()) {
							selectedObjects.forEach(smodel::addToSelection);
							unselectedIds.stream()
									.map(_model::getElementAt)
									.forEach(smodel::removeFromSelection);
						} else {
							smodel.setSelection(selectedObjects);
						}
					} finally {
						_ignoreDataSelectionEvent = oldIDSE;
					}
				}
				Events.postEvent(new SelectEvent<>(command,
						this, null, null, null,
						selectedObjects, prevSeldObjects, collectUnselectedObjects(prevSeldObjects, selectedObjects),
						null, null, AuRequests.parseKeys(data)));
				break;
			case "onColumnPinned":
				if (agGridEvent != null) {
					try {
						String pinned = (String) agGridEvent.get("pinned");
						disableClientUpdate(true);
						agGridEvent.getColumns().forEach(col -> col.setPinned(pinned));
					} finally {
						disableClientUpdate(false);
					}
					Events.postEvent(agGridEvent);
				}
				break;
			case "onColumnVisible":
				if (agGridEvent != null) {
					try {
						boolean visible = (Boolean) agGridEvent.get("visible");
						disableClientUpdate(true);
						agGridEvent.getColumns().forEach(col -> col.setHide(!visible));
					} finally {
						disableClientUpdate(false);
					}
					Events.postEvent(agGridEvent);
				}
				break;
			default:
				if (agGridEvent != null)
					Events.postEvent(agGridEvent);
				else
					super.service(request, everError);
		}
	}

	@SuppressWarnings("unchecked")
	private void handleModelFilter(JSONObject filterModel) {
		if (!filterModel.isEmpty()) {
			if (!(_model instanceof Filterable))
				throw new UiException("Filterable must be implemented in " + _model.getClass());
			Filterable<E> model = ((Filterable<E>) _model);
			Set<Filter<E>> newFilters = new HashSet<>();
			for (Map.Entry<Object, Object> each : filterModel.entrySet()) {
				newFilters.add((Filter<E>) ColumnFilters.build(
					Objects.toString(each.getKey()), (JSONObject) each.getValue()
				));
			}
			if (!newFilters.equals(model.getFilters())) {
				model.removeAllFilters();
				model.applyFilters(newFilters);
			}
		} else {
			if (_model instanceof Filterable)
				((Filterable<?>) _model).removeAllFilters();
		}
	}

	private void handleModelSort(JSONArray sortModel) {
		if (!sortModel.isEmpty()) {
			if (!(_model instanceof Sortable))
				throw new UiException("Sortable must be implemented in " + _model.getClass());
			Sortable<E> model = getSortableModel();
			for (Object each : sortModel) {
				JSONObject sort = (JSONObject) each;
				boolean isAscending = "asc".equals(sort.get("sort"));
				Comparator<E> comp = getColumnSortComparator(this, String.valueOf(sort.get("colId")), isAscending);
				if (comp != null)
					model.sort(comp, isAscending);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Comparator<E> getColumnSortComparator(Component parent, String field, boolean ascending) {
		List<Component> children = parent.getChildren();
		for (Component child : children) {
			Aggridcolumn<E> c = (Aggridcolumn<E>) child;
			if (c.isColumnGroup()) {
				Comparator<E> found = getColumnSortComparator(c, field, ascending);
				if (found != null)
					return found;
			} else if (field.equals(c.getField())) {
				return ascending ? c.getSortAscending() : c.getSortDescending();
			}
		}
		return null;
	}

	private Set<E> collectUnselectedObjects(Set<E> previousSelection, Set<E> currentSelection) {
		Set<E> prevSeldItems = previousSelection != null ? new LinkedHashSet<>(previousSelection)
				: new LinkedHashSet<>();
		if (currentSelection != null && prevSeldItems.size() > 0)
			prevSeldItems.removeAll(currentSelection);
		return prevSeldItems;
	}

	@Override
	public void beforeChildAdded(Component child, Component insertBefore) {
		if (!(child instanceof Aggridcolumn))
			throw new UiException("Unsupported child: " + child);
		if (child instanceof AggridDefaultColumn && _defaultColDef != null && _defaultColDef != child)
			throw new UiException("Only one default column definition is allowed: " + this);
		super.beforeChildAdded(child, insertBefore);
	}

	@Override
	public boolean insertBefore(Component newChild, Component refChild) {
		if (newChild instanceof AggridDefaultColumn) {
			if (super.insertBefore(newChild, refChild)) {
				_defaultColDef = (AggridDefaultColumn) newChild;
				return true;
			}
		} else {
			return super.insertBefore(newChild, refChild);
		}
		return false;
	}

	@Override
	public void onChildRemoved(Component child) {
		super.onChildRemoved(child);
		if (_defaultColDef == child) _defaultColDef = null;
	}

	@Override
	public void onPageAttached(Page newpage, Page oldpage) {
		super.onPageAttached(newpage, oldpage);
		if (_model != null) {
			_model.removeListDataListener(_modelListDataListener);
			_model.addListDataListener(_modelListDataListener);
		}
	}

	@Override
	public void onPageDetached(Page page) {
		super.onPageDetached(page);
		if (_model != null) {
			_model.removeListDataListener(_modelListDataListener);
		}
	}

	@Override
	public String getZclass() {
		return _zclass != null ? _zclass : "z-aggrid";
	}

	private AuxInfo initAuxInfo() {
		if (_auxinf == null)
			_auxinf = new AuxInfo();
		return _auxinf;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object clone() {
		final Aggrid<E> clone = (Aggrid<E>) super.clone();
		if (_auxinf != null)
			clone._auxinf = (AuxInfo) _auxinf.clone();
		return clone;
	}

	/**
	 * Merge multiple members into a single object (and create on demand)
	 * to minimize the footprint.
	 */
	private static class AuxInfo implements Serializable, Cloneable {
		public enum Attr {
			SUPPRESSAUTOSIZE,
			SKIPHEADERONAUTOSIZE,
			SUPPRESSCOLUMNMOVEANIMATION,
			SUPPRESSMOVABLECOLUMNS,
			SUPPRESSFIELDDOTNOTATION,
			UNSORTICON,
			SUPPRESSMULTISORT,
			SUPPRESSMENUHIDE,
			SUPPRESSSETCOLUMNSTATEEVENTS,
			ALLOWDRAGFROMCOLUMNSTOOLPANEL,
			IMMUTABLECOLUMNS,
			CACHEQUICKFILTER,
			ACCENTEDSORT,
			SUPPRESSMAINTAINUNSORTEDORDER,
			EXCLUDECHILDRENWHENTREEDATAFILTERING,
			ROWMULTISELECTWITHCLICK,
			ROWDESELECTION,
			SUPPRESSROWCLICKSELECTION,
			SUPPRESSCELLSELECTION,
			ENABLERANGESELECTION,
			ROWDRAGMANAGED,
			SUPPRESSROWDRAG,
			SUPPRESSMOVEWHENROWDRAGGING,
			SINGLECLICKEDIT,
			SUPPRESSCLICKEDIT,
			ENABLECELLCHANGEFLASH,
			ALLOWSHOWCHANGEAFTERFILTER,
			STOPEDITINGWHENGRIDLOSESFOCUS,
			ENTERMOVESDOWN,
			ENTERMOVESDOWNAFTEREDIT,
			GROUPUSEENTIREROW,
			GROUPSUPPRESSAUTOCOLUMN,
			GROUPMULTIAUTOCOLUMN,
			GROUPSUPPRESSROW,
			GROUPSELECTSCHILDREN,
			GROUPINCLUDEFOOTER,
			GROUPINCLUDETOTALFOOTER,
			GROUPSUPPRESSBLANKHEADER,
			GROUPSELECTSFILTERED,
			GROUPREMOVESINGLECHILDREN,
			GROUPREMOVELOWESTSINGLECHILDREN,
			GROUPHIDEOPENPARENTS,
			PIVOTMODE,
			REMEMBERGROUPSTATEWHENNEWDATA,
			SUPPRESSAGGFUNCINHEADER,
			SUPPRESSAGGATROOTLEVEL,
			AGGREGATEONLYCHANGEDCOLUMNS,
			FUNCTIONSREADONLY,
			SUPPRESSMAKEVISIBLEAFTERUNGROUP,
			PIVOTSUPPRESSAUTOCOLUMN,
			PURGECLOSEDROWNODES,
			SERVERSIDESORTINGALWAYSRESETS,
			ALWAYSSHOWVERTICALSCROLL,
			SUPPRESSHORIZONTALSCROLL,
			SUPPRESSCOLUMNVIRTUALISATION,
			SUPPRESSMAXRENDEREDROWRESTRICTION,
			SUPPRESSSCROLLONNEWDATA,
			SUPPRESSANIMATIONFRAME,
			PAGINATION,
			PAGINATIONAUTOPAGESIZE,
			SUPPRESSPAGINATIONPANEL,
			PAGINATECHILDROWS,
			MASTERDETAIL,
			KEEPDETAILROWS,
			AUTOHEIGHT,
			ANIMATEROWS,
			SUPPRESSROWHOVERHIGHLIGHT,
			SUPPRESSCOPYROWSTOCLIPBOARD,
			COPYHEADERSTOCLIPBOARD,
			SUPPRESSFOCUSAFTERREFRESH,
			SUPPRESSLASTEMPTYLINEONPASTE,
			ENABLECELLTEXTSELECTION,
			SUPPRESSLOADINGOVERLAY,
			SUPPRESSNOROWSOVERLAY,
			ENABLECHARTS,
			VALUECACHE,
			VALUECACHENEVEREXPIRES,
			SUPPRESSMIDDLECLICKSCROLLS,
			SUPPRESSPREVENTDEFAULTONMOUSEWHEEL,
			ENABLEBROWSERTOOLTIPS,
			TOOLTIPMOUSETRACK,
			ENABLECELLEXPRESSIONS,
			ENSUREDOMORDER,
			SUPPRESSPARENTSINROWNODES,
			SUPPRESSDRAGLEAVEHIDESCOLUMNS,
			ENABLERTL,
			DEBUG,
			SUPPRESSCONTEXTMENU,
			PREVENTDEFAULTONCONTEXTMENU,
			SUPPRESSTOUCH,
			SUPPRESSASYNCEVENTS,
			SUPPRESSCSVEXPORT,
			SUPPRESSEXCELEXPORT,
			SUPPRESSPROPERTYNAMESCHECK,
			SUPPRESSROWTRANSFORM,
			SUPPRESSBROWSERRESIZEOBSERVER,
			UNDOREDOCELLEDITING
		}

		// Columns
		// TODO colResizeDefault
		private final BitSet _bitset = new BitSet();
		private int autoSizePadding = 4;
		// Sort and Filter
		// TODO quickFilterText
		// TODO sortingOrder
		// TODO multiSortKey
		// Selection
		private String rowSelection;
		private String editType;
		private int cellFlashDelay = 500;
		private int cellFadeDelay = 1000;
		// Headers
		private int headerHeight = 25;
		private int groupHeaderHeight;
		private int floatingFiltersHeight = 20;
		private int pivotHeaderHeight;
		private int pivotGroupHeaderHeight;
		// Row Grouping
		private int groupDefaultExpanded;
		// TODO autoGroupColumnDef
		private String rowGroupPanelShow = "never";
		// Row Pivoting
		private String pivotPanelShow = "never";
		// TODO aggFuncs
		private String pivotColumnGroupTotals;
		private String pivotRowTotals;
		// Data and Row Models
		// TODO pinnedTopRowData	Data to be displayed as Pinned Top Rows in the grid.
		// TODO pinnedBottomRowData	Data to be displayed as Pinned Bottom Rows in the grid.
		// Row Model: Server-Side
		private int cacheBlockSize = 100;
		private int maxBlocksInCache;
		private int cacheOverflowSize = 1;
		private int maxConcurrentDatasourceRequests = 1;
		private int blockLoadDebounceMillis;
		private int infiniteInitialRowCount;
		// Row Model: Viewport
		private int viewportRowModelPageSize;
		private int viewportRowModelBufferSize;
		// TODO viewportDatasource
		// Pagination
		private int paginationPageSize = 100;
		// Full Width Renderers
		// TODO
		// Master Detail
		// TODO detailCellRendererParams
		private int keepDetailRowsCount = 10;
		private int detailRowHeight;
		// Rendering and Styling
		// TODO icons
		private int rowHeight = 25;
		// TODO rowStyle
		private String rowClass;
		// TODO rowClassRules
		// TODO excelStyles
		private int scrollbarWidth;
		// Clipboard
		private String clipboardDeliminator = "\t";
		// Localisation
		// TODO
		// Components
		// TODO
		// Miscellaneous
		// TODO popupParent
		// TODO defaultExportParams
		private int tooltipShowDelay = 2000;
		private String domLayout = "normal";
		private int rowBuffer = 20;
		// TODO alignedGrids
		private int layoutInterval = 500;
		// TODO context
		// TODO allowContextMenuWithControlKey, statusBar
		private int asyncTransactionWaitMillis = 50;
		private int undoRedoCellEditingLimit = 10;

		public boolean getBoolean(Attr attr) {
			return this._bitset.get(attr.ordinal());
		}

		public void setBoolean(Attr attr, boolean value) {
			this._bitset.set(attr.ordinal(), value);
		}

		@Override
		public Object clone() {
			try {
				return super.clone();
			} catch (CloneNotSupportedException e) {
				throw new InternalError();
			}
		}
	}
}
