/* Configuration.java
 * Par Guillaume Lahaie
 * 
 * Cette classe gère la lecture d'un fichier de configuration pour jouer
 * à roche-papier-ciseaux et enregistre ces configurations.
 * 
 * Dernière modification: 5 novembre 2011.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Configuration {
	
	private ArrayList<Integer> configID;
	private ArrayList<String[]> pieces;
	private boolean rien;
	private int choisi = 0; //On prend toujours la première configuration du fichier.
		
	//Constructeurs.
	Configuration(FileReader fr) throws IOException {
		configID = new ArrayList<Integer>();
		pieces = new ArrayList<String[]>();		
		lireFileReader(fr);
	}
	
	Configuration(String s) throws IOException, NullPointerException {
		this(new FileReader(s));
	}
	
	public String toString() {
		return "Configurations pour Roche-papier-ciseaux. " + configID.size() + " possibilités.";
	}
	
	//Lit le fichier ouvert dans le FileReader. Si une ligne contient un entier, alors 
	//on insère les 4 prochaines lignes dans un tableau pour enregistrer la configuration.
	private void lireFileReader(FileReader fr) throws IOException {
		BufferedReader br = new BufferedReader(fr);
		String oneLine;

		while((oneLine = br.readLine()) != null) {
			int blah = -1;
			try {
				String[] temp = new String[4];
				oneLine.trim();
				blah = Integer.parseInt(oneLine);
				//On insère la configuration dans l'arraylist
				configID.add(blah);
				for(int j = 0; j < 4; j++) {
					temp[j] = br.readLine().trim();					
				}
				rien = false;
				if(!rien)
					pieces.add(temp);
			} catch(NumberFormatException e) {
				rien = true;
			} catch(IOException e2) {
				System.out.println("IOException! numero de config: " + blah);
				rien = true;
			} catch (NullPointerException e2) {
				System.out.println("NullPointerException! numero de config: " + blah);
				rien = true;
			}
		}
		br.close();
	}
	
	//Nombres de configurations.
	protected int longueur() {
		return pieces.size();
	}
	
	protected String[] getSelectedConfig() {
		return pieces.get(choisi);
	}
	
	protected void setSelected(int i){
		this.choisi = i;
	}
	
	//Pour le menu configurations
	protected int getNum(int i) {
		return configID.get(i);
	}
	
	// Pour écrire dans le textArea 
	protected int getNumChoisi() {
		return configID.get(choisi);
	}
} //Fin configuration
