import Day24.Wire.InputWire
import Day24.Wire.GatedWire

class Day24 : Day(24, "Crossed Wires") {

    private val zs: List<Wire>

    init {
        val input = parse()
        val sep = input.indexOf("")
        val gateMapping = mapOf(
            "AND" to Gate.And,
            "OR" to Gate.Or,
            "XOR" to Gate.Xor
        )

        // Input wires
        val wireLookup: MutableMap<String, Wire> = input.subList(0, sep).associate {
            val (id, value) = it.split(": ")
            id to InputWire(id, value.toUByte())
        }.toMutableMap()

        // Gated wires
        input.subList(sep + 1, input.size).forEach {
            val (equation, id) = it.split(" -> ")
            val (lhs, gate, rhs) = equation.split(" ")
            val wire = wireLookup.getOrPut(id) { GatedWire(id) }
            if (wire !is GatedWire) throw IllegalStateException("Wire $id already defined as an input wire")
            wire.setLhs(wireLookup.getOrPut(lhs) { GatedWire(lhs) })
            wire.setRhs(wireLookup.getOrPut(rhs) { GatedWire(rhs) })
            wire.setGate(gateMapping[gate] ?: throw IllegalStateException("Unknown gate $gate"))
        }

        zs = wireLookup.filter { it.key.startsWith('z') }.values.sortedWith { a, b -> b.id().compareTo(a.id()) }
    }

    override fun part1(): Any = zs.map { it.eval() }.fold(0L) { acc, byte -> acc shl 1 or byte.toLong() }

    override fun part2(): Any = TODO()

    sealed interface Wire {
        fun eval(): UByte
        fun id(): String

        class InputWire(private val id: String, private val value: UByte) : Wire {
            override fun eval() : UByte = value
            override fun id(): String = id

            override fun toString(): String = "$id: $value"
        }

        class GatedWire(private val id: String) : Wire {

            private lateinit var lhs: Wire
            private lateinit var rhs: Wire
            private lateinit var gate: Gate

            fun setGate(gate: Gate) {
                if (this::gate.isInitialized) throw IllegalStateException("Gate already set")
                this.gate = gate
            }

            fun setLhs(lhs: Wire) {
                if (this::lhs.isInitialized) throw IllegalStateException("Lhs already set")
                this.lhs = lhs
            }

            fun setRhs(rhs: Wire) {
                if (this::rhs.isInitialized) throw IllegalStateException("Rhs already set")
                this.rhs = rhs
            }

            override fun eval() : UByte {
                if (!this::gate.isInitialized) throw IllegalStateException("Gate not set")
                if (!this::lhs.isInitialized) throw IllegalStateException("Lhs not set")
                if (!this::rhs.isInitialized) throw IllegalStateException("Rhs not set")
                return  gate.eval(lhs, rhs)
            }

            override fun id(): String = id

            override fun toString(): String = "${lhs.id()} ${(gate::class.simpleName)?.uppercase()} ${rhs.id()} -> $id"
        }
    }

    sealed interface Gate {
        fun eval(lhs: Wire, rhs: Wire): UByte

        object And : Gate {
            override fun eval(lhs: Wire, rhs: Wire): UByte = lhs.eval() and rhs.eval()
        }

        object Or : Gate {
            override fun eval(lhs: Wire, rhs: Wire): UByte = lhs.eval() or rhs.eval()
        }

        object Xor : Gate {
            override fun eval(lhs: Wire, rhs: Wire): UByte = lhs.eval() xor rhs.eval()
        }
    }
}

fun main() {
    val day = Day24()
    println(day)
    println(day.part1())
//    println(day.part2())
//    println(DayXX().process())
}
