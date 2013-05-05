package analysis

class CategoryTree() {	
	var root = new Category("Root")
	
	override def toString(): String = {
		var description = "Root"
		description += root.namesOfSubcategories.mkString("\n")
		//Algorithm isn't so easy...
		description
	}
	
	def allNames(): List[String] = {
	  root.namesOfSubcategories.toList ::: (for(subcatName <- root.namesOfSubcategories)
					yield getNamesOfAllSubNodes(root(subcatName)) ).toList.flatten
	} 
	
	def getNamesOfAllSubNodes(node: Category): List[String] = {
	  if(node.namesOfSubcategories.isEmpty)
	    List(node.value)
	  else {
	    val names = (for(subcatName <- node.namesOfSubcategories) 
	    yield getNamesOfAllSubNodes(node(subcatName) ) ).toList.flatten
	    println(names)
	    names
	  }
	    
	}
	def addSubcategories(newCategories: Iterable[String]) {
		root.addSubcategories(newCategories)
	}
	
	def namesOfSubcategories() = root.namesOfSubcategories()
	def apply(subcatName: String) = root.subcategories(subcatName)
}

sealed abstract class Node(val value: String) {
  /** Replaces this node or its children according to the given function */
  def replace(fn: Node => Node): Node

  /** Helper to replace nodes that have a given value */
  def replace(value: String, node: Node): Node =
    replace(n => if (n.value == value) node else n)
}


import scala.collection.mutable.Map
class Category(override val value: String)
     extends Node(value) {
	
	var subcategories = Map[String, Category]()
	
	def replaceSubcategory(name :String, newChild: Category)
	{
		subcategories(name) = newChild
	}
	
	def namesOfSubcategories() = subcategories.keys
	
	def hasChildren(): Boolean = !subcategories.isEmpty
	
	def addSubcategory(cat: Category){
		if (subcategories.keys.exists(x => x == cat.value))
				replaceSubcategory(cat.value, cat)
		subcategories += (cat.value -> cat)
	}
	
	def addSubcategory(categoryName: String){
		if (subcategories.keys.exists(x => x == categoryName))
				replaceSubcategory(categoryName, new Category(categoryName))
		subcategories += (categoryName -> new Category(categoryName))
	}
	
	def addSubcategories(newCategories: Iterable[String]) {
		for(subcategory <- newCategories) {
			addSubcategory(subcategory)
		}
	}
	
	def apply(subcatName: String) = subcategories(subcatName)
  def replace(fn: Node => Node): Node = {
    val newSelf = fn(this)
    if (this eq newSelf) {
        this
      
    } else {
      newSelf
    }
  }
}