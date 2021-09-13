package br.com.impacta.microservices.ib.enums;

public enum FallbackCreditCard {
    
    ADD_PURCHASE("Erro ao gerar compra no cartão de crédito."),
    ADD_CREDIT_CARD("Nenhum cliente cadastrado para adicionar ao cartão de crédito."),
    GET_ALL_PURCHASES("Você ainda não possui nenhuma compra."),
    GET_CREDIT_CARD_BY_NUMBER("Erro ao procurar o cartão de crédito."),
    GET_ALL("Nenhum cartão de crédito cadastrado.");

    private String description;

    public String getDescription() {
        return description;
    }

    FallbackCreditCard(String description) {
        this.description = description;
    }

}
