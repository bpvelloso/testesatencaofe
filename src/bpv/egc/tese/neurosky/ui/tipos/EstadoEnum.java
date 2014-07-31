/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpv.egc.tese.neurosky.ui.tipos;

/**
 *
 * @author velloso
 */
public enum EstadoEnum {
    /**
     * Criação Correta do Ambiente.
     */
    INICIALIZADO("Inicialização ok."),
    /**
     * Conexão efetuado com sucesso.
     */
    CONECTADO("Conectado ao ThinkGear"),
    /**
     *  Coletando Dados na memória, a cenexão deve ter sucesso.
     */
    GRAVANDO("Gravando Dados"),
    /**
     * Dados prontos para serem gravado sem disco, o sistema deve estar desconectado.
     */
    PRONTO("Dados Coletados - Desconectado"),
    /**
     * Arquivo salvo com os dados e objeto disponível para análise.
     */
    ARQUIVO("Visualizando dados - Arquivo de Dados Salvo");
    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    private EstadoEnum(String desc) {
        this.descricao = desc;
    }
    
    
}
