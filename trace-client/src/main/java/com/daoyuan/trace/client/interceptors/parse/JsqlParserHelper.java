package com.daoyuan.trace.client.interceptors.parse;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.SelectUtils;

import java.util.List;

public class JsqlParserHelper {
	
	public static Select getSelect(Table table, List<Column> column, Expression whereExpression){
		Column[] selectColumns = (Column[]) column.toArray(new Column[column.size()]);
		Select select = SelectUtils.buildSelectFromTableAndExpressions(table, selectColumns);
		PlainSelect selectPlain = (PlainSelect) select.getSelectBody(); 
		selectPlain.setWhere(whereExpression);
		return select;
	}
	
}

