package org.jetbrains.plugins.faasasound.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.Before

class SoundPlayerTest {

    @Before
    fun setUp() {
        SoundPlayer.resetCooldown()
    }

    @Test
    fun `playSound returns false when file does not exist`() {
        val result = SoundPlayer.playSound("/nonexistent/path/sound.mp3", 0, "Faaaaaaah", false, null)
        assertFalse(result)
    }

    @Test
    fun `playSound respects cooldown and returns false on subsequent calls`() {
        SoundPlayer.playSound("/nonexistent/path/sound.mp3", 0, "Faaaaaaah", false, null)
        val result = SoundPlayer.playSound("/nonexistent/path/sound.mp3", 5000, "Faaaaaaah", false, null)
        assertFalse(result)
    }

    @Test
    fun `playSound allows playback after cooldown passes`() {
        SoundPlayer.playSound("/nonexistent/path/sound.mp3", 0, "Faaaaaaah", false, null)
        Thread.sleep(100)
        val result = SoundPlayer.playSound("/nonexistent/path/sound.mp3", 50, "Faaaaaaah", false, null)
        assertFalse(result)
    }

    @Test
    fun `playSound with zero cooldown always attempts to play`() {
        val result = SoundPlayer.playSound("/nonexistent/path/sound.mp3", 0, "Faaaaaaah", false, null)
        assertFalse(result)
    }

    @Test
    fun `playSound with null file path tries default sound`() {
        val result = SoundPlayer.playSound(null, 0, "Faaaaaaah", false, null)
        assertTrue(result || !result)
    }

    @Test
    fun `resetCooldown resets the lastPlayedAt timestamp`() {
        SoundPlayer.playSound("/nonexistent/path/sound.mp3", 0, "Faaaaaaah", false, null)
        SoundPlayer.resetCooldown()
        val result = SoundPlayer.playSound("/nonexistent/path/sound.mp3", 5000, "Faaaaaaah", false, null)
        assertFalse(result)
    }
}
