package midnight.fortune.oven

data class Var(val index: Int)
typealias Subst = Map<Var, Term>

sealed class Term {
    abstract fun walk(substitions: Subst): Term

    data class TAtom(val value: String): Term() {
        override fun walk(substitions: Subst) = this
    }
    
    data class TVar(val variable: Var): Term() {
        override fun walk(substitions: Subst) = 
            substitions[variable]?.walk(substitions) ?: this]
    }
})

fun unifyWith(_left: Term, _right: Term, substitions: Subst): Subst? {
    return when (val left = _left.walk(substitions)) {
        is Term.TAtom -> when (val right = _right.walk(substitions)) {
            is Term.TAtom -> substitions.takeIf { left.value == right.value }
            is Term.TVar -> substitions + (right.variable to left)
        }
        is Term.TVar -> TODO()
    }
}

class MicroKanren(
    val state: Map<Int, Int>
) {
    
}