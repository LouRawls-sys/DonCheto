/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;
import Modelo.DemandaProducto;
import Modelo.DemandaCategoria;
import Modelo.ResumenDemanda;
import Servicio.ServicioBI;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author luisg
 */
public class FrmAnalisisDemanda extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmAnalisisDemanda.class.getName());

    /**
     * Creates new form FrmAnalisisDemanda
     */
    private final ServicioBI servicioBI = new ServicioBI();

    private static final int SIMULACION_ID = 1;

    public FrmAnalisisDemanda() {
        initComponents();
        setTitle("SATD - Análisis de demanda");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        btnActualizar.addActionListener(e -> cargarDatos());
        configurarTablas();
        agregarAccesoAnalisisTemporal();
        cargarDatos();
    }
    
    
    private void configurarTablas() {
        jTable1.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Producto",
            "Categoria",
            "Unidades",
            "Promedio diario",
            "Ingresos",
            "Participacion"
            }
        ) 
        {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        tblCategorias.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Categoria",
                    "Unidades",
                    "Ingresos",
                    "Participacion"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        jTable1.setAutoCreateRowSorter(true);
        tblCategorias.setAutoCreateRowSorter(true);
    }

    private void cargarDatos() {

        btnActualizar.setEnabled(false);
        lblEstado.setText("Consultando informacion...");

        try {
            //resumen
            ResumenDemanda resumen =
                    servicioBI.obtenerResumen(SIMULACION_ID);
            lblUnidadesValor.setText(
                    String.valueOf(resumen.getUnidades())
            );
            lblIngresosValor.setText(
                    formatoMoneda(resumen.getIngresos())
            );
            lblProductosValor.setText(
                    String.valueOf(resumen.getProductos())
            );
            lblDiasValor.setText(
                    String.valueOf(resumen.getDias())
            );
            //promedio
            BigDecimal promedio = servicioBI.obtenerPromedioDiarioGeneral(SIMULACION_ID);

            lblSubtitulo.setText("Promedio diario general: " + promedio.setScale(2, java.math.RoundingMode.HALF_UP));

            //demandacpor producto
            cargarTablaProductos(servicioBI.obtenerDemandaPorProducto(SIMULACION_ID));

            //demanda por categoria
            cargarTablaCategorias(
                    servicioBI.obtenerDemandaPorCategoria(SIMULACION_ID)
            );

            lblEstado.setText(
                    "Informacion SIMULADA cargada correctamente"
            );

        } catch (SQLException e) {

            lblEstado.setText("Error al consultar los datos");

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible cargar el analisis de demanda.\n"
                    + e.getMessage(),
                    "Error de consulta",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();

        } finally {

            btnActualizar.setEnabled(true);
        }
    }

    private void cargarTablaProductos(
            List<DemandaProducto> productos) {

        DefaultTableModel modelo =
                (DefaultTableModel) jTable1.getModel();

        modelo.setRowCount(0);

        for (DemandaProducto p : productos) {

            modelo.addRow(new Object[]{
                p.getNombre(),
                p.getCategoria(),
                p.getUnidades(),
                p.getPromedioDiario().setScale(
                        2, java.math.RoundingMode.HALF_UP
                ),
                formatoMoneda(p.getIngresos()),
                p.getParticipacion().toPlainString() + "%"
            });
        }
    }

    private void cargarTablaCategorias(
            List<DemandaCategoria> categorias) {

        DefaultTableModel modelo =
                (DefaultTableModel) tblCategorias.getModel();

        modelo.setRowCount(0);

        for (DemandaCategoria c : categorias) {

            modelo.addRow(new Object[]{
                c.getCategoria(),
                c.getUnidades(),
                formatoMoneda(c.getIngresos()),
                c.getParticipacion().toPlainString() + "%"
            });
        }
    }

    private String formatoMoneda(BigDecimal cantidad) {

        DecimalFormat formato =
                new DecimalFormat("$#,##0.00");

        return formato.format(cantidad);
    }
    
    private void agregarAccesoAnalisisTemporal() {
    javax.swing.JButton btnTemporal =
            new javax.swing.JButton("Análisis temporal");

    btnTemporal.addActionListener(e -> {
        FrmAnalisisTemporal ventana = new FrmAnalisisTemporal();
        ventana.setVisible(true);
    });

    getContentPane().add(
            btnTemporal,
            new AbsoluteConstraints(650, 520, 160, 30)
    );

    setSize(850, 610);
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPrincipal = new javax.swing.JPanel();
        lblSubtitulo = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        lblProductos = new javax.swing.JLabel();
        lblCategorias = new javax.swing.JLabel();
        pnlIndicadores = new javax.swing.JPanel();
        lblIngresosTitulo = new javax.swing.JLabel();
        lblIngresosValor = new javax.swing.JLabel();
        lblUnidadesValor = new javax.swing.JLabel();
        lblProductosValor = new javax.swing.JLabel();
        lblDiasTitulo = new javax.swing.JLabel();
        lblProductosTitulo = new javax.swing.JLabel();
        lblUnidadesTitulo = new javax.swing.JLabel();
        lblDiasValor = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        lblEstado = new javax.swing.JLabel();
        tblProductos = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        scrCategorias = new javax.swing.JScrollPane();
        tblCategorias = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlPrincipal.setBackground(new java.awt.Color(204, 255, 204));

        lblSubtitulo.setText("Indicadores históricos por producto y categoría");

        lblTitulo.setText("Análisis de demanda");

        lblProductos.setText("Demanda por producto");

        lblCategorias.setText("Demanda por categoría");

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSubtitulo)
                            .addComponent(lblTitulo)))
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCategorias)
                            .addComponent(lblProductos))))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(lblTitulo)
                .addGap(18, 18, 18)
                .addComponent(lblSubtitulo)
                .addGap(34, 34, 34)
                .addComponent(lblProductos)
                .addGap(26, 26, 26)
                .addComponent(lblCategorias)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        getContentPane().add(pnlPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 28, 360, 180));

        pnlIndicadores.setBackground(new java.awt.Color(255, 204, 204));

        lblIngresosTitulo.setText("INGRESOS ESTIMADOS");

        lblIngresosValor.setText("--");

        lblUnidadesValor.setText("--");

        lblProductosValor.setText("--");

        lblDiasTitulo.setText("DÍAS ANALIZADOS");

        lblProductosTitulo.setText("PRODUCTOS");

        lblUnidadesTitulo.setText("UNIDADES DEMANDADAS");

        lblDiasValor.setText("--");

        btnActualizar.setText("Actualizar datos");

        lblEstado.setText("Estado");

        javax.swing.GroupLayout pnlIndicadoresLayout = new javax.swing.GroupLayout(pnlIndicadores);
        pnlIndicadores.setLayout(pnlIndicadoresLayout);
        pnlIndicadoresLayout.setHorizontalGroup(
            pnlIndicadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIndicadoresLayout.createSequentialGroup()
                .addGroup(pnlIndicadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlIndicadoresLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(pnlIndicadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUnidadesTitulo)
                            .addComponent(lblUnidadesValor)
                            .addComponent(lblIngresosTitulo)
                            .addComponent(lblIngresosValor)))
                    .addGroup(pnlIndicadoresLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(lblEstado)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                .addGroup(pnlIndicadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnActualizar)
                    .addComponent(lblDiasValor)
                    .addComponent(lblProductosTitulo)
                    .addComponent(lblProductosValor)
                    .addComponent(lblDiasTitulo))
                .addGap(165, 165, 165))
        );
        pnlIndicadoresLayout.setVerticalGroup(
            pnlIndicadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlIndicadoresLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(pnlIndicadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlIndicadoresLayout.createSequentialGroup()
                        .addComponent(lblUnidadesTitulo)
                        .addGap(4, 4, 4)
                        .addComponent(lblUnidadesValor)
                        .addGap(14, 14, 14)
                        .addComponent(lblIngresosTitulo)
                        .addGap(14, 14, 14)
                        .addComponent(lblIngresosValor))
                    .addGroup(pnlIndicadoresLayout.createSequentialGroup()
                        .addComponent(lblProductosTitulo)
                        .addGap(4, 4, 4)
                        .addComponent(lblProductosValor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDiasTitulo)
                        .addGap(18, 18, 18)
                        .addComponent(lblDiasValor)))
                .addGroup(pnlIndicadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlIndicadoresLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblEstado)
                        .addGap(25, 25, 25))
                    .addGroup(pnlIndicadoresLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnActualizar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        getContentPane().add(pnlIndicadores, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 590, 180));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Producto", "Cateoria", "Unidades", "Promedio diari", "Ingresos", "Participación"
            }
        ));
        tblProductos.setViewportView(jTable1);

        getContentPane().add(tblProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 500, 280));

        tblCategorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Categoria", "Unidades", "Ingresos", "Participacion"
            }
        ));
        scrCategorias.setViewportView(tblCategorias);

        getContentPane().add(scrCategorias, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 230, 460, 210));

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        java.awt.EventQueue.invokeLater(() -> new FrmAnalisisDemanda().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblCategorias;
    private javax.swing.JLabel lblDiasTitulo;
    private javax.swing.JLabel lblDiasValor;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblIngresosTitulo;
    private javax.swing.JLabel lblIngresosValor;
    private javax.swing.JLabel lblProductos;
    private javax.swing.JLabel lblProductosTitulo;
    private javax.swing.JLabel lblProductosValor;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUnidadesTitulo;
    private javax.swing.JLabel lblUnidadesValor;
    private javax.swing.JPanel pnlIndicadores;
    private javax.swing.JPanel pnlPrincipal;
    private javax.swing.JScrollPane scrCategorias;
    private javax.swing.JTable tblCategorias;
    private javax.swing.JScrollPane tblProductos;
    // End of variables declaration//GEN-END:variables
}
