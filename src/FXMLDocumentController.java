
/** **************************************************************
 * Autor............: Pedro Lucca Silva Martins
 * Matricula........: 202210183
 * Inicio...........: 20/09/2023
 * Ultima alteracao.: 02/09/2023
 * Nome.............: Desafio do trem
 * Funcao...........: Aprender concorrencia com zona critica
 *************************************************************** */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class FXMLDocumentController implements Initializable {

  // declaracao de botoes no geral
  @FXML
  private Button ButtonIniciar;
  @FXML
  private Button TremAmarelo;
  @FXML
  private Button TremVerde;
  @FXML
  private Label LabelLeftUp;
  @FXML
  private Label LabelLeftDown;
  @FXML
  private Label LabelRightUp;
  @FXML
  private Label LabelRightDown;
  @FXML
  private Slider sliderAmarelo;
  @FXML
  private Slider sliderVerde;
  @FXML
  private RadioButton radioButton1;
  @FXML
  private RadioButton radioButton2;
  @FXML
  private RadioButton radioButton3;
  @FXML
  private RadioButton radioButton4;
  @FXML
  private RadioButton radioButton5;
  @FXML
  private RadioButton radioButton6;
  @FXML
  private RadioButton radioButton7;
  private int opcaoDeParada;

  // declaro todas as threads fora dos metodos, para manter um 
  // escoco global.
  private ThreadTremTwoLeft threadTremAmareloTwoLeft;
  private ThreadTremTwoLeft threadTremVerdeTwoLeft;
  private ThreadTremTwoRight threadTremAmareloTwoRight;
  private ThreadTremTwoRight threadTremVerdeTwoRight;
  private ThreadTremAlternado1 threadTremAmareloAlternado1;
  private ThreadTremAlternado1 threadTremVerdeAlternado1;
  private ThreadTremAlternado2 threadTremAmareloAlternado2;
  private ThreadTremAlternado2 threadTremVerdeAlternado2;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    sliderAmarelo.setValue(2.0);
    sliderVerde.setValue(2.0);
    radioButton5.fire();
    radioButton1.fire();
  }

  @FXML
  public void handleButtonActionIniciar(ActionEvent event) {
    // testo também se alguma thread está iniciada
    // igual ao botao reset para interromper e poder
    // comecar outra
    verificarThreadsComScale();
    // iniciando a movimentação dos trens
    if (radioButton1.isSelected()) {
      Peterson peterson = new Peterson(2);
      iniciarThreadsTwoLeft(peterson); // tenho que instanciar as threads de novo pois na parte de cima do codigo eu interrompo caso alguma esteja em uso
      if (radioButton5.isSelected()) {
        threadTremAmareloTwoLeft.opcaoDeParada(1);
        threadTremVerdeTwoLeft.opcaoDeParada(1);
      } else if (radioButton6.isSelected()) {
        threadTremAmareloTwoLeft.opcaoDeParada(2);
        threadTremVerdeTwoLeft.opcaoDeParada(2);
      } else if (radioButton7.isSelected()) {
        threadTremAmareloTwoLeft.opcaoDeParada(3);
        threadTremVerdeTwoLeft.opcaoDeParada(3);
      }
      threadTremAmareloTwoLeft.start();
      threadTremVerdeTwoLeft.start();
    } else if (radioButton2.isSelected()) {
      Peterson peterson = new Peterson(2);
      iniciarThreadsTwoRight(peterson); // tenho que instanciar as threads de novo pois na parte de cima do codigo eu interrompo caso alguma esteja em uso
      if (radioButton5.isSelected()) { // nessa cadeia de if's eu verifico qual a escolha do usuario sobre o metodo de parada
        threadTremAmareloTwoRight.opcaoDeParada(1);
        threadTremVerdeTwoRight.opcaoDeParada(1);
      } else if (radioButton6.isSelected()) {
        threadTremAmareloTwoRight.opcaoDeParada(2);
        threadTremVerdeTwoRight.opcaoDeParada(2);
      } else if (radioButton7.isSelected()) {
        threadTremAmareloTwoRight.opcaoDeParada(3);
        threadTremVerdeTwoRight.opcaoDeParada(3);
      }
      threadTremAmareloTwoRight.start();
      threadTremVerdeTwoRight.start();
    } else if (radioButton3.isSelected()) {
      Peterson peterson = new Peterson(2);
      iniciarThreadsAlternado1(peterson); // tenho que instanciar as threads de novo pois na parte de cima do codigo eu interrompo caso alguma esteja em uso
      if (radioButton5.isSelected()) {// nessa cadeia de if's eu verifico qual a escolha do usuario sobre o metodo de parada
        threadTremAmareloAlternado1.opcaoDeParada(1);
        threadTremVerdeAlternado1.opcaoDeParada(1);
      } else if (radioButton6.isSelected()) {
        threadTremAmareloAlternado1.opcaoDeParada(2);
        threadTremVerdeAlternado1.opcaoDeParada(2);
      } else if (radioButton7.isSelected()) {
        threadTremAmareloAlternado1.opcaoDeParada(3);
        threadTremVerdeAlternado1.opcaoDeParada(3);
      }
      threadTremAmareloAlternado1.start();
      threadTremVerdeAlternado1.start();
    } else if (radioButton4.isSelected()) {
      Peterson peterson = new Peterson(2);
      iniciarThreadsAlternado2(peterson); // tenho que instanciar as threads de novo pois na parte de cima do codigo eu interrompo caso alguma esteja em uso
      if (radioButton5.isSelected()) {// nessa cadeia de if's eu verifico qual a escolha do usuario sobre o metodo de parada
        threadTremAmareloAlternado2.opcaoDeParada(1);
        threadTremVerdeAlternado2.opcaoDeParada(1);
      } else if (radioButton6.isSelected()) {
        threadTremAmareloAlternado2.opcaoDeParada(2);
        threadTremVerdeAlternado2.opcaoDeParada(2);
      } else if (radioButton7.isSelected()) {
        threadTremAmareloAlternado2.opcaoDeParada(3);
        threadTremVerdeAlternado2.opcaoDeParada(3);
      }
      threadTremAmareloAlternado2.start();
      threadTremVerdeAlternado2.start();
    }
  }

  @FXML
  private void iniciarThreadsTwoLeft(Peterson peterson) {
    //inicializando as threads
    threadTremAmareloTwoLeft = new ThreadTremTwoLeft(TremAmarelo, "Trem Amarelo", opcaoDeParada, peterson);
    threadTremVerdeTwoLeft = new ThreadTremTwoLeft(TremVerde, "Trem Verde", opcaoDeParada, peterson);
  }

  @FXML
  private void iniciarThreadsTwoRight(Peterson peterson) {
    //inicializando as threads
    threadTremAmareloTwoRight = new ThreadTremTwoRight(TremAmarelo, "Trem Amarelo", opcaoDeParada, peterson);
    threadTremVerdeTwoRight = new ThreadTremTwoRight(TremVerde, "Trem Verde", opcaoDeParada, peterson);

  }

  @FXML
  private void iniciarThreadsAlternado1(Peterson peterson) {
    //inicializando as threads
    threadTremAmareloAlternado1 = new ThreadTremAlternado1(TremAmarelo, "Trem Amarelo", opcaoDeParada, peterson);
    threadTremVerdeAlternado1 = new ThreadTremAlternado1(TremVerde, "Trem Verde", opcaoDeParada, peterson);

  }

  @FXML
  private void iniciarThreadsAlternado2(Peterson peterson) {
    // inicializando as threads
    threadTremAmareloAlternado2 = new ThreadTremAlternado2(TremAmarelo, "Trem Amarelo", opcaoDeParada, peterson);
    threadTremVerdeAlternado2 = new ThreadTremAlternado2(TremVerde, "Trem Verde", opcaoDeParada, peterson);

  }

  @FXML
  private void handleButtonActionReset(ActionEvent event) {
    // verifico cada uma das threads para saber se alguma esta em execucao
    // se alguma nao estiver nula eu interrompo e utilizo uma variavel 
    // para, dentro das classes, definir se o trem deve parar e retornar
    // a posicao inicial
    verificarThreads();
  }

  @FXML
  private void handleSliderReleasedYellow(MouseEvent event) {
    // setando uma variavel prioridade que sera usada em uma funcao
    // de cada classe para definir o tempo que a thread ficara em
    // repouso
    double sliderValue = sliderAmarelo.getValue();
    if (radioButton1.isSelected()) {
      threadTremAmareloTwoLeft.setSpeed((int) sliderValue); // qnd estou na classe dos dois a esquerda
    } else if (radioButton2.isSelected()) {
      threadTremAmareloTwoRight.setSpeed((int) sliderValue); // qnd estou na classe dos dois a direita
    } else if (radioButton3.isSelected()) {
      threadTremAmareloAlternado1.setSpeed((int) sliderValue); // qnd estou na classe alternado 1
    } else if (radioButton4.isSelected()) {
      threadTremAmareloAlternado2.setSpeed((int) sliderValue); // qnd estou na classe alternado 2
    }
  }

  @FXML
  private void handleSliderReleasedGreen(MouseEvent event) {
    // setando uma variavel prioridade que sera usada em uma funcao
    // de cada classe para definir o tempo que a thread ficara em
    // repouso
    double sliderValue = sliderVerde.getValue();
    if (radioButton1.isSelected()) {
      threadTremVerdeTwoLeft.setSpeed((int) sliderValue); // qnd estou na classe dos dois a esquerda
    } else if (radioButton2.isSelected()) {
      threadTremVerdeTwoRight.setSpeed((int) sliderValue); // qnd estou na classe dos dois a direita
    } else if (radioButton3.isSelected()) {
      threadTremVerdeAlternado1.setSpeed((int) sliderValue);
    } else if (radioButton4.isSelected()) {
      threadTremVerdeAlternado2.setSpeed((int) sliderValue);
    }
  }

  @FXML
  private void handleCheckBoxAction(ActionEvent event) {
    // testo também se alguma thread está iniciada
    // igual ao botao reset para interromper e poder
    // comecar outra
    verificarThreadsComScale();
    // verifico qual a checkbox que esta selecionada para chamar a funcao que inicia as threads
    if (radioButton1.isSelected()) {
      Peterson peterson = new Peterson(2);
      iniciarThreadsTwoLeft(peterson);
    } else if (radioButton2.isSelected()) {
      Peterson peterson = new Peterson(2);
      iniciarThreadsTwoRight(peterson);
    } else if (radioButton3.isSelected()) {
      Peterson peterson = new Peterson(2);
      iniciarThreadsAlternado1(peterson);
    } else if (radioButton4.isSelected()) {
      Peterson peterson = new Peterson(2);
      iniciarThreadsAlternado2(peterson);
    }
  }
  
  public void verificarThreadsComScale (){
    if (threadTremAmareloTwoLeft != null) {
      threadTremAmareloTwoLeft.interrupt();
      threadTremVerdeTwoLeft.interrupt();
      threadTremAmareloTwoLeft.pararThreadsTwoLeft();
      threadTremVerdeTwoLeft.pararThreadsTwoLeft();
    } else if (threadTremAmareloTwoLeft != null) {
      threadTremAmareloTwoLeft.reiniciarInterrompido1();
      threadTremVerdeTwoLeft.reiniciarInterrompido1();
    }
    if (threadTremAmareloTwoRight != null) {
      threadTremAmareloTwoRight.scaleFalse();
      threadTremVerdeTwoRight.scaleFalse();
      threadTremAmareloTwoRight.interrupt();
      threadTremVerdeTwoRight.interrupt();
      threadTremAmareloTwoRight.pararThreadsTwoRight();
      threadTremVerdeTwoRight.pararThreadsTwoRight();
    } else if (threadTremAmareloTwoRight != null) {
      threadTremAmareloTwoRight.reiniciarInterrompido2();
      threadTremVerdeTwoRight.reiniciarInterrompido2();

    }
    if (threadTremAmareloAlternado1 != null) {
      threadTremVerdeAlternado1.scaleFalse();
      threadTremAmareloAlternado1.interrupt();
      threadTremVerdeAlternado1.interrupt();
      threadTremAmareloAlternado1.pararThreadsAlternado1();
      threadTremVerdeAlternado1.pararThreadsAlternado1();
    } else if (threadTremAmareloAlternado1 != null) {
      threadTremAmareloAlternado1.reiniciarInterrompido3();
      threadTremVerdeAlternado1.reiniciarInterrompido3();

    }
    if (threadTremAmareloAlternado2 != null) {
      threadTremAmareloAlternado2.scaleFalse();
      threadTremAmareloAlternado2.interrupt();
      threadTremVerdeAlternado2.interrupt();
      threadTremAmareloAlternado2.pararThreadsAlternado2();
      threadTremVerdeAlternado2.pararThreadsAlternado2();
    } else if (threadTremAmareloAlternado2 != null) {
      threadTremAmareloAlternado2.reiniciarInterrompido4();
      threadTremVerdeAlternado2.reiniciarInterrompido4();
    }
  }
  
  public void verificarThreads(){
    if (threadTremAmareloTwoLeft != null) {
      threadTremAmareloTwoLeft.interrupt();
      threadTremVerdeTwoLeft.interrupt();
      threadTremAmareloTwoLeft.pararThreadsTwoLeft();
      threadTremVerdeTwoLeft.pararThreadsTwoLeft();

    } else if (threadTremAmareloTwoLeft != null) {
      threadTremAmareloTwoLeft.reiniciarInterrompido1();
      threadTremVerdeTwoLeft.reiniciarInterrompido1();
    }
    if (threadTremAmareloTwoRight != null) {
      threadTremAmareloTwoRight.interrupt();
      threadTremVerdeTwoRight.interrupt();
      threadTremAmareloTwoRight.pararThreadsTwoRight();
      threadTremVerdeTwoRight.pararThreadsTwoRight();
    } else if (threadTremAmareloTwoRight != null) {
      threadTremAmareloTwoRight.reiniciarInterrompido2();
      threadTremVerdeTwoRight.reiniciarInterrompido2();

    }
    if (threadTremAmareloAlternado1 != null) {
      threadTremAmareloAlternado1.interrupt();
      threadTremVerdeAlternado1.interrupt();
      threadTremAmareloAlternado1.pararThreadsAlternado1();
      threadTremVerdeAlternado1.pararThreadsAlternado1();
    } else if (threadTremAmareloAlternado1 != null) {
      threadTremAmareloAlternado1.reiniciarInterrompido3();
      threadTremVerdeAlternado1.reiniciarInterrompido3();

    }
    if (threadTremAmareloAlternado2 != null) {
      threadTremAmareloAlternado2.interrupt();
      threadTremVerdeAlternado2.interrupt();
      threadTremAmareloAlternado2.pararThreadsAlternado2();
      threadTremVerdeAlternado2.pararThreadsAlternado2();
    } else if (threadTremAmareloAlternado2 != null) {
      threadTremAmareloAlternado2.reiniciarInterrompido4();
      threadTremVerdeAlternado2.reiniciarInterrompido4();

    }
  }

}


