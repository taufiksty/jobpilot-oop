package controller;

import java.util.List;
import java.util.Scanner;

import dto.JobApplicationDto;
import dto.request.AddJobApplicationRequestDto;
import dto.request.UpdateJobApplicationRequestDto;
import enums.JobApplicationStatus;
import model.JobApplication;
import service.JobApplicationService;
import util.helper.ConsoleHelper;
import util.security.SessionContext;

public class JobApplicationController {

    private final Scanner scanner;
    private final JobApplicationService jobApplicationService;

    private static final String[] SOURCE_OPTIONS = {
            "LinkedIn",
            "JobStreet",
            "Kalibrr",
            "Glassdoor",
            "Indeed",
            "Glints",
            "TechInAsia",
            "Company Website",
            "Referral",
            "Other"
    };

    public JobApplicationController(JobApplicationService jobApplicationService, Scanner scanner) {
        this.jobApplicationService = jobApplicationService;
        this.scanner = scanner;
    }

    public void handleAddJobApplication() {
        ConsoleHelper.printHeader("ADD JOB APPLICATION");
        System.out.print("Company  : ");
        String company = scanner.nextLine().trim();
        System.out.print("Position : ");
        String position = scanner.nextLine().trim();

        JobApplicationStatus status = selectStatus();
        String source = selectSource();

        String userId = SessionContext.getCurrentUser().getUser().getId();

        AddJobApplicationRequestDto addJobApplicationRequest = new AddJobApplicationRequestDto(userId, company,
                position, status, source);

        JobApplicationDto jobApplication = jobApplicationService.addJobApplication(addJobApplicationRequest);
        ConsoleHelper.success("Job Application added successfully for "
                + jobApplication.getJobApplication().getPosition() + " at " + jobApplication.getCompany().getName());
    }

    public void handleViewMyJobApplications() {
        ConsoleHelper.printHeader("MY JOB APPLICATIONS");
        String userId = SessionContext.getCurrentUser().getUser().getId();
        List<JobApplicationDto> jobApplicationList = jobApplicationService.getMyJobApplications(userId);

        if (jobApplicationList.isEmpty()) {
            ConsoleHelper.info("You have no job applications yet.");
        }

        for (JobApplicationDto dto : jobApplicationList) {
            printJobApplication(dto);
        }
    }

    public void handleUpdateJobApplication() {
        ConsoleHelper.printHeader("UPDATE JOB APPLICATION");
        System.out.print("Enter Job Application ID: ");
        String id = scanner.nextLine().trim();
        JobApplicationDto dto;
        try {
            dto = jobApplicationService.getJobApplicationDetail(id);
        } catch (exception.HandleException e) {
            ConsoleHelper.error(e.getMessage());
            return;
        }

        printJobApplication(dto);
        ConsoleHelper.printDivider();
        ConsoleHelper.info("Leave blank to keep current value.");

        JobApplication current = dto.getJobApplication();

        System.out.print("Company [" + dto.getCompany().getName() + "]: ");
        String company = scanner.nextLine().trim();
        company = company.isEmpty() ? dto.getCompany().getName() : company;

        System.out.print("Position [" + current.getPosition() + "]: ");
        String position = scanner.nextLine().trim();
        position = position.isEmpty() ? current.getPosition() : position;

        JobApplicationStatus status = updateStatus(current.getStatus());

        String source = updateSource(current.getSource());

        UpdateJobApplicationRequestDto updateJobApplicationRequest = new UpdateJobApplicationRequestDto(id,
                current.getUserId(), company, position, status, source);

        JobApplicationDto updated = jobApplicationService.updateJobApplication(updateJobApplicationRequest);

        ConsoleHelper.success("Job application updated: " + updated.getJobApplication().getPosition()
                + " at " + updated.getCompany().getName());
    }

