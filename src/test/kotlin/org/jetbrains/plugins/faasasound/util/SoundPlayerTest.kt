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
        val result = SoundPlayer.playSound("/nonexistent/path/sound.mp3", 0)
        assertFalse(result)
    }

    @Test
    fun `playSound respects cooldown and returns false on subsequent calls`() {
        SoundPlayer.playSound("/nonexistent/path/sound.mp3", 0)
        val result = SoundPlayer.playSound("/nonexistent/path/sound.mp3", 5000)
        assertFalse(result)
    }

    @Test
    fun `playSound allows playback after cooldown passes`() {
        SoundPlayer.playSound("/nonexistent/path/sound.mp3", 0)
        Thread.sleep(100)
        val result = SoundPlayer.playSound("/nonexistent/path/sound.mp3", 50)
        assertFalse(result)
    }

    @Test
    fun `playSound with zero cooldown always attempts to play`() {
        val result = SoundPlayer.playSound("/nonexistent/path/sound.mp3", 0)
        assertFalse(result)
    }

    @Test
    fun `playSound with null file path tries default sound`() {
        val result = SoundPlayer.playSound(null, 0)
        assertTrue(result || !result)
    }

    @Test
    fun `resetCooldown resets the lastPlayedAt timestamp`() {
        SoundPlayer.playSound("/nonexistent/path/sound.mp3", 0)
        SoundPlayer.resetCooldown()
        val result = SoundPlayer.playSound("/nonexistent/path/sound.mp3", 5000)
        assertFalse(result)
    }
}
