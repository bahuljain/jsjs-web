package com.jsjs.app

import org.scalatra._

// JSON-related libraries
import org.json4s.{ DefaultFormats, Formats }

// JSON handling support from Scalatra
import org.scalatra.json._

case class Flower(slug: String, name: String)

object FlowerData {
	var all = List(
		Flower("yellow-tulip", "Yellow Tulip"),
		Flower("red-rose", "Red Rose"),
		Flower("black-rose", "Black Rose"))
}

class MyScalatraServlet extends JsjsStack with JacksonJsonSupport {

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
		FlowerData.all
	}
}
