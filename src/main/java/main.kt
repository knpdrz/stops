import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter

fun main(args: Array<String>) {
    val stopsCsv = "C:\\Users\\Zbigniew\\Desktop\\projekty\\przystanki\\allStops.csv"

    val stops = mutableListOf<Pair<String, String>>()
    stops.addAll(parseGdanskStops())
    stops.addAll(parseBydgoszczStops())
    stops.addAll(parseWarsawStops())
    stops.addAll(parseWroclawStops())

    writeStopsListToCsv(stopsCsv, stops)
}

fun parseWroclawStops(): List<Pair<String, String>> {
    println("parsing Wroclaw")
    val stopsInWrocekTxt = "C:\\Users\\Zbigniew\\Desktop\\projekty\\przystanki\\wroclaw.txt"
    val stopsInWrocekCsv = "C:\\Users\\Zbigniew\\Desktop\\projekty\\przystanki\\wroclaw.csv"

    val stopsInWrocek = mutableListOf<Pair<String, String>>()

    File(stopsInWrocekTxt).forEachLine {
        if (!it.contains("przystanki"))
            stopsInWrocek.add(Pair(it, "Wrocław"))
    }
    writeStopsListToCsv(stopsInWrocekCsv, stopsInWrocek)
    return stopsInWrocek
}

fun parseBydgoszczStops(): List<Pair<String, String>> {
    println("parsing Bydgoszcz")
    val stopsInBydziaTxt = "C:\\Users\\Zbigniew\\Desktop\\projekty\\przystanki\\bydgoszcz.txt"
    val stopsInBydziaCsv = "C:\\Users\\Zbigniew\\Desktop\\projekty\\przystanki\\bydgoszcz.csv"

    val stopsInBydzia = mutableListOf<Pair<String, String>>()

    File(stopsInBydziaTxt).forEachLine {
        val stop = parseBydziaLine(it)
        stop?.let {
            stopsInBydzia.add(Pair(stop, "Bydgoszcz"))
        }
    }
    writeStopsListToCsv(stopsInBydziaCsv, stopsInBydzia)
    return stopsInBydzia
}

fun parseBydziaLine(line: String): String? {
    if (!line.contains("ul.")) return null
    val ulPosition = line.indexOf("ul.")
    val stopWithNumPrefix = line.substring(0, ulPosition - 1)
    val stopWithNumbers = stopWithNumPrefix.split(" ")
    return stopWithNumbers.filter { it -> (it.toIntOrNull()) == null }.joinToString(" ")
}

fun parseGdanskStops(): List<Pair<String, String>> {
    println("parsing Gdansk")
    val stopsInGdaTxt = "C:\\Users\\Zbigniew\\Desktop\\projekty\\przystanki\\gdansk.txt"
    val stopsInGdaCsv = "C:\\Users\\Zbigniew\\Desktop\\projekty\\przystanki\\gdansk.csv"

    val stopsInGda = mutableListOf<Pair<String, String>>()

    var shouldReadLine = true
    File(stopsInGdaTxt).forEachLine {
        if (shouldReadLine) {
            stopsInGda.add(Pair(it, "Gdańsk"))
        }
        shouldReadLine = !shouldReadLine
    }
    writeStopsListToCsv(stopsInGdaCsv, stopsInGda)
    return stopsInGda
}

fun parseWarsawStops(): List<Pair<String, String>> {
    println("parsing Warsaw")
    val stopsInWaTxt = "C:\\Users\\Zbigniew\\Desktop\\projekty\\przystanki\\warszawa.txt"
    val stopsInWaCsv = "C:\\Users\\Zbigniew\\Desktop\\projekty\\przystanki\\warszawa.csv"

    val stopsInWa = mutableListOf<Pair<String, String>>()
    File(stopsInWaTxt).forEachLine { stopsInWa.addAll(parseWarsawLine(it)) }

    writeStopsListToCsv(stopsInWaCsv, stopsInWa)
    return stopsInWa
}

fun parseWarsawLine(line: String): List<Pair<String, String>> {
    if (line.length < 2) return emptyList()
    val stops = mutableListOf<Pair<String, String>>()

    var parsedLine = line
    while (parsedLine.isNotEmpty()) {
        val firstOpeningBracket = parsedLine.indexOf("(")
        val stopName = parsedLine.substring(0, firstOpeningBracket - 1)
        val firstClosingBracket = parsedLine.indexOf(")")

        val cityName = parsedLine.substring(firstOpeningBracket + 1, firstClosingBracket)
        stops.add(Pair(stopName, cityName))
        parsedLine = parsedLine.substring(firstClosingBracket + 1)
    }
    return stops
}

fun writeStopsListToCsv(filePath: String, stops: List<Pair<String, String>>) {
    var fileWriter = FileWriter(filePath)
    var csvWriter = CSVWriter(fileWriter)

    csvWriter.writeNext(arrayOf("Stop name", "City"))
    stops.forEach {
        val stopName = cleanupStopName(it.first)
        if(stopName.isNotEmpty())
            csvWriter.writeNext(arrayOf(stopName, it.second))
    }
    csvWriter.close()
}

fun cleanupStopName(stopName: String): String{
    return stopName.replace("\"","")
}