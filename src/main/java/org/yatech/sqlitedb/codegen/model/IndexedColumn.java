package org.yatech.sqlitedb.codegen.model;

public class IndexedColumn implements Visitable {
	
	private String columnName;
	private OrderDirection orderDirection;

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public OrderDirection getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(OrderDirection orderDirection) {
		this.orderDirection = orderDirection;
	}
	
	@Override
	public String toString() {
		return String.format("%s%s", columnName, orderDirection == null ? "" : " " + orderDirection);
	}
}
