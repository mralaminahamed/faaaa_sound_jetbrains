package org.jetbrains.plugins.faasasound.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.StoredProperty
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@State(
    name = "FaaaaSoundSettings",
    storages = [com.intellij.openapi.components.Storage("faaaa-sound-settings.xml")]
)
@Service
class AppSettings : PersistentStateComponent<AppSettings> {
    var enabled: Boolean = true
    var onTestFailure: Boolean = true
    var onErrors: Boolean = false
    var cooldownMs: Int = 2500
    var customPhrase: String = "Faaaaaaah"
    var readErrorMessage: Boolean = false
    var soundFilePath: String = ""

    override fun getState(): AppSettings = this

    override fun loadState(state: AppSettings) {
        this.enabled = state.enabled
        this.onTestFailure = state.onTestFailure
        this.onErrors = state.onErrors
        this.cooldownMs = state.cooldownMs
        this.customPhrase = state.customPhrase
        this.readErrorMessage = state.readErrorMessage
        this.soundFilePath = state.soundFilePath
    }

    companion object {
        fun getInstance(): AppSettings = service<AppSettings>()
    }
}
