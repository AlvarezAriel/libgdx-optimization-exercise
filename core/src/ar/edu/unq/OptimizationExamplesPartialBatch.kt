package ar.edu.unq

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import java.util.*

class OptimizationExamplesPartialBatch : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var img: Texture
    lateinit var font: BitmapFont


    val particles = mutableListOf<Particle>()
    val cursor = Vector2()
    val aux = Vector2()

    var lastDelta = 0f

    override fun create() {
        val random = Random()

        for (i in 0..1_000_000) {
            val particle = Particle()
            this.particles.add(particle)
            particle.pos.set(
                    random.nextInt(Gdx.graphics.width).toFloat(),
                    random.nextInt(Gdx.graphics.height).toFloat()
            )
            particle.size = random.nextFloat() * 16
        }

        batch = SpriteBatch()
        img = Texture("particle.png")
        this.font = BitmapFont()
        font.setColor(1f, 0.2f, 0f, 1f)
    }

    override fun render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val delta = Gdx.graphics.deltaTime

        cursor.set(Gdx.input.x.toFloat(), Gdx.graphics.height-Gdx.input.y.toFloat())


        renderSlice(0, 500_000)


        showFps(delta)

    }

    inline fun renderSlice(from:Int, to:Int) {

        batch.begin()

        val delta = Gdx.graphics.deltaTime

        for (i in from..to) {
            val it = particles[i]

            val dst = cursor.dst(it.pos)

            aux.set(it.pos)

            if (dst < 100) {
                it.vel.add(aux.sub(cursor).nor().scl( (100/dst) * 20 * delta))
            }


            if (it.vel.len2() < 0.01) {
                it.vel.set(0f,0f)
            } else {
                aux.set(it.vel)
                val friction = aux.nor().scl(-1f * delta)

                it.vel.add(friction)
                it.checked = true
            }

            aux.set(it.vel).scl(delta)

            it.pos.add(aux)

//            if (it.checked) {
                batch.draw(img,
                        it.pos.x,
                        it.pos.y,
                        it.size,
                        it.size
                )

//            }

            it.checked = false
        }

        batch.end()

    }

    var acum = 0f
    var lastFps = 0

    inline fun showFps(delta: Float) {
        batch.begin()
        acum += delta
        (lastDelta + delta) / 2

        if (acum > 1) {
            acum = 0f
            lastFps = (1 / delta).toInt()
        }

        lastDelta = delta
        font.draw(batch, "$lastFps", 20f, 50f)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }
}