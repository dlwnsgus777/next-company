package com.company

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class CompanyBackendApplication

fun main(args: Array<String>) {
	runApplication<CompanyBackendApplication>(*args)
}
