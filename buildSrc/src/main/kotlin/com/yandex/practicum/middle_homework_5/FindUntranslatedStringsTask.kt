package com.yandex.practicum.middle_homework_5

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

abstract class FindUntranslatedStringsTask : DefaultTask() {
    @TaskAction
    fun findUntranslatedStrings() {
        val resDir = File(project.projectDir, "src/main/res")
        val strings = File(resDir, "values/strings.xml")
        val stringsFromXml = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(strings)
            .getElementsByTagName("string")
        val stringIdentities = stringsFromXml.let { nodeList ->
            (0 until nodeList.length).map { i ->
                val node = nodeList.item(i)
                val name = node.attributes?.getNamedItem("name")?.nodeValue ?: ""
                name
            }
        }
        if (stringIdentities.isNotEmpty()) {
            val stringBuilderErrorText = StringBuilder("Missing translations").append(System.lineSeparator())
            stringIdentities.forEach { missing ->
                stringBuilderErrorText
                    .append("=== $missing ===")
                    .append(System.lineSeparator())
                    .append(System.lineSeparator())
            }
            throw GradleException(stringBuilderErrorText.toString())
        }
    }
}