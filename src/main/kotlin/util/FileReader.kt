package util

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

object FileReader {
    fun getExampleList(day: String): MutableList<String> = getListByFile(day, FileType.EXAMPLE)
    fun getInputList(day: String): MutableList<String> = getListByFile(day, FileType.INPUT)

    fun getExampleRaw(day: String): String = getExampleList(day).joinToString("\n")
    fun getInputRaw(day: String): String = getInputList(day).joinToString("\n")

    fun getExampleSplitByEmptyLine(day: String): List<String> = getExampleRaw(day).split("\n\n")
    fun getInputSplitByEmptyLine(day: String): List<String> = getInputRaw(day).split("\n\n")
    private fun getListByFile(day: String, fileType: FileType) =
        Files.readAllLines(Paths.get("src/main/resources/$day/${fileType.fileName}.txt"), StandardCharsets.UTF_8)
}
