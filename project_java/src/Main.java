import models.Student;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Console-based Student Enrollment Management System
 * Based off of the provided pseudocode: admin login (3 attempts), CRUD methods
 * Uses LinkedHashMap to preserve insertion order when listing students
 */
public class Main
{
    // Data structures/utilities/variables
    // In-memory store of students keyed by generated ID: "First_Last_counter"
    private static final Map<String, Student> students = new LinkedHashMap<>();
    // Counter used to make unique IDs when adding students
    private static int studentCounter = 1;
    // Scanner input for the entire app
    private static final Scanner input = new Scanner(System.in);

    // Prompt helper - prompts for a line and return the trimmed user input
    // Use this for required inputs where blank is not meaningful
    private static String promptLine(String prompt)
    {
        System.out.print(prompt);
        return input.nextLine().trim();
    }

    // Prompt for a line and return the raw user input (not trimmed, may be blank)
    // Use this in "blank to skip" flows when editing
    private static String promptLineAllowBlank(String prompt)
    {
        System.out.print(prompt);
        return input.nextLine();
    }

    // Prompt repeatedly until a valid integer is entered
    private static int promptInt(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String s = input.nextLine().trim();
            try
            {
                return Integer.parseInt(s);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid integer input. Please try again.");
            }
        }
    }

    // Prompt repeatedly until a valid double is entered
    private static double promptDouble(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String s = input.nextLine().trim();

            try
            {
                return Double.parseDouble(s);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid double input. Please try again.");
            }
        }
    }

    // Edit helpers
    // Generic field editor - shows the current value and lets the user enter a new value or leave blank to skip
    private static void editFieldString(String fieldPrompt, Supplier<String> getter, Consumer<String> setter)
    {
        String current = getter.get();
        String newVal = promptLineAllowBlank(fieldPrompt + " (current: " + current + "): ");

        if (!newVal.isBlank())
        {
            setter.accept(newVal.trim());
            System.out.println("Field updated.");
        }
        else
        {
            System.out.println("No changes made.");
        }
    }

    // Generic int editor - shows the current value and lets the user enter a new value or leave blank to skip
    private static void editFieldInt(String fieldPrompt, Supplier<Integer> getter, Consumer<Integer> setter)
    {
        String current = String.valueOf(getter.get());
        String line = promptLineAllowBlank(fieldPrompt + " (current: " + current + "): ");

        if (!line.isBlank())
        {
            try
            {
                int val = Integer.parseInt(line.trim());
                setter.accept(val);
                System.out.println("Field updated.");
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid integer input. Please try again.");
            }
        }
        else
        {
            System.out.println("No changes made.");
        }
    }

    // Specialized GPA editor with range validation and "blank to skip" behavior
    private static void editGPA(Student student)
    {
        System.out.println("Enter GPA (0.0 - 4.0), leave blank to skip. Current GPA: " + student.getGpaPrev());
        String newGpa = promptLineAllowBlank("> ");

        if (!newGpa.isBlank())
        {
            try
            {
                double number = Double.parseDouble(newGpa.trim());

                if (number >= 0.0 && number <= 4.0)
                {
                    student.setGpaPrev(number);
                    System.out.println("GPA updated.");
                }
                else
                {
                    // Keep old value if out of range
                    System.out.println("Invalid GPA range.");
                }
            }
            catch (NumberFormatException e)
            {
                // Keep old value if invalid
                System.out.println("Invalid double input. No changes made.");
            }
        }
        else
        {
            System.out.println("No changes made.");
        }
    }

    // Admin login - login up to 3 attempts
    // Usernames and passwords are parallel arrays
    // Returns true on success, false after 3 failures
    private static boolean adminLogin()
    {
        String[] usernames = {"Mark", "Daniel", "Ethan", "Donald"};
        String[] passwords = {"mark123", "daniel678", "ethan999", "donald345"};
        boolean loggedIn = false;
        int attempt = 0;

        System.out.println("=== SAIT Enrollment System Login ===");

        while (attempt < 3 && !loggedIn)
        {
            String username = promptLine("Enter username: ");
            String password = promptLine("Enter password: ");

            // Validate credentials (index-matched)
            for (int i = 0; i < usernames.length; i++)
            {
                if (username.equals(usernames[i]) && password.equals(passwords[i]))
                {
                    loggedIn = true;
                    break;
                }
            }

            if (!loggedIn)
            {
                // Feedback & attempt increment
                System.out.println("Invalid login credentials. Please try again (Attempt " + (attempt + 1) + " of 3 ).");
                attempt =  attempt + 1;
            }
        }

        if (loggedIn)
        {
            // Doesn't track the specific username beyond this point
            System.out.println("Login successful. Welcome " + promptLastEnteredUsernameNote());
            return true;
        }
        else
        {
            System.out.println("Too many failed attempts. Access denied.");
            return false;
        }
    }

    // Username variables
    // Last username entered (used only for the welcome line display)
    private static String lastEnteredUsername = "";

    private static String promptLastEnteredUsernameNote()
    {
        // Returns the last stored username
        return lastEnteredUsername;
    }

    // Override prompt line used above for username to store it
    private static String promptLineStoreUsername(String prompt)
    {
        System.out.print(prompt);
        String v = input.nextLine().trim();
        lastEnteredUsername = v;
        return v;
    }

    // Rewire the username prompt in login to store the user's text
    private static String promptLine(String prompt, boolean storeAsUsername)
    {
        if (storeAsUsername)
        {
            return promptLineStoreUsername(prompt);
        }

        return promptLine(prompt);
    }

    // Re-implement adminLogin to use the storing prompt (to exactly print the name that succeeded)
    static
    {
        // Shadow the previous method by redefining with the correct storing prompt
    }

    /*private static boolean adminLoginExact()
    {
        String[] usernames = {"Mark", "Daniel", "Ethan", "Donald"};
        String[] passwords = {"mark123", "daniel678", "ethan999", "donald345"};
        boolean loggedIn = false;
        int attempt = 0;

        System.out.println("=== SAIT Enrollment System Login ===");

        while (attempt < 3 && !loggedIn)
        {
            String username = promptLine("Enter username: ", true);
            String password = promptLine("Enter password: ");

            for (int i = 0; i < usernames.length; i++)
            {
                if (username.equals(usernames[i]) && password.equals(passwords[i]))
                {
                    loggedIn = true;
                    break;
                }
            }

            if (!loggedIn)
            {
                System.out.println("Invalid login credentials. Please try again (Attempt " + (attempt + 1) + " of 3 ).");
                attempt = attempt + 1;
            }
        }

        if (loggedIn)
        {
            System.out.println("Login successful. Welcome " + lastEnteredUsername);
            return true;
        }
        else
        {
            System.out.println("Too many failed attempts. Access denied.");
            return false;
        }
    }*/

    // Add student
    private static void addStudent()
    {
        System.out.println("=== Add New Student ===");
        Student student = new Student();

        // Read required fields
        student.setFirstName(promptLine("Enter first name: "));
        student.setLastName(promptLine("Enter last name: "));
        student.setDateOfBirth(promptLine("Enter date of birth (YYYY-MM-DD): "));
        student.setGender(promptLine("Enter gender: "));

        // GPA with range validation (0.0 - 4.0)
        while (true)
        {
            double gpa = promptDouble("Enter GPA (0.0 - 4.0): ");

            if (gpa >= 0.0 && gpa <= 4.0)
            {
                student.setGpaPrev(gpa);
                break;
            }
            else
            {
                System.out.println("Invalid GPA range.");
            }
        }

        // Current semester: free-form integer per pseudocode
        student.setCurrentSemester(promptInt("Enter current semester number: "));
        student.setProgram(promptLine("Enter program name: "));

        // Course count with range validation (0 - 12)
        while (true)
        {
            int number = promptInt("Enter number of courses (0 - 12): ");

            if (number >= 0 && number <= 12)
            {
                student.setNumCourses(number);
                break;
            }
            else
            {
                System.out.println("Invalid number of courses. Must be 0 - 12.");
            }
        }

        // Generate unique ID: First_Last_counter
        String id = student.getFirstName() + "_" + student.getLastName() + "_" + studentCounter;
        studentCounter = studentCounter + 1;

        // Prevent accidental overwrite if same ID already exists
        if (students.containsKey(id))
        {
            System.out.println("Error: A student with this name already exists. Try again.");
            return;
        }

        // Save and confirm
        students.put(id, student);
        System.out.println("Student added successfully with ID: " + id);
    }

    // Modify student
    private static void modifyStudent()
    {
        System.out.println("=== Modify Student ===");
        String id = promptLine("Enter student ID: ");

        // Validate ID exists
        if (!students.containsKey(id))
        {
            System.out.println("Error: No student with this ID.");
            return;
        }

        Student student = students.get(id);
        String choice;

        // Show a brief summary of the current record
        System.out.println("Current Record:");
        System.out.println(student.getFirstName() + " " + student.getLastName() + " | " + student.getProgram() +
                " | GPA: " + student.getGpaPrev());

        // Edit loop: continue until user chooses Q to exit
        do
        {
            System.out.println();
            System.out.println("Select what to edit:");
            System.out.println("1. First Name");
            System.out.println("2. Last Name");
            System.out.println("3. Date of Birth");
            System.out.println("4. Gender");
            System.out.println("5. GPA");
            System.out.println("6. Current Semester");
            System.out.println("7. Program");
            System.out.println("8. Number of Courses");
            System.out.println("Q. Exit");
            String raw = promptLine("");

            choice = raw;

            switch (choice)
            {
                case "1" -> editFieldString("Enter new first name", student::getFirstName, student::setFirstName);
                case "2" -> editFieldString("Enter new last name", student::getLastName, student::setLastName);
                case "3" -> editFieldString("Enter new date of birth (YYYY-MM-DD)", student::getDateOfBirth,
                        student::setDateOfBirth);
                case "4" -> editFieldString("Enter gender", student::getGender, student::setGender);
                case "5" -> editGPA(student);
                case "6" -> editFieldInt("Enter new current semester", student::getCurrentSemester, student::setCurrentSemester);
                case "7" -> editFieldString("Enter new program name", student::getProgram, student::setProgram);
                case "8" ->
                {
                    // Special-case with range validation, similar to Add Student
                    String current = String.valueOf(student.getNumCourses());
                    String line = promptLineAllowBlank("Enter new number of courses (0 - 12) (current: " + current + "): ");

                    if (!line.isBlank())
                    {
                        try
                        {
                            int val = Integer.parseInt(line.trim());

                            if (val >= 0 && val <= 12)
                            {
                                student.setNumCourses(val);
                                System.out.println("Field updated.");
                            }
                            else
                            {
                                System.out.println("Invalid range (0 - 12). No change made.");
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("Invalid integer. No changes made.");
                        }
                    }
                    else
                    {
                        System.out.println("No changes made.");
                    }
                }
                case "Q", "q" -> System.out.println("Finished editing.");
                default ->
                {
                    // Guard against invalid choices while staying in the loop
                    if (!choice.equalsIgnoreCase("Q"))
                    {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }

            }
        }
        while (!choice.equalsIgnoreCase("Q"));

        // Re-save the record
        students.put(id, student);
        System.out.println("Student record updated.");
    }

    // Remove student
    private static void removeStudent()
    {
        System.out.println("=== Remove Student ===");
        String id = promptLine("Enter student ID: ");

        if (students.containsKey(id))
        {
            String confirm = promptLine("Are you sure you want to remove this student? [Y/N] ");

            if (confirm.equalsIgnoreCase("Y"))
            {
                students.remove(id);
                System.out.println("Student removed.");
            }
            else
            {
                System.out.println("Action cancelled.");
            }
        }
        else
        {
            System.out.println("Student not found.");
        }
    }

    // List all students
    private static void listStudents()
    {
        System.out.println("=== List of Students ===");

        if (students.isEmpty())
        {
            System.out.println("No student records found.");
            return;
        }

        // Iterate in insertion order (LinkedHashMap)
        for (Map.Entry<String, Student> entry : students.entrySet())
        {
            String id = entry.getKey();
            Student student = entry.getValue();

            System.out.println(id + " | " + student.getFirstName() + " " + student.getLastName() + " | " + student.getDateOfBirth() +
                    " | " + student.getGender() + " | GPA: " + student.getGpaPrev() + " | Semester: " + student.getCurrentSemester() +
                    " | Program: " + student.getProgram() + " | Courses: " + student.getNumCourses());
        }
    }

    // Main menu
    private static void mainMenu()
    {
        System.out.println("=== Welcome to Student Enrollment Management System ===");

        // Require successful login; exit early upon failure
        if (!adminLogin())
        {
            return;
        }

        String choice;

        // Main menu loop
        do
        {
            System.out.println();
            System.out.println("1. Add Student");
            System.out.println("2. Modify Student");
            System.out.println("3. Remove Student");
            System.out.println("4. List of All Students");
            System.out.println("Q. Quit");

            choice = promptLine("");

            switch (choice)
            {
                case "1" -> addStudent();
                case "2" -> modifyStudent();
                case "3" -> removeStudent();
                case "4" -> listStudents();
                case "Q", "q" -> System.out.println("System shutting down... Goodbye.");
                default ->
                {
                    if (!choice.equalsIgnoreCase("Q"))
                    {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }
            }
        }
        while (!choice.equalsIgnoreCase("Q"));
    }

    public static void main(String[] args)
    {
        // Entry point
        mainMenu();
    }
}