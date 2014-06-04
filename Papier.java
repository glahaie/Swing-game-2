/* Papier.java
 * Par Guillaume Lahaie
 * 
 * Sous-classe de Piece, permet de créer des objets de type Papier pour jouer
 * à roche-papier-ciseaux. La classe permet surtout de dessiner un Papier à partir
 * de l'image appropriée (papierImg) et de vérifier si "manger une Pile" est permis.
 * Le Papier peut seulement s'empiler sur une Roche.
 * 
 * Dernière modification: 5 novembre 2011.
 */
import java.awt.image.BufferedImage;

public class Papier extends Piece {
	
	static int noPapier = 1;

	//Constructeurs
	Papier(int i, int j) {
		super(i, j);
		no = noPapier;
		noPapier++;
	}
	
	Papier(int i, int j, int no, boolean dessus) {
		super(i, j, no, dessus);
	}
	
	Papier (Papier that) {
		this(that.i, that.j, that.no, that.dessus);
	}
	
	public String toString() {
		return "Papier de position i = " + i + " j = " + j;
	}

	//Permet d'obtenir une copie de l'objet, et non seulement une référence
	//au même objet.
	public Piece newInstance() {
		return new Papier(this);
	}

	public boolean emporteSur(Piece m) {
		if(m instanceof Roche)
			return true;
		return false;
	}
	
	//Retourne le bon fichier image pour dessiner le Papier.
	public BufferedImage getImage() {
		return papierImg;
	}
	
	//Mise à jour de la position du Papier si elle change de Pile.
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
	
	//Information sur le Papier pour pouvoir annuler un mouvement. 
	public String information() {
		return "G,"+no;
	}
}
