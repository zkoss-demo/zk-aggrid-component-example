/* Aggridcolumn.java

	Purpose:

	Description:

	History:
		Fri Jun 19 16:16:47 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid;

import java.io.IOException;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Comparator;

import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.FieldComparator;

/**
 * @author rudyhuang
 */
public class Aggridcolumn extends AbstractComponent {
	private transient Comparator _sortAsc, _sortDsc;

	// Columns and Column Groups
	private String _headerName;
	private boolean _columnGroupShow;
	private String _headerClass;
	private String _toolPanelClass;
	private boolean _suppressColumnsToolPanel;
	private boolean _suppressFiltersToolPanel;
	// Columns Only
	private String _field;
	private String _colId;
	private String _type;
	private int _width = -1;
	private int _minWidth = -1;
	private int _maxWidth = -1;
	private int _flex = -1;
	private String _filter;
	// TODO filterParams
	private boolean _floatingFilter;
	private String _floatingFilterComponent;
	// TODO floatingFilterComponentParams
	private boolean _hide;
	private String _pinned;
	private boolean _lockPosition;
	private boolean _lockVisible;
	private boolean _lockPinned;
	private boolean _sortable;
	private String _sort;
	private int _sortedAt;
	// TODO Array private String _sortingOrder;
	private boolean _resizable;
	private String _headerTooltip;
	private String _tooltipField;
	private String _tooltip;
	private boolean _checkboxSelection;
	private boolean _headerCheckboxSelection;
	private boolean _headerCheckboxSelectionFilteredOnly;
	private boolean _rowDrag;
	private String _rowDragText;
	private boolean _dndSource;
	// TODO dndSourceOnRowDrag
	private String _cellStyle;
	private String _cellClass;
	// TODO cellClassRules
	private boolean _editable;
	// TODO cellRenderer, cellRendererParams
	// TODO pinnedRowCellRenderer, pinnedRowCellRendererParams
	// TODO cellRendererSelector
	// TODO cellEditor, cellEditorParams
	// TODO headerComponent, headerComponentParams
	/*
	valueGetter(params)	Function or expression. Gets the value from your data for display.
	headerValueGetter(params)	Function or expression. Gets the value for display in the header.
	filterValueGetter(params)	Function or expression. Gets the value for filtering purposes.
	valueFormatter(params)	Function or expression. Formats the value for display.
	pinnedRowValueFormatter(params)	Function or expression. Formatter to use for a pinned row. If missing, the normal valueFormatter will be used.
	valueSetter(params)	Function or expression. Sets the value into your data for saving. Return true if the data changed.
	valueParser(params)	Function or expression. Parses the value for saving.
	keyCreator(params)	Function to return a string key for a value. This string is used for grouping, set filtering, and searching within cell editor dropdowns. When filtering and searching the string is exposed to the user, so make sure to return a human-readable value.
	getQuickFilterText
	* */
	private String _aggFunc;
	private String _allowedAggFuncs;
	private int _rowGroupIndex = -1;
	private int _pivotIndex = -1;
	// TODO comparator(valueA, valueB, nodeA, nodeB, isInverted)
	// TODO pivotComparator(valueA, valueB)
	private boolean _unSortIcon;
	private boolean _enableRowGroup;
	private boolean _enablePivot;
	private boolean _pivotTotals;
	private boolean _enableValue;
	private boolean _enableCellChangeFlash;
	// TODO menuTabs
	private boolean _suppressMenu;
	private boolean _suppressSizeToFit;
	private boolean _suppressMovable;
	private boolean _suppressNavigable;
	private boolean _suppressCellFlash;
	// TODO suppressKeyboardEvent(params)
	private boolean _autoHeight;
	private boolean _singleClickEdit;
	// TODO chartDataType
	// Column Groups Only
	private String _groupId;
	private boolean _marryChildren;
	private boolean _openByDefault;
	// TODO headerGroupComponent, headerGroupComponentParams

