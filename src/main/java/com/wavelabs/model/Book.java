package com.wavelabs.model;

import java.util.Set;
/**
 * Entity class represents book table in relational database.
 * @author gopikrishnag
 *
 */
public class Book {

	private int id;
	private String name;
	private Set<Author> author;

	public Book(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Book() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Author> getAuthor() {
		return author;
	}

	public void setAuthor(Set<Author> author) {
		this.author = author;
	}

}
