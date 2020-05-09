package midnight.fortune.oven

class Var(val index: Int)
typealias Subst = Map<Var, Term>

sealed class Term {
    data class TAtom(val value: String): Term()
    data class TPair(val left: Term, val right: Term): Term()
    data class TVar(val variable: Var): Term()
}

class MicroKanren(
    val state: Map<Int, Int>
) {
    
}