    public void handleDeleteJobApplication() {
        ConsoleHelper.printHeader("DELETE JOB APPLICATION");
        System.out.print("Enter Job Application ID: ");
        String id = scanner.nextLine().trim();
        try {
            JobApplicationDto dto = jobApplicationService.getJobApplicationDetail(id);
            printJobApplication(dto);
            System.out.print("\nAre you sure you want to delete this application? [y/N]: ");
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("y")) {
                jobApplicationService.deleteJobApplication(id);
                ConsoleHelper.success("Job application deleted successfully.");
            } else {
                ConsoleHelper.info("Delete cancelled.");
            }
        } catch (exception.HandleException e) {
            ConsoleHelper.error(e.getMessage());
        }
    }

    // ── Helpers ──

    private JobApplicationStatus selectStatus() {
        JobApplicationStatus[] statuses = JobApplicationStatus.values();
        System.out.println("\nSelect Status:");
        for (int i = 0; i < statuses.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, formatStatusName(statuses[i].name()));
        }

        int choice = 0;
        while (choice < 1 || choice > statuses.length) {
            System.out.print("Choice [1-" + statuses.length + "]: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < 1 || choice > statuses.length) {
                    ConsoleHelper.error("Invalid choice. Please enter a number between 1 and " + statuses.length + ".");
                }
            } catch (NumberFormatException e) {
                ConsoleHelper.error("Invalid input. Please enter a number.");
            }
        }

        JobApplicationStatus selected = statuses[choice - 1];
        ConsoleHelper.success("Status selected: " + formatStatusName(selected.name()));
        return selected;
    }

    private String selectSource() {
        System.out.println("\nSelect Source:");
        for (int i = 0; i < SOURCE_OPTIONS.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, SOURCE_OPTIONS[i]);
        }

        int choice = 0;
        while (choice < 1 || choice > SOURCE_OPTIONS.length) {
            System.out.print("Choice [1-" + SOURCE_OPTIONS.length + "]: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < 1 || choice > SOURCE_OPTIONS.length) {
                    ConsoleHelper.error(
                            "Invalid choice. Please enter a number between 1 and " + SOURCE_OPTIONS.length + ".");
                }
            } catch (NumberFormatException e) {
                ConsoleHelper.error("Invalid input. Please enter a number.");
            }
        }

        String selected = SOURCE_OPTIONS[choice - 1];
        ConsoleHelper.success("Source selected: " + selected);
        return selected;
    }

    private String formatStatusName(String name) {
        String lower = name.toLowerCase().replace('_', ' ');
        String[] words = lower.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                if (result.length() > 0)
                    result.append(" ");
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1));
            }
        }
        return result.toString();
    }

    private void printJobApplication(JobApplicationDto dto) {
        ConsoleHelper.printDivider();
        JobApplication jobApplication = dto.getJobApplication();
        System.out.printf("  ID       : %s%n", jobApplication.getId());
        System.out.printf("  Company  : %s%n", dto.getCompany().getName());
        System.out.printf("  Position : %s%n", jobApplication.getPosition());
        System.out.printf("  Status   : %s%n", formatStatusName(jobApplication.getStatus().name()));
        System.out.printf("  Source   : %s%n", jobApplication.getSource() != null ? jobApplication.getSource() : "-");
        System.out.printf("  Applied  : %s%n",
                jobApplication.getAppliedDateTime() != null ? jobApplication.getAppliedDateTime() : "-");
    }

    private JobApplicationStatus updateStatus(JobApplicationStatus current) {
        System.out.println("Status [" + formatStatusName(current.name()) + "]:");
        JobApplicationStatus[] statuses = JobApplicationStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            String marker = statuses[i] == current ? " (current)" : "";
            System.out.printf("  %d. %s%s%n", i + 1, formatStatusName(statuses[i].name()), marker);
        }
        System.out.print("Choice [press Enter for current]: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty())
            return current;

        try {
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= statuses.length) {
                return statuses[choice - 1];
            }
        } catch (NumberFormatException ignored) {
        }
        ConsoleHelper.error("Invalid choice. Keeping current status.");
        return current;
    }

    private String updateSource(String currentSource) {
        System.out.println("Source [" + (currentSource != null ? currentSource : "") + "]:");
        for (int i = 0; i < SOURCE_OPTIONS.length; i++) {
            String marker = SOURCE_OPTIONS[i].equalsIgnoreCase(hasValue(currentSource)) ? " (current)" : "";
            System.out.printf("  %d. %s%s%n", i + 1, SOURCE_OPTIONS[i], marker);
        }
        System.out.print("Choice [press Enter for current]: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty())
            return currentSource;

        try {
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= SOURCE_OPTIONS.length) {
                return SOURCE_OPTIONS[choice - 1];
            }
        } catch (NumberFormatException ignored) {
        }
        ConsoleHelper.error("Invalid choice. Keeping current source.");
        return currentSource;
    }

    private String hasValue(String value) {
        return value != null ? value : "";
    }
}
