/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpv.egc.tese.neurosky.ui.dialogs;

import bpv.neurosky.data.type.SubjectTestException;

/**
 *
 * @author velloso
 */
public class MensagensDialog extends javax.swing.JDialog {

    

    /**
     * Creates new form MensagensDialog
     */
    public MensagensDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mensagemTf = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mensagemTf.setText("jLabel1");

        jButton1.setText("Fechar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mensagemTf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 294, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mensagemTf, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void mostraErro(final String msg) {

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                MensagensDialog dialog = new MensagensDialog(new javax.swing.JFrame(), true);
                dialog.setMensagem(msg);
                //TODO: Mostrar icone de erro;
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel mensagemTf;
    // End of variables declaration//GEN-END:variables

    private void setMensagem(String msg) {
        this.mensagemTf.setText(msg);
    }
    
    /**
     * Mostra lista de erros contidas em um @SubjectTestException
     * 
     * @param ex @SubjectTestException com erros
     */
    public static void mostraErro(SubjectTestException ex) {
        String errosMsg = "<html><body>Dados Inválidos:<br /><ul>";
        for(String msg: ex.getMensagens()){
             errosMsg+="<li>"+msg+"</li>";
        }
        errosMsg+="</ul></body></html>";
        MensagensDialog.mostraErro(errosMsg);
    }
}
