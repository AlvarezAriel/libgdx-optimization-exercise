package ar.edu.unq

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FillViewport

class World {
    val width = 32
    val height = 32

    val camera: OrthographicCamera
//    val tiledMap = TiledMap()

    init {
        camera = OrthographicCamera()
        camera.setToOrtho(false)
        FillViewport(width.toFloat(), height.toFloat(), camera)
//        tiledMap.init(width, height)
    }
}