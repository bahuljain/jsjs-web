package com.jsjs.app

import java.io.File
import sys.process.Process
import java.io._

case class CompilerResponse(
	status: Int,
	sourceCode: String,
	compiledCode: String = "",
	error: String = "",
	compilationTime: Long)

class JSJS {

	/*
	 * Before running this code, first set the location of the
	 * JSJS compiler (jsjs.out file) in the GLOBAL environment
	 * variable named "JSJS".
	 *
	 * The code below obtains the path of the JSJS compiler from the
	 * global environment.
	 */
	private val jsjsPathFile: File = try { new File(sys.env("JSJS")) } catch {
		case _: NoSuchElementException =>
			throw new NoSuchElementException("No path found in environment for key: JSJS")
	}

	private val defaultCode: String = "print((10 + 10));";

	def compile(code: String = defaultCode): CompilerResponse = {
		/*
		 * The compilation process starts with echoing
		 * the code, which is then grabbed by the jsjs
		 * compiler. The result of the compilation is
		 * then stored in a file called 'jsjs.log'.
		 */
        
        // Save code to a file
        val pw = new PrintWriter(new File("code.jsjs"))
        pw.write(code)
        pw.close

		val compilationProcess = Process("cat code.jsjs") #|
			Process("./jsjs.out", jsjsPathFile) #>
			(new File("jsjs.log"))

		/*  Status Codes:
		 * 		0: Success and return Compiled Code
		 * 		1: Failed and return Error Log
		 */
		val startTime = System.nanoTime();

		compilationProcess.! match {
			case 0 => CompilerResponse(
				status = 0,
				sourceCode = code,
				compiledCode = getCompiledCode,
				compilationTime = System.nanoTime() - startTime)
			case 1 => CompilerResponse(
				status = 1,
				sourceCode = code,
				error = getErrorLog,
				compilationTime = System.nanoTime() - startTime)
		}
	}

	//	Obtains the compiled code, which is stored in out.js that
	//	exists in the same folder as jsjs.out
	private def getCompiledCode: String =
		Process("cat out.js", jsjsPathFile).!!

	//	Runs the compiled javascript code using node.js
	private def runCompiledCode: String =
		Process("node out.js", jsjsPathFile).!!

	private def getErrorLog: String =
		io.Source.fromFile("jsjs.log").mkString.trim
}
