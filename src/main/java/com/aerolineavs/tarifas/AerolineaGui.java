package com.aerolineavs.tarifas;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Ventana Swing para consultar la tarifa recomendada de un cliente.
 */
public final class AerolineaGui extends JFrame {

    private final IPricingService pricingService;
    private final JTextField edadField = new JTextField(12);
    private final JTextField vuelosField = new JTextField(12);
    private final JTextField ingresosField = new JTextField(12);
    private final JComboBox<TipoViajero> tipoViajeroCombo = new JComboBox<>(TipoViajero.values());
    private final JComboBox<ClaseVuelo> claseVueloCombo = new JComboBox<>(ClaseVuelo.values());
    private final JComboBox<RegionDestino> destinoCombo = new JComboBox<>(RegionDestino.values());
    private final JCheckBox viajaConNinosCheck = new JCheckBox("Viaja con niños menores de 12");
    private final JCheckBox viveConPadresCheck = new JCheckBox("Vive con sus padres");
    private final JLabel tarifaValue = new JLabel("Sin calcular");
    private final JLabel descuentoValue = new JLabel("-");

    /**
     * Crea la ventana con el servicio de precios que utilizará para calcular.
     *
     * @param pricingService servicio de negocio para consultar tarifas
     */
    public AerolineaGui(IPricingService pricingService) {
        this.pricingService = pricingService;
        setTitle("Aerolinea VS - Tarifas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(crearContenido());
        setMinimumSize(new java.awt.Dimension(620, 520));
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout(16, 16));
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel titulo = new JLabel("Consulta de tarifa aérea");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
        contenido.add(titulo, BorderLayout.NORTH);

        contenido.add(crearFormulario(), BorderLayout.CENTER);
        contenido.add(crearResultado(), BorderLayout.SOUTH);
        return contenido;
    }

    private JPanel crearFormulario() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Perfil del viajero"));
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(6, 8, 6, 8);
        restricciones.anchor = GridBagConstraints.WEST;
        restricciones.fill = GridBagConstraints.HORIZONTAL;

        agregarCampo(formulario, restricciones, 0, "Edad (años):", edadField);
        agregarCampo(formulario, restricciones, 1, "Vuelos al año:", vuelosField);
        agregarCampo(formulario, restricciones, 2, "Ingresos anuales (€):", ingresosField);
        agregarCampo(formulario, restricciones, 3, "Tipo de viajero:", tipoViajeroCombo);
        agregarCampo(formulario, restricciones, 4, "Clase preferida:", claseVueloCombo);
        agregarCampo(formulario, restricciones, 5, "Destino preferido:", destinoCombo);

        restricciones.gridx = 0;
        restricciones.gridy = 6;
        restricciones.gridwidth = 2;
        formulario.add(viajaConNinosCheck, restricciones);

        restricciones.gridy = 7;
        formulario.add(viveConPadresCheck, restricciones);

        JButton calcularButton = new JButton("Calcular tarifa");
        calcularButton.addActionListener(evento -> calcularTarifa());
        restricciones.gridy = 8;
        restricciones.anchor = GridBagConstraints.EAST;
        formulario.add(calcularButton, restricciones);
        return formulario;
    }

    private void agregarCampo(JPanel formulario, GridBagConstraints restricciones,
                              int fila, String etiqueta, java.awt.Component campo) {
        restricciones.gridx = 0;
        restricciones.gridy = fila;
        restricciones.gridwidth = 1;
        restricciones.weightx = 0;
        formulario.add(new JLabel(etiqueta), restricciones);

        restricciones.gridx = 1;
        restricciones.weightx = 1;
        formulario.add(campo, restricciones);
    }

    private JPanel crearResultado() {
        JPanel resultado = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 8));
        resultado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 200, 210)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        resultado.add(new JLabel("Tarifa:"));
        tarifaValue.setFont(tarifaValue.getFont().deriveFont(Font.BOLD));
        resultado.add(tarifaValue);
        resultado.add(new JLabel("Descuento:"));
        descuentoValue.setFont(descuentoValue.getFont().deriveFont(Font.BOLD));
        resultado.add(descuentoValue);
        return resultado;
    }

    private void calcularTarifa() {
        try {
            ClientePotencial cliente = new ClientePotencial(
                    leerEntero(edadField, "Edad"),
                    leerEntero(vuelosField, "Vuelos al año"),
                    (TipoViajero) tipoViajeroCombo.getSelectedItem(),
                    (ClaseVuelo) claseVueloCombo.getSelectedItem(),
                    (RegionDestino) destinoCombo.getSelectedItem(),
                    leerDecimal(ingresosField, "Ingresos anuales"),
                    viajaConNinosCheck.isSelected(),
                    viveConPadresCheck.isSelected()
            );

            ResultadoTarifa resultado = pricingService.calcular(cliente);
            tarifaValue.setText(resultado.tarifa().getNombre());
            descuentoValue.setText(resultado.tarifa().getDescuentoPorcentaje() + "%");
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Datos no válidos",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private int leerEntero(JTextField campo, String nombre) {
        try {
            return Integer.parseInt(campo.getText().trim());
        } catch (NumberFormatException exception) {
            throw new NumberFormatException(nombre + " debe ser un número entero.");
        }
    }

    private double leerDecimal(JTextField campo, String nombre) {
        try {
            return Double.parseDouble(campo.getText().trim());
        } catch (NumberFormatException exception) {
            throw new NumberFormatException(nombre + " debe ser un número válido.");
        }
    }
}