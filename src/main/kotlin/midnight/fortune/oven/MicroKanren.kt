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
            substitions[variable]?.walk(substitions) ?: this
    }
}

class MicroKanren(
    val state: Map<Int, Int>
) {
    
}