	/**
	 * Returns the ascending sorter, or null if not available.
	 */
	public Comparator getSortAscending() {
		return _sortAsc;
	}

	/**
	 * Sets the ascending sorter, or null for no sorter for
	 * the ascending order.
	 *
	 * @param sorter the comparator used to sort the ascending order.
	 * If you are using the group feature, you can pass an instance of
	 * {@link GroupComparator} to have a better control.
	 * If an instance of {@link GroupComparator} is passed,
	 * {@link GroupComparator#compareGroup} is used to group elements,
	 * and {@link GroupComparator#compare} is used to sort elements
	 * with a group.
	 * Otherwise, {@link Comparator#compare} is used to group elements
	 * and sort elements within a group.
	 */
	public void setSortAscending(Comparator sorter) {
		if (!Objects.equals(_sortAsc, sorter)) {
			_sortAsc = sorter;
		}
	}

	/**
	 * Returns the descending sorter, or null if not available.
	 */
	public Comparator getSortDescending() {
		return _sortDsc;
	}

	/**
	 * Sets the descending sorter, or null for no sorter for the
	 * descending order.
	 *
	 * @param sorter the comparator used to sort the ascending order.
	 * If you are using the group feature, you can pass an instance of
	 * {@link GroupComparator} to have a better control.
	 * If an instance of {@link GroupComparator} is passed,
	 * {@link GroupComparator#compareGroup} is used to group elements,
	 * and {@link GroupComparator#compare} is used to sort elements
	 * with a group.
	 * Otherwise, {@link Comparator#compare} is used to group elements
	 * and sort elements within a group.
	 */
	public void setSortDescending(Comparator sorter) {
		if (!Objects.equals(_sortDsc, sorter)) {
			_sortDsc = sorter;
		}
	}

	public String getHeaderName() {
		return _headerName;
	}

	public void setHeaderName(String headerName) {
		if (!Objects.equals(_headerName, headerName)) {
			_headerName = headerName;
			smartUpdate("headerName", headerName);
		}
	}

	public boolean isColumnGroupShow() {
		return _columnGroupShow;
	}

	public void setColumnGroupShow(boolean columnGroupShow) {
		if (_columnGroupShow != columnGroupShow) {
			_columnGroupShow = columnGroupShow;
			smartUpdate("columnGroupShow", columnGroupShow);
		}
	}

	public String getHeaderClass() {
		return _headerClass;
	}

	public void setHeaderClass(String headerClass) {
		if (!Objects.equals(_headerClass, headerClass)) {
			_headerClass = headerClass;
			smartUpdate("headerClass", headerClass);
		}
	}

	public String getToolPanelClass() {
		return _toolPanelClass;
	}

	public void setToolPanelClass(String toolPanelClass) {
		if (!Objects.equals(_toolPanelClass, toolPanelClass)) {
			_toolPanelClass = toolPanelClass;
			smartUpdate("toolPanelClass", toolPanelClass);
		}
	}

	public boolean isSuppressColumnsToolPanel() {
		return _suppressColumnsToolPanel;
	}

	public void setSuppressColumnsToolPanel(boolean suppressColumnsToolPanel) {
		if (_suppressColumnsToolPanel != suppressColumnsToolPanel) {
			_suppressColumnsToolPanel = suppressColumnsToolPanel;
			smartUpdate("suppressColumnsToolPanel", suppressColumnsToolPanel);
		}
	}

	public boolean isSuppressFiltersToolPanel() {
		return _suppressFiltersToolPanel;
	}

	public void setSuppressFiltersToolPanel(boolean suppressFiltersToolPanel) {
		if (_suppressFiltersToolPanel != suppressFiltersToolPanel) {
			_suppressFiltersToolPanel = suppressFiltersToolPanel;
			smartUpdate("suppressFiltersToolPanel", suppressFiltersToolPanel);
		}
	}

	public String getField() {
		return _field;
	}

