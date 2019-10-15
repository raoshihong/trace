package com.daoyuan.trace.client.interceptors.visitor;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

import java.util.List;

public class TraceExpressionDeParser extends ExpressionDeParser {

    //记录下标
    private List<Integer> indexs;

    public TraceExpressionDeParser(List<Integer> indexs) {
        this.indexs = indexs;
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        super.visit(equalsTo);
        processExpression(equalsTo.getLeftExpression(),equalsTo.getRightExpression());
    }

    @Override
    public void visit(InExpression inExpression) {
        super.visit(inExpression);
        ItemsList itemsList = inExpression.getRightItemsList();
        if (itemsList instanceof ExpressionList) {
            ExpressionList expressionList = (ExpressionList)itemsList;
            expressionList.getExpressions().stream().forEach(expression1 -> {
                if (expression1 instanceof JdbcParameter) {
                    addIndex((JdbcParameter)expression1);
                } else if (expression1 instanceof Function){

                }
            });
        }
    }

    private void processExpression(Expression columnExpression, Expression valueExpression){
        if (valueExpression instanceof JdbcParameter) {
            addIndex((JdbcParameter)valueExpression);
        } else if (valueExpression instanceof Function){

        }
    }

    private void addIndex(JdbcParameter jdbcParameter){
        indexs.add(jdbcParameter.getIndex());
    }
}
