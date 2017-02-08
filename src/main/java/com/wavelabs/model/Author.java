package com.wavelabs.model;

import java.util.Set;
/**
 * Entity class represents Author in relational database.
 * @author gopikrishnag
 *
 */
public class Author {

	private int id;
	private String name;
	private Set<Book> book;

	public Author() {

	}

	public Author(int id, String name) {
		this.id = id;
		this.name = name;
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

	public Set<Book> getBook() {
		return book;
	}

	public void setBook(Set<Book> book) {
		this.book = book;
	}

}
