Text Processing Tool
Overview
The Text Processing Tool is a JavaFX application designed to provide users with a comprehensive text editing and processing experience. This tool allows users to perform various text operations such as searching, replacing, and applying regular expressions. It also provides functionalities for managing text collections, adjusting text area settings, and saving or opening text files.

Features
File Operations:

Open, Save, Save As, and Exit
Save to collection
Edit Operations:

Undo, Redo, Cut, Copy, Paste
Find and Replace
Regex Operations:

Custom regex matching and replacement
Predefined regex for matching emails, phone numbers, and dates
Formatting:

Word Wrap
View:

Zoom In and Zoom Out
Help:

About
Installation
Prerequisites
Java Development Kit (JDK) 8 or later
JavaFX SDK
Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse
Steps
Clone the Repository:

bash
Copy code
git clone <repository-url>
Open the Project:

Open the project in your preferred IDE.
Configure JavaFX:

Ensure that the JavaFX SDK is properly configured in your IDE.
Build and Run:

Build and run the project using your IDE.
Usage
File Operations
Open: Opens a text file for editing.
Save: Saves the current text to the opened file.
Save As: Saves the current text to a new file.
Save to Collection: Saves the current text to a collection for later use.
Exit: Closes the application.
Edit Operations
Undo/Redo: Reverts or reapplies the last action.
Cut/Copy/Paste: Standard text editing operations.
Find: Searches for a specific word in the text.
Replace: Replaces a specific word with another.
Regex Operations
Custom Regex: Matches a custom regex pattern in the text.
Replace with Regex: Replaces text based on a custom regex pattern.
Predefined Regex: Matches common patterns such as emails, phone numbers, and dates.
Formatting
Word Wrap: Toggles word wrapping in the text area.
View
Zoom In/Out: Adjusts the font size in the text area.
Help
About: Provides information about the application.
Code Structure
Controllers
TextProcessingMainController: The main controller for the application, handling user interactions and operations.
Model
TextEditor: A model class for managing text collections.
Views
FXML files define the UI layout and elements.
FXML
The FXML file defines the layout of the application's user interface, including a menu bar, text area, result area, and collection view.


Contributing
Contributions are welcome! Please fork the repository and create a pull request with your changes.