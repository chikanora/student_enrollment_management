from datetime import datetime

class Student:
    def __init__(self, firstName, lastName, dateOfBirth, gender, gpaPrev, currentSemester, program, numCourses):
        self.firstName = firstName
        self.lastName = lastName
        self.dateOfBirth = dateOfBirth
        self.gender = gender
        self.gpaPrev = gpaPrev
        self.currentSemester = currentSemester
        self.program = program
        self.numCourses = numCourses

# Data structures/utilities/variables
# In-memory store of students keyed by generated ID: "First_Last_counter"
students = {}

# Counter used to make unique IDs when adding students
studentCounter = 1 

# Validate DOB format (YYYY-MM-DD)
def validateDateOfBirth(dateOfBirth):
    try:
        datetime.strptime(dateOfBirth, "%Y-%m-%d")
        return True
    except ValueError:
        return False

# Edit helpers 
# Generic field editor - shows the current value and lets the user enter a new value or leave blank to skip
def editField(fieldPrompt, currentValue):
    newVal = input(fieldPrompt + " (current: " + str(currentValue) + "): ").strip()
    if newVal != "":
        print("Field updated.")
        return newVal
    else:
        print("No changes made.")
        return currentValue

# Admin login - login up to 3 attempts
# Usernames and passwords are parallel arrays
# Returns true on success, false after 3 failures
def adminLogin():
    usernames = ["Mark", "Daniel", "Ethan", "Donald"]
    passwords = ["mark123", "daniel678", "ethan999", "donald345"]
    loggedIn = False
    attempt = 0

    print("=== SAIT Enrollment System Login ===")

    while attempt < 3 and not loggedIn:
        username = input("Enter username: ")
        password = input("Enter password: ")

        # Validate credentials (index-matched)
        for i in range(len(usernames)):
            if username == usernames[i] and password == passwords[i]:
                loggedIn = True
                break

        if not loggedIn:
            # Feedback & attempt increment
            print("Invalid login credentials. Please try again (Attempt " + str(attempt + 1) + " of 3 ).")
            attempt += 1

    if loggedIn:
        # Doesn't track the specific username beyond this point
        print("Login successful. Welcome " + username)
        return True
    else:
        print("Too many failed attempts. Access denied.")
        return False

# Add student
def addStudent():
    global studentCounter, students
    
    print("=== Add New Student ===")
    student = Student("", "", "", "", 0.0, 0, "", 0)
    
    # Read required fields
    student.firstName = input("Enter first name: ")
    student.lastName = input("Enter last name: ")

    # Date of birth with validation (YYYY-MM-DD)
    while True:
        student.dateOfBirth = input("Enter date of birth (YYYY-MM-DD): ")
        if validateDateOfBirth(student.dateOfBirth):
            break
        else:
            print("Invalid date format. Try again")

    # Gender with validation (Male/Female)
    while True:
        student.gender = input("Enter gender (Male/Female): ")
        if student.gender == "Male" or student.gender == "Female" or student.gender == "male" or student.gender == "female":
            break
        else:
            print("Invalid input. Try again")

    # GPA with range validation (0.0 - 4.0)
    while True:
        student.gpaPrev = float(input("Enter GPA (0.0 - 4.0): "))
        if 0.0 <= student.gpaPrev <= 4.0:
            break
        else:
            print("Invalid GPA range. Try again")

    # Current semester: free-form integer per pseudocode
    student.currentSemester = int(input("Enter current semester number: "))
    student.program = input("Enter program name: ")

    # Course count with range validation (0 - 12)
    while True:
        student.numCourses = int(input("Enter number of courses (0 - 12): "))
        if 0 <= student.numCourses <= 12:
            break
        else:
            print("Invalid number of courses. Try again")

    # Generate unique ID: First_Last_counter
    id = student.firstName + "_" + student.lastName + "_" + str(studentCounter)
    studentCounter += 1
    
    # Prevent accidental overwrite if same ID already exists
    if id in students:
        print("Error: A student with this name already exists. Try again.")
        return
    
    # Add student to dictionary
    students[id] = student
    print("Student added successfully with ID:", id)
    
