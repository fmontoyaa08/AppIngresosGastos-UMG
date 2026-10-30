package edu.umg.controlingresosgastos

object CategoriaUtils {
    fun obtenerEmojiCategoria(descripcion: String): String {
        val desc = descripcion.lowercase()
        return when {
            // Ingresos
            desc.contains("alquiler") || desc.contains("renta") -> "🏠"
            desc.contains("sistema") || desc.contains("software") || desc.contains("programa") -> "💻"
            desc.contains("ganado") -> "🐄"

            // Egresos
            desc.contains("restaurante") || desc.contains("comida") -> "🍽️"
            desc.contains("transporte") || desc.contains("uber") -> "🚌"
            desc.contains("super") || desc.contains("mercado") || desc.contains("despensa") -> "🛒"
            desc.contains("ropa") -> "👗"
            desc.contains("gasolina") -> "⛽"
            desc.contains("gas") -> "🔥"
            desc.contains("camioneta") || desc.contains("carro") || desc.contains("auto") -> "🚗"
            desc.contains("salud") || desc.contains("medico") || desc.contains("farmacia") -> "🏥"
            desc.contains("prestamo") || desc.contains("banco") -> "🏦"
            desc.contains("transferencia") -> "💸"

            else -> "📌" // Emoji por defecto
        }
    }
}