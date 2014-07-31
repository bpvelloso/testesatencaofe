/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpv.egc.tese.neurosky.ui;

import bpv.egc.tese.neurosky.ui.dialogs.MensagensDialog;
import bpv.egc.tese.neurosky.ui.dialogs.ObservacoesDialog;
import bpv.egc.tese.neurosky.ui.dialogs.SobreDialog;
import bpv.egc.tese.neurosky.ui.tipos.EstadoEnum;
import bpv.neurosky.data.entity.HyperMedia;
import bpv.neurosky.data.entity.SubjectTest;
import bpv.neurosky.data.entity.config.ConfigHandler;
import bpv.neurosky.data.entity.medicao.MedicaoAtencao;
import bpv.neurosky.data.entity.medicao.MedicaoMeditacao;
import bpv.neurosky.data.entity.medicao.MedicaoSinal;
import bpv.neurosky.data.type.SerieEnum;
import bpv.neurosky.data.type.SexoEnum;
import bpv.neurosky.data.type.SubjectTestException;
import bpv.neurosky.data.util.DataConverter;
import bpv.neurosky.xml.SubjectTestXMLReader;
import bpv.neurosky.xml.SubjectTestXMLWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ExtensionFileFilter;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author velloso
 */
public class NeuroSkyUI extends javax.swing.JFrame {
    private String tipoDeExecucao = "DEBUG";
    private SubjectTest subjectTest;
    private ControladorNeuroSky uiControler;
   
    private Integer statusValorAtencao;
    private Integer statusValorMeditacao;
    private Integer statusValorDelta;
    private Integer statusValorTheta;
    private Integer statusValorLowAlpha;
    private Integer statusValorHighAlpha;
    private Integer statusValorLowBeta;
    private Integer statusValorHighBeta;
    private Integer statusValorLowGamma;
    private Integer statusValorHighGamma;

    private ControladorTeste controladorTeste;
    private DefaultCategoryDataset datasetGraficoAtencao = new DefaultCategoryDataset();
    private DefaultCategoryDataset datasetGraficoAtencaoExportacao = new DefaultCategoryDataset();
    private DefaultCategoryDataset datasetStatusBar = new DefaultCategoryDataset();
    private JFreeChart graficoAtencao;
    private JFreeChart graficoAtencaoExportacao;
    private JFreeChart chart;
    private SubjectTest subjectTestExportar;

    public Integer getStatusValorDelta() {
        return statusValorDelta;
    }

    public void setStatusValorDelta(Integer statusValorDelta) {
        this.statusValorDelta = statusValorDelta;
        datasetStatusBar.setValue(this.reducaoLinearSinaisOndas(this.statusValorDelta), "Valor", "Delta");
    }

    public Integer getStatusValorTheta() {
        return statusValorTheta;
    }

    public void setStatusValorTheta(Integer statusValorTheta) {
        this.statusValorTheta = statusValorTheta;
        datasetStatusBar.setValue(this.reducaoLinearSinaisOndas(this.statusValorTheta), "Valor", "Theta");
    }

    public Integer getStatusValorLowAlpha() {
        return statusValorLowAlpha;
    }

    public void setStatusValorLowAlpha(Integer statusValorLowAlpha) {
        this.statusValorLowAlpha = statusValorLowAlpha;
        datasetStatusBar.setValue(this.reducaoLinearSinaisOndas(this.statusValorLowAlpha), "Valor", "LowAlpha");
    }

    public Integer getStatusValorHighAlpha() {
        return statusValorHighAlpha;
    }

    public void setStatusValorHighAlpha(Integer statusValorHighAlpha) {
        this.statusValorHighAlpha = statusValorHighAlpha;
        datasetStatusBar.setValue(this.reducaoLinearSinaisOndas(this.statusValorHighAlpha), "Valor", "HighAlpha");
    }

    public Integer getStatusValorLowBeta() {
        return statusValorLowBeta;
    }

    public void setStatusValorLowBeta(Integer statusValorLowBeta) {
        this.statusValorLowBeta = statusValorLowBeta;
        datasetStatusBar.setValue(this.reducaoLinearSinaisOndas(this.statusValorLowBeta), "Valor", "LowBeta");
    }

    public Integer getStatusValorHighBeta() {
        return statusValorHighBeta;
    }

