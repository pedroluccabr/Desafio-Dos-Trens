
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

public class ThreadTremAlternado1 extends Thread {

  private Button button;
  private String name;
  public int speed = 2;
  private volatile boolean interrompido3 = false;
  Scale scale = new Scale(-1, 1);
  private volatile static int tunel1amarelo = 0;
  private volatile static int tunel2amarelo = 0;
  private volatile static int tunel1verde = 0;
  private volatile static int tunel2verde = 0;
  private volatile static int tunel2EA = 0;
  private volatile static int tunel1EA = 0;
  private int opcaoDeParada = 1;
  private Peterson peterson;

  public ThreadTremAlternado1(Button trem, String name, int opcaoDeParada, Peterson peterson) {
    this.button = trem;
    this.name = name;
    this.opcaoDeParada = opcaoDeParada;
    this.peterson = peterson;
    //colocar os trens alternadamente
    if (button.getId().equals("TremAmarelo")) {
      button.setLayoutX(115);
      button.setLayoutY(318);
    } else if (button.getId().equals("TremVerde")) {
      button.getTransforms().add(scale); // utilizo para inverter a imagem do trem
      button.setLayoutX(890);
      button.setLayoutY(393);
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

    //codigo para o trem amarelo
    if (button.getId().equals("TremAmarelo")) {
      // movimentar o trem amarelo de uma ponta a outra
      while (button.getLayoutX() < 1000) {
        Platform.runLater(() -> button.setLayoutX(button.getLayoutX() + 1));

        // interromper a movimentação do trem amarelo
        if (interrompido3) {
          interromperTremAmarelo();
          tunel2EA = 0;
          tunel1EA = 0;
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

        // realocar trem amarelo
        realocarTremAmarelo();

        // funcao para controlar a velocidade dos trens
        sleep1();
      }
    }

    // codigo para o trem verde
    if (button.getId().equals("TremVerde")) {
      while (button.getLayoutX() > 1) {
        Platform.runLater(() -> button.setLayoutX(button.getLayoutX() - 1));

        // interromper a movimentação do trem verde
        if (interrompido3) {
          interromperTremVerde();
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

        // realocar trem verde
        realocarTremVerde();

        // funcao para controlar a velocidade dos trens
        sleep1();
      }
    }
  }

  public void sleep1() {
    try {
      long waitTime = (long) (60.0 / speed); // utilizo essa inversão de valores para poder usar o slider de forma correta  
      if (waitTime == 60 && button.getId().equals("TremVerde")) {
        button.setLayoutX(button.getLayoutX() + 1); // para deixar o trem verde parado no eixo X 
        // condicionais para fazer o trem verde parar no eixo Y
        pararTremVerde();
      } else if (waitTime == 60 && button.getId().equals("TremAmarelo")) {
        button.setLayoutX(button.getLayoutX() - 1); // para deixar o trem amarelo parado no eixo X
        // condicionais para fazer o trem amarelo parar no eixo Y
        pararTremAmarelo();
      }
      Thread.sleep(waitTime);
    } catch (InterruptedException ex) {

    }
  }// fim do metodo sleep

  public void pararTremVerde() {
    if (button.getLayoutX() < 806 && button.getLayoutX() > 774) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
    } else if (button.getLayoutX() < 652 && button.getLayoutX() > 615) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
    } else if (button.getLayoutX() < 500 && button.getLayoutX() > 460) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
    } else if (button.getLayoutX() < 342 && button.getLayoutX() > 305) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
    }
  }

  public void pararTremAmarelo() {
    if (button.getLayoutX() > 182 && button.getLayoutX() < 222) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
    } else if (button.getLayoutX() > 338 && button.getLayoutX() < 378) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
    } else if (button.getLayoutX() > 500 && button.getLayoutX() < 540) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
    } else if (button.getLayoutX() > 650 && button.getLayoutX() < 690) {
      Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
    }
  }

  public void movimentarTremAmareloVariavelDeTravamento() {
    // condicionais para fazer o trem amarelo se mover
    if (button.getId().equals("TremAmarelo")) {
      if (button.getLayoutX() > 182 && button.getLayoutX() < 222) {
        if (tunel1verde == 1) {
          button.setLayoutX(button.getLayoutX() - 1);
          button.setLayoutY(button.getLayoutY() - 1);
        } else {
          tunel1amarelo = 1;
        }
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      } else if (button.getLayoutX() > 338 && button.getLayoutX() < 378) {
        tunel1amarelo = 0;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
      } else if (button.getLayoutX() > 500 && button.getLayoutX() < 540) {
        if (tunel2verde == 1) {
          button.setLayoutX(button.getLayoutX() - 1);
          button.setLayoutY(button.getLayoutY() - 1);
        } else {
          tunel2amarelo = 1;
        }
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      } else if (button.getLayoutX() > 650 && button.getLayoutX() < 690) {
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
        tunel2amarelo = 0;
      }
    }
  }

  public void realocarTremAmarelo() {
    if (button.getId().equals("TremAmarelo")) {
      if (button.getLayoutX() >= 820) {
        System.out.println(Thread.currentThread().getName() + " Chegou");
        button.setLayoutX(105);
        button.setLayoutY(318);
      }
    }
  }

  public void realocarTremVerde() {
    if (button.getId().equals("TremVerde")) {
      if (button.getLayoutX() <= 180) {
        button.setLayoutX(890);
        button.setLayoutY(393);
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
      } else if (button.getLayoutX() < 342 && button.getLayoutX() > 305) {
        tunel1verde = 0;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      }
    }
  }

  public void movimentarTremAmareloEstritaAlternancia() {
    // condicionais para fazer o trem amarelo se mover
    if (button.getId().equals("TremAmarelo")) {
      if (button.getLayoutX() > 182 && button.getLayoutX() < 222) {
        if (tunel1EA == 1) {
          Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
        } else {
          button.setLayoutX(button.getLayoutX() - 1);
        }
      } else if (button.getLayoutX() > 338 && button.getLayoutX() < 378) {
        tunel1EA = 0;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
      } else if (button.getLayoutX() > 500 && button.getLayoutX() < 540) {
        if (tunel2EA == 1) {
          Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
        } else {
          button.setLayoutX(button.getLayoutX() - 1);
        }
      } else if (button.getLayoutX() > 650 && button.getLayoutX() < 690) {
        tunel2EA = 0;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
      }
    }
  }

  public void movimentarTremVerdeEstritaAlternancia() {
    if (button.getId().equals("TremVerde")) {
      if (button.getLayoutX() < 806 && button.getLayoutX() > 772) {
        if (tunel2EA == 0) {
          Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
        } else {
          button.setLayoutX(button.getLayoutX() + 1);
        }
      } else if (button.getLayoutX() < 652 && button.getLayoutX() > 615) {
        tunel2EA = 1;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      } else if (button.getLayoutX() < 500 && button.getLayoutX() > 460) {
        if (tunel1EA == 0) {
          Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
        } else {
          button.setLayoutX(button.getLayoutX() + 1);
        }
      } else if (button.getLayoutX() < 342 && button.getLayoutX() > 303) {
        tunel1EA = 1;
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      }
    }
  }

  public void movimentarTremAmareloSolucaoDePeterson() {

    // condicionais para fazer o trem amarelo se mover
    if (button.getId().equals("TremAmarelo")) {
      if (button.getLayoutX() > 182 && button.getLayoutX() < 222) {
        peterson.entrarNaZona(0);
        while (button.getLayoutX() > 182 && button.getLayoutX() < 338) {
          Platform.runLater(() -> button.setLayoutX(button.getLayoutX() + 1));
          if (button.getLayoutX() > 182 && button.getLayoutX() < 222) {
            Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
          }
          sleep1();
        }
      } else if (button.getLayoutX() > 338 && button.getLayoutX() < 378) {
        peterson.sairDaZona(0);
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
      } else if (button.getLayoutX() > 500 && button.getLayoutX() < 540) {
        peterson.entrarNaZona2(0);
        while (button.getLayoutX() > 500 && button.getLayoutX() < 540) {
          Platform.runLater(() -> button.setLayoutX(button.getLayoutX() + 1));
          if (button.getLayoutX() > 500 && button.getLayoutX() < 540) {
            Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
          }
          sleep1();
        }
      } else if (button.getLayoutX() > 650 && button.getLayoutX() < 690) {
        peterson.sairDaZona2(0);
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
      }
    }
  }

  public void movimentarTremVerdeSolucaoDePeterson() {

    // condicionais para fazer o trem verde se mover
    if (button.getId().equals("TremVerde")) {
      if (button.getLayoutX() < 806 && button.getLayoutX() > 774) {
        peterson.entrarNaZona2(1);
        while (button.getLayoutX() < 806 && button.getLayoutX() > 774) {
          Platform.runLater(() -> button.setLayoutX(button.getLayoutX() - 1));
          if (button.getLayoutX() < 806 && button.getLayoutX() > 774) {
            Platform.runLater(() -> button.setLayoutY(button.getLayoutY() - 1));
          }
          sleep1();
        }
      } else if (button.getLayoutX() < 652 && button.getLayoutX() > 615) {
        peterson.sairDaZona2(1);
        Platform.runLater(() -> button.setLayoutY(button.getLayoutY() + 1));
      } else if (button.getLayoutX() < 500 && button.getLayoutX() > 460) {
        peterson.entrarNaZona(1);
        while (button.getLayoutX() < 500 && button.getLayoutX() > 460) {
          Platform.runLater(() -> button.setLayoutX(button.getLayoutX() - 1));
          if (button.getLayoutX() < 500 && button.getLayoutX() > 460) {
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

  public void interromperTremVerde() {
    if (button.getId().equals("TremVerde")) {
      button.setLayoutX(890);
      button.setLayoutY(393);
    }
  }

  public void interromperTremAmarelo() {
    if (button.getId().equals("TremAmarelo")) {
      button.setLayoutX(115);
      button.setLayoutY(318);
    }
  }

  public void pararThreadsAlternado1() {
    interrompido3 = true;
  }

  public void reiniciarInterrompido3() {
    interrompido3 = false;
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