	public void setField(String field) {
		if (!Objects.equals(_field, field)) {
			_field = field;
			smartUpdate("field", field);
		}
	}

	public String getColId() {
		return _colId;
	}

	public void setColId(String colId) {
		if (!Objects.equals(_colId, colId)) {
			_colId = colId;
			smartUpdate("colId", colId);
		}
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		if (!Objects.equals(_type, type)) {
			_type = type;
			smartUpdate("type", type);
		}
	}

	public int getWidth() {
		return _width;
	}

	public void setWidth(int width) {
		if (width < 0)
			throw new WrongValueException("shouldn't less than 0: " + width);
		if (_width != width) {
			_width = width;
			smartUpdate("width", width);
		}
	}

	public int getMinWidth() {
		return _minWidth;
	}

	public void setMinWidth(int minWidth) {
		if (minWidth < 0)
			throw new WrongValueException("shouldn't less than 0: " + minWidth);
		if (_minWidth != minWidth) {
			_minWidth = minWidth;
			smartUpdate("minWidth", minWidth);
		}
	}

	public int getMaxWidth() {
		return _maxWidth;
	}

	public void setMaxWidth(int maxWidth) {
		if (maxWidth < 0)
			throw new WrongValueException("shouldn't less than 0: " + maxWidth);
		if (_maxWidth != maxWidth) {
			_maxWidth = maxWidth;
			smartUpdate("maxWidth", maxWidth);
		}
	}

	public int getFlex() {
		return _flex;
	}

	public void setFlex(int flex) {
		if (flex < 0)
			throw new WrongValueException("shouldn't less than 0: " + flex);
		if (_flex != flex) {
			_flex = flex;
			smartUpdate("flex", flex);
		}
	}

	public String getFilter() {
		return _filter;
	}

	public void setFilter(String filter) {
		if (!Objects.equals(_filter, filter)) {
			_filter = filter;
			smartUpdate("filter", filter);
		}
	}

	public boolean isFloatingFilter() {
		return _floatingFilter;
	}

	public void setFloatingFilter(boolean floatingFilter) {
		if (_floatingFilter != floatingFilter) {
			_floatingFilter = floatingFilter;
			smartUpdate("floatingFilter", floatingFilter);
		}
	}

	public String getFloatingFilterComponent() {
		return _floatingFilterComponent;
	}

	public void setFloatingFilterComponent(String floatingFilterComponent) {
		if (!Objects.equals(_floatingFilterComponent, floatingFilterComponent)) {
			_floatingFilterComponent = floatingFilterComponent;
			smartUpdate("floatingFilterComponent", floatingFilterComponent);
		}
	}

	public boolean isHide() {
		return _hide;
	}

	public void setHide(boolean hide) {
		if (_hide != hide) {
			_hide = hide;
			smartUpdate("hide", hide);
		}
	}

	public String getPinned() {
		return _pinned;
	}

	public void setPinned(String pinned) {
		if (pinned != null && !"left".equals(pinned) && !"right".equals(pinned))
			throw new WrongValueException("expected null, left or right: " + pinned);
		if (!Objects.equals(_pinned, pinned)) {
			_pinned = pinned;
			smartUpdate("pinned", pinned);
		}
	}

	public boolean isLockPosition() {
		return _lockPosition;
	}

	public void setLockPosition(boolean lockPosition) {
		if (_lockPosition != lockPosition) {
			_lockPosition = lockPosition;
			smartUpdate("lockPosition", lockPosition);
		}
	}

	public boolean isLockVisible() {
		return _lockVisible;
	}

	public void setLockVisible(boolean lockVisible) {
		if (_lockVisible != lockVisible) {
			_lockVisible = lockVisible;
			smartUpdate("lockVisible", lockVisible);
		}
	}

	public boolean isLockPinned() {
		return _lockPinned;
	}

	public void setLockPinned(boolean lockPinned) {
		if (_lockPinned != lockPinned) {
			_lockPinned = lockPinned;
			smartUpdate("lockPinned", lockPinned);
		}
	}

