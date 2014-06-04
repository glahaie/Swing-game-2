/* Ciseaux.java
 * Par Guillaume Lahaie
 * 
 * Sous-classe de Piece, permet de créer des objets de type Ciseaux pour jouer
 * à roche-papier-ciseaux. La classe permet surtout de dessiner des ciseaux à partir
 * de l'image appropriée (CiseauxImg) et de vérifier si "manger une Pile" est permis.
 * Les Ciseaux peuvent seulement s'empiler sur Papier.
 * 
 * Dernière modification: 5 novembre 2011.
 */

import java.awt.image.BufferedImage;

public class Ciseaux extends Piece {
	
	static int noCiseaux = 1; 

	//Constructeurs
	Ciseaux(int i, int j) {
		super(i, j);
		this.no = noCiseaux;
		noCiseaux++;
	}

	Ciseaux(int i, int j, int no, boolean dessus) {
		super(i, j, no, dessus);
	}
	
	Ciseaux (Ciseaux that) {
		this(that.i, that.j, that.no, that.dessus);
	}
	
	public String toString() {
		return "Ciseaux de position i = " + i + " j = " + j;
	}
	
	//Permet d'obtenir une copie de l'objet, et non seulement une référence
	//au même objet.
	public Piece newInstance() {
		return new Ciseaux(this);
	}

	public boolean emporteSur(Piece m) {
		if(m instanceof Papier)
			return true;
		return false;
	}

	//Retourne le bon fichier image pour dessiner les Ciseaux.
	public BufferedImage getImage() {
		return ciseauxImg;
	}
	
	//Mise à jour de la position des Ciseaux si elle change de Pile.
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

	//Information sur les Ciseaux pour pouvoir annuler un mouvement. 
	public String information() {
		return "B," +no;
	}
}
