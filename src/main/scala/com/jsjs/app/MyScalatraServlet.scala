package com.jsjs.app

import org.scalatra._
import org.scalatra.CorsSupport
import org.json4s.{ DefaultFormats, Formats }
import org.scalatra.json._

class MyScalatraServlet extends JsjsStack with JacksonJsonSupport with CorsSupport {
	protected implicit lazy val jsonFormats: Formats = DefaultFormats
	// Sets up automatic case class to JSON output serialization, required by
	//	protected implicit lazy val jsonFormats: Formats = DefaultFormats

	before() {
		contentType = formats("json")
	}

	get("/") {
		findTemplate("index") map { path =>
			contentType = "text/html"
			layoutTemplate(path)
		} getOrElse resourceNotFound()
	}

	get("/result") {
		val compiler = new JSJS
		compiler.compile()
	}
}
