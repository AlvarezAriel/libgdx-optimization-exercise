package ar.edu.unq

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import java.util.*

class OptimizationExamples : ApplicationAdapter() {

    companion object {
        const val MAX_PARTICLES = 50_000
    }

    lateinit var batch: SpriteBatch
    lateinit var fpsLabel: FpsLabel
    lateinit var shapeRenderer: ShapeRenderer

    val particles = mutableListOf<Particle>()
    val walls = mutableListOf<Wall>()
    val cursor = Vector2()
    val aux = Vector2()

    override fun create() {
        generateParticles()
        generateWalls()

        batch = SpriteBatch()
        Particle.img = Texture("particle.png")
        Particle.green = Texture("green.png")
        Wall.img = Texture("wall.jpg")
        fpsLabel = FpsLabel()
        shapeRenderer = ShapeRenderer()
    }

    fun generateWalls() {
        walls.add(Wall.createFromTo(
                200, 200, 500, 50
        ))
        walls.add(Wall.createFromTo(
                800, 10, 40, 500
        ))
        walls.add(Wall.createFromTo(
                30, 500, 200, 150
        ))
        walls.add(Wall.createFromTo(
                900, 700, 400, 20
        ))
        walls.add(Wall.createFromTo(
                400, 400, 50, 50
        ))
    }

    fun generateParticles() {
        val random = Random()

        for (i in 0..MAX_PARTICLES) {
            val particle = Particle()
            this.particles.add(particle)
            particle.pos.set(
                    random.nextInt(Gdx.graphics.width).toFloat(),
                    random.nextInt(Gdx.graphics.height).toFloat()
            )
            particle.size = random.nextFloat()
        }
    }

    override fun render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        cursor.set(Gdx.input.x.toFloat(), Gdx.graphics.height - Gdx.input.y.toFloat())

        batch.begin()

        walls.forEach {
            it.render(shapeRenderer)
        }

        batch.end()

        batch.begin()

        particles.forEach {

            if (it.checked) {
                moveParticles(it)

                applyWallCollisions(it)

                applyEdgeCollisions(it)

                it.render(batch)

            }
        }

        batch.end()

        batch.begin()
        particles.forEach {

            if (!it.checked) {
                moveParticles(it)

                applyWallCollisions(it)

                applyEdgeCollisions(it)

                it.render(batch)
            }

        }

        fpsLabel.showFps(batch)

        batch.end()
    }

    fun moveParticles(it: Particle) {
        val delta = Gdx.graphics.deltaTime
        val dst = cursor.dst(it.pos)

        aux.set(it.pos)

        if (dst < 100) {
            it.vel.add(aux.sub(cursor).nor().scl((100 / dst) * 20 * delta))
        }

        if (it.vel.len2() < 0.01) {
            it.vel.set(0f, 0f)
        } else {
            aux.set(it.vel)
            val friction = aux.nor().scl(-1f * delta)

            it.vel.add(friction)
        }

        aux.set(it.vel).scl(delta)

        it.pos.add(aux)
    }

    private inline fun applyWallCollisions(p: Particle) {
        walls.forEach {

            // collision heuristic
            if (it.rec.contains(p.pos)) {

                p.checked = true

                val deltaX1 = Math.abs(p.pos.x - it.rec.x)
                val deltaX2 = Math.abs(p.pos.x - (it.rec.x + it.rec.width))
                val deltaY1 = Math.abs(p.pos.y - it.rec.y)
                val deltaY2 = Math.abs(p.pos.y - (it.rec.y + it.rec.height))

                if (deltaX1 < deltaX2 && deltaX1 < deltaY1 && deltaX1 < deltaY2) {
                    p.pos.x = it.rec.x
                    p.vel.x = -p.vel.x
                } else if (deltaX2 < deltaX1 && deltaX2 < deltaY1 && deltaX2 < deltaY2) {
                    p.pos.x = it.rec.x + it.rec.width
                    p.vel.x = -p.vel.x
                } else if (deltaY1 < deltaX1 && deltaY1 < deltaX2 && deltaY1 < deltaY2) {
                    p.pos.y = it.rec.y
                    p.vel.y = -p.vel.y
                } else if (deltaY2 < deltaX2 && deltaY2 < deltaX2 && deltaY2 < deltaY1) {
                    p.pos.y = it.rec.y + it.rec.height
                    p.vel.y = -p.vel.y
                }

            }
        }
    }

    private inline fun applyEdgeCollisions(it: Particle) {
        if (it.pos.x < 0) {
            it.pos.x = -it.pos.x
            it.vel.x = -it.vel.x
        }

        if (it.pos.x > Gdx.graphics.width) {
            it.pos.x = Gdx.graphics.width - Math.abs(Gdx.graphics.width - it.pos.x)
            it.vel.x = -it.vel.x
        }

        if (it.pos.y < 0) {
            it.pos.y = -it.pos.y
            it.vel.y = -it.vel.y
        }

        if (it.pos.y > Gdx.graphics.height) {
            it.pos.y = Gdx.graphics.height - Math.abs(Gdx.graphics.height - it.pos.y)
            it.vel.y = -it.vel.y
        }
    }



    override fun dispose() {
        batch.dispose()
        Particle.img.dispose()
    }
}