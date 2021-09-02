package br.com.impacta.microservices.ib.enums;

public enum FallbackCreditCard {
    
    ADD_PURCHASE("Erro ao gerar compra no cartão de crédito."),
    ADD_CREDIT_CARD("Erro ao gerar cartão de crédito."),
    GET_ALL_PURCHASES("Erro ao procurar todas as compras."),
    GET_CREDIT_CARD_BY_NUMBER("Erro ao procurar o cartão de crédito."),
    GET_ALL("Erro ao procurar cartões de crédito.");

    private String description;

    public String getDescription() {
        return description;
    }

    FallbackCreditCard(String description) {
        this.description = description;
    }

    
}
