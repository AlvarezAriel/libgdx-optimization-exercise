package ar.edu.unq

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle

data class Wall(val rec: Rectangle) {
    companion object {
        lateinit var img: Texture

        fun createFromTo(x1:Int, y1:Int, width: Int, height: Int):Wall {

            val r = Rectangle()

            r.x = x1.toFloat()
            r.y = y1.toFloat()
            r.width = width.toFloat()
            r.height = height.toFloat()

            return Wall(r)
        }
    }

    fun render(shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color.RED
        shapeRenderer.rect(rec.x, rec.y, rec.width, rec.height)
        shapeRenderer.end()
    }
}