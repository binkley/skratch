package hm.binkley.labs.skratch.contexts

fun main(args: Array<String>) {
    println("== CREATING A DEFAULT FAKE TRANSACTION. SHOULD SUCCEED")
    transactional {
        writeAuditLog("Audit this")
    }

    println("== REUSING AN EXISTING TRANSACTION. SHOULD SUCCEED")
    val activeTx = FakeTransaction.open()
    with(activeTx) {
        transactional {
            writeAuditLog("Audit this")
        }
    }

    println("== FAILING WITH A CLOSED TRANSACTION. SHOULD FAIL WITH EXCEPTION")
    val closedTx = FakeTransaction.open()
    println("== EXPLICITLY CLOSING TRANSACTION WITH ROLLBACK")
    closedTx.rollback()
    with(closedTx) {
        try {
            transactional {
                writeAuditLog("Audit this")
            }
        } catch (e: Exception) {
            println("Found a failure: $e")
        }
    }
}

context(activeTx: Transaction)
fun writeAuditLog(message: String) {
    println("== I HAVE A TRANSACTION: $activeTx")
    println("AUDIT: $message")
}
