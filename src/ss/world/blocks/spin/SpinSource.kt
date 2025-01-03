package ss.world.blocks.spin

import arc.scene.ui.layout.*
import mindustry.world.*

class SpinSource(name: String, var defaultProducedSpins: Float, var defaultStressCapacity: Float) : SpinBlock(name) {

    init {
        configurable = true
        update = true
        produceSpin = true
        consumeSpin = false
        stress = 0f
    }

    override fun getGeneratedSpins(tile: Tile): Float {
        val build = tile.build
        return if (build is SpinSourceBuild) build.producedSpins else defaultProducedSpins
    }

    override fun getMaxStress(tile: Tile): Float {
        val build = tile.build
        return if (build is SpinSourceBuild) build.stressCapacity else defaultStressCapacity
    }

    inner class SpinSourceBuild : SpinBuild() {
        var producedSpins: Float = defaultProducedSpins
        var stressCapacity: Float = defaultStressCapacity

        override fun buildConfiguration(table: Table) {
            super.buildConfiguration(table)

            table.add("Produced Spins:").row()
            table.slider(0f, 200f, 1f, producedSpins) { value ->
                configure("spins|$value")
            }.row()

            table.add("Stress Capacity:").row()
            table.slider(0f, 200f, 5f, stressCapacity) { value ->
                configure("stress|$value")
            }.row()
        }

        override fun configure(value: Any?) {
            if (value is String) {
                val parts = value.split("|")
                if (parts.size == 2) {
                    val param = parts[0]
                    val newValue = parts[1].toFloatOrNull() ?: return

                    when (param) {
                        "spins" -> {
                            producedSpins = newValue
                            module.graph?.calculate()
                        }
                        "stress" -> {
                            stressCapacity = newValue
                            module.graph?.calculate()
                        }
                    }
                }
            }
        }
    }
}
