package com.unisc.moedas;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import ADReNA_API.Data.DataSet;
import ADReNA_API.Data.DataSetObject;
import ADReNA_API.NeuralNetwork.Backpropagation;

public class FXMLController implements Initializable {
    
	private Backpropagation neuralNetwork;
	
	private static List<String> anoGravadoData =  Arrays.asList("Não consigo identificar","2012","2014","2015","2016");
	private static List<String> posicaoLogotipoData =  Arrays.asList("Não consigo identificar",
																	 "Superior esquerdo","Superior central","Superior direito",
																	 "Central esquerdo","Central","Central direito",
																	 "Inferior esquerdo","Inferior central","Inferior direito");
	private static List<String> posicaoBrasilData =  Arrays.asList("Não consigo identificar",
																   "Superior esquerdo","Superior central","Superior direito",
																   "Inferior esquerdo","Inferior central","Inferior direito",
															   	   "Não está na borda, mas dentro da moeda");
	
	private static double[][][] moedasValues = {
			//moedas de 2014 (1 ambígua)
			{{0.0,0.0,1.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,1.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0},{1.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,1.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,1.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			//moedas de 2015-1
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			//moedas de 2015-2 (2\/ ambígua (L e B) 4\/ ambígua)
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0}},
			//moedas de 2016 (4 ambígua)
			{{0.0,0.0,0.0,0.0,1.0,
			  0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,0.0,1.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0}},	
			{{0.0,0.0,0.0,0.0,1.0,
			  0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0}},	
			{{0.0,0.0,0.0,0.0,1.0,
			  0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0}},
			//moeda da bandeira especial
			{{0.0,1.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0},{1.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			//moeda que não é
			{{1.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			{{0.0,0.0,0.0,0.0,0.0,
			  1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			{{0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			{{1.0,0.0,0.0,0.0,0.0,
			  1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			
	};
	
	private double[] resultado;
	
	@FXML
	private Label titulo;
	
	@FXML
	private ChoiceBox<String> anoGravado;
	
	@FXML
	private ChoiceBox<String> posicaoLogotipo;
	
	@FXML
	private ChoiceBox<String> posicaoBrasil;
	
    @FXML
    private void btnOnClick(ActionEvent event) {
    	if (validaRespostas())
	    	try {
				resultado = neuralNetwork.Recognize(getRespostas());
	    		showResultScreen(classificaMoedaCompleto(resultado));
			} catch (Exception e) {
				e.printStackTrace();
			}
    	else {
    		showErrorDialog();
    	}
    }
    
    private void showErrorDialog() {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("Perguntas em branco");
    	alert.setHeaderText(null);
    	alert.setContentText("Por favor, responda todas as perguntas!");
    	alert.showAndWait();
    }
    
    private void showResultScreen(String resultado) {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Resultados");
    	alert.setHeaderText("Observa-se a seguir as saídas da rede neural:");
    	alert.setContentText(resultado);
    	alert.showAndWait();
    }
    
    private String classificaMoeda(double[] resultado) {
    	return ("É uma moeda comemorativa? " + formataResultado(resultado[0]) + 
    			((resultado[0]*100 >= 90 ) ?
    			(((resultado[0]*100.0 >= 90) ? ("\nRaridade: " + (resultado[1]*100.0 >= 90 ? "É muito rara " : "Tem uma certa raridade ") + String.format("%1.4f%%", (resultado[1]*100.0)) ) : "") + 
				((resultado[2]*100.0 >= 90) ? "\nMoeda especial (Bandeira): " + formataResultado(resultado[2]) : "") +
				((resultado[3]*100.0 >= 90) ? "\nModalidade (Atletismo): " + formataResultado(resultado[3]) : "") +
				((resultado[4]*100.0 >= 90) ? "\nModalidade (Natação): " + formataResultado(resultado[4]) : "") +
				((resultado[5]*100.0 >= 90) ? "\nModalidade (Paratriatlo): " + formataResultado(resultado[5]) : "") +
				((resultado[6]*100.0 >= 90) ? "\nModalidade (Golfe): " + formataResultado(resultado[6]) : "") +
				((resultado[7]*100.0 >= 90) ? "\nModalidade (Basquetebol): " + formataResultado(resultado[7]) : "") +
				((resultado[8]*100.0 >= 90) ? "\nModalidade (Vela): " + formataResultado(resultado[8]) : "") +
				((resultado[9]*100.0 >= 90) ? "\nModalidade (Paracanoagem): " + formataResultado(resultado[9]) : "") +
				((resultado[10]*100.0 >= 90) ? "\nModalidade (Rugby): " + formataResultado(resultado[10]) : "") +
				((resultado[11]*100.0 >= 90) ? "\nModalidade (Futebol): " + formataResultado(resultado[11]) : "") +
				((resultado[12]*100.0 >= 90) ? "\nModalidade (Voleibol): " + formataResultado(resultado[12]) : "") +
				((resultado[13]*100.0 >= 90) ? "\nModalidade (Atletismo Paralímpico): " + formataResultado(resultado[13]) : "") +
				((resultado[14]*100.0 >= 90) ? "\nModalidade (Judô): " + formataResultado(resultado[14]) : "") +
				((resultado[15]*100.0 >= 90) ? "\nModalidade (Boxe): " + formataResultado(resultado[15]) : "") + 
				((resultado[16]*100.0 >= 90) ? "\nModalidade (Natação Paralímpica): " + formataResultado(resultado[16]) : "") +
				((resultado[17]*100.0 >= 90) ? "\nMascote (Tom): " + formataResultado(resultado[17]) : "") +
				((resultado[18]*100.0 >= 90) ? "\nMascote (Vinícius): " + formataResultado(resultado[18]) : "")) : ""));
    }
    
    private String classificaMoedaCompleto(double[] resultado) {
    	return ("É uma moeda comemorativa?           " + formataResultado(resultado[0]) + 
    			"\nRaridade:                                            " + (resultado[1]*100.0 >= 90 ? "É muito rara" : "Tem uma certa raridade") + String.format(" (%1.4f%%)", (resultado[1]*100.0)) + 
				"\nMoeda especial (Bandeira):                " + formataResultado(resultado[2]) +
				"\nModalidade (Atletismo):                     " + formataResultado(resultado[3]) +
				"\nModalidade (Natação):                       " + formataResultado(resultado[4]) +
				"\nModalidade (Paratriatlo):                    " + formataResultado(resultado[5]) +
				"\nModalidade (Golfe):                            " + formataResultado(resultado[6]) +
				"\nModalidade (Basquetebol):                 " + formataResultado(resultado[7]) +
				"\nModalidade (Vela):                              " + formataResultado(resultado[8]) +
				"\nModalidade (Paracanoagem):             " + formataResultado(resultado[9]) +
				"\nModalidade (Rugby):                          " + formataResultado(resultado[10]) +
				"\nModalidade (Futebol):                        " + formataResultado(resultado[11]) +
				"\nModalidade (Voleibol):                       " + formataResultado(resultado[12]) +
				"\nModalidade (Atletismo Paralímpico): " + formataResultado(resultado[13]) +
				"\nModalidade (Judô):                             " + formataResultado(resultado[14]) +
				"\nModalidade (Boxe):                             " + formataResultado(resultado[15]) + 
				"\nModalidade (Natação Paralímpica):    " + formataResultado(resultado[16]) +
				"\nMascote (Tom):                                   " + formataResultado(resultado[17]) +
				"\nMascote (Vinícius):                              " + formataResultado(resultado[18]));
    }
    
    private String formataResultado(double resultado) {
    	resultado *= 100.0;
    	return (resultado>90 ?  "Sim (" +  String.format("%1.4f%%", resultado) + " de certeza)" : "Não (" + String.format("%1.4f%%", 100-resultado) + " de certeza)");
    }
    
    private boolean validaRespostas() {
    	return (anoGravadoGetResposta() >= 0.0 &&
    			posicaoLogotipoGetResposta() >= 0.0 &&
    			posicaoBrasilGetResposta() >= 0.0);
    }
    
    private double[] getRespostas() {
    	double values[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    	values[anoGravadoGetResposta()] = 1.0;
    	values[posicaoLogotipoGetResposta()+5] = 1.0;
    	values[posicaoBrasilGetResposta()+15] = 1.0;
    	return values;
    }
    
    private int anoGravadoGetResposta() {
    	return anoGravadoData.indexOf(anoGravado.getValue());
    }
    
    private int posicaoLogotipoGetResposta() {
    	return posicaoLogotipoData.indexOf(posicaoLogotipo.getValue());
    }
    
    private int posicaoBrasilGetResposta() {
    	return posicaoBrasilData.indexOf(posicaoBrasil.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	titulo.setText("Classificador de Moedas Olímpicas Comemorativas");
        anoGravado.setItems((ObservableList<String>) FXCollections.observableArrayList(anoGravadoData));
        posicaoLogotipo.setItems((ObservableList<String>) FXCollections.observableArrayList(posicaoLogotipoData));
        posicaoBrasil.setItems((ObservableList<String>) FXCollections.observableArrayList(posicaoBrasilData));
        
        try {
        	neuralNetwork = new Backpropagation(23, 19);
            
            DataSet trainingSet = new DataSet(23, 19);
        	
			trainingSet.Add(new DataSetObject(moedasValues[0][0], moedasValues[0][1]));
			trainingSet.Add(new DataSetObject(moedasValues[1][0], moedasValues[1][1]));
			trainingSet.Add(new DataSetObject(moedasValues[2][0], moedasValues[2][1]));
			trainingSet.Add(new DataSetObject(moedasValues[3][0], moedasValues[3][1]));
			
			trainingSet.Add(new DataSetObject(moedasValues[4][0], moedasValues[4][1]));
			trainingSet.Add(new DataSetObject(moedasValues[5][0], moedasValues[5][1]));
			trainingSet.Add(new DataSetObject(moedasValues[6][0], moedasValues[6][1]));
			trainingSet.Add(new DataSetObject(moedasValues[7][0], moedasValues[7][1]));
			
			trainingSet.Add(new DataSetObject(moedasValues[8][0], moedasValues[8][1]));
			trainingSet.Add(new DataSetObject(moedasValues[9][0], moedasValues[9][1]));
			trainingSet.Add(new DataSetObject(moedasValues[10][0], moedasValues[10][1]));
			trainingSet.Add(new DataSetObject(moedasValues[11][0], moedasValues[11][1]));
			
			trainingSet.Add(new DataSetObject(moedasValues[12][0], moedasValues[12][1]));
			trainingSet.Add(new DataSetObject(moedasValues[13][0], moedasValues[13][1]));
			trainingSet.Add(new DataSetObject(moedasValues[14][0], moedasValues[14][1]));
			trainingSet.Add(new DataSetObject(moedasValues[15][0], moedasValues[15][1]));
			
			trainingSet.Add(new DataSetObject(moedasValues[16][0], moedasValues[16][1]));
			
			trainingSet.Add(new DataSetObject(moedasValues[17][0], moedasValues[17][1]));
			trainingSet.Add(new DataSetObject(moedasValues[18][0], moedasValues[18][1]));
			trainingSet.Add(new DataSetObject(moedasValues[19][0], moedasValues[19][1]));
			trainingSet.Add(new DataSetObject(moedasValues[20][0], moedasValues[20][1]));
			
			neuralNetwork.SetMaxIterationNumber(50000);
			neuralNetwork.SetLearningRate(0.0001);
			neuralNetwork.SetErrorRate(0.0005);
			neuralNetwork.Learn(trainingSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }    
}
