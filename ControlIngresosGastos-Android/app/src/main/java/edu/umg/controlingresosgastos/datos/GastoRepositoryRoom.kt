package edu.umg.controlingresosgastos.datos

import edu.umg.programacion2.gastos.modelo.Gasto
import edu.umg.programacion2.gastos.datos.GastoRepository

class GastoRepositoryRoom(private val dao: GastoDao) : GastoRepository {

    override suspend fun listar(): List<Gasto> {
        return dao.listar().map { it.aGasto() }
    }

    override suspend fun buscarPorId(id: Int): Gasto? {
        return dao.buscarPorId(id)?.aGasto()
    }

    override suspend fun agregar(gasto: Gasto): Gasto {
        dao.insertar(GastoEntity.desde(gasto))
        return gasto
    }

    override suspend fun actualizar(gasto: Gasto): Boolean {
        val filas = dao.actualizar(GastoEntity.desde(gasto))
        return filas > 0
    }

    override suspend fun eliminar(id: Int): Boolean {
        val filasAfectadas = dao.eliminar(id)
        return filasAfectadas > 0
    }
}