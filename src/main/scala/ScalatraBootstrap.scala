import com.jsjs.app._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    // adding CORS support to allow cross domain access from the
    // following domains
    val domains = "http://localhost:8001 http://jsjs.surge.sh"
    context.initParameters("org.scalatra.cors.allowedOrigins") = domains
    context.mount(new MyScalatraServlet, "/*")
  }
}
