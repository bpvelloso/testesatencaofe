/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpv.egc.tese.neurosky.ui;

import bpv.egc.tese.neurosky.ui.tipos.EstadoEnum;
import bpv.egc.tese.neurosky.ui.dialogs.MensagensDialog;
import bpv.neurosky.connector.ThinkGearListener;
import bpv.neurosky.connector.ThinkGearSocket;
import bpv.neurosky.data.entity.SubjectTest;
import bpv.neurosky.data.entity.medicao.MedicaoAtencao;
import bpv.neurosky.data.entity.medicao.MedicaoMeditacao;
import bpv.neurosky.data.entity.medicao.MedicaoOnda;
import bpv.neurosky.data.entity.medicao.MedicaoPiscada;
import bpv.neurosky.data.entity.medicao.MedicaoRaw;
import bpv.neurosky.data.entity.medicao.MedicaoSinal;
import java.net.ConnectException;

/**
 *
 * @author velloso
 */
public class ControladorNeuroSky implements ThinkGearListener{
    
    private SubjectTest subjectTest;
    private ThinkGearSocket conexao;
    private NeuroSkyUI ui;
    
    private EstadoEnum estadoAtual;

    public ControladorNeuroSky(SubjectTest subjectTest, NeuroSkyUI ui) {
        this.ui = ui;
        this.subjectTest = subjectTest;
        this.alterarEstadoAtual(EstadoEnum.INICIALIZADO);        
    }

    private ControladorNeuroSky() {
    }
    
    

    @Override
    public void attentionEvent(int valor) {
        if(this.gravando()){
            this.subjectTest.getDadosAtencao().add(new MedicaoAtencao(valor));
        }
        this.ui.addMensagemDebug("Recebido Valor de Atenção: "+valor);
        this.ui.setStatusValorAtencao(valor);
    }

    @Override
    public void meditationEvent(int valor) {
        if(this.gravando()){
            this.subjectTest.getDadosMeditacao().add(new MedicaoMeditacao(valor));
        }
        this.ui.addMensagemDebug("Recebido Valor de Meditação: "+valor);
        this.ui.setStatusValorMeditacao(valor);
    }

    @Override
    public void poorSignalEvent(int valor) {
        if(this.gravando()){
            this.subjectTest.getDadosSinal().add(new MedicaoSinal(valor));
        }
        ui.setNivelSinal(valor);
        this.ui.addMensagemDebug("Recebido Valor de Sinal: "+valor);
    }

    @Override
    public void blinkEvent(int valor) {
        if(this.gravando()){
            this.subjectTest.getDadosPiscada().add(new MedicaoPiscada(valor));
        }
        this.ui.addMensagemDebug("Recebido Valor de Piscada: "+valor);
    }

    @Override
    public void eegEvent(int delta, int theta, int lowAlpha, int highAlpha, int lowBeta, int highBeta, int lowGamma, int highGamma) {
        if(this.gravando()){
            this.subjectTest.getDadosDelta().add(new MedicaoOnda(delta));
            this.subjectTest.getDadosTheta().add(new MedicaoOnda(theta));
            this.subjectTest.getDadosLowAlpha().add(new MedicaoOnda(lowAlpha));
            this.subjectTest.getDadosHighAlpha().add(new MedicaoOnda(highAlpha));
            this.subjectTest.getDadosLowBeta().add(new MedicaoOnda(lowBeta));
            this.subjectTest.getDadosHighBeta().add(new MedicaoOnda(highBeta));
            this.subjectTest.getDadosLowGama().add(new MedicaoOnda(lowGamma));
            this.subjectTest.getDadosHighGama().add(new MedicaoOnda(highGamma));
        }
        this.ui.addMensagemDebug("Recebido Valor de Ondas: "+delta+", "+theta+", "+lowAlpha+", "+highAlpha+", "+lowBeta+", "+highBeta+", "+lowGamma+", "+highGamma);
        this.ui.setStatusValorDelta(delta);
        this.ui.setStatusValorTheta(theta);
        this.ui.setStatusValorLowAlpha(lowAlpha);
        this.ui.setStatusValorHighAlpha(highAlpha);
        this.ui.setStatusValorLowBeta(lowBeta);
        this.ui.setStatusValorHighBeta(highBeta);
        this.ui.setStatusValorLowGamma(lowGamma);
        this.ui.setStatusValorHighGamma(highGamma);
    }

    @Override
    public void rawEvent(int[] valores) {
        if(this.gravando()){
            for(int i: valores){
                this.subjectTest.getDadosRaw().add(new MedicaoRaw(new Integer(i)));
            }
        }
        this.ui.addMensagemDebug("Recebido Valores Raw: "+valores.length);
    }
    
    public void iniciarConectorSocket() {
        conexao = new ThinkGearSocket(this);
        try {
                conexao.start();
                this.ui.addMensagemInfo("Conexão com ThinkGear efetuada...");
        } catch (ConnectException e) {
                conexao.stop();
                MensagensDialog.mostraErro(e.getMessage());
        }
        this.alterarEstadoAtual(EstadoEnum.CONECTADO);
    }

    void desconectar() {
        conexao.stop();
        this.ui.addMensagemInfo("Desconexão com ThinkGear efetuada...");
        this.alterarEstadoAtual(EstadoEnum.PRONTO);
    }

    public EstadoEnum getEstadoAtual() {
        return estadoAtual;
    }

    public void setEstadoAtual(EstadoEnum estadoAtual) {
        this.estadoAtual = estadoAtual;
    }

    public void alterarEstadoAtual(EstadoEnum estado) {
        switch(estado){
           //TODO: Implementar maquina de estado
            default:{
                    this.estadoAtual = estado;
                }
        }
        this.ui.setMensagemEstadoAtual(this.estadoAtual.getDescricao());
        this.ui.addMensagemInfo("Mudança de Estado da Aplicação para: "+this.estadoAtual.getDescricao());
    }

    private boolean gravando() {
        return this.estadoAtual.equals(EstadoEnum.GRAVANDO);
    }

    public SubjectTest getSubjectTest() {
        return subjectTest;
    }

    public void setSubjectTest(SubjectTest subjectTest) {
        this.subjectTest = subjectTest;
    }

    public boolean isConectado() {
        return (this.estadoAtual.equals(EstadoEnum.CONECTADO) && conexao.isRunning());
    }
}
