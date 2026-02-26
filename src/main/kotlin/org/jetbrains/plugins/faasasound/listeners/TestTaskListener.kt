package org.jetbrains.plugins.faasasound.listeners

import com.intellij.execution.ExecutionListener
import com.intellij.execution.ExecutionManager
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.jetbrains.plugins.faasasound.util.SoundPlayer

class TestTaskListener : StartupActivity.DumbAware {
    private val LOG = Logger.getInstance(TestTaskListener::class.java)

    override fun runActivity(project: Project) {
        LOG.info("Faaaa Sound plugin activated for project: ${project.name}")

        project.messageBus.connect().subscribe(ExecutionManager.EXECUTION_TOPIC, object : ExecutionListener {
            override fun processStarted(executorId: String, environment: ExecutionEnvironment, handler: ProcessHandler) {
                LOG.info("Test process started: ${environment.runProfile.name}")
            }

            override fun processTerminated(executorId: String, environment: ExecutionEnvironment, handler: ProcessHandler, exitCode: Int) {
                val isTest = environment.runProfile.name.contains("Test", ignoreCase = true)
                if (isTest && exitCode != 0) {
                    LOG.info("Test failed with exit code: $exitCode, configuration: ${environment.runProfile.name}")
                    SoundPlayer.playSound(null)
                }
            }
        })
    }
}
