package Vista;

import Modelo.DemandaPeriodo;
import Servicio.ServicioBI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/** Ventana independiente para el análisis temporal de la demanda simulada. */
public class FrmAnalisisTemporal extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int SIMULACION_ID = 1;
    private transient ServicioBI servicio = new ServicioBI();
    private final JLabel lblEstado = new JLabel("Preparado para consultar datos");
    private final JLabel lblResumen = new JLabel("Análisis temporal de la demanda simulada", SwingConstants.LEFT);
    private final JButton btnActualizar = new JButton("Actualizar análisis");
    private final JTable tablaMensual = crearTabla(new String[]{"Mes", "Unidades", "Ingresos estimados", "Promedio diario"});
    private final JTable tablaSemanal = crearTabla(new String[]{"Día", "Unidades", "Ingresos estimados", "Promedio diario"});
    private final GraficaBarras graficaMensual = new GraficaBarras("Demanda mensual", "Unidades", false);
    private final GraficaBarras graficaSemanal = new GraficaBarras("Promedio de demanda por día", "Unidades promedio por día", true);

    public FrmAnalisisTemporal() {
        setTitle("SATD - Análisis temporal de demanda");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(980, 700));
        setSize(1100, 760);
        setLocationRelativeTo(null);
        construirInterfaz();
        btnActualizar.addActionListener(e -> cargarDatos());
        cargarDatos();
    }

    private void construirInterfaz() {
        JPanel raiz = new JPanel(new BorderLayout(12, 12));
        raiz.setBorder(BorderFactory.createEmptyBorder(16, 18, 12, 18));
        raiz.setBackground(new Color(245, 247, 250));

        JPanel encabezado = new JPanel(new BorderLayout(8, 8));
        encabezado.setOpaque(false);
        JLabel titulo = new JLabel("Análisis temporal de demanda");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 23));
        lblResumen.setFont(new Font("SansSerif", Font.PLAIN, 13));
        encabezado.add(titulo, BorderLayout.NORTH);
        encabezado.add(lblResumen, BorderLayout.CENTER);
        JPanel acciones = new JPanel(new BorderLayout());
        acciones.setOpaque(false);
        acciones.add(btnActualizar, BorderLayout.EAST);
        encabezado.add(acciones, BorderLayout.SOUTH);
        raiz.add(encabezado, BorderLayout.NORTH);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Por mes", crearPanelTemporal(graficaMensual, tablaMensual));
        pestañas.addTab("Por día de semana", crearPanelTemporal(graficaSemanal, tablaSemanal));
        raiz.add(pestañas, BorderLayout.CENTER);

        lblEstado.setBorder(BorderFactory.createEmptyBorder(5, 2, 0, 2));
        raiz.add(lblEstado, BorderLayout.SOUTH);
        setContentPane(raiz);
    }

    private JPanel crearPanelTemporal(GraficaBarras grafica, JTable tabla) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        grafica.setPreferredSize(new Dimension(850, 330));
        panel.add(grafica, BorderLayout.CENTER);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(850, 180));
        panel.add(scroll, BorderLayout.SOUTH);
        return panel;
    }

    private JTable crearTabla(String[] columnas) {
        DefaultTableModel modelo = new DefaultTableModel(new Object[0][0], columnas) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.setAutoCreateRowSorter(true);
        return tabla;
    }

    private void cargarDatos() {
        btnActualizar.setEnabled(false);
        lblEstado.setText("Consultando demanda temporal...");
        new SwingWorker<ResultadoTemporal, Void>() {
            @Override
            protected ResultadoTemporal doInBackground() throws Exception {
                List<DemandaPeriodo> mensual = servicio.obtenerDemandaMensual(SIMULACION_ID);
                List<DemandaPeriodo> semanal = servicio.obtenerDemandaPorDiaSemana(SIMULACION_ID);
                return new ResultadoTemporal(mensual, semanal);
            }

            @Override
            protected void done() {
                try {
                    ResultadoTemporal datos = get();
                    List<DemandaPeriodo> mensual = datos.mensual;
                    List<DemandaPeriodo> semanal = datos.semanal;
                    llenarTabla(tablaMensual, mensual);
                    llenarTabla(tablaSemanal, semanal);
                    graficaMensual.setDatos(mensual);
                    graficaSemanal.setDatos(semanal);
                    actualizarResumen(mensual, semanal);
                    lblEstado.setText("Datos simulados cargados correctamente | "
                            + mensual.size() + " meses | " + semanal.size() + " días de semana");
                } catch (Exception ex) {
                    Throwable causa = ex.getCause() == null ? ex : ex.getCause();
                    lblEstado.setText("No fue posible cargar el análisis temporal");
                    JOptionPane.showMessageDialog(FrmAnalisisTemporal.this,
                            "Error al consultar el análisis temporal:\n" + causa.getMessage(),
                            "Error de consulta", JOptionPane.ERROR_MESSAGE);
                } finally {
                    btnActualizar.setEnabled(true);
                }
            }
        }.execute();
    }

    private void llenarTabla(JTable tabla, List<DemandaPeriodo> datos) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        DecimalFormat moneda = new DecimalFormat("$#,##0.00");
        for (DemandaPeriodo p : datos) {
            modelo.addRow(new Object[]{p.getPeriodo(), p.getUnidades(),
                moneda.format(p.getIngresos()), p.getPromedioDiario().setScale(2, RoundingMode.HALF_UP)});
        }
    }

    private void actualizarResumen(List<DemandaPeriodo> mensual, List<DemandaPeriodo> semanal) {
        if (mensual.isEmpty() || semanal.isEmpty()) {
            lblResumen.setText("No hay registros para la simulación seleccionada.");
            return;
        }
        DemandaPeriodo mayorMes = mensual.get(0);
        DemandaPeriodo menorMes = mensual.get(0);
        for (DemandaPeriodo p : mensual) {
            if (p.getUnidades() > mayorMes.getUnidades()) mayorMes = p;
            if (p.getUnidades() < menorMes.getUnidades()) menorMes = p;
        }
        DemandaPeriodo mayorDia = semanal.get(0);
        DemandaPeriodo menorDia = semanal.get(0);
        for (DemandaPeriodo p : semanal) {
            if (p.getPromedioDiario().compareTo(mayorDia.getPromedioDiario()) > 0) mayorDia = p;
            if (p.getPromedioDiario().compareTo(menorDia.getPromedioDiario()) < 0) menorDia = p;
        }
        lblResumen.setText("Mayor mes: " + mayorMes.getPeriodo() + " (" + mayorMes.getUnidades()
                + " unidades) | Menor mes: " + menorMes.getPeriodo() + " (" + menorMes.getUnidades()
                + ") | Día más demandado: " + mayorDia.getPeriodo() + " (" + mayorDia.getPromedioDiario()
                + " promedio/día) | Menor: " + menorDia.getPeriodo() + " (" + menorDia.getPromedioDiario() + ")");
    }


    private static class ResultadoTemporal {
        private final List<DemandaPeriodo> mensual;
        private final List<DemandaPeriodo> semanal;

        ResultadoTemporal(List<DemandaPeriodo> mensual, List<DemandaPeriodo> semanal) {
            this.mensual = mensual;
            this.semanal = semanal;
        }
    }

    /** Gráfica Swing propia: no requiere JFreeChart ni dependencias adicionales. */
    private static class GraficaBarras extends JPanel {
        private static final long serialVersionUID = 1L;
        private final String titulo;
        private final String etiqueta;
        private final boolean usarPromedio;
        private transient List<DemandaPeriodo> datos = new ArrayList<>();

        GraficaBarras(String titulo, String etiqueta, boolean usarPromedio) {
            this.titulo = titulo;
            this.etiqueta = etiqueta;
            this.usarPromedio = usarPromedio;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 225, 232)),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        }

        void setDatos(List<DemandaPeriodo> nuevos) {
            datos = new ArrayList<>(nuevos);
            repaint();
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int ancho = getWidth();
            int alto = getHeight();
            g2.setColor(new Color(35, 48, 68));
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString(titulo, 18, 25);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.setColor(new Color(100, 110, 125));
            g2.drawString(etiqueta, 18, 44);
            if (datos.isEmpty()) {
                g2.drawString("Sin datos disponibles", 25, alto / 2);
                g2.dispose();
                return;
            }
            int izquierda = 55, derecha = 20, arriba = 65, abajo = 55;
            int areaW = Math.max(1, ancho - izquierda - derecha);
            int areaH = Math.max(1, alto - arriba - abajo);
            double max = 1;
            for (DemandaPeriodo p : datos) {
                double v = valor(p);
                if (v > max) max = v;
            }
            g2.setColor(new Color(225, 230, 237));
            for (int i = 0; i <= 4; i++) {
                int y = arriba + areaH - (areaH * i / 4);
                g2.drawLine(izquierda, y, izquierda + areaW, y);
                g2.setColor(new Color(105, 115, 130));
                g2.drawString(String.format(java.util.Locale.US, "%.0f", max * i / 4), 8, y + 4);
                g2.setColor(new Color(225, 230, 237));
            }
            int n = datos.size();
            int espacio = Math.max(2, areaW / Math.max(1, n));
            int barraW = Math.max(8, (int) (espacio * 0.62));
            for (int i = 0; i < n; i++) {
                DemandaPeriodo p = datos.get(i);
                double v = valor(p);
                int bh = (int) Math.round((v / max) * areaH);
                int x = izquierda + i * espacio + (espacio - barraW) / 2;
                int y = arriba + areaH - bh;
                g2.setColor(new Color(47, 105, 160));
                g2.fillRoundRect(x, y, barraW, bh, 7, 7);
                g2.setColor(new Color(45, 55, 70));
                String etiquetaX = etiquetaCorta(p.getPeriodo());
                int sw = g2.getFontMetrics().stringWidth(etiquetaX);
                g2.drawString(etiquetaX, x + (barraW - sw) / 2, arriba + areaH + 18);
            }
            g2.dispose();
        }

        private double valor(DemandaPeriodo p) {
            return usarPromedio ? p.getPromedioDiario().doubleValue() : p.getUnidades();
        }

        private String etiquetaCorta(String periodo) {
            if (periodo.matches("\\d{4}-\\d{2}")) {
                String[] meses = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
                int mes = Integer.parseInt(periodo.substring(5, 7));
                return mes >= 1 && mes <= 12 ? meses[mes - 1] : periodo;
            }
            if (periodo.length() > 3) return periodo.substring(0, 3);
            return periodo;
        }
    }
}