    public void setStatusValorHighBeta(Integer statusValorHighBeta) {
        this.statusValorHighBeta = statusValorHighBeta;
        datasetStatusBar.setValue(this.reducaoLinearSinaisOndas(this.statusValorHighBeta), "Valor", "HighBeta");
    }

    public Integer getStatusValorLowGamma() {
        return statusValorLowGamma;
    }

    public void setStatusValorLowGamma(Integer statusValorLowGamma) {
        this.statusValorLowGamma = statusValorLowGamma;
        datasetStatusBar.setValue(this.reducaoLinearSinaisOndas(this.statusValorLowGamma), "Valor", "LowGamma");
    }

    public Integer getStatusValorHighGamma() {
        return statusValorHighGamma;
    }

    public void setStatusValorHighGamma(Integer statusValorHighGamma) {
        this.statusValorHighGamma = statusValorHighGamma;
        datasetStatusBar.setValue(this.reducaoLinearSinaisOndas(this.statusValorHighGamma), "Valor", "HighGamma");
    }

    public Integer getStatusValorAtencao() {
        return statusValorAtencao;
    }

    public void setStatusValorAtencao(Integer statusValorAtencao) {
        this.statusValorAtencao = statusValorAtencao;
        datasetStatusBar.setValue(this.statusValorAtencao, "Valor", "Atenção");
        if( this.uiControler.getEstadoAtual().equals(EstadoEnum.GRAVANDO)){
            long ordenada = (new Date()).getTime() - subjectTest.getInicio().getTime();
            ordenada/=1000;
            datasetGraficoAtencao.addValue(this.statusValorAtencao, "Atenção", ordenada+"");
        }
    }

    public Integer getStatusValorMeditacao() {
        return statusValorMeditacao;
    }

    public void setStatusValorMeditacao(Integer statusValorMeditacao) {
        this.statusValorMeditacao = statusValorMeditacao;
        datasetStatusBar.setValue(this.statusValorMeditacao, "Valor", "Meditação");
    }

