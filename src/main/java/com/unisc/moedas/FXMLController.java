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
	private static List<String> modalidadeData =  Arrays.asList("Olímpica","Paraolímpica");
	
	private static double[][][] moedasValues = {
			//moedas de 2014 (1 ambígua)
			{{0.0,0.0,1.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,1.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0},{1.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,1.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,1.0},{1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,1.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			//moedas de 2015-1
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			//moedas de 2015-2 (2\/ ambígua (L e B) 4\/ ambígua)
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,1.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0}},
			//moedas de 2016 (4 ambígua)
			{{0.0,0.0,0.0,0.0,1.0,
			  0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0}},	
			{{0.0,0.0,0.0,0.0,1.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,1.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0}},	
			{{0.0,0.0,0.0,0.0,1.0,
			  0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0}},	
			{{0.0,0.0,0.0,0.0,1.0,
			  0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0}},
			//moeda da bandeira especial
			{{0.0,1.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0},{1.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			//moeda que não são comemorativas
			{{1.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			{{0.0,0.0,0.0,0.0,0.0,
			  1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			{{0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			{{1.0,0.0,0.0,0.0,0.0,
			  1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			{{1.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0},{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			{{0.0,0.0,0.0,0.0,0.0,
			  1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0},{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			{{0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0},{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			{{1.0,0.0,0.0,0.0,0.0,
			  1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0},{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			//moedas ambíguas
			{{0.0,0.0,1.0,0.0,0.0,
			  0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0},{1.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}},
				
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,
			  0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0}},
			
			{{0.0,0.0,0.0,1.0,0.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
			  0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0}},
			
			{{0.0,0.0,0.0,0.0,1.0,
			  0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,
			  0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0},{1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0}},
			
	};
	
	@FXML
	private Label titulo;
	
	@FXML
	private ChoiceBox<String> anoGravado;
	
	@FXML
	private ChoiceBox<String> posicaoLogotipo;
	
	@FXML
	private ChoiceBox<String> posicaoBrasil;
	
	@FXML
	private ChoiceBox<String> modalidade;
	
    @FXML
    private void btnOnClick(ActionEvent event) {
    	if (validaRespostas())
	    	try {
				double[] resultado = neuralNetwork.Recognize(getRespostas());
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
    	alert.setResizable(true);
    	alert.setWidth(450);
    	alert.showAndWait();
    }
    
    private String classificaMoedaCompleto(double[] resultado) {
    	return ("É uma moeda comemorativa?           " + formataResultado(resultado[0]) + 
    			"\nRaridade:                                            " + (resultado[1]*100.0 >= 90 ? "É muito rara" : "É pouco rara") + String.format(" (%1.4f%%)", (resultado[1]*100.0)) + 
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
    			posicaoBrasilGetResposta() >= 0.0 && 
    			modalidadeGetResposta() >= 0.0);
    }
    
    private double[] getRespostas() {
    	double values[] = {0.0,0.0,0.0,0.0,0.0, 
    					   0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0, 
    					   0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0, 
    					   0.0};
    	values[anoGravadoGetResposta()] = 1.0;
    	values[posicaoLogotipoGetResposta()+5] = 1.0;
    	values[posicaoBrasilGetResposta()+15] = 1.0;
    	values[23] = modalidadeGetResposta()*1.0;
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
    
    private int modalidadeGetResposta() {
    	return modalidadeData.indexOf(modalidade.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	titulo.setText("Classificador de Moedas Olímpicas Comemorativas");
        anoGravado.setItems((ObservableList<String>) FXCollections.observableArrayList(anoGravadoData));
        posicaoLogotipo.setItems((ObservableList<String>) FXCollections.observableArrayList(posicaoLogotipoData));
        posicaoBrasil.setItems((ObservableList<String>) FXCollections.observableArrayList(posicaoBrasilData));
        modalidade.setItems((ObservableList<String>) FXCollections.observableArrayList(modalidadeData));
        
        try {
        	neuralNetwork = new Backpropagation(24, 19);
            
            DataSet trainingSet = new DataSet(24, 19);
        	
            for (int i = 0; i < moedasValues.length; i++) 
            	trainingSet.Add(new DataSetObject(moedasValues[i][0], moedasValues[i][1]));
            
			neuralNetwork.SetMaxIterationNumber(5000);
			neuralNetwork.SetLearningRate(0.01);
			neuralNetwork.SetErrorRate(0.005);
			neuralNetwork.Learn(trainingSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }    
}
