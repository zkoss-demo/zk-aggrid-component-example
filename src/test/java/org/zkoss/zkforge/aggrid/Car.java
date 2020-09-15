/* Car.java

	Purpose:

	Description:

	History:
		Mon Jun 22 15:24:15 CST 2020, Created by rudyhuang

Copyright (C) 2020 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zkforge.aggrid;

import java.util.StringJoiner;

/**
 * @author rudyhuang
 */
public class Car {
	private String make;
	private String model;
	private int price;

	public Car() {
	}

	public Car(String make, String model, int price) {
		this.make = make;
		this.model = model;
		this.price = price;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Car.class.getSimpleName() + "[", "]")
				.add("make='" + make + "'")
				.add("model='" + model + "'")
				.add("price=" + price)
				.toString();
	}
}
