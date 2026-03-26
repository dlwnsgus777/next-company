package com.company.model.company.command

import com.company.model.company.command.dto.CreateCompanyRequest
import com.company.model.company.command.dto.UpdateCompanyRequest
import com.company.model.company.domain.Company
import com.company.model.company.domain.CompanyRepository
import com.company.model.company.domain.JobChangeStatus
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CompanyCommandService(private val companyRepository: CompanyRepository) {

    fun create(request: CreateCompanyRequest): Long {
        val company = Company(
            name = request.name,
            jobChangeStatus = request.jobChangeStatus,
            memo = request.memo
        )
        return companyRepository.save(company).id
    }

    fun update(id: Long, request: UpdateCompanyRequest) {
        val company = companyRepository.findById(id)
            .orElseThrow { EntityNotFoundException("회사를 찾을 수 없습니다. id=$id") }
        request.name?.let { company.name = it }
        request.jobChangeStatus?.let { company.jobChangeStatus = it }
        request.memo?.let { company.memo = it }
    }

    fun updateStatus(id: Long, status: JobChangeStatus) {
        val company = companyRepository.findById(id)
            .orElseThrow { EntityNotFoundException("회사를 찾을 수 없습니다. id=$id") }
        company.jobChangeStatus = status
    }

    fun delete(id: Long) {
        if (!companyRepository.existsById(id)) {
            throw EntityNotFoundException("회사를 찾을 수 없습니다. id=$id")
        }
        companyRepository.deleteById(id)
    }
}
