
/** **************************************************************
 * Autor............: Pedro Lucca Silva Martins
 * Matricula........: 202210183
 * Inicio...........: 20/09/2023
 * Ultima alteracao.: 02/09/2023
 * Nome.............: Desafio do trem
 * Funcao...........: Aprender concorrencia com zona critica
 *************************************************************** */

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.transform.Scale;

public class ThreadTremTwoRight extends Thread {

  private Button button;
  private String name;
  public int speed = 2;
  private volatile boolean interrompido2 = false;
  Scale scale = new Scale(-1, 1);
  private volatile static int tunel1amarelo = 0;
  private volatile static int tunel2amarelo = 0;
  private volatile static int tunel1verde = 0;
  private volatile static int tunel2verde = 0;
  private volatile static int tunel2EA = 0;
  private volatile static int tunel1EA = 0;
  private int opcaoDeParada = 1;
  private Peterson peterson;

  public ThreadTremTwoRight(Button trem, String name, int opcaoDeParada, Peterson peterson) {
    this.button = trem;
    this.name = name;
    this.opcaoDeParada = opcaoDeParada;
    this.peterson = peterson;
    //colocar os trens do lado direito
    if (button.getId().equals("TremAmarelo")) {
      button.getTransforms().add(scale); // modifico a imagem dos trens logo no construtor para inverter ambos
      button.setLayoutX(900);
      button.setLayoutY(318);
    } else if (button.getId().equals("TremVerde")) {
      button.getTransforms().add(scale); // modifico a imagem dos trens logo no construtor para inverter ambos
      button.setLayoutX(900);
      button.setLayoutY(396);
    }
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }

  public void opcaoDeParada(int opcaoDeParada) {
    this.opcaoDeParada = opcaoDeParada;
  }

  @Override
  public void run() {
    Thread.currentThread().setPriority(1); // setando a prioridade para o trem n iniciar parado

    // movimentar os trens de uma ponta a outra
    while (button.getLayoutX() > 1) {
      Platform.runLater(() -> button.setLayoutX(button.getLayoutX() - 1));

      // interromper a movimentação dos trens para reiniciar o jogo
      if (interrompido2) {
        interromperJogo();
        break;
      }

      switch (opcaoDeParada) {
        case 1:
          // condicionais para fazer o trem amarelo se mover
          movimentarTremAmareloVariavelDeTravamento();
          // condicionais para fazer o trem verde se mover
          movimentarTremVerdeVariavelDeTravamento();
          break;
        case 2:
          // condicionais para fazer o trem amarelo se mover
          movimentarTremAmareloEstritaAlternancia();
          // condicionais para fazer o trem verde se mover
          movimentarTremVerdeEstritaAlternancia();
          break;
        case 3:
          // condicionais para fazer o trem amarelo se mover
          movimentarTremAmareloSolucaoDePeterson();
          // condicionais para fazer o trem verde se mover
          movimentarTremVerdeSolucaoDePeterson();
          break;
      }

      // realocar os trens ao outro lado da tela
      realocarTrens();

      // funcao para controlar a velocidade dos trens
      sleep1();
    }
  }

  public void sleep1() {
    try {
      long waitTime = (long) (60.0 / speed); // utilizo essa inversão de valores para poder usar o slider de forma correta
      if (waitTime == 60) {
        button.setLayoutX(button.getLayoutX() + 1); // para deixar os trens parados no eixo X
        // condicionais para fazer o trem amarelo parar no eixo Y
        if (button.getId().equals("TremAmarelo")) {
          pararTremAmarelo();
        }
        // condicionais para fazer o trem verde parar no eixo Y
        if (button.getId().equals("TremVerde")) {
          pararTremVerde();
        }
      }
      Thread.sleep(waitTime); //multiplico o valor da prioridade para fazer o trem andar mais devagar em milisegundos
    } catch (InterruptedException ex) {

    }
  }

