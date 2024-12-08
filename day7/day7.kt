import java.io.BufferedReader
import java.io.File

class Input(val result: Int, val operands: List<Int>) {
    
}

fun main(args: Array<String>) {
    val bufferedReader: BufferedReader = File("day7/input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }
    val pageStrs = inputString.split("\n")

    val inputs: List<Input> = pageStrs.map { s ->
        val pieces = s.split(": ")
        val res: Int = pieces[0].toInt()
        val ops: List<Int> = pieces[1].split(" ").map {numStr -> numStr.toInt()}
        Input(res, ops)
    }
}