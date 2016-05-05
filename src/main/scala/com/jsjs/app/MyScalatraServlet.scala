package com.jsjs.app

import org.scalatra._
import org.scalatra.CorsSupport
import org.json4s.{ DefaultFormats, Formats }
import org.scalatra.json._

// Format of JSON object received on "/compiler" route
case class CompilerRequest(sourceCode: String)

class MyScalatraServlet extends JsjsStack with JacksonJsonSupport with CorsSupport {
	protected implicit lazy val jsonFormats: Formats = DefaultFormats

	// Sets up automatic case class to JSON output serialization, required by
	//	protected implicit lazy val jsonFormats: Formats = DefaultFormats
	before() {
		contentType = formats("json")
	}

    // respond to pre-flight request
    options("/*"){
      response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
    }

	get("/") {
		findTemplate("index") map { path =>
			contentType = "text/html"
			layoutTemplate(path)
		} getOrElse resourceNotFound()
	}

	post("/compiler") {
		/*
		 * Takes the body of request which will be in JSON and converts it
		 * to format given by case class "CompilerRequest".
		 *
		 * JSON request format:
		 * {
		 * 	"sourceCode": "....some jsjs code here...."
		 * }
		 */
		val compilerRequest = parsedBody.extract[CompilerRequest]

		val compiler = new JSJS
		compiler.compile(compilerRequest.sourceCode)
	}
}
