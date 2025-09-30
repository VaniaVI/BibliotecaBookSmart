package org.example

// Clase sellada que representa los posibles estados de un préstamo
// Esto hace que el compilador obliga a manejar todos los casos en un when.
// Se puede usar object para estados sin datos, y data class para estados con información adicional

sealed class LoanState {
    object Pending : LoanState(){
        override fun toString(): String {
            return "Préstamo pendiente"
        }
    }
    object Loaned : LoanState() {
        override fun toString(): String {
            return "En Prestamo"
        }
    }
    object Returned : LoanState(){
        override fun toString(): String {
            return "Préstamo devuelto"
        }
    }
    // Estado de error muestra un mssje cuando ocurre un problema en el proceso
    data class Error(val message: String) : LoanState()
}