import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Library implements Serializable{

	String library_Name;
	Date date_Created;
	
	ArrayList<Book> list_Of_Books = new ArrayList<Book>();
	int indexOfTheme = 1;
	
	public Library(String library_Name) {
		this.library_Name = library_Name;
		date_Created = new Date();
	}

	public String getLibrary_Name() {
		return library_Name;
	}

	public void setLibrary_Name(String library_Name) {
		this.library_Name = library_Name;
	}

	public Date getDate_Created() {
		return date_Created;
	}

	public void setDate_Created(Date date_Created) {
		this.date_Created = date_Created;
	}

	public int getIndexOfTheme() {
		return indexOfTheme;
	}

	public void setIndexOfTheme(int indexOfTheme) {
		this.indexOfTheme = indexOfTheme;
	}

	public ArrayList<Book> getList_Of_Books() {
		return list_Of_Books;
	}
	
	public String getNum_Of_Books() {
		return list_Of_Books.size() + "";
	}
	
	public void setList_Of_Books(ArrayList<Book> list_Of_Books) {
		Collections.sort(list_Of_Books);
		this.list_Of_Books = list_Of_Books;
	}
	
	public void addABook(Book book) {
		list_Of_Books.add(book);
		Collections.sort(list_Of_Books);
	}
	
	public void removeABook(Book book) {
		list_Of_Books.remove(book);
		Collections.sort(list_Of_Books);
	}
	
}
