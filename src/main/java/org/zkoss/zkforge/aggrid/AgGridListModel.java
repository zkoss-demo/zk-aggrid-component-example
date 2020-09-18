/* AgGridListModel.java

	Purpose:

	Description:

	History:
		Wed Sep 16 18:07:51 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.zkoss.zkforge.aggrid.filter.Filter;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.event.ListDataListener;
import org.zkoss.zul.ext.Selectable;
import org.zkoss.zul.ext.SelectionControl;
import org.zkoss.zul.ext.Sortable;

/**
 * The ag-Grid ListModel, supports column sorting, filtering and item selection.
 *
 * @author rudyhuang
 * @param <E> the type of elements in this ListModel
 */
@SuppressWarnings("unchecked")
public class AgGridListModel<E> implements ListModel<E>, Sortable<E>, Filterable<E>,
		Selectable<E>, Serializable {
	private static final long serialVersionUID = 20200918150922L;

	private final ListModel<E> _model;
	private final Set<Filter<E>> _filters = new HashSet<>();
	private final List<E> _filteredItems = new ArrayList<>();
	private boolean _isFilterDirty;

	public AgGridListModel() {
		this(new ListModelList<>());
	}

	public AgGridListModel(ListModel<E> model) {
		if (!(model instanceof Sortable))
			throw new UnsupportedOperationException("model must implement Sortable interface");
		if (!(model instanceof Selectable))
			throw new UnsupportedOperationException("model must implement Selectable interface");
		this._model = model;
		model.addListDataListener(this::handleListDataEvent);
	}

	private void handleListDataEvent(ListDataEvent event) {
		switch (event.getType()) {
			case ListDataEvent.INTERVAL_ADDED:
			case ListDataEvent.INTERVAL_REMOVED:
			case ListDataEvent.CONTENTS_CHANGED:
				_isFilterDirty = true;
				break;
			default:
		}
	}

	private void updateFilteredResult() {
		if (_isFilterDirty) {
			_isFilterDirty = false;
			_filteredItems.clear();
			if (!_filters.isEmpty()) {
				for (int i = 0, size = _model.getSize(); i < size; i++) {
					_filteredItems.add(_model.getElementAt(i));
				}
				Stream<E> stream = _filteredItems.stream();
				for (Filter<E> f : _filters) {
					stream = stream.filter(f);
				}
				List<E> result = stream.collect(Collectors.toList());
				_filteredItems.clear();
				_filteredItems.addAll(result);
			}
		}
	}

	@Override
	public E getElementAt(int i) {
		updateFilteredResult();
		if (_filters.isEmpty())
			return _model.getElementAt(i);
		else
			return _filteredItems.get(i);
	}

	@Override
	public int getSize() {
		updateFilteredResult();
		if (_filters.isEmpty())
			return _model.getSize();
		else
			return _filteredItems.size();
	}

	@Override
	public void addListDataListener(ListDataListener listDataListener) {
		_model.addListDataListener(listDataListener);
	}

	@Override
	public void removeListDataListener(ListDataListener listDataListener) {
		_model.removeListDataListener(listDataListener);
	}

	@Override
	public boolean applyFilter(Filter<E> filter) {
		_isFilterDirty = true;
		return _filters.add(filter);
	}

	@Override
	public boolean removeFilter(Filter<E> filter) {
		_isFilterDirty = true;
		return _filters.remove(filter);
	}

	@Override
	public Collection<Filter<E>> getFilters() {
		return Collections.unmodifiableSet(_filters);
	}

	@Override
	public void removeAllFilters() {
		_isFilterDirty = false;
		_filters.clear();
		_filteredItems.clear();
	}

	@Override
	public void sort(Comparator<E> comparator, boolean ascending) {
		((Sortable<E>) _model).sort(comparator, ascending);
		_filteredItems.sort(comparator);
	}

	@Override
	public String getSortDirection(Comparator<E> comparator) {
		return ((Sortable<E>) _model).getSortDirection(comparator);
	}

	@Override
	public Set<E> getSelection() {
		return ((Selectable<E>) _model).getSelection();
	}

	@Override
	public void setSelection(Collection<? extends E> collection) {
		((Selectable<E>) _model).setSelection(collection);
	}

	@Override
	public boolean isSelected(Object o) {
		return ((Selectable<E>) _model).isSelected(o);
	}

	@Override
	public boolean isSelectionEmpty() {
		return ((Selectable<E>) _model).isSelectionEmpty();
	}

	@Override
	public boolean addToSelection(E e) {
		return ((Selectable<E>) _model).addToSelection(e);
	}

	@Override
	public boolean removeFromSelection(Object o) {
		return ((Selectable<E>) _model).removeFromSelection(o);
	}

	@Override
	public void clearSelection() {
		((Selectable<E>) _model).clearSelection();
	}

	@Override
	public void setMultiple(boolean b) {
		((Selectable<E>) _model).setMultiple(b);
	}

	@Override
	public boolean isMultiple() {
		return ((Selectable<E>) _model).isMultiple();
	}

	@Override
	public void setSelectionControl(SelectionControl selectionControl) {
		((Selectable<E>) _model).setSelectionControl(selectionControl);
	}

	@Override
	public SelectionControl<E> getSelectionControl() {
		return ((Selectable<E>) _model).getSelectionControl();
	}
}
