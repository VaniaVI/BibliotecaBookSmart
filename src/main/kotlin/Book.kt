package org.example

open class Book(
    val title: String,
    val author:String,
    val category:String,
    val basePrice:Int,
    val loanDays: Int,
){
    init {
        require(basePrice >= 0) { "El precio base del libro no puede ser negativo" }
        require(loanDays >= 0) { "Los dias de prestamos no puede ser negativo" }
    }

    // Propiedad que indica si el libro es prestable según los días de préstamo (> 0)
    // Para que las clases hijas puedan modificar se crean Open
    open val isLoanable: Boolean
        get() = loanDays > 0

    open fun finalCost(): Int = basePrice

    open fun description(): String =
        "$title - $author | Category: $category | $loanDays days | Base price: $basePrice"
}

// Clase hija que representa un libro físico, que puede ser de consulta o prestable
class PhysicalBook(
    title: String,
    author: String,
    category: String,
    basePrice: Int,
    loanDays: Int,
    val isReference: Boolean // Si es verdadero, el libro no se puede prestar
) : Book(title, author, category, basePrice, loanDays) {

    // Sobrescribe/Anula el estado de préstamo
    // Solo se puede prestar si NO es de referencia y tiene días de préstamo válidos
    override val isLoanable: Boolean
        get() = !isReference && super.isLoanable

    // Los libros de referencia no tienen costo; los demás utilizan el precio base.
    override fun finalCost(): Int =
        if (isReference)
            0
        else
            basePrice

    // descripcion basada en el estado de referencia
    override fun description(): String {
        val type = if (isReference) "Reference (NOT loanable)" else "Physical"
        return "[$type] " + super.description()
    }
}

// Clase hija que representa un libro digital, que puede tener restricciones DRM.
// DRM (Digital Rights Management) es una tecnología que se usa para proteger contenidos digitales
// como libros, música o películas, limitando cómo pueden ser usados, copiados o compartidos

class DigitalBook(
    title: String,
    author: String,
    category: String,
    basePrice: Int,
    loanDays: Int,
    val drm: Boolean = true // si es verdadero, el libro tiene proteccion, restringiendolo de plataformas, sin poder compartirlo.
) : Book(title, author, category, basePrice, loanDays) {

    // Se aplica un descuento del 5 % si el libro no tiene DRM.
    override fun finalCost(): Int =
        if (drm) basePrice else (basePrice * 95) / 100

    // Se añade una descripcion dependiendo del estado del DRM
    override fun description(): String {
        val label = if (drm) "Digital (with DRM)" else "Digital (no DRM)"
        return "[$label] " + super.description()
    }
}