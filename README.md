# 📱 PistApp - Executive Finance

**PistApp** es una aplicación móvil desarrollada para Android en **Kotlin**, diseñada bajo un concepto de finanzas ejecutivas. Permite a los usuarios llevar un control riguroso de sus ingresos y gastos personales de manera local y eficiente.

---

## 
- **Estudiante:** Fátima Jacinta Montoya Aguilar
- **Carnet:** 0905-25-24221
- **Universidad:** Universidad Mariano Gálvez de Guatemala, Campus Jutiapa.
- **Facultad:** Ingeniería en Sistemas de Información y Ciencias de la Computación
- **Curso:** Programación II
- **Proyecto:**  Desarrollo de Aplicaciones Móviles Androi

---

## 🛠️ Tecnologías y Arquitectura
- **Lenguaje:** Kotlin
- **IDE:** Android Studio
- **Persistencia de Datos Locales:** 
  - **Room Database** (`gastos.db`) para el almacenamiento estructurado de transacciones (ingresos y gastos).
  - **SharedPreferences** (`PerfilPrefs`) para la persistencia del perfil de usuario y la URI de la foto seleccionada.
- **Concurrencia:** Kotlin Coroutines (`Dispatchers.IO` y `Dispatchers.Main`) para consultas asíncronas a la base de datos sin bloquear la interfaz.
- **Interfaz de Usuario (UI):** XML, Material Components (`MaterialCardView`), `ScrollView`, `RecyclerView` y componentes nativos modernos (`ActivityResultLauncher`).

---

## 🖥️ Documentación de Interfaces y Componentes

### 1. Pantalla Principal (`MainActivity` / `activity_main.xml`)
Es el panel de control principal de la aplicación.
* **Cabecera (Bienvenida y Fecha):** 
  * `tvBienvenida`: Texto de bienvenida institucional.
  * `tvFechaActual`: Muestra de forma dinámica la fecha actual formateada en español (Ej: *Viernes, 30 de octubre de 2026*).
  * `btnPerfil` (ImageButton): Botón circular que abre la vista de configuración del perfil y muestra la foto seleccionada en tiempo real.
* **Tarjeta de Balance General:**
  * `tvBalanceTotal`: Muestra el cálculo matemático en tiempo real del saldo resultante (`Ingresos - Gastos`) en formato de moneda (Quetzales).
* **Tarjetas de Totales:**
  * `tvTotalIngresos`: Sumatoria acumulada de todos los registros marcados como ingresos.
  * `tvTotalGastos`: Sumatoria acumulada de todos los registros de egresos/gastos.
* **Sección de Porcentaje:**
  * `progressBarPorcentaje`: Barra gráfica horizontal que se actualiza dinámicamente según el nivel de gastos.
  * `tvPorcentajeTexto`: Texto descriptivo que indica el porcentaje exacto gastado respecto a los ingresos.
* **Botones de Acción Principal:**
  * `btnNuevoMovimiento`: Despliega un cuadro de diálogo (`AlertDialog`) que permite al usuario elegir entre registrar un nuevo Gasto o un nuevo Ingreso.
  * `btnAnalisis`: Redirecciona a la vista de gráficos y análisis detallado por categorías.

---

### 2. Pantalla de Perfil (`PerfilActivity` / `activity_perfil.xml`)
Permite gestionar la identidad del usuario y sus preferencias locales.
* **Componentes de Imagen:**
  * `imgFotoPerfil` (ImageView): Contenedor de la foto de usuario.
  * `btnCambiarFoto`: Utiliza el contrato moderno `ActivityResultLauncher` con `ACTION_OPEN_DOCUMENT` para abrir el selector de archivos del sistema operativo de manera segura y otorgar permisos de lectura persistentes (`takePersistableUriPermission`).
* **Campos de Texto Editable (`EditText`):**
  * `etNombre`: Captura el nombre del usuario.
  * `etApellido`: Captura el apellido del usuario.
  * `etCorreo`: Captura el correo electrónico institucional o personal.
* **Botón de Guardado:**
  * `btnGuardarPerfil`: Almacena de manera persistente todos los campos de texto y la ruta de la imagen en `SharedPreferences` bajo el archivo `"PerfilPrefs"`, mostrando un aviso flotante (`Toast`) al finalizar.

---

## ⚙️ Lógica de Negocio y Flujo de Datos

1. **Cálculo Financiero:** Cada vez que la actividad principal se crea o se resume (`onResume`), se lanza una corrutina en segundo plano (`Dispatchers.IO`) que consulta la base de datos Room (`gastos.db`) a través del DAO. Clasifica las transacciones leyendo la descripción y actualiza los componentes gráficos en el hilo principal (`Dispatchers.Main`).
2. **Persistencia de Imágenes:** La URI de la imagen seleccionada desde la galería se graba en texto dentro de `SharedPreferences`. Al regresar a la pantalla principal, el método `cargarFotoPerfilPrincipal()` recupera dicho texto, restaura los permisos de lectura de contenido y actualiza el `ImageButton` sin pérdida de datos tras reiniciar la aplicación.

3. ---
---

## 📦 Descarga y Enlaces de Interés
- **Carpeta del Proyecto:** [Haz clic aquí para ver los archivos y el APK en Google Drive](https://drive.google.com/drive/folders/1PJzu7MM1R8QqDv1BH6i6dEkq1lNbDVd_?usp=drive_link)
