/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpv.egc.tese.neurosky.ui;

import bpv.neurosky.data.entity.SubjectTest;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author velloso
 */
class ControladorTeste {
    private SubjectTest subjectTest;
    private NeuroSkyUI neuroSkyUI;
    private long tempo;
    private Thread backgroundTask;
    private boolean valido;

    /**
     * Controla a execução do teste
     * @param s @SubjectTest alvo
     * @param ui interface
     * @param t tempo de teste em minutos
     */
    ControladorTeste(SubjectTest s, NeuroSkyUI ui, Integer t) {
        this.subjectTest = s;
        this.neuroSkyUI = ui;
        //tempo de teste convertido de minutos para milisegundos
        this.tempo = t*60*1000;
        this.valido=true;
    }
    
    public void startTest(){
        subjectTest.setInicio(new Date());
        this.backgroundTask = new Thread(){
            @Override
            public void run(){
                long milis = (new Date()).getTime()-subjectTest.getInicio().getTime();
                while(milis<tempo && valido){
                    milis = (new Date()).getTime()-subjectTest.getInicio().getTime();
                    neuroSkyUI.setCronometro(milis);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ControladorTeste.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                subjectTest.setFim(new Date());
                neuroSkyUI.finalizaTeste();
            }
        };
        this.backgroundTask.start();
    }
    
    
    public void cancelarTeste(){
        this.valido=false;
    }
}
