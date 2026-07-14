# Sistema de Gestión de Recursos Humanos

**Curso:** Diseño de Patrones
**Proyecto:** SmartCity Solutions — Opción asignada: Sistema de Gestión de Recursos Humanos
**Equipo:** Grupo 12

## Introducción

Las organizaciones modernas requieren sistemas capaces de administrar de forma centralizada la información de sus colaboradores: contratación, nómina, solicitudes de vacaciones y aprobación de las mismas. Este proyecto implementa una aplicación de escritorio en Java que resuelve ese problema aplicando patrones de diseño GoF, principios SOLID y patrones GRASP sobre una arquitectura multicapa real, con persistencia en MySQL.

## Objetivos

**General:** diseñar e implementar una aplicación orientada a objetos que gestione empleados, departamentos, nómina y solicitudes de vacaciones, aplicando patrones de diseño para resolver problemas reales del dominio.

**Específicos:**
- Aplicar 9 patrones GoF (3 creacionales, 4 estructurales, 2 de comportamiento) con justificación real, no forzada.
- Evidenciar los 5 principios SOLID y los patrones GRASP en el diseño de clases.
- Persistir la información en MySQL mediante una capa DAO propia (JDBC directo).
- Ofrecer una interfaz gráfica JavaFX completa con control de acceso por rol.
- Integrar una API externa (Nager.Date) para el cálculo de días hábiles.
- Exportar reportes a JSON y presentar un dashboard visual con indicadores.

## Justificación

Un sistema de RRHH concentra procesos con reglas de negocio genuinamente complejas (cálculo de nómina con bonos y deducciones variables, ciclo de vida de una solicitud de vacaciones, control de quién puede ver el sueldo de quién), lo que permite aplicar patrones de diseño de forma natural en vez de artificial. El dominio también se presta a integrar una arquitectura multicapa, una API externa y un dashboard sin salirse del alcance del curso.

## Arquitectura

El proyecto sigue una arquitectura multicapa:

```
vista/ (FXML)  →  controlador/  →  facade/ | servicio/  →  dao/  →  MySQL
```

