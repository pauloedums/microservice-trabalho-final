package br.com.impacta.microservices.ib.enums;

public enum FallbackClientMessages {
    
    ADD_PURCHASE("Erro ao gerar compra no cartão de crédito."),
    ADD_CREDIT_CARD("Erro ao gerar cartão de crédito."),
    GET_ALL_TESOURO_DIRETO("Nenhum tesouro direto cadastrado."),
    CLIENT_INVESTMENTS("O cliente com este cpf não está cadastrado, por favor checar os dados."),
    GET_ALL_INVESTMENTS("O banco ainda não possui nenhum investimento realizado."),
    GET_CREDIT_CARD_BY_NUMBER("Erro ao procurar o cartão de crédito."),
    GET_ALL("Erro ao procurar cartões de crédito.");

    private String description;

    public String getDescription() {
        return description;
    }

    FallbackClientMessages(String description) {
        this.description = description;
    }

}
