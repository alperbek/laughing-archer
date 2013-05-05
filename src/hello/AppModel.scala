package hello

import analysis.CategoryTree
import mobireader.Book
import scalafx.beans.property.DoubleProperty
import scalafx.collections.ObservableBuffer
import java.io.File

class AppModel {
  
 val db = new DBHandler("my_books.db");
	db.createTablesInDB()
    
    def createLibrary() = {
    	val path = "/example/library/"
    	val author1 = new Author("John Doe")
    	val author2 = new Author("George Smith")
    	val author3 = new Author("Anna White")
    	List(new Book("Dynamics", author1, path, "", "Physics"),
    			new Book("Calculus for dummies", author2, path, "Easy", "Math"),
    			new Book("Worms", author3, path, "Bleh", "Biology"),
    			new Book("Algebra for dummies", author1, path, "Easy", "Math"),
    			new Book("Birds", author1, path, "Nice", "Biology"),
    			new Book("Black magic", author2, path, "Hard", "Math"),
    			new Book("Nothing brings something", author1, path, "There isn't really much to say", "Fiction"),
    			new Book("Programming in Scala", author3, path, "Very long, but well written", "Computer Science"),
    			new Book("Magnetic fields", author2, path, "Very Hard", "Physics")
    			)
    }
	
    val library = createLibrary()
    assert(library.size > 0, "Library not generated")
    db.addBooks(library)
    
    
    db.findBooksByCategory("Math")
    db.findBooksByAuthor(new Author("John Doe"))
    def blackMagic = db.findBook("Black magic")	
    
    assert(blackMagic.category == "Math", "Is " + blackMagic.category + ", but should be " + "Math")
    
    var myBooks = db.getAllBooks()
    assert(myBooks.size > 0)
    
    assert(db.getAllAuthors.size == 3, "Should be three authors in DB now" )
    def getBooks() = {
    	val books = new ObservableBuffer[Book]()
    	for(book <- db.getAllBooks())
    	{
    		books += book
    	}
    	books
    }
    
    val books = getBooks()
    
    def updateBooks() {
      for(book <- getBooks())
      if(!(books contains book)) books += book
    }
    val listViewItems = new ObservableBuffer[String]()
    
    val choiceBoxItems = ObservableBuffer("Choice A", "Choice B", "Choice C", "Choice D")
    
    var categories = new CategoryTree
    categories.addSubcategories(List("Science", "Fiction", "Art"))
    categories("Science").addSubcategories(List("Math", "Physics", "Biology", "Computer Science"))
    categories("Fiction").addSubcategories(List("Science Fiction", "Soap operas", "Horror", "Fantasy"))
    categories("Fiction")("Science Fiction").addSubcategories(List("Hard", "Ambitious", "Voyage"))
    
    def namesOfAllCategories = categories.allNames
    assert(!namesOfAllCategories.isEmpty, "No entries in names of all categories")
    assert(namesOfAllCategories.size == 14,
        "Names of all categories has size: " + namesOfAllCategories.size)
    var filePath: String = ""
    var file: File = _
    
}