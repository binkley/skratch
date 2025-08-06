package hm.binkley.labs.skratch.contexts

interface Transaction {
    fun commit(): Transaction

    fun rollback(): Transaction

    val isActive: Boolean
}

class FakeTransaction : Transaction {
    override var isActive = true
        private set

    override fun commit(): Transaction =
        apply {
            println("→ COMMIT")
            isActive = false
        }

    override fun rollback(): Transaction =
        apply {
            println("→ ROLLBACK")
            isActive = false
        }

    override fun toString() = "FakeTransaction(active=$isActive)@${hashCode()}"

    companion object Manager {
        fun open(): FakeTransaction =
            run {
                println("→ OPEN NEW TRANSACTION")
                FakeTransaction()
            }
    }
}

val CLOSED_TRANSACTION = object : RuntimeException("Transaction closed") {}

fun transactional(
    activeTx: Transaction? = null,
    block: context(Transaction) () -> Unit
) {
    val tx =
        if (activeTx != null) {
            println("→ REUSING EXISTING TRANSACTION")
            if (!activeTx.isActive) throw CLOSED_TRANSACTION
            activeTx
        } else {
            println("→ CREATING TRANSACTION ON THE FLY")
            FakeTransaction.open()
        }
    try {
        println("== TRANSACTION: $tx")
        with(tx) {
            if (tx.isActive) {
                println("→ USING OPEN TRANSACTION")
            } else {
                println("→ FAILING WITH PREJUDICE FOR CLOSED TRANSACTION")
                throw CLOSED_TRANSACTION
            }
            block()
        }
        // The block may have already closed the transaction
        if (tx.isActive) tx.commit()
    } catch (e: Exception) {
        // The block may have already closed the transaction
        // The block's calls may have thrown some other exception
        if (tx.isActive) tx.rollback()
        throw e
    }
}
