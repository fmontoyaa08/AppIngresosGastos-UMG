package edu.umg.controlingresosgastos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var btnPerfil: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNuevoMovimiento = findViewById<Button>(R.id.btnNuevoMovimiento)
        val btnAnalisis = findViewById<Button>(R.id.btnAnalisis)
        btnPerfil = findViewById<ImageButton>(R.id.btnPerfil)

        val tvTotalIngresos = findViewById<TextView>(R.id.tvTotalIngresos)
        val tvTotalGastos = findViewById<TextView>(R.id.tvTotalGastos)
        val tvBalanceTotal = findViewById<TextView>(R.id.tvBalanceTotal)
        val tvFechaActual = findViewById<TextView>(R.id.tvFechaActual)

        // Mostrar la fecha actual formateada
        mostrarFechaActual(tvFechaActual)

        // Botón para registrar un nuevo movimiento (Gasto o Ingreso)
        btnNuevoMovimiento?.setOnClickListener {
            val opciones = arrayOf("Registrar Gasto", "Registrar Ingreso")
            AlertDialog.Builder(this)
                .setTitle("¿Qué deseas registrar?")
                .setItems(opciones) { _, welche ->
                    val intent = Intent(this, FormularioGastoActivity::class.java)
                    if (welche == 1) {
                        intent.putExtra("TIPO_MOVIMIENTO", "INGRESO")
                    } else {
                        intent.putExtra("TIPO_MOVIMIENTO", "GASTO")
                    }
                    startActivity(intent)
                }
                .show()
        }

        // Botón para ir a la pantalla de análisis
        btnAnalisis?.setOnClickListener {
            try {
                val intent = Intent(this, AnalisisActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                // Control de excepciones
            }
        }

        // Botón circular de perfil para abrir PerfilActivity
        btnPerfil?.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        // Cargar datos al iniciar
        cargarYSumarMovimientos(tvTotalIngresos, tvTotalGastos, tvBalanceTotal)
        cargarFotoPerfilPrincipal()
    }

    override fun onResume() {
        super.onResume()
        val tvTotalIngresos = findViewById<TextView>(R.id.tvTotalIngresos)
        val tvTotalGastos = findViewById<TextView>(R.id.tvTotalGastos)
        val tvBalanceTotal = findViewById<TextView>(R.id.tvBalanceTotal)

        cargarYSumarMovimientos(tvTotalIngresos, tvTotalGastos, tvBalanceTotal)
        cargarFotoPerfilPrincipal()
    }

    private fun mostrarFechaActual(tvFecha: TextView?) {
        try {
            val fechaStr = SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale("es", "ES")).format(Date())
            val fechaCapitalizada = fechaStr.replaceFirstChar { it.uppercase() }
            tvFecha?.text = fechaCapitalizada
        } catch (e: Exception) {
            tvFecha?.text = "Hoy"
        }
    }

    private fun cargarFotoPerfilPrincipal() {
        val sharedPreferences = getSharedPreferences("PerfilPrefs", MODE_PRIVATE)
        val rutaImagen = sharedPreferences.getString("ruta_foto", null)

        if (!rutaImagen.isNullOrEmpty()) {
            try {
                val uri = Uri.parse(rutaImagen)
                try {
                    contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } catch (e: Exception) {
                    // Ignorar si ya tiene permisos
                }
                btnPerfil.setImageURI(uri)
            } catch (e: Exception) {
                // Si la URI falla
            }
        }
    }

    private fun cargarYSumarMovimientos(tvIngresos: TextView?, tvGastos: TextView?, tvBalance: TextView?) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val database = androidx.room.Room.databaseBuilder(
                    applicationContext,
                    edu.umg.controlingresosgastos.datos.AppDatabase::class.java,
                    "gastos.db"
                ).build()

                val listaMovimientos = database.gastoDao().listar()

                var totalIngresos = 0.0
                var totalGastos = 0.0

                for (mov in listaMovimientos) {
                    val descripcion = mov.descripcion ?: ""
                    val monto = mov.monto

                    if (descripcion.contains("INGRESO", ignoreCase = true)) {
                        totalIngresos += monto
                    } else {
                        totalGastos += monto
                    }
                }

                val balanceFinal = totalIngresos - totalGastos

                val porcentaje = if (totalIngresos > 0.0) {
                    ((totalGastos / totalIngresos) * 100).toInt().coerceIn(0, 100)
                } else {
                    0
                }

                withContext(Dispatchers.Main) {
                    tvIngresos?.text = "Q %.2f".format(totalIngresos)
                    tvGastos?.text = "Q %.2f".format(totalGastos)
                    tvBalance?.text = "Q %.2f".format(balanceFinal)

                    val progressBar = findViewById<ProgressBar>(R.id.progressBarPorcentaje)
                    val tvPorcentajeTexto = findViewById<TextView>(R.id.tvPorcentajeTexto)

                    progressBar?.progress = porcentaje
                    tvPorcentajeTexto?.text = "Has gastado el $porcentaje% de tus ingresos"
                }
            } catch (e: Exception) {
                // Manejo de errores silencioso
            }
        }
    }
}