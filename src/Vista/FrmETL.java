/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;
import ETL.ServicioETL;
import ETL.ServicioETL.ResultadoETL;
import javax.swing.SwingWorker;
import javax.swing.JOptionPane;
import java.util.concurrent.ExecutionException;
/**
 *
 * @author luisg
 */
public class FrmETL extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmETL.class.getName());

    /**
     * Creates new form FrmETL
     */
    public FrmETL() {
    initComponents();

    setTitle("Proceso ETL - Don Cheto Wings");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    inicializarVentana();

        btnLimpiarRegistro.addActionListener(evt -> limpiarRegistro());
    }
    private void inicializarVentana() {

        lblEstadoConexion.setText("En espera");
        lblRegistrosExtraidos.setText("0");
        lblRegistrosDescartados.setText("0");
        lblGruposTransformados.setText("0");

        // jLabel6 es el valor de registros cargados
        jLabel6.setText("0");

        txtRegistroETL.setEditable(false);

        registrarMensaje("Sistema ETL inicializado.");
        registrarMensaje("Esperando ejecución...");
    }
    
    private void ejecutarETL() {

        btnEjecutarETL.setEnabled(false);

        lblEstadoConexion.setText("0.");

        lblRegistrosExtraidos.setText("0");
        lblRegistrosDescartados.setText("0");
        lblGruposTransformados.setText("0");
        jLabel6.setText("0");

        registrarMensaje("");
        registrarMensaje("INICIANDO PROCESO ETL");

        SwingWorker<ResultadoETL, Void> trabajador =
                new SwingWorker<>() {

            @Override
            protected ResultadoETL doInBackground() throws Exception {

                ServicioETL servicio = new ServicioETL();

                return servicio.ejecutar();
            }

            @Override
            protected void done() {

                try {

                    ResultadoETL resultado = get();

                    // Actualizar indicadores
                    lblEstadoConexion.setText("Conexión exitosa");

                    lblRegistrosExtraidos.setText(
                            String.valueOf(resultado.getRegistrosExtraidos()));

                    lblRegistrosDescartados.setText(
                            String.valueOf(resultado.getRegistrosDescartados()));

                    lblGruposTransformados.setText(
                            String.valueOf(resultado.getGruposTransformados()));

                    jLabel6.setText(
                            String.valueOf(resultado.getRegistrosCargados()));

                    // Mostrar resultados
                    registrarMensaje("PROCESO ETL FINALIZADO");
                    registrarMensaje("----------------------------------");

                    registrarMensaje("Registros extraídos: "
                            + resultado.getRegistrosExtraidos());

                    registrarMensaje("Registros descartados: "
                            + resultado.getRegistrosDescartados());

                    registrarMensaje("Grupos transformados: "
                            + resultado.getGruposTransformados());

                    registrarMensaje("Registros cargados: "
                            + resultado.getRegistrosCargados());

                    registrarMensaje("----------------------------------");
                    registrarMensaje("Proceso completado correctamente.");

                    JOptionPane.showMessageDialog(
                            FrmETL.this,
                            "El proceso ETL finalizó correctamente.",
                            "ETL completado",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (InterruptedException ex) {

                    Thread.currentThread().interrupt();

                    lblEstadoConexion.setText("Interrumpido");

                    registrarMensaje("El proceso fue interrumpido.");

                } catch (ExecutionException ex) {

                    lblEstadoConexion.setText("Error");

                    Throwable causa = ex.getCause();

                    registrarMensaje("ERROR DURANTE EL ETL:");

                    registrarMensaje(causa != null
                            ? causa.getMessage()
                            : ex.getMessage());

                    JOptionPane.showMessageDialog(
                            FrmETL.this,
                            "Error al ejecutar el ETL:\n"
                            + (causa != null ? causa.getMessage() : ex.getMessage()),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                } finally {

                    btnEjecutarETL.setEnabled(true);
                }
            }
        };

        trabajador.execute();
    }
    
    private void registrarMensaje(String mensaje) {

        txtRegistroETL.append(mensaje + "\n");

        txtRegistroETL.setCaretPosition(
                txtRegistroETL.getDocument().getLength()
        );
    }
    
    private void limpiarRegistro() {

        txtRegistroETL.setText("");

        registrarMensaje("Registro limpiado.");
        registrarMensaje("Esperando ejecución...");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblRegistrosExtraidos = new javax.swing.JLabel();
        lblEstadoConexion = new javax.swing.JLabel();
        lblGruposTransformados = new javax.swing.JLabel();
        lblRegistrosDescartados = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblRegistrosExtraidos1 = new javax.swing.JLabel();
        lblEstadoConexion1 = new javax.swing.JLabel();
        lblGruposTransformados1 = new javax.swing.JLabel();
        lblRegistrosDescartados1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtRegistroETL = new javax.swing.JTextArea();
        btnEjecutarETL = new javax.swing.JButton();
        btnLimpiarRegistro = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblRegistrosExtraidos.setText("jLabel1");
        getContentPane().add(lblRegistrosExtraidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, -1, -1));

        lblEstadoConexion.setText("jLabel1");
        getContentPane().add(lblEstadoConexion, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, -1, -1));

        lblGruposTransformados.setText("jLabel1");
        getContentPane().add(lblGruposTransformados, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 80, -1, -1));

        lblRegistrosDescartados.setText("jLabel1");
        getContentPane().add(lblRegistrosDescartados, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 40, -1, -1));

        jLabel6.setText("jLabel1");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 130, -1, -1));

        lblRegistrosExtraidos1.setText("Registros extraidos");
        getContentPane().add(lblRegistrosExtraidos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, -1, -1));

        lblEstadoConexion1.setText("Estado");
        getContentPane().add(lblEstadoConexion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, -1, -1));

        lblGruposTransformados1.setText("Grupos transformados");
        getContentPane().add(lblGruposTransformados1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, -1, -1));

        lblRegistrosDescartados1.setText("Registros descartados ");
        getContentPane().add(lblRegistrosDescartados1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, -1, -1));

        jLabel7.setText("Registros cargados");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, -1, -1));

        txtRegistroETL.setColumns(20);
        txtRegistroETL.setRows(5);
        jScrollPane1.setViewportView(txtRegistroETL);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 580, 280));

        btnEjecutarETL.setText("Efecutar");
        btnEjecutarETL.addActionListener(this::btnEjecutarETLActionPerformed);
        getContentPane().add(btnEjecutarETL, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 480, -1, -1));

        btnLimpiarRegistro.setText("Limpiar");
        btnLimpiarRegistro.addActionListener(this::btnLimpiarRegistroActionPerformed);
        getContentPane().add(btnLimpiarRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 480, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEjecutarETLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEjecutarETLActionPerformed
        // TODO add your handling code here:
        ejecutarETL();
    }//GEN-LAST:event_btnEjecutarETLActionPerformed

    private void btnLimpiarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarRegistroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimpiarRegistroActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmETL().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEjecutarETL;
    private javax.swing.JButton btnLimpiarRegistro;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblEstadoConexion;
    private javax.swing.JLabel lblEstadoConexion1;
    private javax.swing.JLabel lblGruposTransformados;
    private javax.swing.JLabel lblGruposTransformados1;
    private javax.swing.JLabel lblRegistrosDescartados;
    private javax.swing.JLabel lblRegistrosDescartados1;
    private javax.swing.JLabel lblRegistrosExtraidos;
    private javax.swing.JLabel lblRegistrosExtraidos1;
    private javax.swing.JTextArea txtRegistroETL;
    // End of variables declaration//GEN-END:variables
}