# Modify student
def modifyStudent():
    global students
    
    print("=== Modify Student ===")
    id = input("Enter student ID: ")
    
    # Validate ID exists
    if id not in students:
        print("Error: No student with this ID.")
        return

    student = students.get(id)
    choice = ""

    # Show a brief summary of the current record
    print("Current Record:")
    print("Name: " + student.firstName + " " + student.lastName + " | Program: " + student.program + " | GPA: " + str(student.gpaPrev))

    # Edit loop: continue until user chooses Q to exit
    while choice != "Q" and choice != "q":
        print()
        print("Select what to edit:")
        print("1. First Name")
        print("2. Last Name")
        print("3. Date of Birth")
        print("4. Gender")
        print("5. GPA")
        print("6. Current Semester")
        print("7. Program")
        print("8. Number of Courses")
        print("Q. Exit")
        choice = input("Enter your choice: ")
        
        match choice:
            case "1":
                student.firstName = editField("Enter new first name", student.firstName)
            case "2":
                student.lastName = editField("Enter new last name", student.lastName)
            case "3":
                while True:
                    student.dateOfBirth = editField("Enter new date of birth (YYYY-MM-DD)", student.dateOfBirth)
                    if validateDateOfBirth(student.dateOfBirth):
                        break
                    else:
                        print("Invalid date format. Try again")
            case "4":
                while True:
                    student.gender = editField("Enter new gender (Male/Female)", student.gender)
                    if student.gender == "Male" or student.gender == "Female" or student.gender == "male" or student.gender == "female":
                        break
                    else:
                        print("Invalid input. Try again")
            case "5":
                while True:
                    student.gpaPrev = float(editField("Enter new GPA (0.0 - 4.0)", student.gpaPrev))
                    if 0.0 <= student.gpaPrev <= 4.0:
                        break
                    else:
                        print("Invalid GPA range. Try again")
            case "6":
                student.currentSemester = int(editField("Enter new current semester number", student.currentSemester))
            case "7":
                student.program = editField("Enter new program name", student.program)
            case "8":
                while True:
                    student.numCourses = int(editField("Enter new number of courses (0 - 12)", student.numCourses))
                    if 0 <= student.numCourses <= 12:
                        break
                    else:
                        print("Invalid number of courses. Try again")
            case "Q"|"q":
                print("Finished editing.")
                break
            case _:
                print("Invalid choice. Please try again.")

    # Re-save the record
    students[id] = student
    print("Student record updated.")

# Remove student
def removeStudent():
    global students
    
    print("=== Remove Student ===")
    id = input("Enter student ID: ")
    
    if id in students:
        confirm = input("Are you sure you want to remove this student? [Y/N] ")
        if confirm == "Y" or confirm == "y":
            del students[id]
            print("Student removed.")
        else:
            print("Action cancelled.")
    else:
        print("Student not found.")

# List all students
def listStudents():
    global students

    print("=== List of Students ===")

    if len(students) == 0:
        print("No student records found.")
        return

    # Iterate in insertion order (LinkedHashMap)
    for id, student in students.items():
        print("ID: " + id + " | Name: " + student.firstName + " " + student.lastName + " | DOB: " + student.dateOfBirth + " | Gender: " + student.gender + " | GPA: " + str(student.gpaPrev) + " | Semester: " + str(student.currentSemester) + " | Program: " + student.program + " | Courses: " + str(student.numCourses))

# Main menu
def mainMenu():
    print("=== Welcome to Student Enrollment Management System ===")

    # Require successful login; exit early upon failure
    if not adminLogin():
        return

    choice = ""

    # Main menu loop
    while choice != "Q" and choice != "q":
        print()
        print("1. Add Student")
        print("2. Modify Student")
        print("3. Remove Student")
        print("4. List of All Students")
        print("Q. Quit")

        choice = input("Enter your choice: ")

        match choice:
            case "1":
                addStudent()
            case "2":
                modifyStudent()
            case "3":
                removeStudent()
            case "4":
                listStudents()
            case "Q"|"q":
                print("System shutting down... Goodbye.")
                break
            case _:
                print("Invalid choice. Please try again.")

if __name__ == "__main__":
    # Entry point
    mainMenu()