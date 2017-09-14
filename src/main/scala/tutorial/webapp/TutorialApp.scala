package tutorial.webapp

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.dom.html
import scala.util.Random

case class Body(x: Double, y: Double, ux: Double, uy: Double, r: Int, g: Int, b: Int)

@JSExport
object ScalaJSExample {

  val tick = 0.2
  val gravity = 5.0

  val yBoundary = 800
  val xBoundary = 1000

  val boxSize = 50

  val energyLoss = 0.05

  var bodies = List(
    Body(50, 50, 45, 0, 255, 0, 0),
    Body(0, yBoundary - boxSize, 20, -20, 0, 0, 255),
  )

  @JSExport
  def main(canvas: html.Canvas): Unit = {

    val ctx = canvas.getContext("2d")
                    .asInstanceOf[dom.CanvasRenderingContext2D]


    def clear() = {
      ctx.fillStyle = "white"
      ctx.fillRect(0, 0, xBoundary, yBoundary)
    }

    def run = {
      clear

      bodies = bodies.map{ bb =>

        var body = bb

        val r = body.r
        val g = body.g
        val b = body.b

        ctx.fillStyle = s"rgb($r, $g, $b)"

        ctx.fillRect(body.x, body.y, boxSize, boxSize)

        // s = ut + 0.5 * a * t^2
        // v = u + at

        val uy = body.uy
        val ux = body.ux

        body = body.copy(y = body.y + (uy * tick) + (0.5 * gravity * tick * tick), uy = body.uy + (gravity * tick), x = body.x + (ux * tick))

        body = if (body.y > (yBoundary - boxSize)) body.copy(y = (yBoundary - boxSize), uy = (-body.uy * (1.0 - energyLoss))) else body

        body = if (body.x > (xBoundary - boxSize)) body.copy(x = (xBoundary - boxSize), ux = -body.ux) else body

        body = if (body.y < 0.0) body.copy(y = 0, uy = -body.uy) else body

        body = if (body.x < 0.0) body.copy(x = 0, ux = (-body.ux * (1.0 - energyLoss))) else body

        body
      }
    }

    dom.window.setInterval(() => run, 50)
  }
}