	public boolean isSortable() {
		return _sortable;
	}

	public void setSortable(boolean sortable) {
		if (_sortable != sortable) {
			_sortable = sortable;
			smartUpdate("sortable", sortable);
		}
	}

	public String getSort() {
		return _sort;
	}

	public void setSort(String sort) {
		if (!"asc".equals(sort) && !"desc".equals(sort) && sort != null)
			throw new WrongValueException("expected null, asc, or desc: " + sort);
		if (!Objects.equals(_sort, sort)) {
			_sort = sort;
			smartUpdate("sort", sort);
		}
	}

	public int getSortedAt() {
		return _sortedAt;
	}

	public void setSortedAt(int sortedAt) {
		if (_sortedAt != sortedAt) {
			_sortedAt = sortedAt;
			smartUpdate("sortedAt", sortedAt);
		}
	}

	public boolean isResizable() {
		return _resizable;
	}

	public void setResizable(boolean resizable) {
		if (_resizable != resizable) {
			_resizable = resizable;
			smartUpdate("resizable", resizable);
		}
	}

	public String getHeaderTooltip() {
		return _headerTooltip;
	}

	public void setHeaderTooltip(String headerTooltip) {
		if (!Objects.equals(_headerTooltip, headerTooltip)) {
			_headerTooltip = headerTooltip;
			smartUpdate("headerTooltip", headerTooltip);
		}
	}

	public String getTooltipField() {
		return _tooltipField;
	}

	public void setTooltipField(String tooltipField) {
		if (!Objects.equals(_tooltipField, tooltipField)) {
			_tooltipField = tooltipField;
			smartUpdate("tooltipField", tooltipField);
		}
	}

	public String getTooltip() {
		return _tooltip;
	}

	public void setTooltip(String tooltip) {
		if (!Objects.equals(_tooltip, tooltip)) {
			_tooltip = tooltip;
			smartUpdate("tooltip", tooltip);
		}
	}

	public boolean isCheckboxSelection() {
		return _checkboxSelection;
	}

	public void setCheckboxSelection(boolean checkboxSelection) {
		if (_checkboxSelection != checkboxSelection) {
			_checkboxSelection = checkboxSelection;
			smartUpdate("checkboxSelection", checkboxSelection);
		}
	}

	public boolean isHeaderCheckboxSelection() {
		return _headerCheckboxSelection;
	}

	public void setHeaderCheckboxSelection(boolean headerCheckboxSelection) {
		if (_headerCheckboxSelection != headerCheckboxSelection) {
			_headerCheckboxSelection = headerCheckboxSelection;
			smartUpdate("headerCheckboxSelection", headerCheckboxSelection);
		}
	}

	public boolean isHeaderCheckboxSelectionFilteredOnly() {
		return _headerCheckboxSelectionFilteredOnly;
	}

	public void setHeaderCheckboxSelectionFilteredOnly(boolean headerCheckboxSelectionFilteredOnly) {
		if (_headerCheckboxSelectionFilteredOnly != headerCheckboxSelectionFilteredOnly) {
			_headerCheckboxSelectionFilteredOnly = headerCheckboxSelectionFilteredOnly;
			smartUpdate("headerCheckboxSelectionFilteredOnly", headerCheckboxSelectionFilteredOnly);
		}
	}

	public boolean isRowDrag() {
		return _rowDrag;
	}

	public void setRowDrag(boolean rowDrag) {
		if (_rowDrag != rowDrag) {
			_rowDrag = rowDrag;
			smartUpdate("rowDrag", rowDrag);
		}
	}

	public String getRowDragText() {
		return _rowDragText;
	}

	public void setRowDragText(String rowDragText) {
		if (!Objects.equals(_rowDragText, rowDragText)) {
			_rowDragText = rowDragText;
			smartUpdate("rowDragText", rowDragText);
		}
	}

	public boolean isDndSource() {
		return _dndSource;
	}

