package org.example

import kotlinx.coroutines.runBlocking
import java.text.NumberFormat
import java.util.Locale

fun main() = runBlocking {
    println("=== SISTEMA BOOKSMART ===")

    // 1. Initialize catalog from LoanManager
    val catalog = LoanManager.initializeCatalog()

    println("\nCatalogo disponible:")
    catalog.forEachIndexed { i: Int, book: Book ->
        println("${i + 1}. ${book.description()} ")
    }

    // 2. Selección de libros por consola
    println("\nSelecciona los libros para prestamo (numeros separados por coma):")
    val input = readln()
    val selectedIndices = input.split(",").mapNotNull { it.trim().toIntOrNull()?.minus(1) }

    val selectedBooks = selectedIndices.mapNotNull { index ->
        catalog.getOrNull(index)
    }.filter {
        if (it is PhysicalBook && it.isReference) {
            println("- ${it.title} es de referencia y no se puede prestar")
            false
        } else true
    }


    // 3. User type
    println("\nIndica tu tipo de usuario (estudiante/docente/externo):")
    val userType = readln().lowercase()


    // 4. Loan validations
    println("\nValidando solicitud...")
    selectedBooks.forEachIndexed { i: Int, book: Book ->
        if (!book.isLoanable) {
            println("- Libro #${i + 1} (${book.title}) no se puede prestar")
        } else {
            println("- Libro #${i + 1} (${book.title}) OK ✅")
        }
    }

    // 5. Process loan with LoanManager
    println("\nProcesando prestamo...")
    val state = try {
        if (selectedBooks.isNotEmpty()) {
            LoanManager.processLoan(selectedBooks[0])
        } else {
            LoanState.Error("No se seleccionaron libros")
        }
    } catch (e: Exception) {
        println("Error al procesar el prestamo: ${e.message}")
        LoanState.Error("Fallo en el prestamo")
    }

    // 6. Calculate subtotal and apply discount
    val subtotal = selectedBooks.sumOf { it.finalCost() }
    val totalWithDiscount = LoanManager.applyDiscount(subtotal, userType)

    // 7. Calculate penalty
    println("\nHubo retraso en la devolucion? Ingresa los dias de retraso (0 si no hubo):")
    val delayDays = readln().toIntOrNull() ?: 0
    val penalty = try {
        if (selectedBooks.isNotEmpty()) {
            LoanManager.calculateFine(selectedBooks[0], delayDays)
        } else 0
    } catch (e: Exception) {
        println("Error al calcular multa: ${e.message}")
        0
    }

    // 8. Generate report
    println("\n=== RESUMEN DEL PRESTAMO ===")
    LoanManager.generateReport(selectedBooks)
    println("Subtotal: ${subtotal.toCLP()}")
    println("Total con descuento ($userType): ${totalWithDiscount.toCLP()}")
    println("Multa por retraso ($delayDays dias): +${penalty.toCLP()}")
    println("Total final: ${(totalWithDiscount + penalty).toCLP()}")

    println("\nEstado final: $state")
    println("Tiempo estimado para retiro/activacion digital: 3 s.")
}