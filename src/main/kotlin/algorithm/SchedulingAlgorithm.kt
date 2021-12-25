package algorithm

import simulation.SimulationResult
import task.TaskDefinition

interface SchedulingAlgorithm {
    fun run(tasks: List<TaskDefinition>, availableNodesNumber: Int, C: Double = 1.05): SimulationResult
}