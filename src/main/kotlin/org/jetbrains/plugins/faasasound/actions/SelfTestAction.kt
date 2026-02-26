package org.jetbrains.plugins.faasasound.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.ui.Messages
import org.jetbrains.plugins.faasasound.util.SoundPlayer

class SelfTestAction : AnAction() {
    private val LOG = Logger.getInstance(SelfTestAction::class.java)

    override fun actionPerformed(e: AnActionEvent) {
        LOG.info("Self test triggered")
        val played = SoundPlayer.playSound(null, 0)
        val message = if (played) "Audio file played" else "Fallback speech used"
        Messages.showInfoMessage(message, "Faaaa Sound Self Test")
    }
}
