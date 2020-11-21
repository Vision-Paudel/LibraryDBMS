import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LibraryDBMS_ver1 extends Application{

	// Static data fields
	private static Library my_Current_Library = new Library("");
	private static Book my_Current_Book = new Book("");
	private static String my_Current_Author = "";
	private static String my_Current_Keyword = "";
	private static FileOutputStream fout = null;
	private static ObjectOutputStream oos = null;
	private static FileInputStream fin = null;
	private static ObjectInputStream ois = null; 
	
	private static ArrayList<String> my_Current_Edit_AuthorList = new ArrayList<String>();
	private static ArrayList<String> my_Current_Edit_KeywordList = new ArrayList<String>();
	
	static ComboBox<String> cbExportMenu = new ComboBox<>();
	static String[] exportMenuArray = new String[] { "Export to" , ".txt file" , ".csv file (format 1)" , ".csv file (format 2)" };
	static ObservableList<String> exportItemsList = FXCollections.observableArrayList(exportMenuArray);
	static int indexOfExport = 0;
	
	static ComboBox<String> cbThemeMenu = new ComboBox<>();
	static String[] themeMenuArray = new String[] { "Red" , "Green" , "Blue" , "Black" };
	static ObservableList<String> themeItemsList = FXCollections.observableArrayList(themeMenuArray);
	static int indexOfTheme = 1;
		
	final static String[] IDLE_BUTTON_STYLES = {"-fx-background-color: transparent; -fx-border-color: red", "-fx-background-color: transparent; -fx-border-color: green" , "-fx-background-color: transparent; -fx-border-color: blue" , "-fx-background-color: transparent; -fx-border-color: black" };
	final static String[] HOVERED_BUTTON_STYLES = { "-fx-background-color: red; -fx-text-fill: white;", "-fx-background-color: green; -fx-text-fill: white;", "-fx-background-color: blue; -fx-text-fill: white;", "-fx-background-color: black; -fx-text-fill: white;" };
	static int styleIndex = 1;
	static String IDLE_BUTTON_STYLE = IDLE_BUTTON_STYLES[styleIndex];
	static String HOVERED_BUTTON_STYLE = HOVERED_BUTTON_STYLES[styleIndex];
	
	// Launch Application
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// Initialize Pane layouts
		BorderPane mainPane = new BorderPane();
		Pane libraryPane = new Pane();
				
		// Create and add title to the top of the Border Pane.
		mainPane.setTop(getTitle());
		mainPane.setCenter(libraryPane);
		
		// If library file exists, open it else create a new one and open it.
		File newLibraryFile = new File("main.library") ;
		if(newLibraryFile.exists()) {
			try {
				fin = new FileInputStream(newLibraryFile);
			    ois = new ObjectInputStream(fin);
			    my_Current_Library = (Library) ois.readObject();
			} catch (Exception ex) {
				ex.printStackTrace();
			}finally {
				closeInputOutput();
			}
		}
		else {
			my_Current_Library = new Library("");
			
			try {
				fout = new FileOutputStream(newLibraryFile);
				oos = new ObjectOutputStream(fout);
				oos.writeObject(my_Current_Library);
				
				fin = new FileInputStream(newLibraryFile);
			    ois = new ObjectInputStream(fin);
			    my_Current_Library = (Library) ois.readObject();
			} catch (Exception ex) {
				ex.printStackTrace();
			}finally {
				closeInputOutput();
			}
		}
		
		// Main Pane
		
		Label lbl_library_Name = new Label("Library Name:");
		lbl_library_Name.setLayoutX(80);
		lbl_library_Name.setLayoutY(55);
		libraryPane.getChildren().add(lbl_library_Name);
		
		Label lbl_date_Created = new Label("Date Created: " + my_Current_Library.getDate_Created());
		lbl_date_Created.setLayoutX(80);
		lbl_date_Created.setLayoutY(85);
		libraryPane.getChildren().add(lbl_date_Created);
		
		Button exportToFile = new Button("Export to File(s)");
		exportToFile.setLayoutX(423);
		exportToFile.setLayoutY(85);
		libraryPane.getChildren().add(exportToFile);
		
		exportToFile.setStyle(IDLE_BUTTON_STYLE);
		exportToFile.setOnMouseEntered(e -> exportToFile.setStyle(HOVERED_BUTTON_STYLE));
		exportToFile.setOnMouseExited(e -> exportToFile.setStyle(IDLE_BUTTON_STYLE));
		
		cbExportMenu.getItems().addAll(exportItemsList);
		
		exportToFile.setOnAction(e -> {
			
			Pane exportToFilePane = new Pane();	
			
			Scene exportToFileScene = new Scene(exportToFilePane, 250, 100);
			Stage primaryStage2 = new Stage();
			
			primaryStage2.setTitle("Export to File(s)");
			primaryStage2.setScene(exportToFileScene);
			primaryStage2.setResizable(false);
			primaryStage2.show();
								
			cbExportMenu.setValue("Export to");
			cbExportMenu.setLayoutX(10);
			cbExportMenu.setLayoutY(10);
			exportToFilePane.getChildren().add(cbExportMenu);
			
			Button exportToFile2 = new Button("Export!");
			exportToFile2.setLayoutX(10);
			exportToFile2.setLayoutY(40);
			exportToFilePane.getChildren().add(exportToFile2);
			exportToFile2.setStyle(IDLE_BUTTON_STYLE);
			exportToFile2.setOnMouseEntered(ex -> exportToFile2.setStyle(HOVERED_BUTTON_STYLE));
			exportToFile2.setOnMouseExited(ex -> exportToFile2.setStyle(IDLE_BUTTON_STYLE));
			
			cbExportMenu.setOnAction ( ex -> {
				indexOfExport = exportItemsList.indexOf(cbExportMenu.getValue());
			});
			
			exportToFile2.setOnAction(ex -> {
			
				if(indexOfExport == 0) {
					
					// Do nothing for now.
					
				}else if (indexOfExport == 1) {
					FileChooser fileChooser = new FileChooser();
		 
		            //Set extension filter for text files
		            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Documents (*.txt)", "*.txt");
		            fileChooser.getExtensionFilters().add(extFilter);
		 
		            //Show save file dialog
		            File file = fileChooser.showSaveDialog(primaryStage2);
		 
		            if (file != null) {
		            	saveTextFile(file);
		            }
				}
				else if (indexOfExport == 2 || indexOfExport == 3) {
					
					Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
					confirmationAlert.setHeaderText("Commas will be Removed!");
					confirmationAlert.setContentText("All Commas will be replaced with ' ' (Whitespace character).");
					confirmationAlert.showAndWait();
					
					FileChooser fileChooser = new FileChooser();
					 
		            //Set extension filter for text files
		            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Comma delimited (*.csv)", "*.csv");
		            fileChooser.getExtensionFilters().add(extFilter);
		 
		            //Show save file dialog
		            File file = fileChooser.showSaveDialog(primaryStage2);
		 
		            if (file != null) {
		            	saveCSVFile(file);
		            }
				}
			
			});
            
        });
		
		cbThemeMenu.getItems().addAll(themeItemsList);
		cbThemeMenu.setValue("Theme");
		cbThemeMenu.setOnAction ( ex -> {
			indexOfTheme = themeItemsList.indexOf(cbThemeMenu.getValue());
			styleIndex = indexOfTheme;
			IDLE_BUTTON_STYLE = IDLE_BUTTON_STYLES[styleIndex];
			HOVERED_BUTTON_STYLE = HOVERED_BUTTON_STYLES[styleIndex];
			mainPane.setTop(getTitle());			
		});
		cbThemeMenu.setLayoutX(440);
		cbThemeMenu.setLayoutY(20);
		libraryPane.getChildren().add(cbThemeMenu);
		
		TextField tf_Library_Name = new TextField();
		tf_Library_Name.setLayoutX(160);
		tf_Library_Name.setLayoutY(55);
		tf_Library_Name.setText(my_Current_Library.getLibrary_Name());
		tf_Library_Name.setEditable(false);
		libraryPane.getChildren().add(tf_Library_Name);
		
		Button btn_Change_Library_Name = new Button("Change");
		btn_Change_Library_Name.setLayoutX(310);
		btn_Change_Library_Name.setLayoutY(55);
		libraryPane.getChildren().add(btn_Change_Library_Name);
		btn_Change_Library_Name.setStyle(IDLE_BUTTON_STYLE);
		btn_Change_Library_Name.setOnMouseEntered(e -> btn_Change_Library_Name.setStyle(HOVERED_BUTTON_STYLE));
		btn_Change_Library_Name.setOnMouseExited(e -> btn_Change_Library_Name.setStyle(IDLE_BUTTON_STYLE));
		
		
		btn_Change_Library_Name.setOnMouseClicked(e -> {
			if (btn_Change_Library_Name.getText().equals("Change")) {
				tf_Library_Name.setEditable(true);
				btn_Change_Library_Name.setText("Save");
			}
			else if (btn_Change_Library_Name.getText().equals("Save")) {
					tf_Library_Name.setEditable(false);
					btn_Change_Library_Name.setText("Change");
					my_Current_Library.setLibrary_Name(tf_Library_Name.getText());	
					saveData(newLibraryFile);		
			}
		});
		
		Image imageSaveBackup = new Image("/img/save_all.png");
        ImageView imageViewSaveBackup = new ImageView(imageSaveBackup);
        Image imageSave = new Image("/img/save.png");
        ImageView imageViewSave = new ImageView(imageSave);
		
		Button btnSaveBackup = new Button("Backup Library Data", imageViewSaveBackup);
		btnSaveBackup.setLayoutX(380);
		btnSaveBackup.setLayoutY(55);
		libraryPane.getChildren().add(btnSaveBackup);
		
		btnSaveBackup.setStyle(IDLE_BUTTON_STYLE);
		btnSaveBackup.setOnMouseEntered(e -> btnSaveBackup.setStyle(HOVERED_BUTTON_STYLE));
		btnSaveBackup.setOnMouseExited(e -> btnSaveBackup.setStyle(IDLE_BUTTON_STYLE));
		
		btnSaveBackup.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
 
            //Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Library files (*.library)", "*.library");
            fileChooser.getExtensionFilters().add(extFilter);
 
            //Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);
 
            if (file != null) {
                saveBackupToFile(file);
            }
        });
		
		
		Label lbl_list_Of_Books = new Label("List of Books (Ctrl + click to deselect): ");
		lbl_list_Of_Books.setLayoutX(80);
		lbl_list_Of_Books.setLayoutY(100);
		libraryPane.getChildren().add(lbl_list_Of_Books);
		
		ObservableList<Book> book_data = FXCollections.observableArrayList();
	    ListView<Book> listViewBooks = new ListView<Book>(book_data);
	    listViewBooks.setLayoutX(80);
	    listViewBooks.setLayoutY(120);
	    listViewBooks.setPrefSize(450, 250);
	    fillBookList(book_data);
	    libraryPane.getChildren().add(listViewBooks);
	    
	    Button btn_Add_Book = new Button("Add a Book!");
	    btn_Add_Book.setLayoutX(100);
	    btn_Add_Book.setLayoutY(380);
		libraryPane.getChildren().add(btn_Add_Book);
		btn_Add_Book.setStyle(IDLE_BUTTON_STYLE);
		btn_Add_Book.setOnMouseEntered(e -> btn_Add_Book.setStyle(HOVERED_BUTTON_STYLE));
		btn_Add_Book.setOnMouseExited(e -> btn_Add_Book.setStyle(IDLE_BUTTON_STYLE));
		
		Button btn_Edit_Book = new Button("Edit Book!");
		btn_Edit_Book.setLayoutX(195);
		btn_Edit_Book.setLayoutY(380);
		btn_Edit_Book.setDisable(true);
		libraryPane.getChildren().add(btn_Edit_Book);
		btn_Edit_Book.setStyle(IDLE_BUTTON_STYLE);
		btn_Edit_Book.setOnMouseEntered(e -> btn_Edit_Book.setStyle(HOVERED_BUTTON_STYLE));
		btn_Edit_Book.setOnMouseExited(e -> btn_Edit_Book.setStyle(IDLE_BUTTON_STYLE));
		
		Button btn_Remove_Book = new Button("Remove Book!");
		btn_Remove_Book.setLayoutX(275);
		btn_Remove_Book.setLayoutY(380);
		btn_Remove_Book.setDisable(true);
		libraryPane.getChildren().add(btn_Remove_Book);
		btn_Remove_Book.setStyle(IDLE_BUTTON_STYLE);
		btn_Remove_Book.setOnMouseEntered(e -> btn_Remove_Book.setStyle(HOVERED_BUTTON_STYLE));
		btn_Remove_Book.setOnMouseExited(e -> btn_Remove_Book.setStyle(IDLE_BUTTON_STYLE));
		
		Label lbl_NumBooks = new Label("Total Number of Books: " + my_Current_Library.getNum_Of_Books());
		lbl_NumBooks.setLayoutX(380);
		lbl_NumBooks.setLayoutY(385);
		libraryPane.getChildren().add(lbl_NumBooks);
	    
		Label lbl_NumPagesShow = new Label("Number of Pages: ");
		lbl_NumPagesShow.setLayoutX(80);
		lbl_NumPagesShow.setLayoutY(440);
		libraryPane.getChildren().add(lbl_NumPagesShow);
		
		Label lbl_ISBNShow = new Label("ISBN#: ");
		lbl_ISBNShow.setLayoutX(80);
		lbl_ISBNShow.setLayoutY(460);
		libraryPane.getChildren().add(lbl_ISBNShow);
		
		Label lbl_KeywordsShow = new Label("Keywords: ");
		lbl_KeywordsShow.setLayoutX(80);
		lbl_KeywordsShow.setLayoutY(480);
		lbl_KeywordsShow.setMaxWidth(510);
		lbl_KeywordsShow.setWrapText(true);
		libraryPane.getChildren().add(lbl_KeywordsShow);
		
		Label lbl_DateShow = new Label("Date Added: ");
		lbl_DateShow.setLayoutX(80);
		lbl_DateShow.setLayoutY(420);
		libraryPane.getChildren().add(lbl_DateShow);
		
		listViewBooks.setOnMouseClicked(e ->{
			my_Current_Book = listViewBooks.getSelectionModel().getSelectedItem();
	    	if (my_Current_Book!= null) {
	    		btn_Remove_Book.setDisable(false);
	    		btn_Edit_Book.setDisable(false);
	    		lbl_NumPagesShow.setText("Number of Pages: " + my_Current_Book.getNumber_Of_Pages());
	    		lbl_ISBNShow.setText("ISBN#: " + my_Current_Book.getISBN());
	    		lbl_KeywordsShow.setText("Keywords: " + my_Current_Book.getKeywords());
	    		lbl_DateShow.setText("Date Added: " + my_Current_Book.getDateAdded());
	    	}
	    	else {
	    		btn_Remove_Book.setDisable(true);
	    		btn_Edit_Book.setDisable(true);
	    		lbl_NumPagesShow.setText("Number of Pages: ");
	    		lbl_ISBNShow.setText("ISBN#: ");
	    		lbl_KeywordsShow.setText("Keywords: ");
	    		lbl_DateShow.setText("Date Added: ");
	    	}
		});
		
		listViewBooks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
		    @Override
		    public void changed(ObservableValue<? extends Book> observable, Book oldValue, Book newValue) {
		    	my_Current_Book = newValue;
		    	if (my_Current_Book!= null) {
		    		btn_Remove_Book.setDisable(false);
		    		btn_Edit_Book.setDisable(false);
		    		lbl_NumPagesShow.setText("Number of Pages: " + my_Current_Book.getNumber_Of_Pages());
		    		lbl_ISBNShow.setText("ISBN#: " + my_Current_Book.getISBN());
		    		lbl_KeywordsShow.setText("Keywords: " + my_Current_Book.getKeywords());
		    		lbl_DateShow.setText("Date Added: " + my_Current_Book.getDateAdded());
		    	}
		    	else {
		    		btn_Remove_Book.setDisable(true);
		    		btn_Edit_Book.setDisable(true);
		    		lbl_NumPagesShow.setText("Number of Pages: ");
		    		lbl_ISBNShow.setText("ISBN#: ");
		    		lbl_KeywordsShow.setText("Keywords: ");
		    		lbl_DateShow.setText("Date Added: ");
		    	}
		    }
		});
		
		// Adding a Book
		
		btn_Add_Book.setOnMouseClicked(e -> {
			listViewBooks.setDisable(true);
			lbl_DateShow.setText("Date Added: ");			
			lbl_NumPagesShow.setText("Number of Pages: ");
    		lbl_ISBNShow.setText("ISBN#: ");
    		lbl_KeywordsShow.setText("Keywords: ");
			
			my_Current_Book = new Book("");
			Pane addABookPane = new Pane();	
			
			Label lbl_Book_Title = new Label("Book Title: ");
			lbl_Book_Title.setLayoutX(10);
			lbl_Book_Title.setLayoutY(10);
			addABookPane.getChildren().add(lbl_Book_Title);
			
			TextField tf_Book_Title = new TextField();
			tf_Book_Title.setLayoutX(70);
			tf_Book_Title.setLayoutY(10);
			tf_Book_Title.setEditable(true);
			addABookPane.getChildren().add(tf_Book_Title);	
			
			Label lbl_Authors = new Label("Authors: ");
			lbl_Authors.setLayoutX(10);
			lbl_Authors.setLayoutY(40);
			addABookPane.getChildren().add(lbl_Authors);
			
			ObservableList<String> author_data = FXCollections.observableArrayList();
		    ListView<String> listViewAuthors = new ListView<String>(author_data);
		    listViewAuthors.setLayoutX(10);
		    listViewAuthors.setLayoutY(60);
		    listViewAuthors.setPrefSize(150, 80);
		    fillAuthorList(author_data);
		    addABookPane.getChildren().add(listViewAuthors);
		    
		    TextField tf_Author = new TextField();
		    tf_Author.setLayoutX(165);
		    tf_Author.setLayoutY(60);
		    tf_Author.setEditable(true);
			addABookPane.getChildren().add(tf_Author);	
			
			Button btn_Add = new Button("Add!");
			btn_Add.setLayoutX(180);
			btn_Add.setLayoutY(100);
			addABookPane.getChildren().add(btn_Add);
			btn_Add.setStyle(IDLE_BUTTON_STYLE);
			btn_Add.setOnMouseEntered(ex -> btn_Add.setStyle(HOVERED_BUTTON_STYLE));
			btn_Add.setOnMouseExited(ex -> btn_Add.setStyle(IDLE_BUTTON_STYLE));
			
			Button btn_Remove = new Button("Clear All Author(s)");
			btn_Remove.setDisable(true);
			btn_Remove.setLayoutX(235);
			btn_Remove.setLayoutY(100);
			addABookPane.getChildren().add(btn_Remove);
			btn_Remove.setStyle(IDLE_BUTTON_STYLE);
			btn_Remove.setOnMouseEntered(ex -> btn_Add.setStyle(HOVERED_BUTTON_STYLE));
			btn_Remove.setOnMouseExited(ex -> btn_Add.setStyle(IDLE_BUTTON_STYLE));
		    
			listViewAuthors.setOnMouseClicked(elistViewAuthors -> {
				
		    	my_Current_Author = listViewAuthors.getSelectionModel().getSelectedItem();
		    	if (my_Current_Author!= null) {
		    		btn_Remove.setDisable(false);
		    		tf_Author.setText(my_Current_Author);
		    	}
		    	else {
		    		btn_Remove.setDisable(true);
		    	}
		    });
		    
			btn_Add.setOnMouseClicked(eAddAuthor -> {
				if (!tf_Author.getText().equals("")) {
					my_Current_Book.addAuthor(tf_Author.getText());
					tf_Author.setText("");
					fillAuthorList(author_data);
					listViewAuthors.refresh();
				}
			});
			
			btn_Remove.setOnMouseClicked(eAddAuthor -> {
				my_Current_Book.removeAllAuthors();
				fillAuthorList(author_data);
				listViewBooks.refresh();
				tf_Author.setText("");
				btn_Remove.setDisable(true);
			});
			
			Button btn_Save = new Button("Save!", imageViewSave);
			btn_Save.setLayoutX(275);
			btn_Save.setLayoutY(380);
			addABookPane.getChildren().add(btn_Save);
			btn_Save.setStyle(IDLE_BUTTON_STYLE);
			btn_Save.setOnMouseEntered(ex -> btn_Save.setStyle(HOVERED_BUTTON_STYLE));
			btn_Save.setOnMouseExited(ex -> btn_Save.setStyle(IDLE_BUTTON_STYLE));
			
			Button btn_Cancel = new Button("Cancel");
			btn_Cancel.setLayoutX(200);
			btn_Cancel.setLayoutY(380);
			addABookPane.getChildren().add(btn_Cancel);
			btn_Cancel.setStyle(IDLE_BUTTON_STYLE);
			btn_Cancel.setOnMouseEntered(ex -> btn_Cancel.setStyle(HOVERED_BUTTON_STYLE));
			btn_Cancel.setOnMouseExited(ex -> btn_Cancel.setStyle(IDLE_BUTTON_STYLE));
			
			Scene addABookScene = new Scene(addABookPane, 380, 435);
			Stage primaryStage2 = new Stage();
						
			primaryStage2.setTitle("Add a Book");
			primaryStage2.setScene(addABookScene);
			primaryStage2.setResizable(false);
			primaryStage2.show();
			
			btn_Add_Book.setDisable(true);
			btn_Edit_Book.setDisable(true);
			btn_Remove_Book.setDisable(true);
			
			Label lbl_Volume = new Label("Volume: ");
			lbl_Volume.setLayoutX(10);
			lbl_Volume.setLayoutY(150);
			addABookPane.getChildren().add(lbl_Volume);
			
			TextField tf_Volume = new TextField();
			tf_Volume.setLayoutX(110);
			tf_Volume.setLayoutY(150);
			tf_Volume.setMaxWidth(110);
			tf_Volume.setEditable(true);
			addABookPane.getChildren().add(tf_Volume);
			
			Label lbl_Edition = new Label("Edition: ");
			lbl_Edition.setLayoutX(10);
			lbl_Edition.setLayoutY(180);
			addABookPane.getChildren().add(lbl_Edition);
			
			TextField tf_Edition = new TextField();
			tf_Edition.setLayoutX(110);
			tf_Edition.setLayoutY(180);
			tf_Edition.setMaxWidth(110);
			tf_Edition.setEditable(true);
			addABookPane.getChildren().add(tf_Edition);
			
			Label lbl_NumPages = new Label("Number of Pages: ");
			lbl_NumPages.setLayoutX(10);
			lbl_NumPages.setLayoutY(210);
			addABookPane.getChildren().add(lbl_NumPages);
			
			TextField tf_NumPages = new TextField();
			tf_NumPages.setLayoutX(110);
			tf_NumPages.setLayoutY(210);
			tf_NumPages.setEditable(true);
			tf_NumPages.setMaxWidth(110);
			addABookPane.getChildren().add(tf_NumPages);
			
			Label lbl_ISBN = new Label("ISBN#: ");
			lbl_ISBN.setLayoutX(10);
			lbl_ISBN.setLayoutY(240);
			addABookPane.getChildren().add(lbl_ISBN);
			
			TextField tf_ISBN = new TextField();
			tf_ISBN.setLayoutX(110);
			tf_ISBN.setLayoutY(240);
			tf_ISBN.setEditable(true);
			tf_ISBN.setMaxWidth(110);
			addABookPane.getChildren().add(tf_ISBN);
						
			Label lbl_Keywords = new Label("Keywords: ");
			lbl_Keywords.setLayoutX(10);
			lbl_Keywords.setLayoutY(270);
			addABookPane.getChildren().add(lbl_Keywords);
			
			ObservableList<String> keywords_data = FXCollections.observableArrayList();
		    ListView<String> listViewKeywords = new ListView<String>(keywords_data);
		    listViewKeywords.setLayoutX(10);
		    listViewKeywords.setLayoutY(290);
		    listViewKeywords.setPrefSize(150, 80);
		    fillKeywordsList(keywords_data);
		    addABookPane.getChildren().add(listViewKeywords);
		    
		    TextField tf_Keywords = new TextField();
		    tf_Keywords.setLayoutX(165);
		    tf_Keywords.setLayoutY(290);
		    tf_Keywords.setEditable(true);
			addABookPane.getChildren().add(tf_Keywords);	
			
			Button btn_AddKeyword = new Button("Add!");
			btn_AddKeyword.setLayoutX(180);
			btn_AddKeyword.setLayoutY(330);
			addABookPane.getChildren().add(btn_AddKeyword);
			btn_AddKeyword.setStyle(IDLE_BUTTON_STYLE);
			btn_AddKeyword.setOnMouseEntered(ex -> btn_AddKeyword.setStyle(HOVERED_BUTTON_STYLE));
			btn_AddKeyword.setOnMouseExited(ex -> btn_AddKeyword.setStyle(IDLE_BUTTON_STYLE));
			
			Button btn_RemoveKeyword = new Button("Clear All Keyword(s)");
			btn_RemoveKeyword.setDisable(true);
			btn_RemoveKeyword.setLayoutX(235);
			btn_RemoveKeyword.setLayoutY(330);
			addABookPane.getChildren().add(btn_RemoveKeyword);
			btn_RemoveKeyword.setStyle(IDLE_BUTTON_STYLE);
			btn_RemoveKeyword.setOnMouseEntered(ex -> btn_RemoveKeyword.setStyle(HOVERED_BUTTON_STYLE));
			btn_RemoveKeyword.setOnMouseExited(ex -> btn_RemoveKeyword.setStyle(IDLE_BUTTON_STYLE));
			
			listViewKeywords.setOnMouseClicked(elistViewKeywords -> {
				
		    	my_Current_Keyword = listViewKeywords.getSelectionModel().getSelectedItem();
		    	if (my_Current_Keyword!= null) {
		    		btn_RemoveKeyword.setDisable(false);
		    		tf_Keywords.setText(my_Current_Keyword);
		    	}
		    	else {
		    		btn_RemoveKeyword.setDisable(true);
		    	}
		    });
			
			btn_AddKeyword.setOnMouseClicked(eAddKeyword -> {
				if (!tf_Keywords.getText().equals("")) {
					my_Current_Book.addKeyword(tf_Keywords.getText());
					tf_Keywords.setText("");
					fillKeywordsList(keywords_data);
					listViewKeywords.refresh();
				}
			});
			
			btn_RemoveKeyword.setOnMouseClicked(eAddKeyword -> {
				my_Current_Book.removeAllKeywords();
				fillKeywordsList(keywords_data);
				listViewKeywords.refresh();
				tf_Keywords.setText("");
				btn_RemoveKeyword.setDisable(true);
			});
			
			btn_Save.setOnMouseClicked(eSave -> {
				
				my_Current_Book.setTitle(tf_Book_Title.getText());
				my_Current_Book.setVolume(tf_Volume.getText());
				my_Current_Book.setEdition(tf_Edition.getText());
				my_Current_Book.setNumber_Of_Pages(tf_NumPages.getText());
				my_Current_Book.setISBN(tf_ISBN.getText());
				my_Current_Library.addABook(my_Current_Book);
				listViewBooks.setDisable(false);
				fillBookList(book_data);
				listViewBooks.refresh();
				btn_Add_Book.setDisable(false);
				lbl_NumBooks.setText("Total Number of Books: " + my_Current_Library.getNum_Of_Books());
				saveData(newLibraryFile);	
				primaryStage2.close();
			});
			
			btn_Cancel.setOnMouseClicked(eCancel -> {
				listViewBooks.refresh();
				listViewBooks.setDisable(false);
				btn_Add_Book.setDisable(false);
				primaryStage2.close();
			});
			
			primaryStage2.setOnCloseRequest(event -> {
				listViewBooks.setDisable(false);
				btn_Add_Book.setDisable(false);
				
			});
			
		});
		
		// Editing Book
		
		btn_Edit_Book.setOnMouseClicked(e -> {
			
			listViewBooks.setDisable(true);
			lbl_DateShow.setText("Date Added: ");			
			lbl_NumPagesShow.setText("Number of Pages: ");
    		lbl_ISBNShow.setText("ISBN#: ");
    		lbl_KeywordsShow.setText("Keywords: ");
			
			Pane editBookPane = new Pane();	
			
			Label lbl_Book_Title = new Label("Book Title: ");
			lbl_Book_Title.setLayoutX(10);
			lbl_Book_Title.setLayoutY(10);
			editBookPane.getChildren().add(lbl_Book_Title);
			
			TextField tf_Book_Title = new TextField();
			tf_Book_Title.setText(my_Current_Book.getTitle());
			tf_Book_Title.setLayoutX(70);
			tf_Book_Title.setLayoutY(10);
			tf_Book_Title.setEditable(true);
			editBookPane.getChildren().add(tf_Book_Title);	
			
			Label lbl_Authors = new Label("Authors: ");
			lbl_Authors.setLayoutX(10);
			lbl_Authors.setLayoutY(40);
			editBookPane.getChildren().add(lbl_Authors);
			
			ObservableList<String> author_data = FXCollections.observableArrayList();
		    ListView<String> listViewAuthors = new ListView<String>(author_data);
		    listViewAuthors.setLayoutX(10);
		    listViewAuthors.setLayoutY(60);
		    listViewAuthors.setPrefSize(150, 80);
		    fillAuthorList(author_data);
		    editBookPane.getChildren().add(listViewAuthors);
		    
		    TextField tf_Author = new TextField();
		    tf_Author.setLayoutX(165);
		    tf_Author.setLayoutY(60);
		    tf_Author.setEditable(true);
			editBookPane.getChildren().add(tf_Author);	
			
			Button btn_Add = new Button("Add!");
			btn_Add.setLayoutX(180);
			btn_Add.setLayoutY(100);
			editBookPane.getChildren().add(btn_Add);
			btn_Add.setStyle(IDLE_BUTTON_STYLE);
			btn_Add.setOnMouseEntered(ex -> btn_Add.setStyle(HOVERED_BUTTON_STYLE));
			btn_Add.setOnMouseExited(ex -> btn_Add.setStyle(IDLE_BUTTON_STYLE));
			
			Button btn_Remove = new Button("Clear All Author(s)");
			btn_Remove.setDisable(true);
			btn_Remove.setLayoutX(235);
			btn_Remove.setLayoutY(100);
			editBookPane.getChildren().add(btn_Remove);
			btn_Remove.setStyle(IDLE_BUTTON_STYLE);
			btn_Remove.setOnMouseEntered(ex -> btn_Remove.setStyle(HOVERED_BUTTON_STYLE));
			btn_Remove.setOnMouseExited(ex -> btn_Remove.setStyle(IDLE_BUTTON_STYLE));
			
			my_Current_Edit_AuthorList = (ArrayList<String>)(my_Current_Book.getAuthors()).clone();
			
			listViewAuthors.setOnMouseClicked(elistViewAuthors -> {
				
		    	my_Current_Author = listViewAuthors.getSelectionModel().getSelectedItem();
		    	if (my_Current_Author!= null) {
		    		btn_Remove.setDisable(false);
		    		tf_Author.setText(my_Current_Author);
		    	}
		    	else {
		    		btn_Remove.setDisable(true);
		    	}
		    });
		    
			btn_Add.setOnMouseClicked(eAddAuthor -> {
				if (!tf_Author.getText().equals("")) {
					my_Current_Edit_AuthorList.add(tf_Author.getText());
					tf_Author.setText("");
					fillEditAuthorList(author_data);
					listViewAuthors.refresh();
				}
			});
			
			btn_Remove.setOnMouseClicked(eAddAuthor -> {
				my_Current_Edit_AuthorList.clear();
				fillEditAuthorList(author_data);
				listViewBooks.refresh();
				tf_Author.setText("");
				btn_Remove.setDisable(true);
			});
			
			Button btn_Save = new Button("Save!", imageViewSave);
			btn_Save.setLayoutX(275);
			btn_Save.setLayoutY(380);
			editBookPane.getChildren().add(btn_Save);
			btn_Save.setStyle(IDLE_BUTTON_STYLE);
			btn_Save.setOnMouseEntered(ex -> btn_Save.setStyle(HOVERED_BUTTON_STYLE));
			btn_Save.setOnMouseExited(ex -> btn_Save.setStyle(IDLE_BUTTON_STYLE));
			
			Button btn_Cancel = new Button("Cancel");
			btn_Cancel.setLayoutX(200);
			btn_Cancel.setLayoutY(380);
			editBookPane.getChildren().add(btn_Cancel);
			btn_Cancel.setStyle(IDLE_BUTTON_STYLE);
			btn_Cancel.setOnMouseEntered(ex -> btn_Cancel.setStyle(HOVERED_BUTTON_STYLE));
			btn_Cancel.setOnMouseExited(ex -> btn_Cancel.setStyle(IDLE_BUTTON_STYLE));
			
			Scene editBookScene = new Scene(editBookPane, 380, 425);
			Stage primaryStage2 = new Stage();
						
			primaryStage2.setTitle("Edit Book");
			primaryStage2.setScene(editBookScene);
			primaryStage2.setResizable(false);
			primaryStage2.show();
			
			btn_Add_Book.setDisable(true);
			btn_Edit_Book.setDisable(true);
			btn_Remove_Book.setDisable(true);
			
			Label lbl_Volume = new Label("Volume: ");
			lbl_Volume.setLayoutX(10);
			lbl_Volume.setLayoutY(150);
			editBookPane.getChildren().add(lbl_Volume);
			
			TextField tf_Volume = new TextField();
			tf_Volume.setText(my_Current_Book.getVolume());
			tf_Volume.setLayoutX(110);
			tf_Volume.setLayoutY(150);
			tf_Volume.setMaxWidth(110);
			tf_Volume.setEditable(true);
			editBookPane.getChildren().add(tf_Volume);
			
			Label lbl_Edition = new Label("Edition: ");
			lbl_Edition.setLayoutX(10);
			lbl_Edition.setLayoutY(180);
			editBookPane.getChildren().add(lbl_Edition);
			
			TextField tf_Edition = new TextField();
			tf_Edition.setText(my_Current_Book.getEdition());
			tf_Edition.setLayoutX(110);
			tf_Edition.setLayoutY(180);
			tf_Edition.setMaxWidth(110);
			tf_Edition.setEditable(true);
			editBookPane.getChildren().add(tf_Edition);
			
			Label lbl_NumPages = new Label("Number of Pages: ");
			lbl_NumPages.setLayoutX(10);
			lbl_NumPages.setLayoutY(210);
			editBookPane.getChildren().add(lbl_NumPages);
			
			TextField tf_NumPages = new TextField();
			tf_NumPages.setText(my_Current_Book.getNumber_Of_Pages());
			tf_NumPages.setLayoutX(110);
			tf_NumPages.setLayoutY(210);
			tf_NumPages.setEditable(true);
			tf_NumPages.setMaxWidth(110);
			editBookPane.getChildren().add(tf_NumPages);
			
			Label lbl_ISBN = new Label("ISBN#: ");
			lbl_ISBN.setLayoutX(10);
			lbl_ISBN.setLayoutY(240);
			editBookPane.getChildren().add(lbl_ISBN);
			
			TextField tf_ISBN = new TextField();
			tf_ISBN.setText(my_Current_Book.getISBN());
			tf_ISBN.setLayoutX(110);
			tf_ISBN.setLayoutY(240);
			tf_ISBN.setEditable(true);
			tf_ISBN.setMaxWidth(110);
			editBookPane.getChildren().add(tf_ISBN);
						
			Label lbl_Keywords = new Label("Keywords: ");
			lbl_Keywords.setLayoutX(10);
			lbl_Keywords.setLayoutY(270);
			editBookPane.getChildren().add(lbl_Keywords);
			
			my_Current_Edit_KeywordList = (ArrayList<String>)(my_Current_Book.getKeywords()).clone();
			
			ObservableList<String> keywords_data = FXCollections.observableArrayList();
		    ListView<String> listViewKeywords = new ListView<String>(keywords_data);
		    listViewKeywords.setLayoutX(10);
		    listViewKeywords.setLayoutY(290);
		    listViewKeywords.setPrefSize(150, 80);
		    fillKeywordsList(keywords_data);
		    editBookPane.getChildren().add(listViewKeywords);
		    
		    TextField tf_Keywords = new TextField();
		    tf_Keywords.setLayoutX(165);
		    tf_Keywords.setLayoutY(290);
		    tf_Keywords.setEditable(true);
			editBookPane.getChildren().add(tf_Keywords);	
			
			Button btn_AddKeyword = new Button("Add!");
			btn_AddKeyword.setLayoutX(180);
			btn_AddKeyword.setLayoutY(330);
			editBookPane.getChildren().add(btn_AddKeyword);
			btn_AddKeyword.setStyle(IDLE_BUTTON_STYLE);
			btn_AddKeyword.setOnMouseEntered(ex -> btn_AddKeyword.setStyle(HOVERED_BUTTON_STYLE));
			btn_AddKeyword.setOnMouseExited(ex -> btn_AddKeyword.setStyle(IDLE_BUTTON_STYLE));
			
			Button btn_RemoveKeyword = new Button("Clear All Keyword(s)");
			btn_RemoveKeyword.setDisable(true);
			btn_RemoveKeyword.setLayoutX(235);
			btn_RemoveKeyword.setLayoutY(330);
			editBookPane.getChildren().add(btn_RemoveKeyword);
			btn_RemoveKeyword.setStyle(IDLE_BUTTON_STYLE);
			btn_RemoveKeyword.setOnMouseEntered(ex -> btn_RemoveKeyword.setStyle(HOVERED_BUTTON_STYLE));
			btn_RemoveKeyword.setOnMouseExited(ex -> btn_RemoveKeyword.setStyle(IDLE_BUTTON_STYLE));
			
			listViewKeywords.setOnMouseClicked(elistViewKeywords -> {
				
		    	my_Current_Keyword = listViewKeywords.getSelectionModel().getSelectedItem();
		    	if (my_Current_Keyword!= null) {
		    		btn_RemoveKeyword.setDisable(false);
		    		tf_Keywords.setText(my_Current_Keyword);
		    	}
		    	else {
		    		btn_RemoveKeyword.setDisable(true);
		    	}
		    });
			
			btn_AddKeyword.setOnMouseClicked(eAddKeyword -> {
				if (!tf_Keywords.getText().equals("")) {
					my_Current_Edit_KeywordList.add(tf_Keywords.getText());
					tf_Keywords.setText("");
					fillEditKeywordsList(keywords_data);
					listViewKeywords.refresh();
				}
			});
			
			btn_RemoveKeyword.setOnMouseClicked(eAddKeyword -> {
				my_Current_Edit_KeywordList.clear();
				fillEditKeywordsList(keywords_data);
				listViewKeywords.refresh();
				tf_Keywords.setText("");
				btn_RemoveKeyword.setDisable(true);
			});
			
			btn_Save.setOnMouseClicked(eSave -> {
				my_Current_Library.removeABook(my_Current_Book);
				my_Current_Book.setTitle(tf_Book_Title.getText());
				my_Current_Book.setVolume(tf_Volume.getText());
				my_Current_Book.setEdition(tf_Edition.getText());
				my_Current_Book.setNumber_Of_Pages(tf_NumPages.getText());
				my_Current_Book.setISBN(tf_ISBN.getText());
				my_Current_Book.setAuthors(my_Current_Edit_AuthorList);
				my_Current_Book.setKeywords(my_Current_Edit_KeywordList);
				my_Current_Library.addABook(my_Current_Book);
				listViewBooks.setDisable(false);
				fillBookList(book_data);
				listViewBooks.refresh();
				btn_Add_Book.setDisable(false);
				saveData(newLibraryFile);	
				primaryStage2.close();
				
			});
			
			btn_Cancel.setOnMouseClicked(eCancel -> {
				listViewBooks.refresh();
				listViewBooks.setDisable(false);
				btn_Add_Book.setDisable(false);
				primaryStage2.close();
			});
			
			primaryStage2.setOnCloseRequest(event -> {
				listViewBooks.setDisable(false);
				btn_Add_Book.setDisable(false);
				
			});
			
		});
		
		//Removing Book
		
		btn_Remove_Book.setOnMouseClicked(e -> {			
			my_Current_Library.removeABook(my_Current_Book);
			fillBookList(book_data);
			listViewBooks.refresh();
			btn_Remove_Book.setDisable(true);
    		btn_Edit_Book.setDisable(true);
    		saveData(newLibraryFile);	
    		lbl_NumBooks.setText("Total Number of Books: " + my_Current_Library.getNum_Of_Books());
		});
	    
		
		
		Scene mainScene = new Scene(mainPane, 600, 600);											// Create 600 by 600 scene with main pane
		primaryStage.setTitle("Library Database Management System version 1.9 by Vision Paudel");	// Set title
		primaryStage.setScene(mainScene);															// Set scene unto stage
		primaryStage.setResizable(false);															// Disable window resizing
		primaryStage.show();																		// Display the stage
		
		//Save on Exit		
		primaryStage.setOnCloseRequest(event -> {
			saveData(newLibraryFile);
		});
			
	}
	
	private void saveCSVFile(File file) {
		PrintWriter newWriter;
		try {
			newWriter = new PrintWriter(file);
			
			ArrayList<Book> listOfBooks = my_Current_Library.getList_Of_Books();
			
			if (indexOfExport == 3) {
				
				newWriter.println("Library name: " + "," + my_Current_Library.getLibrary_Name().replaceAll(",", " "));
				newWriter.println("Total number of books: " + "," + listOfBooks.size());
				newWriter.println();
				
				newWriter.print("Title: ,");
				for(int i = 0 ; i < listOfBooks.size(); i++) {
					newWriter.print(listOfBooks.get(i).getTitle().replaceAll(",", " ") + ",");
				}
				newWriter.println();
				
				newWriter.print("Volume: ,");
				for(int i = 0 ; i < listOfBooks.size(); i++) {
					newWriter.print(listOfBooks.get(i).getVolume().replaceAll(",", " ") + ",");
				}
				newWriter.println();
				
				newWriter.print("Edition: ,");
				for(int i = 0 ; i < listOfBooks.size(); i++) {
					newWriter.print(listOfBooks.get(i).getEdition().replaceAll(",", " ") + ",");
				}
				newWriter.println();
				
				newWriter.print("Number of Pages: ,");
				for(int i = 0 ; i < listOfBooks.size(); i++) {
					newWriter.print(listOfBooks.get(i).getNumber_Of_Pages().replaceAll(",", " ") + ",");
				}
				newWriter.println();
				
				newWriter.print("ISBN: ,");
				for(int i = 0 ; i < listOfBooks.size(); i++) {
					newWriter.print(listOfBooks.get(i).getISBN().replaceAll(",", " ") + ",");
				}
				newWriter.println();
				
				newWriter.print("Keywords: ,");
				for(int i = 0 ; i < listOfBooks.size(); i++) {
					newWriter.print(listOfBooks.get(i).getKeywords().toString().replaceAll(",", " ") + ",");
				}
				newWriter.println();
				
				newWriter.print("Date Added: ,");
				for(int i = 0 ; i < listOfBooks.size(); i++) {
					newWriter.print(listOfBooks.get(i).getDateAdded() + ",");
				}
				newWriter.println();
				
				
			}else if (indexOfExport == 2) {
				
				newWriter.println("Library name: " + "," + my_Current_Library.getLibrary_Name().replaceAll(",", " "));
				newWriter.println("Total number of books: " + "," + listOfBooks.size());
				newWriter.println();
				
				newWriter.println("Title: ,Volume: ,Edition: ,Number of Pages: ,ISBN: ,Keywords: , Date Added: ,");
				for(int i = 0 ; i < listOfBooks.size(); i++) {
					newWriter.println(listOfBooks.get(i).getTitle().replaceAll(",", " ") + "," + listOfBooks.get(i).getVolume().replaceAll(",", " ") + "," + listOfBooks.get(i).getEdition().replaceAll(",", " ") + "," + listOfBooks.get(i).getNumber_Of_Pages().replaceAll(",", " ") + "," + listOfBooks.get(i).getISBN().replaceAll(",", " ") + "," + listOfBooks.get(i).getKeywords().toString().replaceAll(",", " ") + "," + listOfBooks.get(i).getDateAdded());
				}
			}
			
			newWriter.close();
		}catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

	// Saving .txt file
	private void saveTextFile(File file) {
		PrintWriter newWriter;
		try {
			newWriter = new PrintWriter(file);
			
			ArrayList<Book> listOfBooks = my_Current_Library.getList_Of_Books();
			newWriter.println(my_Current_Library.getLibrary_Name());
			newWriter.println("Total number of books: " + listOfBooks.size());
			newWriter.println();
			for(int i = 0 ; i < listOfBooks.size(); i++) {
				newWriter.println((i+1) + ". " + listOfBooks.get(i));
				newWriter.println("Number of Pages: " + listOfBooks.get(i).getNumber_Of_Pages());
				newWriter.println("ISBN#: " + listOfBooks.get(i).getISBN());
				newWriter.println("Keywords: " + listOfBooks.get(i).getKeywords());
				newWriter.println("Date Added: " + listOfBooks.get(i).getDateAdded());
				newWriter.println();
			}
			
			newWriter.close();
		}catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

	// Saving Backup File
	private void saveBackupToFile(File file) {
		try {
			fout = new FileOutputStream(file);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(my_Current_Library);		
		}catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			closeInputOutput();
		}
	}
		
	// Updating Keywords during Editing of Book
	private void fillEditKeywordsList(ObservableList<String> keywords_data) {
		keywords_data.clear();
		ArrayList<String> list_Of_Keywords = my_Current_Edit_KeywordList;	
	    for(int i = 0 ; i < list_Of_Keywords.size(); i++) {
	    	keywords_data.add(list_Of_Keywords.get(i));
	    }
		
	}
	
	// Updating Authors during Editing of Book
	private void fillEditAuthorList(ObservableList<String> author_data) {
		author_data.clear();
		ArrayList<String> list_Of_Authors = my_Current_Edit_AuthorList;	
	    for(int i = 0 ; i < list_Of_Authors.size(); i++) {
	    	author_data.add(list_Of_Authors.get(i));
	    }
		
	}

	// Saves current library into file and loads it back from the file
	private void saveData(File newLibraryFile) {
		try {
			fout = new FileOutputStream(newLibraryFile);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(my_Current_Library);
		
			fin = new FileInputStream(newLibraryFile);
			ois = new ObjectInputStream(fin);
			my_Current_Library = (Library) ois.readObject();
				
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			closeInputOutput();
		}
				
	}

	// Closes input and output
	private void closeInputOutput() {
		if (fout != null) {
			try {
				fout.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		if (fin != null) {
			try {
				fin.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		if (oos != null) {
			try {
				oos.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		if (ois != null) {
			try {
				ois.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	// Fills the keyword list
	private void fillKeywordsList(ObservableList<String> keywords_data) {
		keywords_data.clear();
		ArrayList<String> list_Of_Keywords = my_Current_Book.getKeywords();	
	    for(int i = 0 ; i < list_Of_Keywords.size(); i++) {
	    	keywords_data.add(list_Of_Keywords.get(i));
	    }
		
	}
	
	// Fills the author list
	private void fillAuthorList(ObservableList<String> data) {
		data.clear();
		ArrayList<String> list_Of_Authors = my_Current_Book.getAuthors();	
	    for(int i = 0 ; i < list_Of_Authors.size(); i++) {
	    	data.add(list_Of_Authors.get(i));
	    }
	}
	
	// Fills/ updates the book list
	private void fillBookList(ObservableList<Book> data) {
		data.clear();
		ArrayList<Book> list_Of_Books = my_Current_Library.getList_Of_Books();		
	    for(int i = 0 ; i < list_Of_Books.size(); i++) {
	    	data.add(list_Of_Books.get(i));
	    }		
	}
		
	// Creates a StackPane, adds a text title to it and returns the StackPane.
	private StackPane getTitle() {	
		Color newColor = new Color(0.5,0.5,0.5,0.5);
		switch(indexOfTheme) {
			case 0: newColor = Color.RED; break;
			case 1: newColor = Color.GREEN; break;
			case 2: newColor = Color.BLUE; break;
			case 3: newColor = Color.BLACK; break;		
		}
		
		StackPane top = new StackPane();
		Text title = new Text(" Library Database Management System ");
		title.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
		title.setFill(newColor);
		Reflection r = new Reflection();
		r.setFraction(0.5f);
		title.setEffect(r);
		top.getChildren().add(title);
		
		return top;
	}

}
