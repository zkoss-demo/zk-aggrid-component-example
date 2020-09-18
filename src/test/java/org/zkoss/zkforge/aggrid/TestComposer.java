/* TestComposer.java

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.zkoss.zk.ui.util.GenericComposer;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

/**
 * @author rudyhuang
 */
public class TestComposer extends GenericComposer<Aggrid<Winner>> {
	@Override
	public void doAfterCompose(Aggrid<Winner> comp) throws Exception {
		super.doAfterCompose(comp);

		Type listType = new TypeToken<List<Winner>>() {}.getType();
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
		List<Winner> winners = gson.fromJson(new InputStreamReader(getClass()
				.getResourceAsStream("/olympicWinnersSmall.json")), listType);

		ListModel<Winner> model = new AgGridListModel<>(new ListModelList<>(winners));
		comp.setModel(model);
	}
}
