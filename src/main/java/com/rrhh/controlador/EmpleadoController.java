package com.rrhh.controlador;

import com.rrhh.modelo.Cargo;
import com.rrhh.modelo.Departamento;
import com.rrhh.modelo.Empleado;
import com.rrhh.modelo.enums.EstadoEmpleado;
import com.rrhh.servicio.CargoServicio;
import com.rrhh.servicio.DepartamentoServicio;
import com.rrhh.servicio.EmpleadoServicio;
import com.rrhh.util.NavegadorVistas;
import com.rrhh.util.TareasFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class EmpleadoController {

    @FXML
    private TableView<Empleado> tablaEmpleados;
    @FXML
    private TableColumn<Empleado, String> colNombre;
    @FXML
    private TableColumn<Empleado, String> colDni;
    @FXML
    private TableColumn<Empleado, String> colDepartamento;
    @FXML
    private TableColumn<Empleado, String> colEstado;

    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoApellido;
    @FXML
    private TextField campoDni;
    @FXML
    private TextField campoEmail;
    @FXML
    private DatePicker campoFechaNacimiento;
    @FXML
    private DatePicker campoFechaContratacion;
    @FXML
    private TextField campoSalario;
    @FXML
    private ComboBox<EstadoEmpleado> comboEstado;
    @FXML
    private ComboBox<Departamento> comboDepartamento;
    @FXML
    private ComboBox<Cargo> comboCargo;
    @FXML
    private Label labelMensaje;

    private final EmpleadoServicio empleadoServicio = new EmpleadoServicio();
    private final DepartamentoServicio departamentoServicio = new DepartamentoServicio();
    private final CargoServicio cargoServicio = new CargoServicio();

    private Empleado empleadoSeleccionado;

    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colDepartamento.setCellValueFactory(data -> {
            Departamento departamento = data.getValue().getDepartamento();
            return new SimpleStringProperty(departamento != null ? departamento.getNombre() : "-");
        });
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado().name()));

        comboEstado.setItems(FXCollections.observableArrayList(EstadoEmpleado.values()));

        tablaEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) {
                cargarEnFormulario(seleccionado);
            }
        });

        cargarCombos();
        cargarTabla();
    }

    private record CombosEmpleado(List<Departamento> departamentos, List<Cargo> cargos) {
    }

    private void cargarCombos() {
        TareasFX.ejecutar(
                () -> new CombosEmpleado(departamentoServicio.listarTodos(), cargoServicio.listarTodos()),
                combos -> {
                    comboDepartamento.setItems(FXCollections.observableArrayList(combos.departamentos()));
                    comboCargo.setItems(FXCollections.observableArrayList(combos.cargos()));
                },
                error -> mostrarMensaje("Error al cargar departamentos/cargos: " + error.getMessage(), true));
    }

    private void cargarTabla() {
        TareasFX.ejecutar(
                empleadoServicio::listarTodos,
                lista -> tablaEmpleados.setItems(FXCollections.observableArrayList(lista)),
                error -> mostrarMensaje("Error al cargar empleados: " + error.getMessage(), true));
    }

    private void cargarEnFormulario(Empleado empleado) {
        empleadoSeleccionado = empleado;
        campoNombre.setText(empleado.getNombre());
        campoApellido.setText(empleado.getApellido());
        campoDni.setText(empleado.getDni());
        campoEmail.setText(empleado.getEmail());
        campoFechaNacimiento.setValue(empleado.getFechaNacimiento());
        campoFechaContratacion.setValue(empleado.getFechaContratacion());
        campoSalario.setText(empleado.getSalarioBase().toString());
        comboEstado.setValue(empleado.getEstado());
        comboDepartamento.setValue(empleado.getDepartamento());
        comboCargo.setValue(empleado.getCargo());
    }

    @FXML
    private void onGuardar() {
        try {
            Empleado empleado = leerFormulario(null);
            empleadoServicio.registrar(empleado);
            mostrarMensaje("Empleado registrado correctamente", false);
            cargarTabla();
            onLimpiar();
        } catch (Exception e) {
            mostrarMensaje(e.getMessage(), true);
        }
    }

    @FXML
    private void onActualizar() {
        if (empleadoSeleccionado == null) {
            mostrarMensaje("Selecciona un empleado de la tabla primero", true);
            return;
        }
        try {
            Empleado empleado = leerFormulario(empleadoSeleccionado.getId());
            empleadoServicio.actualizar(empleado);
            mostrarMensaje("Empleado actualizado correctamente", false);
            cargarTabla();
            onLimpiar();
        } catch (Exception e) {
            mostrarMensaje(e.getMessage(), true);
        }
    }

    @FXML
    private void onEliminar() {
        if (empleadoSeleccionado == null) {
            mostrarMensaje("Selecciona un empleado de la tabla primero", true);
            return;
        }
        empleadoServicio.eliminar(empleadoSeleccionado.getId());
        mostrarMensaje("Empleado eliminado", false);
        cargarTabla();
        onLimpiar();
    }

    @FXML
    private void onLimpiar() {
        empleadoSeleccionado = null;
        campoNombre.clear();
        campoApellido.clear();
        campoDni.clear();
        campoEmail.clear();
        campoFechaNacimiento.setValue(null);
        campoFechaContratacion.setValue(null);
        campoSalario.clear();
        comboEstado.setValue(null);
        comboDepartamento.setValue(null);
        comboCargo.setValue(null);
        tablaEmpleados.getSelectionModel().clearSelection();
    }

    @FXML
    private void onVolver() {
        NavegadorVistas.cambiarVista("/vista/MenuPrincipalView.fxml", "RRHH - Menú principal");
    }

    private Empleado leerFormulario(Integer id) {
        EstadoEmpleado estado = comboEstado.getValue() != null ? comboEstado.getValue() : EstadoEmpleado.ACTIVO;
        BigDecimal salario = new BigDecimal(campoSalario.getText().trim());
        LocalDate fechaContratacion = campoFechaContratacion.getValue() != null
                ? campoFechaContratacion.getValue() : LocalDate.now();

        return new Empleado(id, campoNombre.getText().trim(), campoApellido.getText().trim(),
                campoDni.getText().trim(), campoEmail.getText().trim(), campoFechaNacimiento.getValue(),
                fechaContratacion, salario, estado, comboDepartamento.getValue(), comboCargo.getValue());
    }

    private void mostrarMensaje(String texto, boolean esError) {
        labelMensaje.setText(texto);
        labelMensaje.getStyleClass().removeAll("error-label", "exito-label");
        labelMensaje.getStyleClass().add(esError ? "error-label" : "exito-label");
    }
}
