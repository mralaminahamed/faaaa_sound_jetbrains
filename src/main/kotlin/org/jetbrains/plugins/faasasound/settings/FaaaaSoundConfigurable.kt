package org.jetbrains.plugins.faasasound.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSeparator

class FaaaaSoundConfigurable : Configurable {
    private var enabledCheckBox: JBCheckBox? = null
    private var onTestFailureCheckBox: JBCheckBox? = null
    private var onErrorsCheckBox: JBCheckBox? = null
    private var cooldownSpinner: JBIntSpinner? = null
    private var customPhraseField: JBTextField? = null
    private var readErrorMessageCheckBox: JBCheckBox? = null
    private var soundFilePathField: TextFieldWithBrowseButton? = null
    private var mainPanel: JPanel? = null

    override fun getDisplayName() = "Faaaa Sound"

    override fun createComponent(): JPanel {
        enabledCheckBox = JBCheckBox("Enable Faaaa Sound plugin")
        enabledCheckBox?.isSelected = true

        onTestFailureCheckBox = JBCheckBox("Play sound when tests fail")
        onTestFailureCheckBox?.isSelected = true

        onErrorsCheckBox = JBCheckBox("Play sound on new errors")
        onErrorsCheckBox?.isSelected = false

        readErrorMessageCheckBox = JBCheckBox("Read error message before playing sound")
        readErrorMessageCheckBox?.isSelected = false

        cooldownSpinner = JBIntSpinner(2500, 0, 60000, 100)
        
        customPhraseField = JBTextField("Faaaaaaah", 20)

        soundFilePathField = TextFieldWithBrowseButton()
        soundFilePathField?.addBrowseFolderListener(
            "Select Sound File",
            "Choose a sound file to play (mp3, wav)",
            null,
            FileChooserDescriptorFactory.createSingleFileDescriptor()
        )

        mainPanel = JPanel(GridBagLayout())
        val gbc = GridBagConstraints()
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.weightx = 1.0
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.insets = java.awt.Insets(2, 4, 2, 4)
        gbc.anchor = GridBagConstraints.WEST

        mainPanel?.add(enabledCheckBox, gbc.clone())
        mainPanel?.add(JSeparator(), gbc.clone())
        mainPanel?.add(onTestFailureCheckBox, gbc.clone())
        mainPanel?.add(onErrorsCheckBox, gbc.clone())
        mainPanel?.add(readErrorMessageCheckBox, gbc.clone())
        
        gbc.gridwidth = 1
        gbc.weightx = 0.0
        mainPanel?.add(JLabel("Cooldown (ms):"), gbc.clone())
        
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.weightx = 1.0
        mainPanel?.add(cooldownSpinner, gbc.clone())
        
        gbc.gridwidth = 1
        gbc.weightx = 0.0
        mainPanel?.add(JLabel("Custom phrase:"), gbc.clone())
        
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.weightx = 1.0
        mainPanel?.add(customPhraseField, gbc.clone())
        
        gbc.gridwidth = 1
        gbc.weightx = 0.0
        mainPanel?.add(JLabel("Sound file:"), gbc.clone())
        
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.weightx = 1.0
        mainPanel?.add(soundFilePathField, gbc.clone())

        return mainPanel!!
    }

    override fun isModified(): Boolean {
        val settings = AppSettings.getInstance()
        return enabledCheckBox?.isSelected != settings.enabled ||
                onTestFailureCheckBox?.isSelected != settings.onTestFailure ||
                onErrorsCheckBox?.isSelected != settings.onErrors ||
                cooldownSpinner?.number != settings.cooldownMs ||
                customPhraseField?.text != settings.customPhrase ||
                readErrorMessageCheckBox?.isSelected != settings.readErrorMessage ||
                soundFilePathField?.text != settings.soundFilePath
    }

    override fun apply() {
        val settings = AppSettings.getInstance()
        settings.enabled = enabledCheckBox?.isSelected ?: true
        settings.onTestFailure = onTestFailureCheckBox?.isSelected ?: true
        settings.onErrors = onErrorsCheckBox?.isSelected ?: false
        settings.cooldownMs = cooldownSpinner?.number ?: 2500
        settings.customPhrase = customPhraseField?.text ?: "Faaaaaaah"
        settings.readErrorMessage = readErrorMessageCheckBox?.isSelected ?: false
        settings.soundFilePath = soundFilePathField?.text ?: ""
    }

    override fun reset() {
        val settings = AppSettings.getInstance()
        enabledCheckBox?.isSelected = settings.enabled
        onTestFailureCheckBox?.isSelected = settings.onTestFailure
        onErrorsCheckBox?.isSelected = settings.onErrors
        cooldownSpinner?.number = settings.cooldownMs
        customPhraseField?.text = settings.customPhrase
        readErrorMessageCheckBox?.isSelected = settings.readErrorMessage
        soundFilePathField?.text = settings.soundFilePath
    }
}
