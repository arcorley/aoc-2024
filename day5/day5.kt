import java.io.File
import java.io.BufferedReader

class Rule(val first: Int, val second: Int) {
    fun ruleApplies(pages: List<Int>): Boolean {
        return pages.contains(this.first) && pages.contains(this.second);
    }

    fun isValid(pages: List<Int>): Boolean {
        val firstIdx = pages.indexOfFirst { i -> i == this.first }
        val secondIdx = pages.indexOfFirst { i -> i == this.second }

        return !((firstIdx > 0 && secondIdx > 0) && firstIdx > secondIdx)
    }

    override fun toString(): String {
        return "{first: $first, second: $second}"
    }
}

fun main(args: Array<String>) {
    var bufferedReader: BufferedReader = File("day5/pages.txt").bufferedReader()
    var inputString = bufferedReader.use { it.readText() }
    val pageStrs = inputString.split("\n")
    val pageLists: List<List<Int>> = pageStrs
        .map { str -> str.split(",") }
        .map { strList -> strList.map { s -> s.trim().toInt() } }

    bufferedReader = File("day5/rules.txt").bufferedReader()
    inputString = bufferedReader.use { it.readText() }
    val ruleStrs = inputString.split("\n")
    val rules: List<Rule> = ruleStrs.map { str ->
        val pieces = str.split("|")
        Rule(first = pieces[0].trim().toInt(), second = pieces[1].trim().toInt())
    }

    part1(pageLists, rules)
    part2(pageLists, rules)
}

fun getModifiedList(list: List<Int>, rules: List<Rule>): List<Int> {
    // Create a directed graph from the rules
    val graph = mutableMapOf<Int, MutableList<Int>>()
    val nodesBefore = mutableMapOf<Int, Int>()

    for (page in list) {
        graph[page] = mutableListOf()
        nodesBefore[page] = 0
    }

    for (rule in rules) {
        if (rule.ruleApplies(list)) {
            graph[rule.first]?.add(rule.second)
            nodesBefore[rule.second] = nodesBefore.getOrDefault(rule.second, 0) + 1
        }
    }

    // Perform topological sort
    val queue = ArrayDeque<Int>()
    for ((page, numNodes) in nodesBefore) {
        // If this node has no dependencies, add it to be processed
        if (numNodes == 0) queue.add(page)
    }

    val sortedList = mutableListOf<Int>()
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        sortedList.add(current)

        for (neighbor in graph[current] ?: emptyList()) {
            nodesBefore[neighbor] = nodesBefore[neighbor]!! - 1
            if (nodesBefore[neighbor] == 0) queue.add(neighbor)
        }
    }

    // If the graph is acyclic, sortedList will contain all pages
    return sortedList
}

fun part2(pageLists: List<List<Int>>, rules: List<Rule>) {
    val invalidLists = mutableListOf<List<Int>>()
    val validLists = mutableListOf<List<Int>>()

    for (list in pageLists) {
        var isValid = true
        for (rule in rules) {
            if (rule.ruleApplies(list) && !rule.isValid(list)) {
                isValid = false
                break
            }
        }
        if (isValid) validLists.add(list) else invalidLists.add(list)
    }

    val fixedLists = invalidLists.map { getModifiedList(it, rules) }
    val total = fixedLists.sumOf { it[it.size / 2] }

    println("TOTAL PART 2: $total")
}

fun part1(pageLists: List<List<Int>>, rules: List<Rule>) {
    var total = 0
    for (l in pageLists) {
        var pass = true
        for (rule in rules) {
            if (rule.ruleApplies(l) && !rule.isValid(l)) {
                pass = false
                break
            }
        }

        if (pass) {
            total += l[l.size / 2]
        }
    }

    println("TOTAL PART 1: $total")
}
