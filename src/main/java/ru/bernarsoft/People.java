package ru.bernarsoft;


public class People {

    private String name;
    private Integer age;
    private double salary;

    public People(String name, Integer age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public People() {

    }

    public void printSalary() {
        System.out.println("Salary is - " + salary);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
