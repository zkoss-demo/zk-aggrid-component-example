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
import org.zkoss.zul.GroupComparator;

/**
 * @author rudyhuang
 */
public class Aggridcolumn<E> extends AbstractComponent {
	private transient Comparator<E> _sortAsc, _sortDsc;
	private AuxInfo _auxinf;

	/**
	 * Returns the ascending sorter, or null if not available.
	 */
	public Comparator<E> getSortAscending() {
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
	public void setSortAscending(Comparator<E> sorter) {
		if (!Objects.equals(_sortAsc, sorter)) {
			_sortAsc = sorter;
		}
	}

	/**
	 * Returns the descending sorter, or null if not available.
	 */
	public Comparator<E> getSortDescending() {
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
	public void setSortDescending(Comparator<E> sorter) {
		if (!Objects.equals(_sortDsc, sorter)) {
			_sortDsc = sorter;
		}
	}

	public String getHeaderName() {
		return _auxinf != null ? _auxinf._headerName : null;
	}

	public void setHeaderName(String headerName) {
		if (!Objects.equals(getHeaderName(), headerName)) {
			initAuxInfo()._headerName = headerName;
			smartUpdate("headerName", headerName);
		}
	}

	public boolean isColumnGroupShow() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.COLUMNGROUPSHOW);
	}

	public void setColumnGroupShow(boolean columnGroupShow) {
		if (isColumnGroupShow() != columnGroupShow) {
			initAuxInfo().setBoolean(AuxInfo.Attr.COLUMNGROUPSHOW, columnGroupShow);
			smartUpdate("columnGroupShow", columnGroupShow);
		}
	}

	public String getHeaderClass() {
		return _auxinf != null ? _auxinf._headerClass : null;
	}

	public void setHeaderClass(String headerClass) {
		if (!Objects.equals(getHeaderName(), headerClass)) {
			initAuxInfo()._headerClass = headerClass;
			smartUpdate("headerClass", headerClass);
		}
	}

	public String getToolPanelClass() {
		return _auxinf != null ? _auxinf._toolPanelClass : null;
	}

	public void setToolPanelClass(String toolPanelClass) {
		if (!Objects.equals(getToolPanelClass(), toolPanelClass)) {
			initAuxInfo()._toolPanelClass = toolPanelClass;
			smartUpdate("toolPanelClass", toolPanelClass);
		}
	}

