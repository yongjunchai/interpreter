package com.craftinginterpreters.lox;

public class AstPrinterPolishNotation implements  Expr.Visitor<String>{
    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return null;
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if(expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return null;
    }

    private String parenthesize(String name, Expr ...exprs) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        for(Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }

    private static Expr binaryExpr() {
        return new Expr.Binary(
                new Expr.Unary(new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(123)),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(new Expr.Literal(45.67)
                ));
    }

    private static Expr complexExpr() {
        return new Expr.Binary(
                new Expr.Binary(new Expr.Literal( "1"),
                     new Token(TokenType.AND, "+", null, 1),
                        new Expr.Literal("2")
                        ),



                new Token(TokenType.STAR, "*", null, 1),



                new Expr.Binary(new Expr.Literal("4"),
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal( "3")
                )
        );

    }

    public static void main(String[] args) {
        System.out.println(new AstPrinterPolishNotation().print(binaryExpr()));
        System.out.println(new AstPrinterPolishNotation().print(complexExpr()));
    }

}
