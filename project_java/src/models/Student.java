package models;

public class Student
{
    // Variables
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private double gpaPrev;
    private int currentSemester;
    private String program;
    private int numCourses;

    /*public Student(String firstName, String lastName, String dateOfBirth, String gender,
                   double gpaPrev, int currentSemester, String program, int numCourses)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.gpaPrev = gpaPrev;
        this.currentSemester = currentSemester;
        this.program = program;
        this.numCourses = numCourses;
    }*/

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public double getGpaPrev()
    {
        return gpaPrev;
    }

    public void setGpaPrev(double gpaPrev)
    {
        this.gpaPrev = gpaPrev;
    }

    public int getCurrentSemester()
    {
        return currentSemester;
    }

    public void setCurrentSemester(int currentSemester)
    {
        this.currentSemester = currentSemester;
    }

    public String getProgram()
    {
        return program;
    }

    public void setProgram(String program)
    {
        this.program = program;
    }

    public int getNumCourses()
    {
        return numCourses;
    }

    public void setNumCourses(int numCourses)
    {
        this.numCourses = numCourses;
    }

    @Override
    public String toString()
    {
        return firstName + " " + lastName + " | " + dateOfBirth + " | " + gender
                + " | GPA: " + gpaPrev
                + " | Semester: " + currentSemester
                + " | Program: " + program
                + " | Courses: " + numCourses;
    }
}
