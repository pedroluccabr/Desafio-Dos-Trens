
/** **************************************************************
 * Autor............: Pedro Lucca Silva Martins
 * Matricula........: 202210183
 * Inicio...........: 18/09/2023
 * Ultima alteracao.: 20/09/2023
 * Nome.............: solucao de Peterson 
 * Funcao...........: solucionar os problemas de regiao critica
 *************************************************************** */

public class Peterson {

  private volatile int vez1;
  private volatile int vez2;
  private volatile boolean interessado[];
  private volatile boolean interessado2[];
  private volatile int outro1 = 0;
  private volatile int outro2 = 0;

  public Peterson(int i) {
    interessado = new boolean[i];
    interessado2 = new boolean[i];
  }

  public void entrarNaZona(int processID) {
    System.out.println("ENTRO NA ZONA");
    outro1 = 1 - processID;
    interessado[processID] = true;
    vez1 = processID;
    System.out.println(vez1);
    System.out.println(interessado[outro1]);
    while (vez1 == processID && interessado[outro1]) {
      System.out.println("");
    }
  }

  public void sairDaZona(int processID) {
    interessado[processID] = false;
  }
  
  public void entrarNaZona2(int processID) {
    System.out.println("ENTRO NA ZONA");
    outro2 = 1 - processID;
    interessado2[processID] = true;
    vez2 = processID;
    System.out.println(vez2);
    System.out.println(interessado2[outro2]);
    while (vez2 == processID && interessado2[outro2]) {
      System.out.println("");
    } 
  }

  public void sairDaZona2(int processID) {
    interessado2[processID] = false;
  }
  
  public boolean interesse1 (){
    if(interessado[outro1]){
      return true;
    }
    return false;
  }
  
  public boolean interesse2 (){
    if(interessado2[outro2]){
      return true;
    }
    return false;
  }
}
