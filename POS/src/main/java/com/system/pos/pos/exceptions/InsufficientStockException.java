package com.system.pos.pos.exceptions;

public class InsufficientStockException extends BusinessException {
    private final int availableQuantity;
    //Ao tentar realizar uma venda, se o produto não tiver estoque disponivel a exceção é lançada
    public InsufficientStockException(int availableQuantity) {
        super("Estoque insuficiente. Disponível: " + availableQuantity);
        this.availableQuantity = availableQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }
}