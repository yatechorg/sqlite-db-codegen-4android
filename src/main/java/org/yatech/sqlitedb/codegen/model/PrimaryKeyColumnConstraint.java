package org.yatech.sqlitedb.codegen.model;

public class PrimaryKeyColumnConstraint extends ColumnConstraint {
	
	private OrderDirection orderDirection;
	private boolean autoIncrement = false;

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public OrderDirection getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(OrderDirection orderDirection) {
		this.orderDirection = orderDirection;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
	
	@Override
	public String toString() {
		String sDirection = orderDirection == null ? "" : " " + orderDirection;
		String sAuto = autoIncrement ? " AUTOINCREMENT" : ""; 
		return String.format("PRIMARY KEY%s%s", sDirection, sAuto);
	}
}
