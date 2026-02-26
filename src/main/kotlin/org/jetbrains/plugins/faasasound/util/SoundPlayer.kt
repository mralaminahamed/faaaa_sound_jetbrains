package org.jetbrains.plugins.faasasound.util

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.SystemInfo
import java.io.File
import java.nio.file.Path
import kotlin.concurrent.withLock

object SoundPlayer {
    private val LOG = Logger.getInstance(SoundPlayer::class.java)
    @Volatile var lastPlayedAt = 0L
    private val lock = java.util.concurrent.locks.ReentrantLock()

    fun resetCooldown() {
        lastPlayedAt = 0L
    }

    fun playSound(soundFilePath: String?, cooldownMs: Long = 2500, customPhrase: String = "Faaaaaaah", readError: Boolean = false, errorMessage: String? = null): Boolean {
        return lock.withLock {
            val now = System.currentTimeMillis()
            if (now - lastPlayedAt < cooldownMs) {
                LOG.info("Skipped by cooldown: cooldownMs=$cooldownMs")
                return@withLock false
            }
            lastPlayedAt = now

            if (readError && errorMessage != null) {
                LOG.info("Reading error message before sound: $errorMessage")
                speak(errorMessage, customPhrase)
            }

            val filePath = soundFilePath ?: getDefaultSoundPath()
            val file = File(filePath)

            if (!file.exists()) {
                LOG.warn("Sound file not found: $filePath, falling back to speech")
                speak(customPhrase)
                return@withLock false
            }

            val success = playAudioFile(file)
            if (!success) {
                LOG.info("Audio file playback failed, falling back to speech")
                speak(customPhrase)
            }
            return@withLock success
        }
    }

    fun speak(text: String, customPhrase: String = "Faaaaaaah") {
        val sanitized = sanitizeText(text.ifEmpty { customPhrase })
        val command = when {
            SystemInfo.isMac -> listOf("say", sanitized)
            SystemInfo.isWindows -> listOf("powershell", "-Command", 
                "Add-Type -AssemblyName System.Speech; (New-Object System.Speech.Synthesis.SpeechSynthesizer).Speak('$sanitized')")
            else -> listOf("spd-say", sanitized).takeIf { 
                Runtime.getRuntime().exec(arrayOf("which", "spd-say")).waitFor() == 0
            } ?: listOf("espeak", sanitized).takeIf {
                Runtime.getRuntime().exec(arrayOf("which", "espeak")).waitFor() == 0
            } ?: listOf("echo", "-e", "\u0007")
        }

        try {
            val process = Runtime.getRuntime().exec(command.toTypedArray())
            process.waitFor()
            LOG.info("Speech command executed: ${command.first()}")
        } catch (e: Exception) {
            LOG.warn("Speech failed: ${e.message}")
            System.out.print("\u0007")
        }
    }

    private fun playAudioFile(file: File): Boolean {
        val command = when {
            SystemInfo.isMac -> listOf("afplay", file.absolutePath)
            SystemInfo.isWindows -> {
                val escaped = file.absolutePath.replace("'", "''")
                listOf("powershell", "-Command", 
                    "\$p=New-Object -ComObject WMPlayer.OCX;\$m=\$p.newMedia('$escaped');\$p.currentPlaylist.appendItem(\$m);\$p.controls.play();Start-Sleep -Seconds 3")
            }
            else -> {
                val ffplayExists = Runtime.getRuntime().exec(arrayOf("which", "ffplay")).waitFor() == 0
                if (ffplayExists) {
                    listOf("ffplay", "-nodisp", "-autoexit", "-loglevel", "quiet", file.absolutePath)
                } else {
                    listOf("mpg123", file.absolutePath).takeIf {
                        Runtime.getRuntime().exec(arrayOf("which", "mpg123")).waitFor() == 0
                    }
                } ?: return false
            }
        }

        return try {
            val process = Runtime.getRuntime().exec(command.toTypedArray())
            val exitCode = process.waitFor()
            LOG.info("Audio playback exit code: $exitCode")
            exitCode == 0
        } catch (e: Exception) {
            LOG.warn("Audio playback failed: ${e.message}")
            false
        }
    }

    private fun getDefaultSoundPath(): String {
        val pluginHome = System.getProperty("idea.plugins.path") ?: "."
        return "$pluginHome/Faaaa Sound/faaah.mp3"
    }

    private fun sanitizeText(text: String): String {
        return text.replace(Regex("\\s+"), " ").trim().take(300).ifEmpty { "Faaaaaaah" }
    }
}
