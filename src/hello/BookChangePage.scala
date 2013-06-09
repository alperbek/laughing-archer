package hello

import scalafx.scene.control._
import scalafx.scene.layout.{TilePane, HBox, VBox}
import scalafx.geometry.Insets
import scalafx.event.ActionEvent
import javafx.scene.control.Dialogs
import scalafx.stage.Stage
import scalafx.Includes._
import domain.{Category, Author, Book}


class BookChangePage(book: Book, dialogStage: Stage, model: AppModel) extends ScrollPane {
  //TODO make sure that changing affects priorityBooks
  val bookChangingForm = new VBox() {
    spacing = 10
    padding = Insets(10, 10, 10, 10)

    val titleSection = new HBox {
      spacing = 10
      val titleEdit = new TextField() {
        text = book.getTitle
      }
      content = List(
        new Label("Title: "),
        titleEdit
      )
    }

    val authorSection = new HBox {
      spacing = 10
      val authorEdit = new ComboBox[String] {
        items = model.namesOfAuthors
        editable = true
        selectionModel.value.select(book.getAuthor.getName)
      }
      content = List(
        new Label("Author: "),
        authorEdit
      )
    }

    val categorySection = new HBox {
      spacing = 10
      val categoryEdit = new ComboBox[String]() {
        items = model.namesOfAllCategories
        editable = true
        selectionModel.value.select(book.category.category)
      }
      content = List(
        new Label("Category: "),
        categoryEdit
      )
    }

    val descriptionSection = new HBox {
      spacing = 10
      val descriptionEdit = new TextArea() {
        text = book.description
        if (book.description.isEmpty) {
          text = "No description available, write some"
        }
        wrapText = true
        prefColumnCount = 26
        prefRowCount = 5
      }
      content = List(
        new Label("Description: "),
       descriptionEdit
      )
    }

    val decisionButtons = new TilePane {
      hgap = 8
      val updateButton = new Button("Update") {
        onAction = {
          e: ActionEvent => {
            val dialogResponse = Dialogs.showConfirmDialog(dialogStage,
              "Update a book",
              "Are you sure, that you want to update this book?", "Update confirmation")
            if (dialogResponse == Dialogs.DialogResponse.YES) {
              model.books -= book
              model.db.removeBook(book)
              val title = titleSection.titleEdit.text.value
              val authorName = authorSection.authorEdit.value.value
              val description: String = descriptionSection.descriptionEdit.text.value
              val categoryDescription = categorySection.categoryEdit.value.value
              val changedBook = new Book(title, new Author(authorName), book.getPathToContent,
              description, new Category(categoryDescription))
              model.books += changedBook
              model.db.addBook(book)
              model.updateNamesOfAuthors()
              Dialogs.showInformationDialog(dialogStage, "Book was updated", "Update complete", "Book update info")
            }

          }
        }
      }
      val cancelButton = new Button("Cancel") {
        onAction = {
          e: ActionEvent =>
            dialogStage.close
        }
      }
      content = List(updateButton, cancelButton)
    }
    content = List(
      titleSection,
      authorSection,
      descriptionSection,
      categorySection,
      decisionButtons

    )
  }

  margin = Insets(10, 10, 10, 10)
  prefWidth = 500
  prefHeight = 280

  content = bookChangingForm

}