	public boolean isSuppressColumnsToolPanel() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSCOLUMNSTOOLPANEL);
	}

	public void setSuppressColumnsToolPanel(boolean suppressColumnsToolPanel) {
		if (isSuppressColumnsToolPanel() != suppressColumnsToolPanel) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSCOLUMNSTOOLPANEL, suppressColumnsToolPanel);
			smartUpdate("suppressColumnsToolPanel", suppressColumnsToolPanel);
		}
	}

	public boolean isSuppressFiltersToolPanel() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSFILTERSTOOLPANEL);
	}

	public void setSuppressFiltersToolPanel(boolean suppressFiltersToolPanel) {
		if (isSuppressFiltersToolPanel() != suppressFiltersToolPanel) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SUPPRESSFILTERSTOOLPANEL, suppressFiltersToolPanel);
			smartUpdate("suppressFiltersToolPanel", suppressFiltersToolPanel);
		}
	}

	public String getField() {
		return _auxinf != null ? _auxinf._field : null;
	}

	public void setField(String field) {
		if (!Objects.equals(getField(), field)) {
			initAuxInfo()._field = field;
			smartUpdate("field", field);
		}
	}

	public String getColId() {
		return _auxinf != null ? _auxinf._colId : null;
	}

	public void setColId(String colId) {
		if (!Objects.equals(getColId(), colId)) {
			initAuxInfo()._colId = colId;
			smartUpdate("colId", colId);
		}
	}

	public String getType() {
		return _auxinf != null ? _auxinf._type : null;
	}

	public void setType(String type) {
		if (!Objects.equals(getType(), type)) {
			initAuxInfo()._type = type;
			smartUpdate("type", type);
		}
	}

	public int getWidth() {
		return _auxinf != null ? _auxinf._width : -1;
	}

	public void setWidth(int width) {
		if (width < 0)
			throw new WrongValueException("shouldn't be less than 0: " + width);
		if (getWidth() != width) {
			initAuxInfo()._width = width;
			smartUpdate("width", width);
		}
	}

	public int getMinWidth() {
		return _auxinf != null ? _auxinf._minWidth : -1;
	}

	public void setMinWidth(int minWidth) {
		if (minWidth < 0)
			throw new WrongValueException("shouldn't be less than 0: " + minWidth);
		if (getMinWidth() != minWidth) {
			initAuxInfo()._minWidth = minWidth;
			smartUpdate("minWidth", minWidth);
		}
	}

	public int getMaxWidth() {
		return _auxinf != null ? _auxinf._maxWidth : -1;
	}

	public void setMaxWidth(int maxWidth) {
		if (maxWidth < 0)
			throw new WrongValueException("shouldn't be less than 0: " + maxWidth);
		if (getMaxWidth() != maxWidth) {
			initAuxInfo()._maxWidth = maxWidth;
			smartUpdate("maxWidth", maxWidth);
		}
	}

	public int getFlex() {
		return _auxinf != null ? _auxinf._flex : -1;
	}

	public void setFlex(int flex) {
		if (flex < 0)
			throw new WrongValueException("shouldn't be less than 0: " + flex);
		if (getFlex() != flex) {
			initAuxInfo()._flex = flex;
			smartUpdate("flex", flex);
		}
	}

	public String getFilter() {
		return _auxinf != null ? _auxinf._filter : null;
	}

	public void setFilter(String filter) {
		if (!Objects.equals(getFilter(), filter)) {
			initAuxInfo()._filter = filter;
			smartUpdate("filter", filter);
		}
	}

	public boolean isFloatingFilter() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.FLOATINGFILTER);
	}

	public void setFloatingFilter(boolean floatingFilter) {
		if (isFloatingFilter() != floatingFilter) {
			initAuxInfo().setBoolean(AuxInfo.Attr.FLOATINGFILTER, floatingFilter);
			smartUpdate("floatingFilter", floatingFilter);
		}
	}

	public String getFloatingFilterComponent() {
		return _auxinf != null ? _auxinf._floatingFilterComponent : null;
	}

	public void setFloatingFilterComponent(String floatingFilterComponent) {
		if (!Objects.equals(getFloatingFilterComponent(), floatingFilterComponent)) {
			initAuxInfo()._floatingFilterComponent = floatingFilterComponent;
			smartUpdate("floatingFilterComponent", floatingFilterComponent);
		}
	}

	public boolean isHide() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.HIDE);
	}

	public void setHide(boolean hide) {
		if (isHide() != hide) {
			initAuxInfo().setBoolean(AuxInfo.Attr.HIDE, hide);
			smartUpdate("hide", hide);
		}
	}

	public String getPinned() {
		return _auxinf != null ? _auxinf._pinned : null;
	}

	public void setPinned(String pinned) {
		if (pinned != null && !"left".equals(pinned) && !"right".equals(pinned))
			throw new WrongValueException("expected null, left or right: " + pinned);
		if (!Objects.equals(getPinned(), pinned)) {
			initAuxInfo()._pinned = pinned;
			smartUpdate("pinned", pinned);
		}
	}

	public boolean isLockPosition() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.LOCKPOSITION);
	}

	public void setLockPosition(boolean lockPosition) {
		if (isLockPosition() != lockPosition) {
			initAuxInfo().setBoolean(AuxInfo.Attr.LOCKPOSITION, lockPosition);
			smartUpdate("lockPosition", lockPosition);
		}
	}

	public boolean isLockVisible() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.LOCKVISIBLE);
	}

	public void setLockVisible(boolean lockVisible) {
		if (isLockVisible() != lockVisible) {
			initAuxInfo().setBoolean(AuxInfo.Attr.LOCKVISIBLE, lockVisible);
			smartUpdate("lockVisible", lockVisible);
		}
	}

	public boolean isLockPinned() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.LOCKPINNED);
	}

	public void setLockPinned(boolean lockPinned) {
		if (isLockPinned() != lockPinned) {
			initAuxInfo().setBoolean(AuxInfo.Attr.LOCKPINNED, lockPinned);
			smartUpdate("lockPinned", lockPinned);
		}
	}

	public boolean isSortable() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SORTABLE);
	}

	public void setSortable(boolean sortable) {
		if (isSortable() != sortable) {
			initAuxInfo().setBoolean(AuxInfo.Attr.SORTABLE, sortable);
			smartUpdate("sortable", sortable);
		}
	}

	public String getSort() {
		return _auxinf != null ? _auxinf._sort : null;
	}

	public void setSort(String sort) {
		if (!"asc".equals(sort) && !"desc".equals(sort) && sort != null)
			throw new WrongValueException("expected null, asc, or desc: " + sort);
		if (!Objects.equals(getSort(), sort)) {
			initAuxInfo()._sort = sort;
			smartUpdate("sort", sort);
		}
	}

	public int getSortedAt() {
		return _auxinf != null ? _auxinf._sortedAt : 0;
	}

	public void setSortedAt(int sortedAt) {
		if (getSortedAt() != sortedAt) {
			initAuxInfo()._sortedAt = sortedAt;
			smartUpdate("sortedAt", sortedAt);
		}
	}

	public boolean isResizable() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.RESIZABLE);
	}

	public void setResizable(boolean resizable) {
		if (isResizable() != resizable) {
			initAuxInfo().setBoolean(AuxInfo.Attr.RESIZABLE, resizable);
			smartUpdate("resizable", resizable);
		}
	}

	public String getHeaderTooltip() {
		return _auxinf != null ? _auxinf._headerTooltip : null;
	}

	public void setHeaderTooltip(String headerTooltip) {
		if (!Objects.equals(getHeaderTooltip(), headerTooltip)) {
			initAuxInfo()._headerTooltip = headerTooltip;
			smartUpdate("headerTooltip", headerTooltip);
		}
	}

	public String getTooltipField() {
		return _auxinf != null ? _auxinf._tooltipField : null;
	}

	public void setTooltipField(String tooltipField) {
		if (!Objects.equals(getTooltipField(), tooltipField)) {
			initAuxInfo()._tooltipField = tooltipField;
			smartUpdate("tooltipField", tooltipField);
		}
	}

	public String getTooltip() {
		return _auxinf != null ? _auxinf._tooltip : null;
	}

	public void setTooltip(String tooltip) {
		if (!Objects.equals(getTooltip(), tooltip)) {
			initAuxInfo()._tooltip = tooltip;
			smartUpdate("tooltip", tooltip);
		}
	}

	public boolean isCheckboxSelection() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.CHECKBOXSELECTION);
	}

	public void setCheckboxSelection(boolean checkboxSelection) {
		if (isCheckboxSelection() != checkboxSelection) {
			_auxinf.setBoolean(AuxInfo.Attr.CHECKBOXSELECTION, checkboxSelection);
			smartUpdate("checkboxSelection", checkboxSelection);
		}
	}

	public boolean isHeaderCheckboxSelection() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.HEADERCHECKBOXSELECTION);
	}

	public void setHeaderCheckboxSelection(boolean headerCheckboxSelection) {
		if (isHeaderCheckboxSelection() != headerCheckboxSelection) {
			_auxinf.setBoolean(AuxInfo.Attr.HEADERCHECKBOXSELECTION, headerCheckboxSelection);
			smartUpdate("headerCheckboxSelection", headerCheckboxSelection);
		}
	}

	public boolean isHeaderCheckboxSelectionFilteredOnly() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.HEADERCHECKBOXSELECTIONFILTEREDONLY);
	}

	public void setHeaderCheckboxSelectionFilteredOnly(boolean headerCheckboxSelectionFilteredOnly) {
		if (isHeaderCheckboxSelectionFilteredOnly() != headerCheckboxSelectionFilteredOnly) {
			_auxinf.setBoolean(AuxInfo.Attr.HEADERCHECKBOXSELECTIONFILTEREDONLY, headerCheckboxSelectionFilteredOnly);
			smartUpdate("headerCheckboxSelectionFilteredOnly", headerCheckboxSelectionFilteredOnly);
		}
	}

	public boolean isRowDrag() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ROWDRAG);
	}

	public void setRowDrag(boolean rowDrag) {
		if (isRowDrag() != rowDrag) {
			_auxinf.setBoolean(AuxInfo.Attr.ROWDRAG, rowDrag);
			smartUpdate("rowDrag", rowDrag);
		}
	}

	public String getRowDragText() {
		return _auxinf != null ? _auxinf._rowDragText : null;
	}

	public void setRowDragText(String rowDragText) {
		if (!Objects.equals(getRowDragText(), rowDragText)) {
			initAuxInfo()._rowDragText = rowDragText;
			smartUpdate("rowDragText", rowDragText);
		}
	}

	public boolean isDndSource() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.DNDSOURCE);
	}

	public void setDndSource(boolean dndSource) {
		if (isDndSource() != dndSource) {
			_auxinf.setBoolean(AuxInfo.Attr.DNDSOURCE, dndSource);
			smartUpdate("dndSource", dndSource);
		}
	}

	public String getCellStyle() {
		return _auxinf != null ? _auxinf._cellStyle : null;
	}

	public void setCellStyle(String cellStyle) {
		if (!Objects.equals(getCellStyle(), cellStyle)) {
			initAuxInfo()._cellStyle = cellStyle;
			smartUpdate("cellStyle", cellStyle);
		}
	}

	public String getCellClass() {
		return _auxinf != null ? _auxinf._cellClass : null;
	}

	public void setCellClass(String cellClass) {
		if (!Objects.equals(getCellClass(), cellClass)) {
			initAuxInfo()._cellClass = cellClass;
			smartUpdate("cellClass", cellClass);
		}
	}

	public boolean isEditable() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.EDITABLE);
	}

	public void setEditable(boolean editable) {
		if (isEditable() != editable) {
			_auxinf.setBoolean(AuxInfo.Attr.EDITABLE, editable);
			smartUpdate("editable", editable);
		}
	}

	public String getAggFunc() {
		return _auxinf != null ? _auxinf._aggFunc : null;
	}

	public void setAggFunc(String aggFunc) {
		if (!Objects.equals(getAggFunc(), aggFunc)) {
			initAuxInfo()._aggFunc = aggFunc;
			smartUpdate("aggFunc", aggFunc);
		}
	}

	public String getAllowedAggFuncs() {
		return _auxinf != null ? _auxinf._allowedAggFuncs : null;
	}

	public void setAllowedAggFuncs(String allowedAggFuncs) {
		if (!Objects.equals(getAllowedAggFuncs(), allowedAggFuncs)) {
			initAuxInfo()._allowedAggFuncs = allowedAggFuncs;
			smartUpdate("allowedAggFuncs", allowedAggFuncs);
		}
	}

	public int getRowGroupIndex() {
		return _auxinf != null ? _auxinf._rowGroupIndex : -1;
	}

	public void setRowGroupIndex(int rowGroupIndex) {
		if (rowGroupIndex < 0)
			throw new WrongValueException("expected a positive value: " + rowGroupIndex);
		if (getRowGroupIndex() != rowGroupIndex) {
			initAuxInfo()._rowGroupIndex = rowGroupIndex;
			smartUpdate("rowGroupIndex", rowGroupIndex);
		}
	}

	public int getPivotIndex() {
		return _auxinf != null ? _auxinf._pivotIndex : -1;
	}

	public void setPivotIndex(int pivotIndex) {
		if (pivotIndex < 0)
			throw new WrongValueException("expected a positive value: " + pivotIndex);
		if (getPivotIndex() != pivotIndex) {
			initAuxInfo()._pivotIndex = pivotIndex;
			smartUpdate("pivotIndex", pivotIndex);
		}
	}

	public boolean isUnSortIcon() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.UNSORTICON);
	}

	public void setUnSortIcon(boolean unSortIcon) {
		if (isUnSortIcon() != unSortIcon) {
			_auxinf.setBoolean(AuxInfo.Attr.UNSORTICON, unSortIcon);
			smartUpdate("unSortIcon", unSortIcon);
		}
	}

	public boolean isEnableRowGroup() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENABLEROWGROUP);
	}

	public void setEnableRowGroup(boolean enableRowGroup) {
		if (isEnableRowGroup() != enableRowGroup) {
			_auxinf.setBoolean(AuxInfo.Attr.ENABLEROWGROUP, enableRowGroup);
			smartUpdate("enableRowGroup", enableRowGroup);
		}
	}

	public boolean isEnablePivot() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENABLEPIVOT);
	}

	public void setEnablePivot(boolean enablePivot) {
		if (isEnablePivot() != enablePivot) {
			_auxinf.setBoolean(AuxInfo.Attr.ENABLEPIVOT, enablePivot);
			smartUpdate("enablePivot", enablePivot);
		}
	}

	public boolean isPivotTotals() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.PIVOTTOTALS);
	}

	public void setPivotTotals(boolean pivotTotals) {
		if (isPivotTotals() != pivotTotals) {
			_auxinf.setBoolean(AuxInfo.Attr.PIVOTTOTALS, pivotTotals);
			smartUpdate("pivotTotals", pivotTotals);
		}
	}

	public boolean isEnableValue() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENABLEVALUE);
	}

	public void setEnableValue(boolean enableValue) {
		if (isEnableValue() != enableValue) {
			_auxinf.setBoolean(AuxInfo.Attr.ENABLEVALUE, enableValue);
			smartUpdate("enableValue", enableValue);
		}
	}

	public boolean isEnableCellChangeFlash() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.ENABLECELLCHANGEFLASH);
	}

	public void setEnableCellChangeFlash(boolean enableCellChangeFlash) {
		if (isEnableCellChangeFlash() != enableCellChangeFlash) {
			_auxinf.setBoolean(AuxInfo.Attr.ENABLECELLCHANGEFLASH, enableCellChangeFlash);
			smartUpdate("enableCellChangeFlash", enableCellChangeFlash);
		}
	}

	public boolean isSuppressMenu() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSMENU);
	}

	public void setSuppressMenu(boolean suppressMenu) {
		if (isSuppressMenu() != suppressMenu) {
			_auxinf.setBoolean(AuxInfo.Attr.SUPPRESSMENU, suppressMenu);
			smartUpdate("suppressMenu", suppressMenu);
		}
	}

	public boolean isSuppressSizeToFit() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSSIZETOFIT);
	}

	public void setSuppressSizeToFit(boolean suppressSizeToFit) {
		if (isSuppressSizeToFit() != suppressSizeToFit) {
			_auxinf.setBoolean(AuxInfo.Attr.SUPPRESSSIZETOFIT, suppressSizeToFit);
			smartUpdate("suppressSizeToFit", suppressSizeToFit);
		}
	}

	public boolean isSuppressMovable() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSMOVABLE);
	}

	public void setSuppressMovable(boolean suppressMovable) {
		if (isSuppressMovable() != suppressMovable) {
			_auxinf.setBoolean(AuxInfo.Attr.SUPPRESSMOVABLE, suppressMovable);
			smartUpdate("suppressMovable", suppressMovable);
		}
	}

	public boolean isSuppressNavigable() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSNAVIGABLE);
	}

	public void setSuppressNavigable(boolean suppressNavigable) {
		if (isSuppressNavigable() != suppressNavigable) {
			_auxinf.setBoolean(AuxInfo.Attr.SUPPRESSNAVIGABLE, suppressNavigable);
			smartUpdate("suppressNavigable", suppressNavigable);
		}
	}

	public boolean isSuppressCellFlash() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SUPPRESSCELLFLASH);
	}

	public void setSuppressCellFlash(boolean suppressCellFlash) {
		if (isSuppressCellFlash() != suppressCellFlash) {
			_auxinf.setBoolean(AuxInfo.Attr.SUPPRESSCELLFLASH, suppressCellFlash);
			smartUpdate("suppressCellFlash", suppressCellFlash);
		}
	}

	public boolean isAutoHeight() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.AUTOHEIGHT);
	}

	public void setAutoHeight(boolean autoHeight) {
		if (isAutoHeight() != autoHeight) {
			_auxinf.setBoolean(AuxInfo.Attr.AUTOHEIGHT, autoHeight);
			smartUpdate("autoHeight", autoHeight);
		}
	}

	public boolean isSingleClickEdit() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.SINGLECLICKEDIT);
	}

	public void setSingleClickEdit(boolean singleClickEdit) {
		if (isSingleClickEdit() != singleClickEdit) {
			_auxinf.setBoolean(AuxInfo.Attr.SINGLECLICKEDIT, singleClickEdit);
			smartUpdate("singleClickEdit", singleClickEdit);
		}
	}

	public String getGroupId() {
		return _auxinf != null ? _auxinf._groupId : null;
	}

	public void setGroupId(String groupId) {
		if (!Objects.equals(getGroupId(), groupId)) {
			initAuxInfo()._groupId = groupId;
			smartUpdate("groupId", groupId);
		}
	}

	public boolean isMarryChildren() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.MARRYCHILDREN);
	}

	public void setMarryChildren(boolean marryChildren) {
		if (isMarryChildren() != marryChildren) {
			_auxinf.setBoolean(AuxInfo.Attr.MARRYCHILDREN, marryChildren);
			smartUpdate("marryChildren", marryChildren);
		}
	}

	public boolean isOpenByDefault() {
		return _auxinf != null && _auxinf.getBoolean(AuxInfo.Attr.OPENBYDEFAULT);
	}

	public void setOpenByDefault(boolean openByDefault) {
		if (isOpenByDefault() != openByDefault) {
			_auxinf.setBoolean(AuxInfo.Attr.OPENBYDEFAULT, openByDefault);
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
		if (_auxinf != null) {
			render(renderer, "headerName", getHeaderName());
			render(renderer, "columnGroupShow", isColumnGroupShow());
			render(renderer, "headerClass", getHeaderName());
			render(renderer, "toolPanelClass", getToolPanelClass());
			render(renderer, "suppressColumnsToolPanel", isSuppressColumnsToolPanel());
			render(renderer, "suppressFiltersToolPanel", isSuppressFiltersToolPanel());
			render(renderer, "field", getField());
			render(renderer, "colId", getColId());
			render(renderer, "type", getType());
			if (_auxinf._width >= 0)
				render(renderer, "width", _auxinf._width);
			if (_auxinf._minWidth >= 0)
				render(renderer, "minWidth", _auxinf._minWidth);
			if (_auxinf._maxWidth >= 0)
				render(renderer, "maxWidth", _auxinf._maxWidth);
			if (_auxinf._flex >= 0)
				render(renderer, "flex", _auxinf._flex);
			if (Boolean.parseBoolean(_auxinf._filter))
				render(renderer, "filter", true);
			else
				render(renderer, "filter", _auxinf._filter);
			render(renderer, "floatingFilter", isFloatingFilter());
			render(renderer, "floatingFilterComponent", getFloatingFilterComponent());
			render(renderer, "hide", isHide());
			render(renderer, "pinned", getPinned());
			render(renderer, "lockPosition", isLockPosition());
			render(renderer, "lockVisible", isLockVisible());
			render(renderer, "lockPinned", isLockPinned());
			render(renderer, "sortable", isSortable());
			render(renderer, "sort", getSort());
			if (_auxinf._sortedAt > 0)
				render(renderer, "sortedAt", _auxinf._sortedAt);
			render(renderer, "resizable", isResizable());
			render(renderer, "headerTooltip", getHeaderTooltip());
			render(renderer, "tooltipField", getTooltipField());
			render(renderer, "tooltip", getTooltip());
			render(renderer, "checkboxSelection", isCheckboxSelection());
			render(renderer, "headerCheckboxSelection", isHeaderCheckboxSelection());
			render(renderer, "headerCheckboxSelectionFilteredOnly", isHeaderCheckboxSelectionFilteredOnly());
			render(renderer, "rowDrag", isRowDrag());
			render(renderer, "rowDragText", getRowDragText());
			render(renderer, "dndSource", isDndSource());
			render(renderer, "cellStyle", getCellStyle());
			render(renderer, "cellClass", getCellClass());
			render(renderer, "editable", isEditable());
			render(renderer, "aggFunc", getAggFunc());
			if (_auxinf._rowGroupIndex >= 0)
				render(renderer, "rowGroupIndex", _auxinf._rowGroupIndex);
			if (_auxinf._pivotIndex >= 0)
				render(renderer, "pivotIndex", _auxinf._pivotIndex);
			render(renderer, "unSortIcon", isUnSortIcon());
			render(renderer, "enableRowGroup", isEnableRowGroup());
			render(renderer, "enablePivot", isEnablePivot());
			render(renderer, "pivotTotals", isPivotTotals());
			render(renderer, "enableValue", isEnableValue());
			render(renderer, "enableCellChangeFlash", isEnableCellChangeFlash());
			render(renderer, "suppressMenu", isSuppressMenu());
			render(renderer, "suppressSizeToFit", isSuppressSizeToFit());
			render(renderer, "suppressMovable", isSuppressMovable());
			render(renderer, "suppressNavigable", isSuppressNavigable());
			render(renderer, "suppressCellFlash", isSuppressCellFlash());
			render(renderer, "autoHeight", isAutoHeight());
			render(renderer, "singleClickEdit", isSingleClickEdit());
			render(renderer, "groupId", getGroupId());
			render(renderer, "marryChildren", isMarryChildren());
			render(renderer, "openByDefault", isOpenByDefault());
		}
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

	private AuxInfo initAuxInfo() {
		if (_auxinf == null)
			_auxinf = new AuxInfo();
		return _auxinf;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object clone() {
		final Aggridcolumn<E> clone = (Aggridcolumn<E>) super.clone();
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
			AGGFUNC,
			AUTOHEIGHT,
			CELLCLASS,
			CELLSTYLE,
			CHECKBOXSELECTION,
			COLLD,
			COLUMNGROUP,
			COLUMNGROUPSHOW,
			DNDSOURCE,
			EDITABLE,
			ENABLECELLCHANGEFLASH,
			ENABLEPIVOT,
			ENABLEROWGROUP,
			ENABLEVALUE,
			FIELD,
			FILTER,
			FLEX,
			FLOATINGFILTER,
			FLOATINGFILTERCOMPONENT,
			GROUPID,
			HEADERCHECKBOXSELECTION,
			HEADERCHECKBOXSELECTIONFILTEREDONLY,
			HEADERCLASS,
			HEADERNAME,
			HEADERTOOPTIP,
			HIDE,
			LOCKPINNED,
			LOCKPOSITION,
			LOCKVISIBLE,
			MARRYCHILDREN,
			MAXWIDTH,
			MINWIDTH,
			OPENBYDEFAULT,
			PINNED,
			PIVOTINDEX,
			PIVOTTOTALS,
			RESIZABLE,
			ROWDRAG,
			ROWDRAGTEXT,
			ROWGROUPINDEX,
			SINGLECLICKEDIT,
			SORT,
			SORTABLE,
			SORTASCENDING,
			SORTDESCENDING,
			SORTEDAT,
			SUPPRESSCELLFLASH,
			SUPPRESSCOLUMNSTOOLPANEL,
			SUPPRESSFILTERSTOOLPANEL,
			SUPPRESSMENU,
			SUPPRESSMOVABLE,
			SUPPRESSNAVIGABLE,
			SUPPRESSSIZETOFIT,
			TOOLPANELCLASS,
			TOOLTIP,
			TOOLTIPFIELD,
			TYPE,
			UNSORTICON,
			WIDTH
		}

		private final BitSet _bitset = new BitSet();
		// Columns and Column Groups
		private String _headerName;
		private String _headerClass;
		private String _toolPanelClass;
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
		private String _floatingFilterComponent;
		// TODO floatingFilterComponentParams
		private String _pinned;
		private String _sort;
		private int _sortedAt;
		// TODO Array private String _sortingOrder;
		private String _headerTooltip;
		private String _tooltipField;
		private String _tooltip;
		private String _rowDragText;
		// TODO dndSourceOnRowDrag
		private String _cellStyle;
		private String _cellClass;
		// TODO cellClassRules
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
		*/
		private String _aggFunc;
		private String _allowedAggFuncs;
		private int _rowGroupIndex = -1;
		private int _pivotIndex = -1;
		// TODO comparator(valueA, valueB, nodeA, nodeB, isInverted)
		// TODO pivotComparator(valueA, valueB)
		// TODO menuTabs
		// TODO suppressKeyboardEvent(params)
		// TODO chartDataType
		// Column Groups Only
		private String _groupId;
		// TODO headerGroupComponent, headerGroupComponentParams

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
