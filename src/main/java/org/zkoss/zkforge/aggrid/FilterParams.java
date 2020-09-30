/* FilterParams.java

	Purpose:
		
	Description:
		
	History:
		Wed Sep 30 10:51:43 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid;

import java.util.Collection;
import java.util.Collections;

import org.zkoss.json.JSONObject;

/**
 * @author rudyhuang
 */
@SuppressWarnings("unchecked")
public class FilterParams extends JSONObject {
	public Collection<String> getButtons() {
		return (Collection<String>) getOrDefault("buttons", Collections.emptyList());
	}

	public void setButtons(Collection<String> buttons) {
		put("buttons", buttons);
	}

	public boolean isCloseOnApply() {
		return (boolean) getOrDefault("closeOnApply", false);
	}

	public void setCloseOnApply(boolean closeOnApply) {
		put("closeOnApply", closeOnApply);
	}

	public int getDebounceMs() {
		return (int) getOrDefault("debounceMs", 500);
	}

	public void setDebounceMs(int debounceMs) {
		put("debounceMs", debounceMs);
	}

	public boolean isAlwaysShowBothConditions() {
		return (boolean) getOrDefault("alwaysShowBothConditions", false);
	}

	public void setAlwaysShowBothConditions(boolean alwaysShowBothConditions) {
		put("alwaysShowBothConditions", alwaysShowBothConditions);
	}

	public Collection<String> getFilterOptions() {
		return (Collection<String>) getOrDefault("filterOptions", Collections.emptyList());
	}

	public void setFilterOptions(Collection<String> filterOptions) {
		put("filterOptions", filterOptions);
	}

	public String getDefaultOption() {
		return (String) getOrDefault("defaultOption", "");
	}

	public void setDefaultOption(String defaultOption) {
		put("defaultOption", defaultOption);
	}

	public String getDefaultJoinOperator() {
		return (String) getOrDefault("defaultJoinOperator", "AND");
	}

	public void setDefaultJoinOperator(String defaultJoinOperator) {
		put("defaultJoinOperator", defaultJoinOperator);
	}

	public boolean isSuppressAndOrCondition() {
		return (boolean) getOrDefault("suppressAndOrCondition", false);
	}

	public void setSuppressAndOrCondition(boolean suppressAndOrCondition) {
		put("suppressAndOrCondition", suppressAndOrCondition);
	}

	// TODO textCustomComparator

	public boolean isCaseSensitive() {
		return (boolean) getOrDefault("caseSensitive", false);
	}

	public void setCaseSensitive(boolean caseSensitive) {
		put("caseSensitive", caseSensitive);
	}

	// TODO textFormatter

	public boolean isInRangeInclusive() {
		return (boolean) getOrDefault("inRangeInclusive", false);
	}

	public void setInRangeInclusive(boolean inRangeInclusive) {
		put("inRangeInclusive", inRangeInclusive);
	}

	public boolean isIncludeBlanksInEquals() {
		return (boolean) getOrDefault("includeBlanksInEquals", false);
	}

	public void setIncludeBlanksInEquals(boolean includeBlanksInEquals) {
		put("includeBlanksInEquals", includeBlanksInEquals);
	}

	public boolean isIncludeBlanksInLessThan() {
		return (boolean) getOrDefault("includeBlanksInLessThan", false);
	}

	public void setIncludeBlanksInLessThan(boolean includeBlanksInLessThan) {
		put("includeBlanksInLessThan", includeBlanksInLessThan);
	}

	public boolean isIncludeBlanksInGreaterThan() {
		return (boolean) getOrDefault("includeBlanksInGreaterThan", false);
	}

	public void setIncludeBlanksInGreaterThan(boolean includeBlanksInGreaterThan) {
		put("includeBlanksInGreaterThan", includeBlanksInGreaterThan);
	}

	// TODO allowedCharPattern
	// TODO numberParser
	// TODO comparator

	public boolean isBrowserDatePicker() {
		return (boolean) getOrDefault("browserDatePicker", true);
	}

	public void setBrowserDatePicker(boolean browserDatePicker) {
		put("browserDatePicker", browserDatePicker);
	}
}
