package com.library.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BOOK")
public class Book {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	@Column(name = "name")
	private String name;
	@Column(name = "author")
	private String author;
	private String borrowerId="0";


	public String getBorrowerId() {
		return borrowerId;
	}

	public void setBorrowerId(String borrowerId) {
		this.borrowerId = borrowerId;
	}

	public Book(String id, String name, String author, String borrower_id) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.borrowerId = borrower_id;
	}

	public Book() {
		super();
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}
}
