package ar.edu.unq

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

class Particle(
        val pos: Vector2 = Vector2(),
        val vel : Vector2 = Vector2(),
        var size : Float = 1f,
        var checked: Boolean = false
) {

    companion object {
        lateinit var img: Texture
        lateinit var green: Texture
    }

    fun reset() {
        this.pos.set(0f,0f)
        this.vel.set(0f,0f)
    }

    fun render(batch: SpriteBatch) {
        val side = size * 16
        batch.draw(if (checked) green else img,
                pos.x - side/2,
                pos.y - side/2,
                side,
                side
        )
    }

}