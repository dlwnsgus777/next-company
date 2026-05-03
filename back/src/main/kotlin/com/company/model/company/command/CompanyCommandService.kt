package com.company.model.company.command

import com.company.model.company.command.dto.CreateCompanyRequest
import com.company.model.company.command.dto.UpdateCompanyRequest
import com.company.model.company.domain.Company
import com.company.model.company.domain.CompanyRepository
import com.company.model.company.domain.JobChangeStatus
import com.company.model.member.domain.Member
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CompanyCommandService(
    private val companyRepository: CompanyRepository,
    private val objectMapper: ObjectMapper
) {

    fun create(member: Member, request: CreateCompanyRequest): Long {
        val company = Company(
            member = member,
            name = request.name,
            targetStatus = request.targetStatus,
            jobPostingUrl = request.jobPostingUrl,
            recruitmentDeadline = request.recruitmentDeadline,
            jobChangeStatus = request.jobChangeStatus,
            scores = objectMapper.writeValueAsString(request.scores),
            memo = request.memo
        )
        return companyRepository.save(company).id
    }

    fun update(member: Member, id: Long, request: UpdateCompanyRequest) {
        val company = companyRepository.findByIdAndMember(id, member)
            ?: throw EntityNotFoundException("Company not found. id=$id")
        request.name?.let { company.name = it }
        request.targetStatus?.let { company.targetStatus = it }
        request.jobPostingUrl?.let { company.jobPostingUrl = it }
        request.recruitmentDeadline?.let { company.recruitmentDeadline = it }
        request.jobChangeStatus?.let { company.jobChangeStatus = it }
        request.scores?.let { company.scores = objectMapper.writeValueAsString(it) }
        request.memo?.let { company.memo = it }
    }

    fun updateStatus(member: Member, id: Long, status: JobChangeStatus) {
        val company = companyRepository.findByIdAndMember(id, member)
            ?: throw EntityNotFoundException("Company not found. id=$id")
        company.jobChangeStatus = status
    }

    fun delete(member: Member, id: Long) {
        val company = companyRepository.findByIdAndMember(id, member)
            ?: throw EntityNotFoundException("Company not found. id=$id")
        companyRepository.delete(company)
    }
}
