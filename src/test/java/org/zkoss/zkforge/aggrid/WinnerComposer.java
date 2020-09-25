/* WinnerComposer.java

	Purpose:

	Description:

	History:
		Fri Sep 18 15:59:33 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

/**
 * @author rudyhuang
 */
public class WinnerComposer extends SelectorComposer<Aggrid<Winner>> {
	private Aggrid<Winner> comp;
	private ListModelList<Winner> winnerModel;

	@Override
	public void doAfterCompose(Aggrid<Winner> comp) throws Exception {
		super.doAfterCompose(comp);
		this.comp = comp;

		Type listType = new TypeToken<List<Winner>>() {}.getType();
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
		List<Winner> winners = gson.fromJson(new InputStreamReader(getClass()
				.getResourceAsStream("/olympicWinnersSmall.json")), listType);

		winnerModel = new ListModelList<>(winners);

		ListModel<Winner> model = new AgGridListModel<>(winnerModel);
		comp.setModel(model);
		winnerModel.setMultiple(true);
		winnerModel.addToSelection(winnerModel.getElementAt(0));
		winnerModel.addToSelection(winnerModel.getElementAt(100));
		winnerModel.addToSelection(winnerModel.getElementAt(200));
	}

	@Listen("onClick = #btnCurrSel")
	public void handleModelCurrentSelection() {
		Clients.log(winnerModel.getSelection()
				.stream()
				.map(Winner::toString)
				.collect(Collectors.joining(", ")));
	}

	@Listen("onClick = #btnAddSel")
	public void handleModelAddSelect() {
		winnerModel.addToSelection(winnerModel.getElementAt(1));
	}

	@Listen("onClick = #btnAllSel")
	public void handleModelAllSelect() {
		winnerModel.getSelectionControl().setSelectAll(true);
	}

	@Listen("onClick = #btnClrSel")
	public void handleModelClearSelect() {
		winnerModel.getSelectionControl().setSelectAll(false);
	}

	@Listen("onClick = #btnModelNew")
	public void handleModelNew() {
		ListModel<Winner> model = new AgGridListModel<>(winnerModel);
		comp.setModel(model);
	}

	@Listen("onClick = #btnModelNull")
	public void handleModelNull() {
		comp.setModel(null);
	}

	@Listen("onClick = #btnExport")
	public void handleExport() {
		comp.exportDataAsCsv();
	}

	@Listen("onCellValueChanged = #ag")
	public void inlineEditAge(AgGridEvent<Winner> event) {
		event.getNode().setAge(Integer.parseInt((String) event.get("newValue")));
		Notification.show("Modified");
	}
}