	public void setDndSource(boolean dndSource) {
		if (_dndSource != dndSource) {
			_dndSource = dndSource;
			smartUpdate("dndSource", dndSource);
		}
	}

	public String getCellStyle() {
		return _cellStyle;
	}

	public void setCellStyle(String cellStyle) {
		if (!Objects.equals(_cellStyle, cellStyle)) {
			_cellStyle = cellStyle;
			smartUpdate("cellStyle", cellStyle);
		}
	}

	public String getCellClass() {
		return _cellClass;
	}

	public void setCellClass(String cellClass) {
		if (!Objects.equals(_cellClass, cellClass)) {
			_cellClass = cellClass;
			smartUpdate("cellClass", cellClass);
		}
	}

	public boolean isEditable() {
		return _editable;
	}

	public void setEditable(boolean editable) {
		if (_editable != editable) {
			_editable = editable;
			smartUpdate("editable", editable);
		}
	}

	public String getAggFunc() {
		return _aggFunc;
	}

	public void setAggFunc(String aggFunc) {
		if (!Objects.equals(_aggFunc, aggFunc)) {
			_aggFunc = aggFunc;
			smartUpdate("aggFunc", aggFunc);
		}
	}

	public String getAllowedAggFuncs() {
		return _allowedAggFuncs;
	}

	public void setAllowedAggFuncs(String allowedAggFuncs) {
		if (!Objects.equals(_allowedAggFuncs, allowedAggFuncs)) {
			_allowedAggFuncs = allowedAggFuncs;
			smartUpdate("allowedAggFuncs", allowedAggFuncs);
		}
	}

	public int getRowGroupIndex() {
		return _rowGroupIndex;
	}

	public void setRowGroupIndex(int rowGroupIndex) {
		if (rowGroupIndex < 0)
			throw new WrongValueException("expected a positive value: " + rowGroupIndex);
		if (_rowGroupIndex != rowGroupIndex) {
			_rowGroupIndex = rowGroupIndex;
			smartUpdate("rowGroupIndex", rowGroupIndex);
		}
	}

	public int getPivotIndex() {
		return _pivotIndex;
	}

	public void setPivotIndex(int pivotIndex) {
		if (pivotIndex < 0)
			throw new WrongValueException("expected a positive value: " + pivotIndex);
		if (_pivotIndex != pivotIndex) {
			_pivotIndex = pivotIndex;
			smartUpdate("pivotIndex", pivotIndex);
		}
	}

	public boolean isUnSortIcon() {
		return _unSortIcon;
	}

	public void setUnSortIcon(boolean unSortIcon) {
		if (_unSortIcon != unSortIcon) {
			_unSortIcon = unSortIcon;
			smartUpdate("unSortIcon", unSortIcon);
		}
	}

	public boolean isEnableRowGroup() {
		return _enableRowGroup;
	}

	public void setEnableRowGroup(boolean enableRowGroup) {
		if (_enableRowGroup != enableRowGroup) {
			_enableRowGroup = enableRowGroup;
			smartUpdate("enableRowGroup", enableRowGroup);
		}
	}

	public boolean isEnablePivot() {
		return _enablePivot;
	}

	public void setEnablePivot(boolean enablePivot) {
		if (_enablePivot != enablePivot) {
			_enablePivot = enablePivot;
			smartUpdate("enablePivot", enablePivot);
		}
	}

	public boolean isPivotTotals() {
		return _pivotTotals;
	}

	public void setPivotTotals(boolean pivotTotals) {
		if (_pivotTotals != pivotTotals) {
			_pivotTotals = pivotTotals;
			smartUpdate("pivotTotals", pivotTotals);
		}
	}

	public boolean isEnableValue() {
		return _enableValue;
	}

	public void setEnableValue(boolean enableValue) {
		if (_enableValue != enableValue) {
			_enableValue = enableValue;
			smartUpdate("enableValue", enableValue);
		}
	}

