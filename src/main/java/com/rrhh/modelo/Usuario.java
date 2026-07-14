package com.rrhh.modelo;

import com.rrhh.interfaces.IAutenticable;
import com.rrhh.modelo.enums.OpcionMenu;
import com.rrhh.modelo.enums.Permiso;
import com.rrhh.modelo.enums.TipoUsuario;
import com.rrhh.util.PasswordUtil;

import java.util.List;
import java.util.Set;

public abstract class Usuario implements IAutenticable {

    protected Integer id;
    protected String username;
    protected String passwordHash;
    protected Empleado empleado;
    protected boolean activo;

    protected Usuario(Integer id, String username, String passwordHash, Empleado empleado, boolean activo) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.empleado = empleado;
        this.activo = activo;
    }

    public abstract Set<Permiso> getPermisos();

    public abstract List<OpcionMenu> getMenuDisponible();

    public abstract TipoUsuario getTipoUsuario();

    @Override
    public boolean validarCredenciales(String passwordPlano) {
        return activo && passwordHash.equals(PasswordUtil.hash(passwordPlano));
    }

    public boolean tienePermiso(Permiso permiso) {
        return getPermisos().contains(permiso);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
