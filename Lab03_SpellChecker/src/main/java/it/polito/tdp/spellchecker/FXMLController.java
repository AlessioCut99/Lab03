/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.spellchecker;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import it.polito.tdp.spellchecker.model.Dictionary;
import it.polito.tdp.spellchecker.model.RichWord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Dictionary dizionario;
	private List<String> inputTextList;
	
	private final static boolean dichotomicSearch = true;
	private final static boolean linearSearch = false;
	

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxLingua"
    private ComboBox<String> boxLingua;

    @FXML // fx:id="txtDaCorreggere"
    private TextArea txtDaCorreggere; // Value injected by FXMLLoader

    @FXML // fx:id="bntSpellCheck"
    private Button bntSpellCheck; // Value injected by FXMLLoader

    @FXML // fx:id="txtCorretto"
    private TextArea txtCorretto; // Value injected by FXMLLoader

    @FXML // fx:id="lblErrori"
    private Label lblErrori; // Value injected by FXMLLoader

    @FXML // fx:id="bntClearText"
    private Button bntClearText; // Value injected by FXMLLoader

    @FXML // fx:id="lblStato"
    private Label lblStato; // Value injected by FXMLLoader

    @FXML
    void doActivation(ActionEvent event) {

    	if(boxLingua.getValue() != null) {
    		
    		txtDaCorreggere.setDisable(false);
    		txtCorretto.setDisable(false);
    		bntSpellCheck.setDisable(false);
    		bntClearText.setDisable(false);
    		txtDaCorreggere.clear();
    		txtCorretto.clear();
    		
    	}else {
    		
    		txtDaCorreggere.setDisable(true);
    		txtCorretto.setDisable(true);
    		bntSpellCheck.setDisable(true);
    		bntClearText.setDisable(true);
    		txtDaCorreggere.setText("Seleziona una lingua");
    		
    	}
    }

    @FXML
    void doClearText(ActionEvent event) {
    	
    	txtDaCorreggere.clear();
    	txtCorretto.clear();
    	lblErrori.setText("Number of Errors: ");
    	lblStato.setText("Spell Check Status: ");

    }

    @FXML
    void doSpellCheck(ActionEvent event) {
    	
    	txtCorretto.clear();
    	inputTextList = new LinkedList<String>();
    	
    	if(boxLingua.getValue() == null) {
    		txtDaCorreggere.setText("Seleziona una lingua!");
    		return;
    	}
    	
    	if(!dizionario.loadDictionary(boxLingua.getValue())) {
    		txtDaCorreggere.setText("Errore nel caricamento del dizionario! ");
    		return;
    	}
    	
    	String inputText = txtDaCorreggere.getText();
    	
    	if(inputText.isEmpty())  {
    		txtDaCorreggere.setText("Inserire un testo da correggere!");
    		return;
    	}
    	
    	inputText = inputText.replaceAll("\n", " ");
		inputText = inputText.replaceAll("[.,\\/#!$%\\^&\\*;:{}=\\-_`~()\\[\\]?]", "");
    	
    	StringTokenizer st = new StringTokenizer(inputText, " ");
    	
    	while( st.hasMoreTokens()) {
    		inputTextList.add(st.nextToken());
    	}
    	
    	long start = System.nanoTime();
    	//List<RichWord> outputTextList;
    	//outputTextList = dizionario.spellCheckText(inputTextList);
    	List<RichWord> outputTextList;
		
		
		if (dichotomicSearch) {
			outputTextList = dizionario.spellCheckTextDichotomic(inputTextList);
		} else if (linearSearch) {
			outputTextList = dizionario.spellCheckTextLinear(inputTextList);
		} else {
			outputTextList = dizionario.spellCheckText(inputTextList);
		}
    	
    	long end= System.nanoTime();
    	
    	int numErrori= 0;
    	StringBuilder richText = new StringBuilder();
    	
    	for( RichWord r: outputTextList) {
    		if(!r.isCorrect()) {
    			numErrori++;
    			richText.append(r.getWord()+ "\n");
    		}
    	}
    	txtCorretto.setText(richText.toString());
    	lblErrori.setText("The text contains " +numErrori + " errors");
    	lblStato.setText("Spell check completed in " +(end- start)/1e9 + " seconds");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxLingua != null : "fx:id=\"boxLingua\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDaCorreggere != null : "fx:id=\"txtDaCorreggere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert bntSpellCheck != null : "fx:id=\"bntSpellCheck\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCorretto != null : "fx:id=\"txtCorretto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lblErrori != null : "fx:id=\"lblErrori\" was not injected: check your FXML file 'Scene.fxml'.";
        assert bntClearText != null : "fx:id=\"bntClearText\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lblStato != null : "fx:id=\"lblStato\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
  public void setModel(Dictionary model) {
    	
	  
    	txtDaCorreggere.setDisable(true);
    	txtDaCorreggere.setText("Selezionare una lingua");
    	
    	txtCorretto.setDisable(true);
    	boxLingua.getItems().addAll("English","Italian");
    	
    	bntClearText.setDisable(true);
    	bntSpellCheck.setDisable(true);
 	
    	
    	this.dizionario = model;
    }
}
