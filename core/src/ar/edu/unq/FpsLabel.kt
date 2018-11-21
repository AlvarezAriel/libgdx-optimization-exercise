package ar.edu.unq

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class FpsLabel {
    var acum = 0f
    var lastFps = 0
    var lastDelta = 0f
    var font: BitmapFont = BitmapFont()

    init {
        font.setColor(1f, 0.2f, 0f, 1f)
    }

    inline fun showFps(batch:SpriteBatch) {
        val delta = Gdx.graphics.deltaTime

        acum += delta
        (lastDelta + delta) / 2

        if (acum > 1) {
            acum = 0f
            lastFps = (1 / delta).toInt()
        }

        lastDelta = delta
        font.draw(batch, "$lastFps", 20f, 50f)
    }
}