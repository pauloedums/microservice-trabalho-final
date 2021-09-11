package br.com.impacta.microservices.ib.enums;

public enum FallbackMessages {
    
    ADD_INVESTMENT("Sem balanço para realizar investimentos. Adicione créditos em sua conta"),
    GET_TESOURO_DIRETO("Não existe nenhum tesouro direto com este código."),
    GET_INVESTIMENTS("Não existe nenhum investimento disponível."),
    GET_ALL_TESOURO_DIRETO("Não foi possível encontrar a lista do Tesouro Direto."),
    CREATE_TESOURO_DIRETO("Não foi possível criar nenhum Tesouro Direto.");
    
    private String descricao;
    
    FallbackMessages(String descricao){
        this.descricao = descricao;
    }

    public String getDescription() {
        return descricao;
    }
}
