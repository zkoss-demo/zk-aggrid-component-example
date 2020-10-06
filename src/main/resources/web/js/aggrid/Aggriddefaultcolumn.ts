/* Aggriddefaultcolumn.ts

	Purpose:
		
	Description:
		
	History:
		Tue Sep 29 16:28:36 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
// @ts-ignore
import {ColDef} from '@ag-grid-community/core';

(() => {
aggrid.Aggriddefaultcolumn = zk.$extends(aggrid.Aggridcolumn, {
	toColDef(): ColDef {
		return zk.copy({}, this._colDef);
	}
});
})();