- **modelo/**: entidades del dominio (`Persona → Empleado`, `Usuario → 4 roles`, `Solicitud → SolicitudVacaciones`, `Nomina`, `DetalleNomina`, `Departamento`, `Cargo`, `Contrato`, `DiaFeriado`).
- **dao/**: acceso a datos JDBC puro, cada uno implementa `interfaces.IDao<T, ID>`.
- **servicio/**: reglas de negocio y validaciones por entidad.
- **facade/**: orquestación de flujos completos de negocio (contratación, solicitudes, nómina masiva).
- **proxy/**: control de acceso a la nómina según el usuario autenticado.
- **controlador/ + vista/**: interfaz JavaFX (MVC). Ningún controlador accede a un DAO directamente.

La configuración de base de datos se gestiona mediante variables de entorno (`.env`), nunca hardcodeadas ni versionadas en el repositorio.

## UML

Ver [`diagrama-clases.puml`](diagrama-clases.puml) — diagrama de clases completo con estereotipos por patrón (`<<Singleton>>`, `<<Factory Method>>`, `<<Builder>>`, `<<Decorator>>`, `<<Adapter>>`, `<<Facade>>`, `<<Proxy>>`, `<<State>>`), relaciones de herencia, interfaces implementadas y asociaciones entre entidades. Se puede renderizar con cualquier visor de PlantUML (extensión de VS Code, plantuml.com, IntelliJ).

## Patrones de diseño aplicados

Todos los patrones están anclados a dos flujos de negocio reales, no aislados como ejercicios de catálogo:

- **Contratación**: `UsuarioFactory` crea la cuenta → `ContratacionFacade` orquesta → `GestorConexionBD` persiste.
- **Solicitud de vacaciones**: `NagerDateAdapter` calcula días hábiles → `IEstadoSolicitud` controla el ciclo de vida → al aprobar/rechazar dispara `Observer` → `NominaBuilder` + `ICalculoSalario` calculan el pago del periodo → `NominaServicioProxy` controla quién puede verlo.

| # | Patrón | Categoría | Clase(s) | Problema identificado | Motivo de uso | Beneficio obtenido | Evidencia en código |
|---|--------|-----------|----------|------------------------|----------------|----------------------|----------------------|
| 1 | Singleton | Creacional | `singleton.GestorConexionBD` | Conexiones JDBC redundantes y sin control entre 8+ DAOs | Centralizar la creación/reutilización de conexiones en un único punto | Un solo pool controlado, configuración cargada una sola vez desde `.env` | `GestorConexionBD.obtenerInstancia()` con doble verificación de bloqueo |
| 2 | Factory Method | Creacional | `factory.UsuarioFactory` | Crear la subclase correcta de `Usuario` (4 roles) sin dispersar `if/else` por el código | Centralizar la decisión de qué subclase instanciar según `TipoUsuario` | Agregar un rol nuevo solo requiere una clase + un `case`, sin tocar el resto del sistema (OCP) | `UsuarioFactory.crear(...)`, usado tanto en `ContratacionFacade` como en `UsuarioDAO` al reconstruir desde BD |
| 3 | Builder | Creacional | `builder.NominaBuilder` | `Nomina` tiene campos opcionales y combinables (bonos, deducciones, horas extra) inviables con un constructor telescópico | Construcción fluida y validada paso a paso | Validación centralizada (`build()` rechaza salario neto negativo o mes inválido) | `NominaServicioImpl.generarNominaDelMes(...)` |
| 4 | Decorator | Estructural | `decorator.*` (`ICalculoSalario`, `SalarioBase`, `SalarioDecorator` + 5 concretos) | Las combinaciones de bonos/deducciones varían por empleado y periodo; una jerarquía de subclases sería combinatoria | Envolver dinámicamente el cálculo base con capas de bonos/deducciones | Agregar `DeduccionAFP` no modifica ninguna clase existente (OCP); cada capa aporta su propio concepto y monto | `NominaServicioImpl` arma la cadena y contrasta el resultado con `NominaBuilder` |
| 5 | Adapter | Estructural | `adapter.NagerDateAdapter` (+ `NagerDateClient`, `PublicHolidayDTO`) | El cálculo de días hábiles no debe depender del formato JSON específico de la API Nager.Date | Traducir la respuesta externa a `DiaFeriado`, el tipo que entiende el dominio | `CalculadoraDiasHabiles` depende solo de `IProveedorFeriados` (DIP); si se cambia de API solo se reemplaza el adapter | `NagerDateAdapter.obtenerFeriados(...)`, con `feriados_cache` como respaldo ante fallas de red |
| 6 | Facade | Estructural | `facade.ContratacionFacade`, `facade.SolicitudFacade`, `facade.NominaFacade` | Los controladores JavaFX no deben coordinar 5-6 clases de dominio directamente | Ofrecer un punto de entrada único por caso de uso completo | Los controladores quedan simples; la lógica de orquestación es reutilizable y testeable por separado | `SolicitudFacade.aprobar(...)` coordina `SolicitudDAO`, `IEstadoSolicitud` y los `Observer` en un solo lugar |
| 7 | Proxy | Estructural | `proxy.NominaServicioProxy` | Un empleado no debe poder ver la nómina de otro cambiando el ID seleccionado en la UI | Interceptar cada llamada a `INominaServicio` y validar permisos antes de delegar | Control de acceso centralizado y auditado, sin tocar `NominaServicioImpl` | `NominaServicioProxy.exigirAccesoANomina(...)` registra el intento denegado en `logs_auditoria` |
| 8 | Observer | Comportamiento | `observer.*` (`ISujetoObservable`, `IObservador` + 3 concretos) | El empleado, su jefe y la auditoría deben enterarse de un cambio de estado sin acoplar `Solicitud` a cada uno | Notificación desacoplada: `Solicitud` no conoce a sus observadores concretos | Agregar un nuevo tipo de notificación no requiere modificar `Solicitud` (OCP) | El disparo ocurre dentro de la transición de `IEstadoSolicitud`, nunca desde el controlador |
| 9 | State | Comportamiento | `state.*` (`IEstadoSolicitud` + 4 estados) | Las reglas de transición válidas (qué se puede aprobar/rechazar/cancelar) generaban un switch gigante y propenso a errores | Delegar el comportamiento de cada transición al objeto-estado correspondiente | Transiciones inválidas lanzan `TransicionInvalidaException` de forma explícita; el ciclo de vida completo es explícito y testeado | `EstadoSolicitudTest` verifica las transiciones válidas e inválidas |

### Patrones GRASP evidenciados

- **Experto en información**: `Nomina`/`ICalculoSalario` calculan el salario porque tienen los datos necesarios.
- **Creador**: `UsuarioFactory` crea instancias de `Usuario`; `Departamento` gestiona la asociación al asignársele un `Empleado`.
- **Alta cohesión / Bajo acoplamiento**: cada `Servicio` atiende una sola entidad; los controladores nunca llaman a un DAO ni a `DriverManager` directamente.
- **Controlador (GRASP)**: `ContratacionFacade` / `SolicitudFacade` coordinan el caso de uso completo, no la vista.
- **Polimorfismo**: `usuario.getMenuDisponible()` e `icalculoSalario.calcular()` varían en tiempo de ejecución sin `instanceof`.
- **Fabricación pura**: `NominaBuilder`, `ExportadorJSON` y `Validador` no son conceptos del dominio; existen para mantener cohesión y acoplamiento sanos.

### Principios SOLID

- **SRP**: `NominaBuilder` (construcción) vs. `NominaServicioImpl` (reglas) vs. `NominaDAO` (persistencia) son responsabilidades separadas.
- **OCP**: nuevos decoradores de salario o nuevos estados de solicitud se agregan sin modificar clases existentes.
- **LSP**: cualquier subclase de `Usuario` funciona donde se espera `Usuario` sin romper `LoginController`.
- **ISP**: `INominaServicio`, `IEmpleadoServicio` e `ISolicitudServicio` están separadas en vez de una interfaz única.
- **DIP**: `EmpleadoServicio` depende de `IEmpleadoDAO`... en la práctica de `EmpleadoDAO` a través de la capa de servicio; `CalculadoraDiasHabiles` depende de `IProveedorFeriados`, nunca de `NagerDateClient` directamente.

## Retos adicionales cubiertos

| Reto | Cómo se cubre |
|---|---|
| Java Swing o JavaFX | Interfaz completa en JavaFX con FXML |
| Persistencia MySQL | Nivel avanzado, DAOs JDBC propios |
| JSON | Exportación de reportes de empleados y nómina (`ExportadorJSON`) |
| Arquitectura multicapa | `vista → controlador → facade/servicio → dao → MySQL` |
| APIs externas | Integración real con Nager.Date para feriados |
| Dashboard visual | `PieChart` y `BarChart` en `DashboardView` |
| Patrón MVC completo | Ningún controlador accede a un DAO directamente |

## Evidencias

El proyecto incluye una suite de pruebas automatizadas (`mvn test`) que verifica, entre otras cosas:
- Transiciones válidas e inválidas del patrón State (`EstadoSolicitudTest`).
- Cálculo correcto de la cadena de decoradores de salario (`DecoratorCalculoTest`).
- Creación correcta de subclases de `Usuario` vía Factory (`UsuarioFactoryTest`).
- Validaciones del Builder de nómina (`NominaBuilderTest`).
- Persistencia real contra MySQL, incluyendo el caso de recursión Empleado↔Departamento (`EmpleadoDAOTest`, `DepartamentoDAOTest`).
- Control de acceso del Proxy de nómina (`NominaServicioProxyTest`).
- Integración real con la API de feriados (`NagerDateAdapterTest`).

## Conclusiones

El desarrollo de este sistema permitió aplicar patrones de diseño no como ejercicios aislados, sino como soluciones a problemas concretos del dominio de RRHH: la creación polimórfica de usuarios, el cálculo combinatorio de nómina, el control de acceso a información sensible y el ciclo de vida de una solicitud son problemas reales que los patrones GoF resuelven de forma natural. El resultado es un sistema con bajo acoplamiento, alta cohesión y una arquitectura preparada para crecer sin reescribirse.