  public void pararTremVerde() {
    if (button.getLayoutX() < 806 && button.getLayoutX() > 774) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
    } else if (button.getLayoutX() < 652 && button.getLayoutX() > 615) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
    } else if (button.getLayoutX() < 500 && button.getLayoutX() > 460) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
    } else if (button.getLayoutX() < 342 && button.getLayoutX() > 303) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
    }
  }

  public void pararTremAmarelo() {
    if (button.getLayoutX() < 806 && button.getLayoutX() > 764) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
    } else if (button.getLayoutX() < 652 && button.getLayoutX() > 610) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
    } else if (button.getLayoutX() < 500 && button.getLayoutX() > 458) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
    } else if (button.getLayoutX() < 342 && button.getLayoutX() > 300) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
    }
  }

  public void interromperJogo() {
    if (interrompido2) {
      if (button.getId().equals("TremVerde")) {
        button.setLayoutX(890);
        button.setLayoutY(393);
      } else if (button.getId().equals("TremAmarelo")) {
        button.setLayoutX(890);
        button.setLayoutY(320);
      }
    }
  }

  public void movimentarTremAmareloVariavelDeTravamento() {
    if (button.getId().equals("TremAmarelo")) {
      if (button.getLayoutX() < 806 && button.getLayoutX() > 764) {
        if (tunel2verde == 1) {
          button.setLayoutX(button.getLayoutX() + 1);
          button.setLayoutY(button.getLayoutY() - 1);
        } else {
          tunel2amarelo = 1;
        }
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      } else if (button.getLayoutX() < 652 && button.getLayoutX() > 610) {
        tunel2amarelo = 0;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
      } else if (button.getLayoutX() < 500 && button.getLayoutX() > 458) {
        if (tunel1verde == 1) {
          button.setLayoutX(button.getLayoutX() + 1);
          button.setLayoutY(button.getLayoutY() - 1);
        } else {
          tunel1amarelo = 1;
        }
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      } else if (button.getLayoutX() < 342 && button.getLayoutX() > 300) {
        tunel1amarelo = 0;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
      }
    }
  }

  public void movimentarTremVerdeVariavelDeTravamento() {
    if (button.getId().equals("TremVerde")) {
      if (button.getLayoutX() < 806 && button.getLayoutX() > 774) {
        if (tunel2amarelo == 1) {
          button.setLayoutX(button.getLayoutX() + 1);
          button.setLayoutY(button.getLayoutY() + 1);
        } else {
          tunel2verde = 1;
        }
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
      } else if (button.getLayoutX() < 652 && button.getLayoutX() > 615) {
        tunel2verde = 0;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      } else if (button.getLayoutX() < 500 && button.getLayoutX() > 460) {
        if (tunel1amarelo == 1) {
          button.setLayoutX(button.getLayoutX() + 1);
          button.setLayoutY(button.getLayoutY() + 1);
        } else {
          tunel1verde = 1;
        }
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
      } else if (button.getLayoutX() < 342 && button.getLayoutX() > 303) {
        tunel1verde = 0;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      }
    }
  }

  public void movimentarTremAmareloEstritaAlternancia() {
    if (button.getId().equals("TremAmarelo")) {
      if (button.getLayoutX() < 806 && button.getLayoutX() > 765) {
        if (tunel1EA == 1) {
          Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
        } else {
          button.setLayoutX(button.getLayoutX() + 1);
        }
      } else if (button.getLayoutX() < 652 && button.getLayoutX() > 615) {
        tunel1EA = 0;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
      } else if (button.getLayoutX() < 500 && button.getLayoutX() > 460) {
        if (tunel2EA == 1) {
          Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
        } else {
          button.setLayoutX(button.getLayoutX() + 1);
        }
      } else if (button.getLayoutX() < 342 && button.getLayoutX() > 303) {
        tunel2EA = 0;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
      }
    }
  }

  public void movimentarTremVerdeEstritaAlternancia() {
    if (button.getId().equals("TremVerde")) {
      if (button.getLayoutX() < 806 && button.getLayoutX() > 772) {
        if (tunel1EA == 0) {
          Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
        } else {
          button.setLayoutX(button.getLayoutX() + 1);
        }
      } else if (button.getLayoutX() < 652 && button.getLayoutX() > 615) {
        tunel1EA = 1;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      } else if (button.getLayoutX() < 500 && button.getLayoutX() > 460) {
        if (tunel2EA == 0) {
          Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
          System.out.println(tunel1EA);
        } else {
          button.setLayoutX(button.getLayoutX() + 1);
        }
      } else if (button.getLayoutX() < 342 && button.getLayoutX() > 303) {
        tunel2EA = 1;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      }
    }
  }

  public void movimentarTremVerdeSolucaoDePeterson() {

    // condicionais para fazer o trem verde se mover
    if (button.getId().equals("TremVerde")) {
      if (button.getLayoutX() < 806 && button.getLayoutX() > 774) {
        peterson.entrarNaZona2(1);
        while(button.getLayoutX() < 806 && button.getLayoutX() > 774){
          Platform.runLater(() -> button.setLayoutX(button.getLayoutX() - 1));
           if (button.getLayoutX() < 806 && button.getLayoutX() > 774){
             Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
           }
          sleep1();
        }
      } else if (button.getLayoutX() < 652 && button.getLayoutX() > 615) {
        peterson.sairDaZona2(1);
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      } else if (button.getLayoutX() < 500 && button.getLayoutX() > 460) {
        peterson.entrarNaZona(1);
        while(button.getLayoutX() < 500 && button.getLayoutX() > 460){
          Platform.runLater(() -> button.setLayoutX(button.getLayoutX() - 1));
           if (button.getLayoutX() < 500 && button.getLayoutX() > 460){
             Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
           }
          sleep1();
        }
      } else if (button.getLayoutX() < 342 && button.getLayoutX() > 303) {
        peterson.sairDaZona(1);
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      }
    }
  }

  public void movimentarTremAmareloSolucaoDePeterson() {

    // condicionais para fazer o trem amarelo se mover
    if (button.getId().equals("TremAmarelo")) {
      if (button.getLayoutX() < 806 && button.getLayoutX() > 774) {
        peterson.entrarNaZona2(0);
        while(button.getLayoutX() < 806 && button.getLayoutX() > 774){
          Platform.runLater(() -> button.setLayoutX(button.getLayoutX() - 1));
           if (button.getLayoutX() < 806 && button.getLayoutX() > 774){
             Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
           }
          sleep1();
        }
      } else if (button.getLayoutX() < 652 && button.getLayoutX() > 615) {
        peterson.sairDaZona2(0);
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
      } else if (button.getLayoutX() < 500 && button.getLayoutX() > 460) {
        peterson.entrarNaZona(0);
        while(button.getLayoutX() < 500 && button.getLayoutX() > 460){
          Platform.runLater(() -> button.setLayoutX(button.getLayoutX() - 1));
           if (button.getLayoutX() < 500 && button.getLayoutX() > 460){
             Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
           }
          sleep1();
        }
      } else if (button.getLayoutX() < 342 && button.getLayoutX() > 303) {
        peterson.sairDaZona(0);
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
      }
    }
  }

  public void realocarTrens() {
    if (button.getLayoutX() <= 180) {
      if (button.getId().equals("TremVerde")) {// retornar o trem verde ao outro lado
        button.setLayoutX(890);
        button.setLayoutY(393);
      } else if (button.getId().equals("TremAmarelo")) {// retornar o trem amarelo ao outro lado
        button.setLayoutX(890);
        button.setLayoutY(320);
      }
    }
  }

  public void pararThreadsTwoRight() {
    interrompido2 = true;
  }

  public void reiniciarInterrompido2() {
    interrompido2 = false;
  }

  public void scaleTrue() {
    button.getTransforms().clear();
    Scale scaleTrue = new Scale(-1, 1);
    button.getTransforms().add(scaleTrue);
  }

  public void scaleFalse() {
    button.getTransforms().clear();
    Scale scaleFalse = new Scale(1, 1);
    button.getTransforms().add(scaleFalse);
  }
}
