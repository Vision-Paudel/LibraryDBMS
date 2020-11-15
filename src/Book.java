import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Book implements Comparable<Book>, Serializable {

	String title;
	ArrayList<String> authors = new ArrayList<String>();
	String volume = "";
	String edition = "";
	String number_Of_Pages;
	
	String ISBN;
	ArrayList<String> keywords = new ArrayList<String>();
	Date dateAdded;
	
	public Book(String title) {
		this.title = title;
		dateAdded = new Date();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<String> getAuthors() {
		return authors;
	}

	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
		Collections.sort(authors);
	}
	
	public void addAuthor(String author) {
		authors.add(author);
		Collections.sort(authors);
	}
	
	public void removeAuthor(String author) {
		authors.remove(author);
		Collections.sort(authors);
	}
	
	public void removeAllAuthors() {
		authors = new ArrayList<String>();
	}
	
	public int findAuthor(String author) {
		return authors.indexOf(author);
	}
	
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getNumber_Of_Pages() {
		return number_Of_Pages;
	}

	public void setNumber_Of_Pages(String number_Of_Pages) {
		this.number_Of_Pages = number_Of_Pages;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public ArrayList<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
		Collections.sort(this.keywords);
	}
	
	public void addKeyword(String keyword) {
		keywords.add(keyword);
		Collections.sort(keywords);
	}
	
	public void removeKeyword(String keyword) {
		keywords.remove(keyword);
		Collections.sort(keywords);
	}
	
	public void removeAllKeywords() {
		keywords = new ArrayList<String>();
	}
	
	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	@Override
	public String toString() {
		String authorsCommaSeparated = "";
		
		if (authors.size() >= 1) {	
			for(int i = 0; i< authors.size() -1; i++)
				authorsCommaSeparated += authors.get(i) + ", ";			
			authorsCommaSeparated += authors.get(authors.size() -1) + ".";
		}
		
		if (volume.equals("") && edition.equals(""))
			return  title + " by " + authorsCommaSeparated;
		else if (!volume.equals("") && edition.equals(""))
			return title + " vol. " + volume + " by " + authorsCommaSeparated;
		else if (volume.equals("") && !edition.equals(""))
			return title + " " + edition + " edition by " + authorsCommaSeparated;
		else {
			return title + " vol. " + volume + " " + edition + " edition by " + authorsCommaSeparated;
		}
	}
	
	@Override
	public int compareTo(Book anotherBook) {	// Compare based on Title
		return this.getTitle().compareTo(anotherBook.getTitle());
	}
	
}