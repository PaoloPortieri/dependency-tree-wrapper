package com.iad.dtreewrapper.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import it.iad.constants.Constants;
import it.iad.entities.Project;
import it.iad.entities.ProjectInfo;
import it.iad.entities.ProjectSummary;
import it.iad.exceptions.ProjectDataServiceException;
import it.iad.exceptions.ProjectSummaryServiceException;
import it.iad.services.ProjectDataService;
import it.iad.services.ProjectSummaryService;
import it.iad.services.ProjectsAnalyzerService;
import it.iad.services.ReportService;

@Controller
@RequestMapping("/reports")
public class ReportController {

	private ReportService reportService;

	public ReportController() {
		this.reportService = new ReportService();
	}

	@GetMapping("/getDependencyAnalysis")
	public List<ProjectInfo> generateFullDependencyAnalysis() throws ProjectSummaryServiceException, ProjectDataServiceException {
		List<ProjectSummary> projectSummaryList;
		projectSummaryList = new ProjectSummaryService().getProjectSummaryList(Constants.SICRA_JSON_REPOSITORY,
				Constants.SICRA_JSON_PATH, Constants.SICRA_JSON_BRANCH);
		List<Project> projectDataList = new ProjectDataService().getProjectDataList(projectSummaryList);
		return new ProjectsAnalyzerService().analyzeProjects(projectDataList);
	}

	@PostMapping("/generateHtml")
	public ResponseEntity<?> generateHtmlReport(@RequestBody String dependencyReportJson) {
		try {
			reportService.generateHtmlReport(dependencyReportJson);
			return ResponseEntity.ok().body("HTML report generated successfully.");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error in generating HTML report: " + e.getMessage());
		}
	}

	@PostMapping("/generatePdf")
	public ResponseEntity<?> generatePdfReport(@RequestBody String dependencyReportJson) {
		try {
			reportService.generatePdfReport(dependencyReportJson);
			return ResponseEntity.ok().body("PDF report generated successfully.");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error in generating PDF report: " + e.getMessage());
		}
	}

}
