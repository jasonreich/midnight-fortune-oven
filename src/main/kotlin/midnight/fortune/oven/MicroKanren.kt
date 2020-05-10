package midnight.fortune.oven

data class Var private constructor(val index: Int) {
    companion object {
        val ZERO = Var(index = 0)
    }

    fun next() = Var(index = index + 1)

    val term = Term.TVar(this)
}

typealias Subst = Map<Var, Term>
data class State(val substitions: Subst, val nextVar: Var)
typealias Goal = (State) -> Sequence<State>

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

fun unify(_left: Term, _right: Term, substitions: Subst): Subst? {
    return when (val left = _left.walk(substitions)) {
        is Term.TAtom -> when (val right = _right.walk(substitions)) {
            is Term.TAtom -> substitions.takeIf { left.value == right.value }
            is Term.TVar -> substitions + (right.variable to left)
        }
        is Term.TVar -> when (val right = _right.walk(substitions)) {
            is Term.TAtom -> substitions + (left.variable to right)
            is Term.TVar -> null
        }
    }
}

object MicroKanren {
    infix fun Term.eq(other: Term): Goal = { state ->
        unify(this, other, state.substitions)?.let { newSubstitions ->
            sequenceOf(state.copy(newSubstitions))
        } ?: emptySequence()
    }

    fun fresh(action: (Term) -> Goal): Goal = { state ->
        action(state.nextVar.term)(state.copy(nextVar = state.nextVar.next()))
    }
}