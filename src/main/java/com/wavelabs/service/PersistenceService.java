package com.wavelabs.service;

import java.util.HashSet;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.wavelabs.model.Author;
import com.wavelabs.model.Book;
import com.wavelabs.utility.Helper;
/**
 * </h2> Performs CRUD operations on {@link Book} {@link Author} domains. </h2>
 * @author gopikrishnag
 *
 */
public class PersistenceService {
	/**
	 * <h3> Persist Book and associated Authors </h3>
	 * <ul>
	 * 		<li> Creates  set of Authors </li>
	 * 		<li> Adds authors to Set </li>
	 * 		<li> Create Book object and sets all properties </li>
	 * 		<li> Persist book </li>
	 * </ul>
	 *  
	 * @param id of Book
	 * @param name of Book
	 * @param authors of Book
	 */
	public static void createBook(int id, String name, Author[] authors) {
		Session session = Helper.getSession();
		Transaction tx = session.beginTransaction();
		Set<Author> setOfAuthors = new HashSet<Author>();
		for (Author author : authors) {
			setOfAuthors.add(author);
		}
		Book b1 = new Book();
		b1.setId(id);
		b1.setName(name);
		b1.setAuthor(setOfAuthors);
		session.save(b1);
		tx.commit();
		session.close();
	}
	/**
	 * <h3> Persist the Author and associated books </h3>
	 * <ul> 
	 * 		<li> creates set of Books </li>
	 * 		<li> add books to set </li>
	 * 		<li> create author object and set all properties to Author</li>
	 * 		<li> persist author </li>
	 * </ul> 
	 * @param id of Author
	 * @param name of Author
	 * @param books of Author
	 */
	public static void createAuthor(int id, String name, Book[] books) {
		Session session = Helper.getSession();
		Transaction tx = session.beginTransaction();
		Set<Book> setOfBooks = new HashSet<Book>();
		for (Book book : books) {
			setOfBooks.add(book);
		}
		Author a1 = new Author();
		a1.setId(id);
		a1.setName(name);
		a1.setBook(setOfBooks);
		session.save(a1);
		tx.commit();
		session.close();
	}
	/**
	 * <h3> update the book name for matching id </h3>
	 * <ul>
	 * 		<li> Gets book record for given id </li>
	 * 		<li> set new book name to book record </li>
	 * 		<li> persists the changes </li>
	 * </ul>
	 * @param id of book
	 * @param newBookName of book
	 */
	public static void updateBookName(int id, String newBookName) {
		
		Session session = Helper.getSession();
		Transaction tx = session.beginTransaction();
		Book book = (Book)session.get(Book.class,id);
		book.setName(newBookName);
		session.flush();
		tx.commit();
		session.close();
	}
	/**
	 * <h3> updates author name for matching id </h3>
	 * 	<ul> 
	 * 		<li> gets Author of given id <li>
	 * 		<li> Set new Name to author </li>
	 * 		<li> persist the changes </li>
	 *  </ul>
	 *  
	 * @param id of Author
	 * @param newName of Author 
	 */
	public static void updateAuthorName(int id, String newName)
	{
		Session session = Helper.getSession();
		Transaction tx = session.beginTransaction();
		Author author = (Author)  session.get(Author.class, id);
		author.setName(newName);
		session.flush();
		tx.commit();
		session.close();
	}
	/**
	 * <h3> Delete book of given id </h3>
	 * 	<ul>
	 * 		<li> Load book of given id </li>
	 * 		<li> delete book of given id </li>
	 * 	<ul>
	 * @param id of Book
	 */
	public static void deleteBook(int id)
	{
		Session session = Helper.getSession();
		Transaction tx = session.beginTransaction();
		Book book = (Book) session.get(Book.class, id);
		session.delete(book);
		session.flush();
		tx.commit();
		session.close();
	}
	
}
