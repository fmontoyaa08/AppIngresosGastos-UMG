package edu.umg.controlingresosgastos // (mantén el paquete que ya tiene tu app de Android)

import android.app.Application

class ControlGastosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Como Dependencias ya maneja la instanciación con 'by lazy',
        // ya no hace falta llamar a ningun método iniciar(this).
    }
}