package org.jetbrains.plugins.faasasound.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.plugins.faasasound.settings.AppSettings
import org.jetbrains.plugins.faasasound.util.SoundPlayer

class PlaySoundAction : AnAction() {
    private val LOG = Logger.getInstance(PlaySoundAction::class.java)

    override fun actionPerformed(e: AnActionEvent) {
        LOG.info("Manual play sound triggered")
        val settings = AppSettings.getInstance()
        val soundPath = settings.soundFilePath.ifEmpty { null }
        SoundPlayer.playSound(
            soundFilePath = soundPath,
            cooldownMs = 0,
            customPhrase = settings.customPhrase,
            readError = false,
            errorMessage = null
        )
    }
}
