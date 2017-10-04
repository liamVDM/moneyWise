import java.io.File

fun main(args: Array<String>) {
//    println(File("asdf").absoluteFile)
    val f = File("src/main/resources/data/holidays_2017")
    val dateStrings = mutableListOf<String>()
    f.forEachLine { line ->
        if (line.contains(":"))
            dateStrings.add(line.split(":").first().trim())
    }

    dateStrings.forEach { ds ->
//        LocalDate.parse(ds, )
    }
}