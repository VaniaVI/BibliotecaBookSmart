package org.example
import kotlinx.coroutines.delay // Import necesario para simular espera en corrutinas

// Objeto que contiene la lógica de negocio para préstamos de libros
object LoanManager {

    // Inicializa el catálogo de libros disponibles en la biblioteca
    fun initializeCatalog(): List<Book> = listOf(
        PhysicalBook("Estructuras de Datos", "Goodrich", "Informatica", basePrice = 12990, loanDays = 7, isReference = false),
        PhysicalBook("Diccionario Enciclopedico", "Varios", "Referencia", basePrice = 15990, loanDays = 0, isReference = true),
        DigitalBook("Programacion en Kotlin", "JetBrains", "Programacion", basePrice = 9990, loanDays = 10, drm = true),
        DigitalBook("Algoritmos Basicos", "Cormen", "Algoritmos", basePrice = 11990, loanDays = 10, drm = false)
    )

    // Aplica descuento según el tipo de usuario (estudiante, docente, externo)
    fun getDiscountRate(userType: String): Double {
        return when (userType.lowercase()) {
            "estudiante" -> 0.10
            "docente" -> 0.15
            else -> 0.0
        }
    }

    fun applyDiscount(subtotal: Int, userType: String): Int {
        val discountRate = getDiscountRate(userType)
        return (subtotal * (1 - discountRate)).toInt()
    }

    // Simula el procesamiento de un préstamo con espera utilizando delay
    suspend fun processLoan(book: Book): LoanState {
        // Si el libro no se puede prestar, retorna un estado de error
        if (!book.isLoanable) {
            return LoanState.Error("El libro '${book.title}' no se puede prestar.")
        }
        delay(3000)
        return LoanState.Loaned
    }

    // Calcula la multa por retraso en la devolución del libro
    fun calculateFine(book: Book, delayDays: Int): Int {
        // Si hay días de retraso, se multiplica por el valor fijo de la multa
        return if (delayDays > 0) {
            val finePerDay = 500
            delayDays * finePerDay
        } else 0 // Sin multa si no hay retraso
    }

    fun generateReport(loanedBooks: List<Book>) {
        println("Reporte de libros prestados:")
        // Filtra los libros que sí se pueden prestar y muestra su descripción
        loanedBooks
            .filter { it.isLoanable }
            .map { it.description() }
            .forEach { println(it) }

        // Calcula el total sin aplicar descuentos
        val total = loanedBooks.sumOf { it.finalCost() }
        println("Total sin descuentos: ${total.toCLP()}")
    }
}