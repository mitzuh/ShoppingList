package fi.tamk.tiko.gui;

import java.awt.FlowLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Optional;

import fi.tamk.tiko.parser.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;

/**
 * This is the class for the user interface. It holds all info concerning the application window.
 * Also holds the main method for the application.
 * 
 * @author      Jimi Savola
 * @version     1.8, 2018.1120
 * @since       2018.1107
 */
public class GUI extends Application {
    TextField nameInput, quantityInput;
    TableView<JSONObject> table;
    ContextMenu contextMenu;

    FileWriter fstream;
    BufferedWriter out;
    JSONParser parser;
    JSONFileReader fileReader;

    Button addButton, saveButton, readButton;

    LinkedList<JSONObject> jsonObjectList;

    boolean fileCreated;

    /**
     * Constructs the GUI.
     */
    public GUI() {
        fileCreated = false;
        jsonObjectList = new LinkedList<>();
        parser = new JSONParser();
        fileReader = new JSONFileReader();
    }

    /**
     * Initializes the window.
     */
    @Override
    public void start(Stage window) {
        System.out.println("Author: Jimi Savola");
        window.setTitle("Shopping List");  

        // Product name column
        TableColumn<JSONObject, String> nameColumn = new TableColumn<>("Product");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Product"));

        // Quantity column
        TableColumn<JSONObject, String> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(200);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));

        // Name input
        nameInput = new TextField();
        nameInput.setPromptText("Product");
        nameInput.setMinWidth(100);

        // Quantity input
        quantityInput = new TextField();
        quantityInput.setPromptText("Quantity");
        quantityInput.setMinWidth(100);

        // Buttons
        addButton = new Button("Add");
        addButton.setOnAction(e -> addButtonAction());
        saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveButtonClicked());
        readButton = new Button("Read");
        readButton.setOnAction(e -> readButtonClicked(window));

        // Context menu
        contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Edit");
        item1.setOnAction(e -> editClicked(getObject()));
        MenuItem item2 = new MenuItem("Remove");
        item2.setOnAction(e -> removeClicked(getObject()));
        contextMenu.getItems().addAll(item1, item2);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(nameInput, quantityInput, addButton, saveButton, readButton);

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(nameColumn, quantityColumn);


        // Open context menu when right clicking on the table.
        table.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(table, me.getScreenX(), me.getScreenY());
                }
                else if (me.getButton() == MouseButton.PRIMARY) {
                    contextMenu.hide();
                }
            }
        });

        table.setItems(getProducts());

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, hBox);

        Scene scene = new Scene(vBox, 640,480);
        window.setScene(scene);
        window.show();
    }

    /**
     * Returns currently clicked object on the table.
     * @return clicked JSONObject or null if empty.
     */
    public JSONObject getObject() {
        JSONObject obj = table.getSelectionModel().selectedItemProperty().get();

        return obj;
    }

    /**
     * Edits the selected JSONObject on the shopping list.
     * @param object currently selected JSONObject on the table.
     * @return editable JSONObject.
     */
    public JSONObject editClicked(JSONObject object) {
        nameInput.setText(object.getProduct());
        quantityInput.setText(object.getQuantity());
        addButton.setOnAction(e -> addButtonAction(object));
        return object;
    }

    /**
     * Removes selected JSONObject from the shopping list.
     * @param object currently selected JSONObject on the table.
     * @return removed JSONObject.
     */
    public JSONObject removeClicked(JSONObject object) {
        ObservableList<JSONObject> selectedProduct, allProducts;
        allProducts = table.getItems();
        selectedProduct = table.getSelectionModel().getSelectedItems();

        selectedProduct.forEach(allProducts::remove);
        removeJSONObjectFromList(object);

        return object;
    }

    /**
     * Adds the product to the list.
     * 
     * <p>Adds the product to the end of the list if the input fields are filled correctly.
     */
    public void addButtonAction() {
        if ((!nameInput.getText().equals("") && !quantityInput.getText().equals("")) &&
        isPlusInteger(quantityInput.getText())) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Product", nameInput.getText());
            jsonObject.put("Quantity", quantityInput.getText());
            table.getItems().add(jsonObject);
            addJSONObjectToList(jsonObject);
            nameInput.clear();
            quantityInput.clear();
        }   
    }

    /**
     * Adds the product to the list.
     * 
     * <p>Adds the product to the end of the list if the input fields are filled correctly.
     * @param obj
     */
    public void addButtonAction(JSONObject obj) {
        if ((!nameInput.getText().equals("") && !quantityInput.getText().equals("")) &&
        isPlusInteger(quantityInput.getText())) {
            obj.put("Product", nameInput.getText());
            obj.put("Quantity", quantityInput.getText());
            nameInput.clear();
            quantityInput.clear();
            addButton.setOnAction(e -> addButtonAction());
            
            table.getItems().clear();
            table.setItems(getProducts());
        }
    }

    /**
     * Saves the shopping list as a .json file if it is not empty.
     */
    public void saveButtonClicked() {
        if(jsonObjectList.size() > 0) {
            writeJSON(jsonObjectList);
        }
    }


    public void readButtonClicked(Stage window) {
        FileChooser chooser = new FileChooser();
        LinkedList<JSONObject> newList = new LinkedList<>();
        chooser.setTitle("Open .json File");
        File selectedFile = chooser.showOpenDialog(window);
        
        if(selectedFile != null) {
            newList = fileReader.readFile(selectedFile);
            jsonObjectList.clear();
            table.getItems().clear();
            for(int i=0; i<newList.size(); i++) {
                JSONObject tmp = new JSONObject();
                tmp.put("Product", newList.get(i).get("Product"));
                tmp.put("Quantity", newList.get(i).get("Quantity"));

                addJSONObjectToList(tmp);
            }
            table.setItems(getProducts());
        }
    }

    /**
     * Checks if the given product name is already on the list.
     * @param text Text from the latest user input.
     * @return boolean if duplicate was found or not.
     */
    public boolean isDuplicate(String text) {
        boolean result = false;
        text = text.toLowerCase();
        text = text.replaceAll("\\s+", "");

        for(int i=0; i<jsonObjectList.size(); i++) {
            String comparison = jsonObjectList.get(i).getProduct();
            comparison = comparison.toLowerCase();
            comparison = comparison.replaceAll("\\s+", "");

            if(text.equals(comparison)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Checks if the user given input for quantity is positive integer
     * @param text User quantity input
     * @return true if positive integer, false if netagive, zero or not an integer.
     */
    public boolean isPlusInteger(String text) {
        text = text.replaceAll("\\s+", "");
        try {
            int number = Integer.parseInt(text);
            if(number > 0) {
                return true;
            }
            else {
                return false;
            }
            
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

    /**
     * Stops the application from running.
     */
    public void stop() {
        System.out.println("Application closing");
    }

    /**
     * Adds passed JSONObject to the end of the linked list containing all the objects.
     * @param object passed JSONObject to add to the list.
     */
    public void addJSONObjectToList(JSONObject object) {
        jsonObjectList.add(object);
    }

    /**
     * Removes passed JSONObject from the linked list.
     * @param object passed JSONObject to remove from the list.
     */
    public void removeJSONObjectFromList(JSONObject object) {
        jsonObjectList.remove(object);
    }
    
    public ObservableList<JSONObject> getProducts() {
        ObservableList<JSONObject> products = FXCollections.observableArrayList();

        for(int i=0; i<jsonObjectList.size(); i++) {
            products.add(jsonObjectList.get(i));
        }
        return products;
    }

    /**
     * Makes a new json -file and writes the passed JSONObject values in string format to the file.
     */
    public void writeJSON(LinkedList<JSONObject> objs) {
        LinkedList<JSONObject> objects = new LinkedList<>();

        for (int i=0; i<objs.size(); i++) {
            JSONObject tmp = new JSONObject();

            tmp.put("Product", objs.get(i).get("Product"));
            tmp.put("Quantity", objs.get(i).get("Quantity"));

            objects.add(tmp);
        }

        try {
            JSONObject obj = new JSONObject();
            fstream = new FileWriter("ShoppingList.json");
            out = new BufferedWriter(fstream);
            obj.put("Shopping List", objects);
            parser.writeJSONString(obj, out);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method of the application, which launches the application.
     * @param args command line arguments.
     */
    public static void main(String [] args) {
        launch();
    }
}