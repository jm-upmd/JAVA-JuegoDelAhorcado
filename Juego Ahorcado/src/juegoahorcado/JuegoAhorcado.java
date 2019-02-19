package juegoahorcado;

import java.util.Scanner;

public class JuegoAhorcado {
	
	// Array de strings. Cada string representa un estado de la horca pintada con caracteres.
	// Cada string de cada dibujo de la horca se ha escrito  en varivas líneas concatenadas para que 
	// visualmente sea más legible el patrón de dibujo que representan.
	// Por ejemplo, la primera horca podría representarse con el siguiente string en una sola línea:
	//   "  +---+  \n  |   |  \n      |  \n      |  \n      |  \n      |  \n=========\n"
		
	private static final String[] IMAGENES_AHORCADO = {
		//primera horca	
		"  +---+  \n" +
		"  |   |  \n" +
		"      |  \n" +
		"      |  \n" +
		"      |  \n" +
		"      |  \n" +
		"=========\n" ,
		//segunda horca
		"         \n" +
		"  +---+  \n" +
		"  |   |  \n" +
		"  O   |  \n" +
		"      |  \n" +
		"      |  \n" +
		"      |  \n" +
		"=========\n" ,
		// tercera horca
		"         \n" +
		"  +---+  \n" +
		"  |   |  \n" +
		"  O   |  \n" +
		"  |   |  \n" +
		"      |  \n" +
		"      |  \n" +
		"=========\n" ,
		
		"         \n" +
		"  +---+  \n" +
		"  |   |  \n" +
		"  O   |  \n" +
		" /|   |  \n" +
		"      |  \n" +
		"      |  \n" +
		"=========\n" ,
		
		"         \n" +
		"  +---+  \n" +
		"  |   |  \n" +
		"  O   |  \n" +
		" /|\\  |  \n" +  // caracter de barra invertida \ hay que duplicarlo para que lo interprete como caracter literal y no de escape.
		"      |  \n" +
		"      |  \n" +
		"=========\n" ,
		
		"         \n" +
		"  +---+  \n" +
		"  |   |  \n" +
		"  O   |  \n" +
		" /|\\  |  \n" +
		" /    |  \n" +
		"      |  \n" +
		"=========\n" ,
		
		"         \n" +
		"  +---+  \n" +
		"  |   |  \n" +
		"  O   |  \n" +
		" /|\\  |  \n" +
		" / \\  |  \n" +
		"      |  \n" +
		"=========\n" };
	
	static Scanner sc;
	
	static final int MAX_FALLOS = IMAGENES_AHORCADO.length -1;
	
	static final String[] PALABRAS = {"ORDENADOR", "CONFEDERACION", "ALTAVOZ","TIBURON","CONSTELACION","MOTOCICLETA",
			"ESTRATIFICACION","REPRESENTANTE","FUNCIONALIDAD","POLIMORFISMO","EXPERIMENTACION"};
		
	static String palabraADescrubir;  // Palabra a descubrir.
		
	// Palabra en construcción. Array donde se irá construyendo la palabra a medida que el usuario da letras.
	static char[] palapraEnConstruccion; 
	
	public static void main(String[] args) {
		
		palabraADescrubir = damePalabra();  // Palabra aleatoria de entre las existentes en PALABRAS.
		
		palapraEnConstruccion = new char[palabraADescrubir.length()]; // Se irán añadiendo letras coincidentes suministradas por usuario.
		
		char letra='_'; // Caracter para representar la posicón (hueco) de las letras que aún faltan.
		
		sc = new Scanner(System.in);  // Objeto para leer las letras por consola.
		
		int contFallos = 0;	// Contador de fallos. Se incrementa por cada letra suministrada que no existenta en la palabra a descubrir.
		
		boolean partidaTerminada = false; 	// Centinela que determinará si la partida a terminado o no.
		
		int aciertos; 	// Número de ocurrencias de la letra suministrada por el usuario en  la palabra a descubrir.
		
		nuevaPartida(); // Crea una nueva partida.(Pinta horca inicial y patrón (huecos) de la palabra a descubrir ...)
		
		while (!partidaTerminada){
			
			letra = pideLetra("Escribe la letra: ");  	// Pide letra por consola.
			
			borraConsola();
			
			// 1. Comprueba ocurencias de la letra en la palabra a descubrir
			// 2. Las añade a la palabra en construcción. 
			// 3. Escribe la palabra en construcción actualizada
			// 4. Devuelve número de aciertos (ocurrencias de la letra).
			aciertos = pintaPalabra(letra);  	
			
			// Si letra no está en la palabra a descubrir incrementa contador de fallos.
			contFallos = aciertos == 0 ? contFallos + 1 : contFallos;  	
			
			pintaHorca(contFallos); // Repinta horca según número de fallos.
			
			// Evalua si se ha llegado al total de fallos o se ha completado la palabra, en cuyo caso 
			// emite el correspondiente mensaje por consola y devuelve true indicar que la partida a terminado.
			// Si no se da ninguna de las condiciones anteriores devuelve false para iterar bucle y seguir pidiendo letras.
			partidaTerminada = resultadoTirada (contFallos,aciertos);
			
			
			// Si la partida ha terminado preguntamos si quiere jugar otra
			if(partidaTerminada) {
				// Pregunta por consola si jugar otra partida y en caso positivo comienza una nueva.
				// Vuelve a poner partidaTerminada = false para volver a iterar en el bucle.
				if( (contFallos = jugarOtraPartida()) == 0) {
					partidaTerminada = false;
				}

			}	
		} // fin while()
		
		System.out.println("\n¡Hasta pronto!");

	}  // fin main()
	
	
	/**
	 * Pregunta si se quiere jugar otra partida y en caso afirmativo llama a 
	 * a nuevaPartida para generar una nueva partida.
	 * @return 0 si hemos pulsado S para jugar otra partida; -1 en caso contrario 
	 */
	private static int jugarOtraPartida() {
		if(pideLetra("\n¿Quieres jugar otra partida? [S/N]: ") == 'S') {
			nuevaPartida();
			return 0;
		} else return -1;
		
	}
	
