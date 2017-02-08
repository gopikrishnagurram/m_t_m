package com.wavelabs.service;

import org.hibernate.internal.util.xml.XmlDocument;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.wavelabs.metadata.CollectionAttributes;
import com.wavelabs.metadata.CollectionType;
import com.wavelabs.metadata.HbmFileMetaData;
import com.wavelabs.metadata.ManyToManyAttributes;
import com.wavelabs.metadata.XmlDocumentBuilder;
import com.wavelabs.model.Author;
import com.wavelabs.model.Book;
import com.wavelabs.service.PersistenceService;
import com.wavelabs.tableoperations.CRUDTest;
import com.wavelabs.utility.Helper;

/**
 * Performs unit test cases on domain mappings and {@link PersistenceService}
 * methods.
 * 
 * @author gopikrishnag
 *
 */
public class PersistenceServiceTest {

	private HbmFileMetaData authorHbm = null;
	private HbmFileMetaData bookHbm = null;
	private CRUDTest crud = null;

	/**
	 * <p>
	 * Initializes {@link HbmFileMetaData}, {@link CRUDTest} Class objects. This
	 * objects useful through out all unit test cases.
	 * </p>
	 * 
	 */
	@BeforeTest
	public void intillization() {
		XmlDocumentBuilder xdb = new XmlDocumentBuilder();
		XmlDocument xd = xdb.getXmlDocumentObject("src/main/resources/com/wavelabs/model/Author.hbm.xml");
		authorHbm = new HbmFileMetaData(xd, Helper.getSessionFactory());
		xd = xdb.getXmlDocumentObject("src/main/resources/com/wavelabs/model/Book.hbm.xml");
		bookHbm = new HbmFileMetaData(xd, Helper.getSessionFactory());
		crud = new CRUDTest(Helper.getSessionFactory(), Helper.getConfiguration(), Helper.getSession());
	}

	/**
	 * Cheks join table name. If join table is not autor_book then test case
	 * will fail.
	 */
	@Test(priority = 1, description = "verifies join table name as per requirement or not")
	public void testJoinTableName() {
		Assert.assertEquals(authorHbm.getAttributeOfSet(0, CollectionAttributes.table), "author_book",
				"Join table name should be author_book in author hbm");
		Assert.assertEquals(bookHbm.getAttributeOfSet(0, CollectionAttributes.table), "author_book",
				"Join table name should be author_book in book hbm");
	}

	/**
	 * Test lazy attribute value for Set collection in book hbm
	 */
	@Test(priority = 2, dependsOnMethods = "testJoinTableName", description = "Checks lazy attribute value in book hbm for collection")
	public void testLazyOfAuthorCollection() {
		Assert.assertEquals(bookHbm.getAttributeOfSet(0, CollectionAttributes.lazy), "true",
				"map correct lazy value to collection in bookHbm");
	}

	/**
	 * Test lazy attribute value for Set collection in author hbm.
	 */
	@Test(priority = 3, dependsOnMethods = "testLazyOfAuthorCollection", description = "Checks lazy attribute value in Author hbm")
	public void testLazyOfBookCollection() {
		Assert.assertEquals(authorHbm.getAttributeOfSet(0, CollectionAttributes.lazy), "extra",
				"map correct lazy value to collection in authorHbm");
	}

	/**
	 * Checks inverse value in Author mapping at collections.
	 */
	@Test(priority = 4, dependsOnMethods = "testLazyOfBookCollection", description = "Checks book is owner of relationship or not.")
	public void testForInverseInAuthorHbm() {
		Assert.assertEquals(authorHbm.getAttributeOfSet(0, CollectionAttributes.inverse), "true",
				"Book should be as owner of relatinship");
	}

	/**
	 * Checks cascade value in author mapping for Collection.
	 */
	@Test(priority = 5, dependsOnMethods = "testForInverseInAuthorHbm", description = "Checks cascade value in Author hbm")
	public void testForCascadeInAuthorHbm() {
		Assert.assertEquals(authorHbm.getAttributeOfSet(0, CollectionAttributes.cascade), "save-update",
				"persisting of Author should persist books.");
	}

	/**
	 * Checks cascade value in bookhbm collection
	 */
	@Test(priority = 6, dependsOnMethods = "testForCascadeInAuthorHbm", description = "verfies collection cascade value is as per requirement or not")
	public void testForCascadeInBookHbm() {
		Assert.assertEquals(bookHbm.getAttributeOfSet(0, CollectionAttributes.cascade), "save-update",
				"Persisting of Book Should persist author");
	}

