package com.jsjs.app

import java.io.File
import sys.process.Process
import scala.util.{ Try, Success, Failure }

case class CompilerResponse(status: Int, code: String = "", error: String = "")

class JSJS {

	/*
	 * Before running this code, first set the location of the
	 * JSJS compiler (jsjs.out file) in the GLOBAL environment
	 * variable named "JSJS".
	 *
	 * The below code obtains the path of the JSJS compiler from the
	 * global environment.
	 */
	val jsjsPath: String = try { sys.env("JSJS") } catch {
		case _: NoSuchElementException =>
			throw new NoSuchElementException("No path found in environment for key: JSJS")
	}

	val defaultCode: String = "print((10 + 10));";

	def compile(code: String = defaultCode): CompilerResponse = {
		/*
		 * The compilation process starts with echoing
		 * the code, which is then grabbed by the jsjs
		 * compiler. The result of the compilation is
		 * then stored in a file called 'log'.
		 */
		val compilationProcess = Process(s"echo $code") #|
			Process("./jsjs.out", new File(jsjsPath)) #>
			(new File("jsjs.log"))

		val status_code = compilationProcess.!

		/*  Status Codes:
		 * 		0: Success and return Compiled Code
		 * 		1: Failed and return Error Log
		 */
		status_code match {
			case 0 => CompilerResponse(status = 0, code = getCompiledCode)
			case 1 => CompilerResponse(status = 1, error = io.Source.fromFile("jsjs.log").mkString)
		}
	}

	//	Obtains the compiled code, which is stored in out.js that exists the same folder as jsjs.out
	private def getCompiledCode: String =
		Process("cat out.js", new File(jsjsPath)).!!

	//	Runs the compiled javascript code using node.js
	private def runCompiledCode: String =
		Process("node out.js", new File(jsjsPath)).!!
}

object JSJSCompilerTest extends App {
	val jsjs = new JSJS
	println(jsjs.compile())
}