	public boolean isEnableCellChangeFlash() {
		return _enableCellChangeFlash;
	}

	public void setEnableCellChangeFlash(boolean enableCellChangeFlash) {
		if (_enableCellChangeFlash != enableCellChangeFlash) {
			_enableCellChangeFlash = enableCellChangeFlash;
			smartUpdate("enableCellChangeFlash", enableCellChangeFlash);
		}
	}

	public boolean isSuppressMenu() {
		return _suppressMenu;
	}

	public void setSuppressMenu(boolean suppressMenu) {
		if (_suppressMenu != suppressMenu) {
			_suppressMenu = suppressMenu;
			smartUpdate("suppressMenu", suppressMenu);
		}
	}

	public boolean isSuppressSizeToFit() {
		return _suppressSizeToFit;
	}

	public void setSuppressSizeToFit(boolean suppressSizeToFit) {
		if (_suppressSizeToFit != suppressSizeToFit) {
			_suppressSizeToFit = suppressSizeToFit;
			smartUpdate("suppressSizeToFit", suppressSizeToFit);
		}
	}

	public boolean isSuppressMovable() {
		return _suppressMovable;
	}

	public void setSuppressMovable(boolean suppressMovable) {
		if (_suppressMovable != suppressMovable) {
			_suppressMovable = suppressMovable;
			smartUpdate("suppressMovable", suppressMovable);
		}
	}

	public boolean isSuppressNavigable() {
		return _suppressNavigable;
	}

	public void setSuppressNavigable(boolean suppressNavigable) {
		if (_suppressNavigable != suppressNavigable) {
			_suppressNavigable = suppressNavigable;
			smartUpdate("suppressNavigable", suppressNavigable);
		}
	}

	public boolean isSuppressCellFlash() {
		return _suppressCellFlash;
	}

	public void setSuppressCellFlash(boolean suppressCellFlash) {
		if (_suppressCellFlash != suppressCellFlash) {
			_suppressCellFlash = suppressCellFlash;
			smartUpdate("suppressCellFlash", suppressCellFlash);
		}
	}

	public boolean isAutoHeight() {
		return _autoHeight;
	}

	public void setAutoHeight(boolean autoHeight) {
		if (_autoHeight != autoHeight) {
			_autoHeight = autoHeight;
			smartUpdate("autoHeight", autoHeight);
		}
	}

	public boolean isSingleClickEdit() {
		return _singleClickEdit;
	}

	public void setSingleClickEdit(boolean singleClickEdit) {
		if (_singleClickEdit != singleClickEdit) {
			_singleClickEdit = singleClickEdit;
			smartUpdate("singleClickEdit", singleClickEdit);
		}
	}

	public String getGroupId() {
		return _groupId;
	}

	public void setGroupId(String groupId) {
		if (!Objects.equals(_groupId, groupId)) {
			_groupId = groupId;
			smartUpdate("groupId", groupId);
		}
	}

	public boolean isMarryChildren() {
		return _marryChildren;
	}

	public void setMarryChildren(boolean marryChildren) {
		if (_marryChildren != marryChildren) {
			_marryChildren = marryChildren;
			smartUpdate("marryChildren", marryChildren);
		}
	}

	public boolean isOpenByDefault() {
		return _openByDefault;
	}

	public void setOpenByDefault(boolean openByDefault) {
		if (_openByDefault != openByDefault) {
			_openByDefault = openByDefault;
			smartUpdate("openByDefault", openByDefault);
		}
	}

	/**
	 * Returns if this column is a column group or not.
	 *
	 * @return if this column is a column group
	 */
	public boolean isColumnGroup() {
		return !getChildren().isEmpty();
	}

	@Override
	public void beforeChildAdded(Component child, Component insertBefore) {
		if (!(child instanceof Aggridcolumn))
			throw new UiException("Unsupported child: " + child);
		super.beforeChildAdded(child, insertBefore);
	}