	/**
	 * Checks join table author id column is author_id or not
	 */
	@Test(priority = 7, dependsOnMethods = "testForCascadeInBookHbm", description = "Verifies Author id column name in join table")
	public void testAuthorIdColumnInJoinTable() {
		Assert.assertEquals(authorHbm.getSetKeyColumn(0), "author_id",
				"Author id should be as author_id in join table");
		Assert.assertEquals(bookHbm.getManyToManyAttribute(CollectionType.set, 0, ManyToManyAttributes.column),
				"author_id", "Authod id should be as author_id in join table");
	}

	/**
	 * Checks join table book id column is book_id or not
	 */
	@Test(priority = 8, dependsOnMethods = "testAuthorIdColumnInJoinTable", description = "Verifies Book id column in join table")
	public void testBookIdColumnInJoinTable() {
		Assert.assertEquals(authorHbm.getManyToManyAttribute(CollectionType.set, 0, ManyToManyAttributes.column),
				"book_id", "Book id should be as book_id in join table");
		Assert.assertEquals(bookHbm.getSetKeyColumn(0), "book_id", "book id should be as book_id in join table");
	}
	/**
	 * Verifies {@link PersistenceService#createAuthor(int, String, Book[])} Persisting author and associated books.
	 */
	@Test(priority=10, dependsOnMethods="testCreateBook", description="Checks createAuthor(int, String, Book[]) persisting records or not in table")
	public void testCreateAuthor() {
		PersistenceService.createAuthor(1, "Gopi krishna", new Book[] { new Book(1, "A"), new Book(2, "B") });
		crud.setSession(Helper.getSession());
		Assert.assertEquals(crud.isRecordInserted(Author.class, 1), true,
				"createAuthor method failes to insert record in table");
		Assert.assertEquals(crud.isRecordInserted(Book.class, 1), true,
				"createAuthor method failes to insert book record in table");
		Assert.assertEquals(crud.isRecordInserted(Book.class, 2), true,
				"createAuthor method failes to insert book record in table");
	}
	/**
	 * Verifies {@link PersistenceService#updateBookName(int, String)} updating book name or not in table.
	 */
	@Test(priority=11,dependsOnMethods="testCreateAuthor", description="checks updateBookName(int, String) is updates record or not in table.")
	public void testUpdateBookName() {
		PersistenceService.updateBookName(2, "C");
		PersistenceService.updateBookName(1, "D");
		crud.setSession(Helper.getSession());
		Assert.assertEquals(crud.isColumnUpdated(Book.class, "name", "C", 2), true);
		Assert.assertEquals(crud.isColumnUpdated(Book.class, "name", "D", 1), true);
	}
	/**
	 * Verifies {@link PersistenceService#updateAuthorName(int, String)} updating author name or not in author table.
	 */
	@Test(priority=12, dependsOnMethods="testUpdateBookName", description="checks updateAuthorName(int, String) is updates record or not in table")
	public void testUpdateAuthorName() {
		PersistenceService.updateAuthorName(1, "krishna");
		crud.setSession(Helper.getSession());
		crud.isColumnUpdated(Author.class, "name", "krishna", 1);
		Assert.assertEquals(crud.isColumnUpdated(Author.class, "name", "krishna", 1), true);
	}

	/**
	 * Checks {@code PersistenceService#createBook(int, String, Author[])}
	 * persisting records or not in table.
	 */
	@Test(priority = 9, description = "Checks createBook method in PersistenceService persisting book and associated authors", dependsOnMethods = "testBookIdColumnInJoinTable")
	public void testCreateBook() {
		Author[] author = new Author[2];
		author[0] = new Author(2, "GAVIN");
		author[1] = new Author(3, "KING");
		PersistenceService.createBook(3, "JAVA", author);
		crud.setSession(Helper.getSession());
		Assert.assertEquals(crud.isRecordInserted(Book.class, 3), true,
				"createBook(int, String, Author[]) fails to insert a record in book table");
		Assert.assertEquals(crud.isRecordInserted(Author.class, 2), true,
				"createBook(int, String, Author[]) fails to insert a record in author table");
		Assert.assertEquals(crud.isRecordInserted(Author.class, 3), true,
				"createBook(int, String, Author[]) fails to insert a record in author table");
	}
	/**
	 * Verifies {@link PersistenceService#deleteBook(int)} deletes records from table or not.
	 */
	@Test(priority=13,description="verifies deleteBook(int) deletes record from table or not",dependsOnMethods="testUpdateAuthorName")
	public void testDeleteBook() {
		PersistenceService.deleteBook(2);
		PersistenceService.deleteBook(1);
		crud.setSession(Helper.getSession());
		Assert.assertEquals(crud.isRecordDeleted(Book.class, 2), true, "deleteBook(int) fails to delete record");
		Assert.assertEquals(crud.isRecordDeleted(Book.class, 1), true, "deleteBook(int) fails to delete record");
	}

}
