# ZK Aggrid
This is a community project for demonstrating how you can create a custom component using TypeScript. This component is not an official ZK component and is not supported by ZK.

## Prerequisite
* Java 8
* Maven 3

# Usage

pom.xml
```xml
<dependency>
	<groupId>org.zkoss.zkforge</groupId>
	<artifactId>aggrid</artifactId>
	<version>${aggrid.version}</version>
</dependency>
```

Car.java
```java
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
}
```

index.zul
```xml
<zk>
	<zscript><![CDATA[
	ListModelList m = new ListModelList();
	m.add(new Car("Toyota", "Celica", 35000));
	m.add(new Car("Ford", "Mondeo", 32000));
	m.add(new Car("Porsche", "Boxter", 72000));

	ListModel model = new org.zkoss.zkforge.aggrid.AgGridListModel(m);
	]]>
	</zscript>
	<ag-grid model="${model}" theme="ag-theme-material"
			 width="600px" height="600px" rowSelection="multiple" pagination="true">
		<ag-grid-default-column resizable="true" sortable="true" filter="true"/>
		<ag-grid-column headerName="#" width="60" checkboxSelection="true" pinned="left"/>
		<ag-grid-column headerName="Spec">
			<ag-grid-column field="make"/>
			<ag-grid-column field="model"/>
		</ag-grid-column>
		<ag-grid-column headerName="Price" field="price" filter="agNumberColumnFilter"/>
	</ag-grid>
</zk>
```

# Run test app
```
mvn jetty:run
```

Then open http://localhost:8080 in any browser.

# JavaScript/TypeScript linting
* Need `npm` or `yarn`
```
npm run lint
```
```
yarn lint
```

# Watch changes and start livereload server
* Need `npm` or `yarn`
* Need to "Run test app" first
```
npm run dev
```
```
yarn dev
```

Once TypeScript/JavaScript/LESS files being changed, the compiled files will be updated. The browser will be reloaded automatically to see the changes. It is convenient when developing a widget.

The default livereload proxy: http://localhost:3000/

# Make bundle package

```
mvn package
```

The result will be in the `target` folder.
