/* Roche.java
 * Par Guillaume Lahaie
 * 
 * Sous-classe de Piece, permet de créer des objets de type Roche pour jouer
 * à roche-papier-ciseaux. La classe permet surtout de dessiner une roche à partir
 * de l'image appropriée (rocheImg) et de vérifier si "manger une Pile" est permis.
 * La roche peut seulement s'empiler sur Ciseaux.
 * 
 * Dernière modification: 5 novembre 2011.
 */
import java.awt.image.BufferedImage;

public class Roche extends Piece {
	
	static int noRoche = 1;

	Roche(int i, int j) {
		super(i, j);
		this.no = noRoche;
		noRoche++;
	}	
	
	Roche(int i, int j, int no, boolean dessus) {
		super(i, j, no, dessus);
	}

	Roche(Roche that) {
		this(that.i, that.i, that.no, that.dessus); 
	}
	
	public String toString() {
		return "Roche de position i = " + i + " j = " + j;
	}
	
	//Permet d'obtenir une copie de la Roche, et non seulement une référence
	//au même objet.
	public Piece newInstance() {
		return new Roche(this);
	}

	public boolean emporteSur(Piece m) {
		if(m instanceof Ciseaux)
			return true;
		return false;
	}
	
	//Retourne le bon fichier image pour dessiner la Roche.
	public BufferedImage getImage() {
		return rocheImg;
	}
	
	//Mise à jour de la position de la Roche si elle change de Pile.
	public void setPos(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	public int getPosI() {
		return i;
	}
	public int getPosJ() {
		return j;
	}
	
	//Information sur la Roche pour pouvoir annuler un mouvement. 
	public String information() {
		return "R," +no;
	}

}
