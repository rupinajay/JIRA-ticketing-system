//Rupin's project that on recreating JIRA
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

// User class
class User {
    private String name;
    private String role; // Roles can be Developer, Manager, etc.
    private ArrayList<Issue> assignedIssues;

    public User(String name, String role) {
        this.name = name;
        this.role = role;
        this.assignedIssues = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void assignIssue(Issue issue) {
        assignedIssues.add(issue);
    }

    public ArrayList<Issue> getAssignedIssues() {
        return assignedIssues;
    }
}

// Issue class
class Issue {
    private String title;
    private String description;
    private String priority; // High, Medium, Low
    private String status; // Open, In Progress, Resolved, Closed
    private User assignee;
    private Date createdAt;
    private ArrayList<String> comments;

    public Issue(String title, String description, String priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = "Open"; // Default status is 'Open'
        this.createdAt = new Date();
        this.comments = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getPriority() {
        return priority;
    }

    public void setAssignee(User user) {
        this.assignee = user;
        user.assignIssue(this);
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public String getStatus() {
        return status;
    }

    public User getAssignee() {
        return assignee;
    }

    public String getDescription() {
        return description;
    }
}

// Project class
class Project {
    private String name;
    private ArrayList<Issue> issues;

    public Project(String name) {
        this.name = name;
        this.issues = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addIssue(Issue issue) {
        issues.add(issue);
    }

    public ArrayList<Issue> getIssues() {
        return issues;
    }

    public ArrayList<Issue> searchIssuesByStatus(String status) {
        ArrayList<Issue> result = new ArrayList<>();
        for (Issue issue : issues) {
            if (issue.getStatus().equalsIgnoreCase(status)) {
                result.add(issue);
            }
        }
        return result;
    }

    public void viewAllIssues() {
        for (Issue issue : issues) {
            System.out.println("Issue: " + issue.getTitle() +
                    " | Status: " + issue.getStatus() +
                    " | Assignee: " + (issue.getAssignee() != null ? issue.getAssignee().getName() : "None") +
                    " | Priority: " + issue.getPriority());
        }
    }
}

// JiraSystem class
class JiraSystem {
    private ArrayList<User> users;
    private ArrayList<Project> projects;

    public JiraSystem() {
        this.users = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public void createUser(String name, String role) {
        users.add(new User(name, role));
    }

    public void createProject(String projectName) {
        projects.add(new Project(projectName));
    }

    public void addIssueToProject(String projectName, Issue issue) {
        for (Project project : projects) {
            if (project.getName().equals(projectName)) {
                project.addIssue(issue);
                return;
            }
        }
        System.out.println("Project not found.");
    }

    public void assignIssueToUser(Issue issue, String userName) {
        for (User user : users) {
            if (user.getName().equals(userName)) {
                issue.setAssignee(user);
                return;
            }
        }
        System.out.println("User not found.");
    }

    public void updateIssueStatus(Issue issue, String newStatus) {
        issue.updateStatus(newStatus);
    }

    public void viewAllIssuesInProject(String projectName) {
        for (Project project : projects) {
            if (project.getName().equals(projectName)) {
                project.viewAllIssues();
                return;
            }
        }
        System.out.println("Project not found.");
    }

    // Method to access projects (to avoid private access issue)
    public ArrayList<Project> getProjects() {
        return projects;
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        JiraSystem jira = new JiraSystem();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- JIRA Management System ---");
            System.out.println("1. Create User");
            System.out.println("2. Create Project");
            System.out.println("3. Add Issue to Project");
            System.out.println("4. Assign Issue to User");
            System.out.println("5. Update Issue Status");
            System.out.println("6. View Issues in Project");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: // Create User
                    System.out.print("Enter user name: ");
                    String userName = scanner.nextLine();
                    System.out.print("Enter user role: ");
                    String role = scanner.nextLine();
                    jira.createUser(userName, role);
                    System.out.println("User created.");
                    break;

                case 2: // Create Project
                    System.out.print("Enter project name: ");
                    String projectName = scanner.nextLine();
                    jira.createProject(projectName);
                    System.out.println("Project created.");
                    break;

                case 3: // Add Issue to Project
                    System.out.print("Enter project name: ");
                    String projectForIssue = scanner.nextLine();
                    System.out.print("Enter issue title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter issue description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter issue priority (High/Medium/Low): ");
                    String priority = scanner.nextLine();
                    Issue issue = new Issue(title, description, priority);
                    jira.addIssueToProject(projectForIssue, issue);
                    System.out.println("Issue added to project.");
                    break;

                case 4: // Assign Issue to User
                    System.out.print("Enter project name: ");
                    String projectForAssign = scanner.nextLine();
                    System.out.print("Enter issue title: ");
                    String issueTitle = scanner.nextLine();
                    System.out.print("Enter user name to assign: ");
                    String assigneeName = scanner.nextLine();

                    // Find the issue in the specified project
                    for (Project project : jira.getProjects()) {
                        if (project.getName().equals(projectForAssign)) {
                            for (Issue i : project.getIssues()) {
                                if (i.getTitle().equals(issueTitle)) {
                                    jira.assignIssueToUser(i, assigneeName);
                                    System.out.println("Issue assigned.");
                                    break; // Exit the loop after assigning
                                }
                            }
                            break; // Exit the outer loop after finding the project
                        }
                    }
                    break;

                case 5: // Update Issue Status
                    System.out.print("Enter issue title: ");
                    String updateIssueTitle = scanner.nextLine();
                    System.out.print("Enter new status: ");
                    String newStatus = scanner.nextLine();
                    for (Project project : jira.getProjects()) { // Access projects using the method
                        for (Issue i : project.getIssues()) {
                            if (i.getTitle().equals(updateIssueTitle)) {
                                jira.updateIssueStatus(i, newStatus);
                                System.out.println("Issue status updated.");
                                break;
                            }
                        }
                    }
                    break;

                case 6: // View Issues in Project
                    System.out.print("Enter project name: ");
                    String viewProjectName = scanner.nextLine();
                    jira.viewAllIssuesInProject(viewProjectName);
                    break;

                case 7: // Exit
                    running = false;
                    System.out.println("Exiting the JIRA Management System.");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