    /**
     * Creates new form NeuroSkyUI
     */
    public NeuroSkyUI() {
        initComponents();
        initComboBoxes();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barraDeStatus = new javax.swing.JPanel();
        barraDeSinal = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        mensagemEstadoAtual = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        consoleStatus = new javax.swing.JTextArea();
        painelPrincipal = new javax.swing.JPanel();
        abas = new javax.swing.JTabbedPane();
        status = new javax.swing.JPanel();
        conectarBt = new javax.swing.JButton();
        desconectarBt = new javax.swing.JButton();
        graficoStatusPanel = new javax.swing.JPanel();
        gravarDadosBt = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        nomeTf = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        sexoCb = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        dataNascimentoCb = new org.jdesktop.swingx.JXDatePicker();
        jLabel6 = new javax.swing.JLabel();
        serieCb = new javax.swing.JComboBox();
        conceitoEscolarCb = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        numeroTentativaCb = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        hiperMidiaCb = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tempoTesteCb = new javax.swing.JSpinner();
        iniciarTesteBt = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        graficoAtencaoPanel = new javax.swing.JPanel();
        cronometroLabel = new javax.swing.JLabel();
        cancelarTesteBt = new javax.swing.JButton();
        salvarArquivoBt = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        graficoSinaisExportar = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        resumoTesteTA = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Teste de Atenção");

        barraDeStatus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        barraDeStatus.setPreferredSize(new java.awt.Dimension(400, 30));

        barraDeSinal.setPreferredSize(new java.awt.Dimension(148, 18));

        jLabel1.setText("Sinal:");

        mensagemEstadoAtual.setText("  ");

        javax.swing.GroupLayout barraDeStatusLayout = new javax.swing.GroupLayout(barraDeStatus);
        barraDeStatus.setLayout(barraDeStatusLayout);
        barraDeStatusLayout.setHorizontalGroup(
            barraDeStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, barraDeStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mensagemEstadoAtual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraDeSinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3))
        );
        barraDeStatusLayout.setVerticalGroup(
            barraDeStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, barraDeStatusLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(barraDeStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(barraDeStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(mensagemEstadoAtual))
                    .addComponent(barraDeSinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jSplitPane1.setDividerLocation(450);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        consoleStatus.setEditable(false);
        consoleStatus.setColumns(20);
        consoleStatus.setRows(5);
        consoleStatus.setWrapStyleWord(true);
        jScrollPane1.setViewportView(consoleStatus);

        jSplitPane1.setBottomComponent(jScrollPane1);

        conectarBt.setText("Conectar");
        conectarBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conectarBtActionPerformed(evt);
            }
        });

        desconectarBt.setText("Desconectar");
        desconectarBt.setEnabled(false);
        desconectarBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                desconectarBtActionPerformed(evt);
            }
        });

        graficoStatusPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                graficoStatusPanelComponentResized(evt);
            }
        });

        javax.swing.GroupLayout graficoStatusPanelLayout = new javax.swing.GroupLayout(graficoStatusPanel);
        graficoStatusPanel.setLayout(graficoStatusPanelLayout);
        graficoStatusPanelLayout.setHorizontalGroup(
            graficoStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 547, Short.MAX_VALUE)
        );
        graficoStatusPanelLayout.setVerticalGroup(
            graficoStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 370, Short.MAX_VALUE)
        );

        gravarDadosBt.setText("Gravar");
        gravarDadosBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gravarDadosBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout statusLayout = new javax.swing.GroupLayout(status);
        status.setLayout(statusLayout);
        statusLayout.setHorizontalGroup(
            statusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusLayout.createSequentialGroup()
                        .addGap(0, 304, Short.MAX_VALUE)
                        .addComponent(gravarDadosBt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(conectarBt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(desconectarBt))
                    .addComponent(graficoStatusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        statusLayout.setVerticalGroup(
            statusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(graficoStatusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(conectarBt)
                    .addComponent(desconectarBt)
                    .addComponent(gravarDadosBt))
                .addContainerGap())
        );

        abas.addTab("Status", status);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados do Sujeito de Teste"));

        jLabel2.setText("Nome:");

        jLabel3.setText("Sexo:");

        sexoCb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Masculino", "Feminino" }));

        jLabel4.setText("Curso:");

        jLabel5.setText("Data de Nascimento:");

        jLabel6.setText("Conceito do Sujeito:");

        serieCb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        conceitoEscolarCb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10", "9", "8", "7", "6", "5", "4", "3", "2", "1", "0" }));
        conceitoEscolarCb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conceitoEscolarCbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nomeTf))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sexoCb, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(serieCb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dataNascimentoCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(conceitoEscolarCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 163, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nomeTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sexoCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(serieCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataNascimentoCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(conceitoEscolarCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados do Teste"));

        jLabel7.setText("Número da tentetiva:");

        numeroTentativaCb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        numeroTentativaCb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numeroTentativaCbActionPerformed(evt);
            }
        });

        jLabel8.setText("Objeto de Aprendizagem: ");

        hiperMidiaCb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setText("Tempo de Teste: ");

        jLabel11.setText("minutos");

        tempoTesteCb.setValue(5);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hiperMidiaCb, 0, 389, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(numeroTentativaCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(6, 6, 6)
                                .addComponent(tempoTesteCb, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(numeroTentativaCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(hiperMidiaCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(tempoTesteCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        iniciarTesteBt.setText("Iniciar Teste");
        iniciarTesteBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iniciarTesteBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(iniciarTesteBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(iniciarTesteBt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );

        abas.addTab("Teste de Atenção", jPanel2);

        graficoAtencaoPanel.setAlignmentX(0.0F);
        graficoAtencaoPanel.setAlignmentY(0.0F);
        graficoAtencaoPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                graficoAtencaoPanelComponentResized(evt);
            }
        });

        javax.swing.GroupLayout graficoAtencaoPanelLayout = new javax.swing.GroupLayout(graficoAtencaoPanel);
        graficoAtencaoPanel.setLayout(graficoAtencaoPanelLayout);
        graficoAtencaoPanelLayout.setHorizontalGroup(
            graficoAtencaoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        graficoAtencaoPanelLayout.setVerticalGroup(
            graficoAtencaoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 236, Short.MAX_VALUE)
        );

        cronometroLabel.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        cronometroLabel.setForeground(new java.awt.Color(0, 0, 255));
        cronometroLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cronometroLabel.setText("00:00.000");
        cronometroLabel.setBorder(javax.swing.BorderFactory.createTitledBorder("Duração do Teste"));

        cancelarTesteBt.setText("Cancelar Teste");
        cancelarTesteBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarTesteBtActionPerformed(evt);
            }
        });

        salvarArquivoBt.setText("Salvar dados do Teste");
        salvarArquivoBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvarArquivoBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 301, Short.MAX_VALUE)
                        .addComponent(salvarArquivoBt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelarTesteBt))
                    .addComponent(cronometroLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
                    .addComponent(graficoAtencaoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(graficoAtencaoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cronometroLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(salvarArquivoBt)
                    .addComponent(cancelarTesteBt))
                .addGap(33, 33, 33))
        );

        abas.addTab("Andamento do Teste", jPanel3);

        graficoSinaisExportar.setAlignmentX(0.0F);
        graficoSinaisExportar.setAlignmentY(0.0F);
        graficoSinaisExportar.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                graficoSinaisExportarComponentResized(evt);
            }
        });

        javax.swing.GroupLayout graficoSinaisExportarLayout = new javax.swing.GroupLayout(graficoSinaisExportar);
        graficoSinaisExportar.setLayout(graficoSinaisExportarLayout);
        graficoSinaisExportarLayout.setHorizontalGroup(
            graficoSinaisExportarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 551, Short.MAX_VALUE)
        );
        graficoSinaisExportarLayout.setVerticalGroup(
            graficoSinaisExportarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados do Arquivo"));

        resumoTesteTA.setEditable(false);
        resumoTesteTA.setColumns(20);
        resumoTesteTA.setLineWrap(true);
        resumoTesteTA.setRows(5);
        jScrollPane2.setViewportView(resumoTesteTA);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
        );

        jButton1.setText("Carregar Arquivo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Exportar para CSV");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Exportar arquivos em Lote para CSV");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(graficoSinaisExportar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(graficoSinaisExportar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        abas.addTab("Exportar Dados", jPanel5);

        javax.swing.GroupLayout painelPrincipalLayout = new javax.swing.GroupLayout(painelPrincipal);
        painelPrincipal.setLayout(painelPrincipalLayout);
        painelPrincipalLayout.setHorizontalGroup(
            painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(abas)
        );
        painelPrincipalLayout.setVerticalGroup(
            painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(abas)
        );

        jSplitPane1.setLeftComponent(painelPrincipal);

        jMenu1.setText("Arquivo");

        jMenuItem1.setText("Sair");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ajuda");

        jMenuItem2.setText("Sobre");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barraDeStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraDeStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void conectarBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conectarBtActionPerformed
        
        
        this.conectar();
        
        this.conectarBt.setEnabled(false);
        this.desconectarBt.setEnabled(true);   
    }//GEN-LAST:event_conectarBtActionPerformed

    private void desconectarBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_desconectarBtActionPerformed
        this.desconectarThinkGear();
        this.conectarBt.setEnabled(true);
        this.desconectarBt.setEnabled(false);
    }//GEN-LAST:event_desconectarBtActionPerformed

    private void graficoStatusPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_graficoStatusPanelComponentResized
        ChartPanel cp = new ChartPanel(chart);
        cp.setBounds(graficoStatusPanel.getBounds());
        graficoStatusPanel.removeAll();
        graficoStatusPanel.add(cp);
    }//GEN-LAST:event_graficoStatusPanelComponentResized

    private void gravarDadosBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gravarDadosBtActionPerformed
        this.uiControler.alterarEstadoAtual(EstadoEnum.GRAVANDO);
    }//GEN-LAST:event_gravarDadosBtActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        this.mostrarDialogSobre();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void salvarArquivoBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salvarArquivoBtActionPerformed
        this.mostrarDialogObservacoes();
    }//GEN-LAST:event_salvarArquivoBtActionPerformed

    private void numeroTentativaCbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numeroTentativaCbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numeroTentativaCbActionPerformed

    private void iniciarTesteBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniciarTesteBtActionPerformed
        this.validarECarregarSubjectTest();
    }//GEN-LAST:event_iniciarTesteBtActionPerformed

    private void conceitoEscolarCbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conceitoEscolarCbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_conceitoEscolarCbActionPerformed

    private void graficoAtencaoPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_graficoAtencaoPanelComponentResized
        ChartPanel cp = new ChartPanel(graficoAtencao);
        cp.setBounds(graficoAtencaoPanel.getBounds());
        graficoAtencaoPanel.removeAll();
        graficoAtencaoPanel.add(cp);
    }//GEN-LAST:event_graficoAtencaoPanelComponentResized

    private void cancelarTesteBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarTesteBtActionPerformed
        this.uiControler.desconectar();
        this.controladorTeste.cancelarTeste();
    }//GEN-LAST:event_cancelarTesteBtActionPerformed

    private void graficoSinaisExportarComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_graficoSinaisExportarComponentResized
        ChartPanel cp = new ChartPanel(graficoAtencaoExportacao);
        cp.setBounds(graficoSinaisExportar.getBounds());
        graficoSinaisExportar.removeAll();
        graficoSinaisExportar.add(cp);
    }//GEN-LAST:event_graficoSinaisExportarComponentResized

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.carregarArquivoExportacao();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.exportarDadosCsv();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.exportarLoteCsv();
    }//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane abas;
    private javax.swing.JProgressBar barraDeSinal;
    private javax.swing.JPanel barraDeStatus;
    private javax.swing.JButton cancelarTesteBt;
    private javax.swing.JComboBox conceitoEscolarCb;
    private javax.swing.JButton conectarBt;
    private javax.swing.JTextArea consoleStatus;
    private javax.swing.JLabel cronometroLabel;
    private org.jdesktop.swingx.JXDatePicker dataNascimentoCb;
    private javax.swing.JButton desconectarBt;
    private javax.swing.JPanel graficoAtencaoPanel;
    private javax.swing.JPanel graficoSinaisExportar;
    private javax.swing.JPanel graficoStatusPanel;
    private javax.swing.JButton gravarDadosBt;
    private javax.swing.JComboBox hiperMidiaCb;
    private javax.swing.JButton iniciarTesteBt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel mensagemEstadoAtual;
    private javax.swing.JTextField nomeTf;
    private javax.swing.JComboBox numeroTentativaCb;
    private javax.swing.JPanel painelPrincipal;
    private javax.swing.JTextArea resumoTesteTA;
    private javax.swing.JButton salvarArquivoBt;
    private javax.swing.JComboBox serieCb;
    private javax.swing.JComboBox sexoCb;
    private javax.swing.JPanel status;
    private javax.swing.JSpinner tempoTesteCb;
    // End of variables declaration//GEN-END:variables

    void setNivelSinal(int valor) {
        this.barraDeSinal.setValue(100-(valor>100?100:valor));
    }

    void addMensagemDebug(String msg) {
        if(this.tipoDeExecucao.equals("DEBUG")){
            this.addMensagem(msg, "DEBUG");
        }
    }

    void addMensagemInfo(String msg) {
        this.addMensagem(msg, "INFO");
    }

    private void addMensagem(String msg, String tipo){
        this.consoleStatus.append("\n"+tipo+": "+(new Date())+": "+msg);
        this.consoleStatus.setCaretPosition(this.consoleStatus.getText().length());
    }
    private void validarECarregarSubjectTest() {
        if((this.uiControler==null) || (!this.uiControler.isConectado())){
            this.conectar();
        }
        
        try {
            this.validarDadosSujeito();
            this.carregaDadosSujeito();
        } catch (SubjectTestException ex) {
            MensagensDialog.mostraErro(ex); 
            return;
        }
        Integer tempoTeste = (Integer) tempoTesteCb.getValue();
        this.controladorTeste = new ControladorTeste(subjectTest, this, tempoTeste);
        this.controladorTeste.startTest();
        this.uiControler.alterarEstadoAtual(EstadoEnum.GRAVANDO);
        this.abas.setSelectedIndex(2);
        this.graficoAtencao = this.inicializarGraficoAtencao(datasetGraficoAtencao, graficoAtencaoPanel);
    }

    private void desconectarThinkGear() {
        this.uiControler.desconectar();
    }
    
    private Integer reducaoLinearSinaisOndas(Integer original){
        Integer transformado=0;
        if(original!=null){
            transformado = (int)((100f/65532f)*(float)original);
        }
        return transformado;
    }

    private void inicializarStatus() {
        datasetStatusBar = new DefaultCategoryDataset();
        chart = ChartFactory.createBarChart(null, null, null, 
                datasetStatusBar, PlotOrientation.VERTICAL,
                false, true, false);
        
        ChartPanel cp = new ChartPanel(chart);
        cp.setBounds(graficoStatusPanel.getBounds());
        CategoryPlot plot = (CategoryPlot)chart.getPlot(); 
        plot.getRangeAxis().setAutoRange(false);
        plot.getRangeAxis().setRange(0f, 100f);
        graficoStatusPanel.removeAll();
        graficoStatusPanel.add(cp);
    }
    
    public void setMensagemEstadoAtual(String msg){
        this.mensagemEstadoAtual.setText(msg);
    }

    public void salvarDadosComObservacoes(String obs){
        this.subjectTest.setObservacoes(obs);
        this.salvarDadosXml();
    }
    
    private void salvarDadosXml() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new ExtensionFileFilter(".xml.gz - Xml Compactado", ".xml.gz"));
        fc.setDialogTitle("Salvar dados...");
        String nomeArquivo="dados.xml.gz";
        if(!this.subjectTest.getNome().isEmpty()){
            SimpleDateFormat df = new SimpleDateFormat("HHmmssddMMyyyy");
            nomeArquivo = "dados-"+
                          this.subjectTest.getNome().trim()+"-"+
                          df.format(new Date())+
                          ".xml.gz";
            nomeArquivo=nomeArquivo.replaceAll(" ", "_");
        }
        fc.setSelectedFile(new File(nomeArquivo));
        int returnVal = fc.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            SubjectTestXMLWriter xmlWriter =  new SubjectTestXMLWriter(subjectTest);
            xmlWriter.gerarArquivoXML(fc.getSelectedFile());
            this.addMensagemInfo("Dados Salvos em: "+fc.getSelectedFile().getAbsolutePath());
        }else{
            MensagensDialog.mostraErro("Erro Abrindo Arquivo!");
        }
        
    }

    private void initComboBoxes() {
        
        EnumComboBoxModel dominioSexo =  new EnumComboBoxModel(SexoEnum.class);
        sexoCb.setModel(dominioSexo);
        
        EnumComboBoxModel dominioSerie =  new EnumComboBoxModel(SerieEnum.class);
        serieCb.setModel(dominioSerie);

        DefaultComboBoxModel dominioHiperMidia = new DefaultComboBoxModel(
                    ConfigHandler.getInstancia().getConfiguracao().getHipermidias().toArray()
                );
        hiperMidiaCb.setModel(dominioHiperMidia);
            
    }

    private void validarDadosSujeito() throws  SubjectTestException{
        SubjectTestException subjectTestException = new SubjectTestException();
        
        if(nomeTf.getText().isEmpty()){
            subjectTestException.addMensagem("Nome");
        }
        
        if(dataNascimentoCb.getDate() == null){
            subjectTestException.addMensagem("Data de Nascimento");
        }
        
        if(!subjectTestException.isEmpty()){
            throw subjectTestException;
        }
        
    }

    private void carregaDadosSujeito() {
        subjectTest.setNome(nomeTf.getText());
        subjectTest.setSexo((SexoEnum)sexoCb.getSelectedItem());
        subjectTest.setSerie((SerieEnum)serieCb.getSelectedItem());
        subjectTest.setDataNascimento(dataNascimentoCb.getDate());
        subjectTest.setConceitoEscolar(Float.parseFloat((String)conceitoEscolarCb.getSelectedItem()));
        
        subjectTest.setTentativa(Integer.parseInt((String)numeroTentativaCb.getSelectedItem()));
        subjectTest.setHipermidia((HyperMedia)hiperMidiaCb.getSelectedItem());
       
    }

    private void conectar() {
        this.subjectTest = new SubjectTest();
        this.uiControler = new ControladorNeuroSky(subjectTest, this);
        this.uiControler.iniciarConectorSocket();
        this.inicializarStatus();
    }

    void setCronometro(long milis) {
        long minutoLong = 1*60*1000L;
        long segundoLong = 1000L;
        
        long numMinutos = milis/minutoLong;
        milis-=(numMinutos*minutoLong);
        
        long numSegundos = milis/segundoLong;
        milis-=(numSegundos*segundoLong);
        
        String tempoCronometro = (numMinutos<10?"0"+numMinutos:numMinutos)+":"+
                                 (numSegundos<10?"0"+numSegundos:numSegundos)+"."+
                                 (milis<100?(milis<10?"00"+milis:"0"+milis):milis);
        
        cronometroLabel.setText(tempoCronometro);
        
        
    }

    void finalizaTeste() {
        this.uiControler.alterarEstadoAtual(EstadoEnum.PRONTO);
        this.uiControler.desconectar();
        this.addMensagem("Fim do Teste Atual", "INFO");
        this.addMensagem("***************************", "INFO");
        this.addMensagem("Início em: "+subjectTest.getInicio(), "INFO");
        this.addMensagem("Fim em: "+subjectTest.getFim(), "INFO");
        this.addMensagem("Número de amostras: "+subjectTest.getDadosAtencao().size(), "INFO");
        this.addMensagem("***************************", "INFO");
        this.abas.setSelectedIndex(2);
    }
    

    private void mostrarDialogObservacoes() {
        ObservacoesDialog observacoesDialog = new ObservacoesDialog(this);
        observacoesDialog.setVisible(true);
    }

    private void mostrarDialogSobre() {
        SobreDialog sobreDialog = new SobreDialog(this, false);
        sobreDialog.setVisible(true);
    }

    private void carregarArquivoExportacao() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new ExtensionFileFilter(".xml.gz - Xml Compactado", ".xml.gz"));
        fc.setDialogTitle("Carregar dados...");
        int returnVal = fc.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            SubjectTestXMLReader xmlReader =  new SubjectTestXMLReader();
            this.subjectTestExportar = xmlReader.carregarXML(fc.getSelectedFile());
            this.addMensagemInfo("Dados carregados de: "+fc.getSelectedFile().getAbsolutePath());
            this.mostrarDadosExportacao();
        }else{
            MensagensDialog.mostraErro("Erro Abrindo Arquivo!");
        }
    }

    private void mostrarDadosExportacao() {
        SubjectTest s = subjectTestExportar;
        String resumo = "Teste sujeito: "+s.getNome()+" Sexo "+s.getSexo()+" Nascimento: "+s.getDataNascimento()+"\n";
        Long duracao = (s.getFim().getTime()-s.getInicio().getTime())/1000;
        resumo+=" Início em: "+s.getInicio() +" Fim em: "+s.getFim()+" Duração:"+duracao+"s\n";
        resumo+="Objeto: "+s.getHipermidia()+" Tentativa: "+s.getTentativa()+"\n";
        resumo+=s.getDadosAtencao().size()+" valores de atenção medidos; ";
        resumo+=s.getDadosMeditacao().size()+" valores de meditação medidos; ";
        resumo+=s.getDadosSinal().size()+" valores de sinal medidos;\n";
        resumo+="Observações do Teste: "+s.getObservacoes();
        resumoTesteTA.setText(resumo);
        
        this.graficoAtencaoExportacao = this.inicializarGraficoAtencao(datasetGraficoAtencaoExportacao, graficoSinaisExportar, true);
        this.graficoAtencaoExportacao.getLegend().setPosition(RectangleEdge.RIGHT);
        for(MedicaoAtencao atencao: subjectTestExportar.getDadosAtencao()){
            long ordenada = atencao.getHora().getTime() - subjectTestExportar.getInicio().getTime();
            ordenada/=1000;
            datasetGraficoAtencaoExportacao.addValue(atencao.getValor(), "Atenção", ordenada+"");
        }
        for(MedicaoMeditacao meditacao: subjectTestExportar.getDadosMeditacao()){
            long ordenada = meditacao.getHora().getTime() - subjectTestExportar.getInicio().getTime();
            ordenada/=1000;
            datasetGraficoAtencaoExportacao.addValue(meditacao.getValor(), "Meditação", ordenada+"");
        }
        for(MedicaoSinal sinal: subjectTestExportar.getDadosSinal()){
            long ordenada = sinal.getHora().getTime() - subjectTestExportar.getInicio().getTime();
            ordenada/=1000;
            datasetGraficoAtencaoExportacao.addValue(sinal.getValor(), "Sinal", ordenada+"");
        }
        
    }

    private JFreeChart inicializarGraficoAtencao(DefaultCategoryDataset dataset, JPanel painel){
        return this.inicializarGraficoAtencao( dataset, painel, false);
    }
    
    private JFreeChart inicializarGraficoAtencao(DefaultCategoryDataset dataset, JPanel painel, Boolean legend) {
        JFreeChart grafico = ChartFactory.createLineChart("Nível de Atenção", "", "", 
                                    dataset, PlotOrientation.VERTICAL, 
                                    legend, false, false);
        
        ChartPanel cp = new ChartPanel(grafico);
        cp.setBounds(painel.getBounds());
        CategoryPlot plot = (CategoryPlot)grafico.getPlot(); 
        plot.getRangeAxis().setAutoRange(false);
        plot.getRangeAxis().setRange(0f, 100f);
        
        painel.removeAll();
        painel.add(cp);
        return grafico;
    }

    private void exportarDadosCsv() {
        if(this.subjectTestExportar!=null){
            String csv = DataConverter.subjectToCsv(subjectTestExportar);
            salvarDadosCsv(csv);
        }else{
            MensagensDialog.mostraErro("Arquivo de dados não carregado!");
        }
    }
    
    private void salvarDadosCsv(String csv) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new ExtensionFileFilter(".csv - Comma-separated values", ".scv"));
        fc.setDialogTitle("Salvar dados...");
        String nomeArquivo="dados.csv";
        if(!this.subjectTestExportar.getNome().isEmpty()){
            SimpleDateFormat df = new SimpleDateFormat("HHmmssddMMyyyy");
            nomeArquivo = "dados-"+
                          this.subjectTestExportar.getNome().trim()+"-"+
                          df.format(subjectTestExportar.getFim())+
                          ".csv";
            nomeArquivo=nomeArquivo.replaceAll(" ", "_");
        }
        fc.setSelectedFile(new File(nomeArquivo));
        int returnVal = fc.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            gravarDadosArquivoCSV(fc.getSelectedFile(), csv); 
        }else{
            MensagensDialog.mostraErro("Erro Abrindo Arquivo!");
        }
        
    }

    private void exportarLoteCsv() {
        new Thread(){
            @Override
            public void run(){
                carregarLoteExportacao();
            }
        }.start();
        
    }

    private void carregarLoteExportacao() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new ExtensionFileFilter(".xml.gz - Xml Compactado", ".xml.gz"));
        fc.setMultiSelectionEnabled(true);
        fc.setDialogTitle("Carregar dados...");
        int returnVal = fc.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            int totalArquivos = fc.getSelectedFiles().length;
            int arquivoAtual = 0;
            for(File arqDados: fc.getSelectedFiles()){
                arquivoAtual+=1;
                SubjectTestXMLReader xmlReader =  new SubjectTestXMLReader();
                this.addMensagemInfo("***** ["+arquivoAtual+"/"+totalArquivos+"]Preparando para exportar dados de: "+arqDados.getName());
                this.subjectTestExportar = xmlReader.carregarXML(arqDados);
                this.addMensagemInfo("Dados carregados de: "+arqDados.getAbsolutePath());
                this.mostrarDadosExportacao();
                String csv = DataConverter.subjectToCsv(subjectTestExportar);
                String nomeArquivoCSV = (arqDados.getName().split(".xml.gz")[0])+".csv";
                String caminhoCompletoArquivo = arqDados.getParent() + File.separator + nomeArquivoCSV;
                File arquivoCSV = new File(caminhoCompletoArquivo);
                this.gravarDadosArquivoCSV(arquivoCSV, csv);
                this.addMensagemInfo("***** Dados Exportados para: "+arquivoCSV.getAbsolutePath());
            }
            this.addMensagemInfo("FIM DA EXPORTAÇÃO: "+arquivoAtual+" arquivos processados");
        }else{
            MensagensDialog.mostraErro("Erro Abrindo Arquivo!");
        }
    }

    private void gravarDadosArquivoCSV(File arquivo, String csv) {
        try {
            PrintWriter out = new PrintWriter(arquivo);
            out.println(csv);
            out.close();
            this.addMensagemInfo("Dados Salvos em: "+arquivo.getAbsolutePath());
        } catch (FileNotFoundException ex) {
            MensagensDialog.mostraErro("Erro Gravando o Arquivo CSV!");
        }
    }

   
}
