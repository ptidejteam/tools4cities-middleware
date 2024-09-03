package ca.concordia.ngci.tools4cities.middleware.middleware;	

public class Person { 
	private String name;
	private int age;
	private String city;

	public Person(String name, int age, String city) {
		this.name = name;
		this.age = age;
		this.city = city;
	}

	public int getAge() {
		return this.age;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", city=" + city
				+ "]";
	}

}