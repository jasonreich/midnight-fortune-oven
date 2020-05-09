package midnight.fortune.oven

data class Var(val index: Int)
typealias Subst = Map<Var, Term>

sealed class Term {
    abstract fun walk(substitions: Subst): Term
    abstract fun unifyWith(other: Term, substitions: Subst): Subst?

    data class TAtom(val value: String): Term() {
        override fun walk(substitions: Subst) = this

        override fun unifyWith(other: Term, substitions: Subst): Subst?
            = when (other) {
                is TAtom -> substitions.takeIf { this.value == other.value }
                is TVar -> mapOf(other.variable to this) + substitions
            }
    }
    
    data class TVar(val variable: Var): Term() {
        override fun walk(substitions: Subst) = 
            substitions[variable]?.walk(substitions) ?: this]

        override fun unifyWith(other: Term, substitions: Subst): Subst?
            = when (other) {
                is TVar -> substitions.takeIf { this.variable == other.variable }
                is TAtom -> mapOf(this.variable to other) + substitions
            }
    }
})

class MicroKanren(
    val state: Map<Int, Int>
) {
    
}