	/**
	 * Crea una nueva partida. Para ello  inicializa las  varibles palabra, palabraParcial, 
	 * y pinta por consola la horca inicial y el patrón con guiones bajos de la palabra a descubrir
	 */
	private static void nuevaPartida() {
		borraConsola();
		palabraADescrubir = damePalabra();
		palapraEnConstruccion= new char[palabraADescrubir.length()];
		pintaPalabra('_');
		pintaHorca(0);
	}
	
	/**
	 * Evalua la jugada y escribe por consola mensaje de derrota o victoria en el caso de haber 
	 * agotado el número de intentos o haber completado la palabra respectivamente.
	 * @param contador Numero de intentos acumulado para adivinar la palabra
	 * @param aciertos Numero de letras coincidentes con la última letra suministrada.
	 * @return true en caso de que la partida haya terminado, bien por derrota o victoria
	 *         false en caso contrario (la partida continua).
	 */
	private static boolean resultadoTirada(int contador, int aciertos) {
		if (aciertos == 0 && contador == MAX_FALLOS) {
			System.out.println("Ooooh, fallaste");
			System.out.println("La palabra era: " + palabraADescrubir);
			return true;
		} else if (String.valueOf(palapraEnConstruccion).equals(palabraADescrubir)) {
			System.out.println("¡ENHORABUENA, has resuelto la palabra!");
			return true;
		} else return false;
		

	}
	
	/**
	 * Pide una letra por consola y la convierte a mayúscula
	 * @param t Texto utilizado para pedir la letra.
	 * @return La letra suministrada por consola, convertida a mayúscula.
	 */
	private static char pideLetra(String t) {
		System.out.print(t);
		sc.reset();
		return  Character.toUpperCase(sc.next().charAt(0));
		
		
	}
	
	/**
	 * 
	 * SI letra = '_': 
     *   Inicializa cada letra de palabraEnConstruccion con caracter '_' 
     *   Escribe por consola palabraEnConstruccion.
	 * EN CASO CONTRAIO:
	 *    Añade la letra a palabraEnConstruccion, siempre que esta esté presente en palabraADescubrir.
	 *    La letra es añadida en la misma posición que se encuentra en palabraADescrubir.
	 *    Escribre palabraEnConstruccion por consola.
	 * 
	 * 
	 * @param letra Letra proporcionada para colocar en palabraADescubrir.
	 * @return número de letras añadidas a palabraEnConstruccion. 
	 *         -1 si letra = '_'
	 */

	private static int pintaPalabra(char letra) {
		int contador = 0;
		System.out.println("\n\n");
		// Pinta por consola solo los guiones de posición de letra.
		if(letra == '_') {
			for(int i=0; i<palapraEnConstruccion.length; i++) {
				palapraEnConstruccion[i] ='_';
				System.out.print(letra +" ");
				
			}
			contador = -1;
			System.out.println("\n");
			
		} else {
			// copia a array palabraEnConstruccion los caracteres coincidentes con letra en palabraADescubrir.
			for(int i=0; i<palabraADescrubir.length(); i++) {
				if(palabraADescrubir.charAt(i) == letra) {
					palapraEnConstruccion[i] = letra;
					contador++;
				}
				System.out.print(String.valueOf(palapraEnConstruccion[i]) + " " );
			}
			System.out.println("\n\n");
		}
		
		return contador; // Número de caracteres coincidentes añadidos a palabraADescubrir.
		
	}

	private static void pintaHorca(int i) {
		System.out.println(IMAGENES_AHORCADO[i]);
		
	}
	
	/**
	 * Escribe 200 líneas en blanco para simular un borrado de consola

	 */
	private static void borraConsola()  {
		
		// Utilizar esta forma si se ejecuta en consola de comandos de Windows.
		/*try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		// Esto si utilizamos la consola de Eclipse.		
		for(int i=0; i< 200; i++) {
			System.out.println();
		}
	}
	
	/**
	 * Devuelve una palabra del array PALABRAS. La palabra corresponede a una  
	 * posición aleatoria en el rango  0 - (n-1),  donde n es el número de palabras 
	 * almacenadas en el array PALABRAS
	 * @return La palabra seleccionada aleatoriamente.
	 */
	private static String damePalabra() {
		int n = PALABRAS.length;
		
		// Genero entero aleatorio entre 0 y n - 1;

		n = (int)(Math.random()*n);

		return PALABRAS[n];

		}

}
