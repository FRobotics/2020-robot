package frc.robot.subsystem.base

abstract class Subsystem (var state: Int, private val stateMap: Map<Int, Int>) {

    private var startTime: Long = 0
    var stateQueue: MutableList<Int> = mutableListOf()

    abstract fun onPeriodic()

    fun periodic() {
        val stateTime = stateMap[state] ?: -1
        if(stateTime != -1 && System.currentTimeMillis() - startTime > stateTime) {
            state = stateQueue.removeAt(0)
            startTime = System.currentTimeMillis()
        }
        onPeriodic()
    }
}