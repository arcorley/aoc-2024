import java.io.BufferedReader
import java.io.File

class Point(val row: Int, val col: Int, val value: Char) {
    override fun toString(): String {
        return "{row: $row, col: $col, value: $value}"
    }
}

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

fun main(args: Array<String>) {
    val bufferedReader: BufferedReader = File("day6/input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }
    val pageStrs = inputString.split("\n")
    var startingPoint: Point? = null;
    val grid: List<List<Point>> = pageStrs.mapIndexed { i, s -> s.mapIndexed { j, c ->
        val p = Point(i, j, s[j])
        if (p.value == '^') {
            startingPoint = p;
        }

        p
    } }

    println(grid)

    if (startingPoint == null) {
        throw Exception("Could not determine the starting point")
    }

    val seenPoints: MutableSet<Point> = HashSet()
    seenPoints.add(startingPoint!!)
    var currentPoint = startingPoint!!;
    var direction = Direction.UP

    while (true) {
        seenPoints.add(currentPoint)
        val nextPoint = getNextPoint(grid, currentPoint, direction) ?: break

        if (nextPoint.value == '.' || nextPoint.value == '^') {
            currentPoint = nextPoint
        } else if (nextPoint.value == '#') {
            // Don't change the point but change direction
            direction = getNextDirection(direction)
        } else {
            throw Exception("Got into a weird state. Current point: $currentPoint. Current direction: $direction. Next point: $nextPoint")
        }
    }

    println("Part 1: ${seenPoints.size}")

    val potentialPlaces = seenPoints.filter { p -> p.value != '^' }
    val leadsToLoop: MutableSet<Point> = HashSet()

    potentialPlaces.forEach { p ->
        var newGrid: MutableList<MutableList<Point>> = ArrayList(grid.toMutableList().map { r -> r.toMutableList() })
        newGrid[p.row][p.col] = Point(p.row, p.col, '#')
        if (gridEndsInCycle(newGrid, startingPoint!!)) {
            leadsToLoop.add(p)
        }
    }

    println("Part2: ${leadsToLoop.size}")
}

fun gridEndsInCycle(grid: List<List<Point>>, startingPoint: Point): Boolean {
    var currentPoint = startingPoint;
    var direction = Direction.UP
    var seenPoints: MutableSet<Point> = HashSet()
    var iterations = 0

    while (iterations < 100000) {
        seenPoints.add(currentPoint)
        val nextPoint = getNextPoint(grid, currentPoint, direction)

        if (nextPoint == null) {
            return false
        }

        if (nextPoint.value == '.' || nextPoint.value == '^') {
            currentPoint = nextPoint
        } else if (nextPoint.value == '#') {
            // Don't change the point but change direction
            direction = getNextDirection(direction)
        } else {
            throw Exception("Got into a weird state. Current point: $currentPoint. Current direction: $direction. Next point: $nextPoint")
        }

        iterations++
    }

    return true
}

fun getNextPoint(grid: List<List<Point>>, point: Point, direction: Direction): Point? {
    if (direction == Direction.UP) {
        val newRow = point.row - 1

        if (newRow  < 0) {
            return null;
        }

        return grid[newRow][point.col]
    } else if (direction == Direction.LEFT) {
        val newCol = point.col - 1

        if (newCol < 0) {
            return null
        }

        return grid[point.row][newCol]
    } else if (direction == Direction.RIGHT) {
        val newCol = point.col + 1

        if (newCol >= grid[point.row].size) {
            return null
        }

        return grid[point.row][newCol]
    } else {
        val newRow = point.row + 1

        if (newRow >= grid.size) {
            return null
        }

        return grid[newRow][point.col]
    }
}

fun getNextDirection(currentDirection: Direction): Direction {
    if (currentDirection == Direction.UP) {
        return Direction.RIGHT
    } else if (currentDirection == Direction.DOWN) {
        return Direction.LEFT
    } else if (currentDirection == Direction.LEFT) {
        return Direction.UP
    } else {
        return Direction.DOWN
    }
}