package com.piotrkalitka.fluttertodo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlutterToDoApplication

fun main(args: Array<String>) {
	runApplication<FlutterToDoApplication>(*args)
}
