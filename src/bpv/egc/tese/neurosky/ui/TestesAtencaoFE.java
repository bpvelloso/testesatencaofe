/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpv.egc.tese.neurosky.ui;

/**
 *
 * @author velloso
 */
public class TestesAtencaoFE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestesAtencaoFE testesAtencaoFE = new TestesAtencaoFE();
        testesAtencaoFE.iniciaUI();
    }
    private NeuroSkyUI neuroSkyUI;

    public TestesAtencaoFE() {
        //init
    }

    private void iniciaUI() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(NeuroSkyUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                neuroSkyUI = new NeuroSkyUI();
                neuroSkyUI.setVisible(true);
            }
        });
        
    }
    
}
