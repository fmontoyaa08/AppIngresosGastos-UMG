package edu.umg.controlingresosgastos

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnalisisActivity : AppCompatActivity() {

    private var mostrandoGastos = true
    private var mesSeleccionadoActual = 0

    private lateinit var spinnerMeses: Spinner
    private lateinit var btnTabGastos: Button
    private lateinit var btnTabIngresos: Button
    private lateinit var listViewCategorias: ListView
    private var progressBarPorcentaje: ProgressBar? = null
    private var txtPorcentajeGasto: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analisis)

        spinnerMeses = findViewById(R.id.spinnerMeses)
        btnTabGastos = findViewById(R.id.btnTabGastos)
        btnTabIngresos = findViewById(R.id.btnTabIngresos)
        listViewCategorias = findViewById(R.id.listViewCategorias)

        progressBarPorcentaje =
            findViewById(resources.getIdentifier("progressBarPorcentaje", "id", packageName))
                ?: run {
                    val idGen = resources.getIdentifier("progressBar", "id", packageName)
                    if (idGen != 0) findViewById(idGen) else null
                }

        val idTexto = resources.getIdentifier("txtPorcentajeGasto", "id", packageName)
        txtPorcentajeGasto = if (idTexto != 0) findViewById(idTexto) else null

        val meses = arrayOf(
            "📅 Octubre en adelante ",
            "Octubre",
            "Noviembre",
            "Diciembre"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, meses)
        spinnerMeses.adapter = adapter

        spinnerMeses.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                mesSeleccionadoActual = position
                actualizarVistaAnalisis(mesSeleccionadoActual, mostrandoGastos)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnTabGastos.setOnClickListener {
            mostrandoGastos = true
            actualizarEstilosPestañas()
            actualizarVistaAnalisis(mesSeleccionadoActual, mostrandoGastos)
        }

        btnTabIngresos.setOnClickListener {
            mostrandoGastos = false
            actualizarEstilosPestañas()
            actualizarVistaAnalisis(mesSeleccionadoActual, mostrandoGastos)
        }

        actualizarEstilosPestañas()
        actualizarVistaAnalisis(mesSeleccionadoActual, mostrandoGastos)
    }

    private fun actualizarEstilosPestañas() {
        val colorTealActivo = android.graphics.Color.parseColor("#0F766E")
        val colorGrisInactivo = android.graphics.Color.parseColor("#94A3B8")

        if (mostrandoGastos) {
            btnTabGastos.backgroundTintList =
                android.content.res.ColorStateList.valueOf(colorTealActivo)
            btnTabIngresos.backgroundTintList =
                android.content.res.ColorStateList.valueOf(colorGrisInactivo)
            btnTabGastos.setTextColor(android.graphics.Color.WHITE)
            btnTabIngresos.setTextColor(android.graphics.Color.WHITE)
        } else {
            btnTabIngresos.backgroundTintList =
                android.content.res.ColorStateList.valueOf(colorTealActivo)
            btnTabGastos.backgroundTintList =
                android.content.res.ColorStateList.valueOf(colorGrisInactivo)
            btnTabIngresos.setTextColor(android.graphics.Color.WHITE)
            btnTabGastos.setTextColor(android.graphics.Color.WHITE)
        }
    }

    private fun actualizarVistaAnalisis(filtroMesIndex: Int, esGastoBuscado: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val database = androidx.room.Room.databaseBuilder(
                    applicationContext,
                    edu.umg.controlingresosgastos.datos.AppDatabase::class.java,
                    "gastos.db"
                ).build()

                val listaEntidades = database.gastoDao().listar()
                val movimientos = listaEntidades ?: emptyList<Any>()

                var totalIngresosGlobal = 0.0
                var totalGastosGlobal = 0.0

                movimientos.forEach { mov ->
                    if (mov != null) {
                        val monto = getMonto(mov)
                        if (esGastoRealObjeto(mov)) {
                            totalGastosGlobal += monto
                        } else {
                            totalIngresosGlobal += monto
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    actualizarBarraPorcentaje(totalGastosGlobal, totalIngresosGlobal)

                    val movimientosFiltrados = movimientos.filter { mov ->
                        if (mov == null) return@filter false

                        val esGastoMov = esGastoRealObjeto(mov)
                        if (esGastoBuscado != esGastoMov) return@filter false

                        val fechaStr = getFecha(mov)
                        val partesFecha = fechaStr.split("-")

                        val coincideMes = if (filtroMesIndex == 0) {
                            val mesMov = partesFecha.getOrNull(1)?.toIntOrNull() ?: 0
                            mesMov >= 10
                        } else {
                            val mesObjetivo = filtroMesIndex + 9
                            val mesMov = partesFecha.getOrNull(1)?.toIntOrNull() ?: 0
                            mesMov == mesObjetivo
                        }

                        coincideMes
                    }

                    val categoriasAgrupadas = movimientosFiltrados.groupBy { mov ->
                        obtenerNombreConEmoji(mov!!, esGastoBuscado)
                    }.map { (categoriaConEmoji, lista) ->
                        val total = lista.sumOf { getMonto(it!!) }
                        "$categoriaConEmoji — Q ${String.format("%.2f", total)}"
                    }

                    val adapter = ArrayAdapter(this@AnalisisActivity, android.R.layout.simple_list_item_1, categoriasAgrupadas)
                    listViewCategorias.adapter = adapter
                }
            } catch (e: Exception) {
                Log.e("DEBUG_FATIMA", "Error cargando análisis de Room: ${e.message}")
            }
        }
    }

    private fun actualizarBarraPorcentaje(gastos: Double, ingresos: Double) {
        try {
            val porcentaje = if (ingresos > 0.0) {
                ((gastos / ingresos) * 100).toInt().coerceIn(0, 100)
            } else {
                if (gastos > 0) 100 else 0
            }

            progressBarPorcentaje?.progress = porcentaje
            txtPorcentajeGasto?.text = "$porcentaje% gastado"
        } catch (e: Exception) {
        }
    }

    private fun esGastoRealObjeto(obj: Any): Boolean {
        if (obj is edu.umg.controlingresosgastos.datos.GastoEntity) {
            val desc = obj.descripcion.uppercase()
            if (desc.contains("INGRESO") || desc.contains("[INGRESO]")) return false
            return true
        }
        val textoUnido = extraerTodoElTexto(obj).uppercase()
        if (textoUnido.contains("[INGRESO]") || textoUnido.contains("INGRESO")) {
            return false
        }
        return true
    }

    private fun extraerTodoElTexto(obj: Any): String {
        if (obj is edu.umg.controlingresosgastos.datos.GastoEntity) {
            return "${obj.descripcion} ${obj.monto}"
        }
        val sb = StringBuilder()
        try {
            for (method in obj.javaClass.methods) {
                if (method.parameterCount == 0 && !method.name.startsWith("getCl")) {
                    val res = method.invoke(obj)
                    if (res != null) {
                        sb.append(" ").append(res.toString())
                    }
                }
            }
        } catch (e: Exception) {
        }
        return sb.toString()
    }
    private fun getMonto(obj: Any): Double {
        return try {
            // Si es directamente una entidad de Room, leemos su propiedad sin reflexión
            if (obj is edu.umg.controlingresosgastos.datos.GastoEntity) {
                return obj.monto
            }
            val getter = obj.javaClass.methods.firstOrNull {
                it.name.equals("getMonto", ignoreCase = true) ||
                        it.name.equals("getValor", ignoreCase = true)
            }
            val res = getter?.invoke(obj)
            when (res) {
                is Number -> res.toDouble()
                is String -> res.toDoubleOrNull() ?: 0.0
                else -> 0.0
            }
        } catch (e: Exception) {
            0.0
        }
    }

    private fun getFecha(obj: Any): String {
        return try {
            if (obj is edu.umg.controlingresosgastos.datos.GastoEntity) {
                return obj.fecha
            }
            val getter = obj.javaClass.methods.firstOrNull {
                it.name.equals("getFecha", ignoreCase = true) ||
                        it.name.equals("getDate", ignoreCase = true)
            }
            getter?.invoke(obj)?.toString() ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun obtenerNombreConEmoji(obj: Any, esGasto: Boolean): String {
        val texto = extraerTodoElTexto(obj).uppercase()

        return if (esGasto) {
            when {
                texto.contains("ROPA") || texto.contains("ZAPATO") || texto.contains("VESTIDO") || texto.contains("ZAPATOS") -> "👕 Ropa"
                texto.contains("EDUCACION") || texto.contains("CURSO") || texto.contains("LIBRO") || texto.contains("ESCUELA") || texto.contains("COLEGIO") || texto.contains("UNIVERSIDAD") -> "🏫 Escuela"
                texto.contains("AGROPECUARIO") || texto.contains("CAMPO") || texto.contains("PLANTA") || texto.contains("AGRO") -> "🌱 Agropecuario"
                texto.contains("TRANSPORTE") || texto.contains("UBER") || texto.contains("BUS") || texto.contains("TAXI") || texto.contains("PASAJE") -> "🚌 Transporte"
                texto.contains("SUPER") || texto.contains("MERCADO") || texto.contains("COMPRAS") || texto.contains("SUPERMERCADO") || texto.contains("DESPENSA") -> "🛒 Supermercado"
                texto.contains("RESTAURANTE") || texto.contains("COMIDA") || texto.contains("ALMUERZO") || texto.contains("HAMBURGUESA") || texto.contains("BURGER") || texto.contains("CENA") || texto.contains("DESAYUNO") -> "🍔 Restaurantes"
                texto.contains("CARRO") || texto.contains("AUTOMOVIL") || texto.contains("VEHICULO") || texto.contains("AUTO") || texto.contains("CUOTA") -> "🚗 Carro"
                texto.contains("GASOLINA") || texto.contains("COMBUSTIBLE") || texto.contains("GASOLINERA") || texto.contains(" GAS ") -> "⛽ Gasolina"
                else -> "📦 Otros Gastos"
            }
        } else {
            when {
                texto.contains("SOFTWARE") || texto.contains("SOFT") || texto.contains("PROGRAMA") || texto.contains("APP") || texto.contains("SISTEMA") -> "💻 Software Negocios"
                texto.contains("SALARIO") || texto.contains("SUELDO") -> "💰 Salario"
                texto.contains("RENTA") || texto.contains("ALQUILER") || texto.contains("CASA") -> "🏠 Renta"
                texto.contains("TRABAJO") || texto.contains("EXTRA") || texto.contains("FREELANCE") -> "🛠️ Trabajos Extras"
                else -> "📦 Otros Ingresos"
            }
        }
    }
}