	@Override
	protected void renderProperties(ContentRenderer renderer) throws IOException {
		super.renderProperties(renderer);
		initSortComparator();
		render(renderer, "headerName", _headerName);
		render(renderer, "columnGroupShow", _columnGroupShow);
		render(renderer, "headerClass", _headerClass);
		render(renderer, "toolPanelClass", _toolPanelClass);
		render(renderer, "suppressColumnsToolPanel", _suppressColumnsToolPanel);
		render(renderer, "suppressFiltersToolPanel", _suppressFiltersToolPanel);
		render(renderer, "field", _field);
		render(renderer, "colId", _colId);
		render(renderer, "type", _type);
		if (_width >= 0)
			render(renderer, "width", _width);
		if (_minWidth >= 0)
			render(renderer, "minWidth", _minWidth);
		if (_maxWidth >= 0)
			render(renderer, "maxWidth", _maxWidth);
		if (_flex >= 0)
			render(renderer, "flex", _flex);
		if (Boolean.parseBoolean(_filter))
			render(renderer, "filter", true);
		else
			render(renderer, "filter", _filter);
		render(renderer, "floatingFilter", _floatingFilter);
		render(renderer, "floatingFilterComponent", _floatingFilterComponent);
		render(renderer, "hide", _hide);
		render(renderer, "pinned", _pinned);
		render(renderer, "lockPosition", _lockPosition);
		render(renderer, "lockVisible", _lockVisible);
		render(renderer, "lockPinned", _lockPinned);
		render(renderer, "sortable", _sortable);
		render(renderer, "sort", _sort);
		if (_sortedAt > 0)
			render(renderer, "sortedAt", _sortedAt);
		render(renderer, "resizable", _resizable);
		render(renderer, "headerTooltip", _headerTooltip);
		render(renderer, "tooltipField", _tooltipField);
		render(renderer, "tooltip", _tooltip);
		render(renderer, "checkboxSelection", _checkboxSelection);
		render(renderer, "headerCheckboxSelection", _headerCheckboxSelection);
		render(renderer, "headerCheckboxSelectionFilteredOnly", _headerCheckboxSelectionFilteredOnly);
		render(renderer, "rowDrag", _rowDrag);
		render(renderer, "rowDragText", _rowDragText);
		render(renderer, "dndSource", _dndSource);
		render(renderer, "cellStyle", _cellStyle);
		render(renderer, "cellClass", _cellClass);
		render(renderer, "editable", _editable);
		render(renderer, "aggFunc", _aggFunc);
		if (_rowGroupIndex >= 0)
			render(renderer, "rowGroupIndex", _rowGroupIndex);
		if (_pivotIndex >= 0)
			render(renderer, "pivotIndex", _pivotIndex);
		render(renderer, "unSortIcon", _unSortIcon);
		render(renderer, "enableRowGroup", _enableRowGroup);
		render(renderer, "enablePivot", _enablePivot);
		render(renderer, "pivotTotals", _pivotTotals);
		render(renderer, "enableValue", _enableValue);
		render(renderer, "enableCellChangeFlash", _enableCellChangeFlash);
		render(renderer, "suppressMenu", _suppressMenu);
		render(renderer, "suppressSizeToFit", _suppressSizeToFit);
		render(renderer, "suppressMovable", _suppressMovable);
		render(renderer, "suppressNavigable", _suppressNavigable);
		render(renderer, "suppressCellFlash", _suppressCellFlash);
		render(renderer, "autoHeight", _autoHeight);
		render(renderer, "singleClickEdit", _singleClickEdit);
		render(renderer, "groupId", _groupId);
		render(renderer, "marryChildren", _marryChildren);
		render(renderer, "openByDefault", _openByDefault);
	}

	private void initSortComparator() {
		if (isSortable()) {
			String field = getField();
			if (getSortAscending() == null)
				setSortAscending(new FieldComparator(field, true));
			if (getSortDescending() == null)
				setSortDescending(new FieldComparator(field, false));
		}
	}
}
