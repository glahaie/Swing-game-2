/* Pile.java
 * Par Guillaume Lahaie
 * 
 * Cette classe permet de créer des piles de Piece pour jouer à roche-papier-ciseaux.
 * Elle permet d'empiler deux Piles et d'avoir la bonne Pile avec les bonnes Pièces
 * à la fin. Elle donne aussi des informations qui permettent d'annuler les mouvements
 * effectués. Les champs donnent des informations sur sa position dans le jeu, mais aussi
 * pour pouvoir la dessiner au bon endroit. De plus, la Pile connait ses voisins dans le
 * jeu et les mouvements possibles autour d'elle.
 * 
 * Dernière modification: 5 novembre 2011.
 */
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Pile extends JPanel{
	protected int nb;
	protected Piece dessus;
	protected Piece dessous;
	private int x, y, i, j;
	protected boolean selected, gauche = false, droite = false, haut = false, 
	                  bas = false;
	protected int valeurG = -1, valeurD = -1, valeurH = -1, valeurB = -1;
	
	//Constructeurs
	Pile(char c, int i, int j, int x, int y) {
		nb = 1;
		this.dessus = null;  //Le jeu commence toujours avec toute les pièces sans dessus.
		this.i = i;
		this.j = j;
		this.x = x;
		this.y = y;
		this.selected = false;
		this.dessous = creerPiece(c);		
	}
	
	//Pour cloner une Pile sans dessus.
	Pile(int i, int j, int x, int y, int nb, int pieceNo, String piece) {
		this.i = i;
		this.j = j;
		this.x = x;
		this.y = y;
		this.nb = nb;
		this.dessous = creerPiece(piece, pieceNo, i, j,false);
		this.selected = false;	
	}
	
	//Pour cloner une Piece avec un dessus.
	Pile(int i, int j, int x, int y, int nb, int pieceNo1, int pieceNo2,
			String piece1, String piece2) {
		this(i, j, x, y, nb, pieceNo1, piece1);
		this.dessus = creerPiece(piece2, pieceNo2, i, j,true);
	}
	
	//Permet de choisir le bon constructeur pour cloner une Pile.
	public static Pile newInstance (String[] s) {
		int longueur = s.length;
		int[] temp = new int[7];
		for(int i = 0; i < 7; i++) {
			temp[i] = Integer.parseInt(s[i]);
		}
		if(s[longueur-1].trim().equals("-")) {
			//La pile n'avait pas un dessus
			return new Pile(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], s[7]);
		} else {
			//La pile avait un dessus
			return new Pile(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6], 
					s[7], s[8]);
		}
	}
	
	public String toString() {
		String temp = ((nb <= 1)?"":("["+nb+"]"))+((dessus ==null)?"":(typeOfPiece(dessus)
				+""+dessus.no));
		return typeOfPiece(dessous)+""+dessous.no + temp;
	}
	
	//Créé la bonne Pièce, selon le caractère en paramètre.
	private Piece creerPiece(char c) {
		switch (c) {
			case 'B': return new Ciseaux(i, j);
			case 'R': return new Roche(i, j);
			case 'V':
			case 'G': return new Papier(i, j);
			default:  return null;		
		}
	}
	
	//Pour pouvoir annuler un mouvement, on créé une Piece avec toutes ses informations.
	private Piece creerPiece(String s, int no, int posI, int posJ, boolean dessus) {
		switch(s.charAt(0)) {
			case 'R': return new Roche(posI, posJ, no, dessus);
			case 'G': return new Papier(posI, posJ, no, dessus);
			case 'B': return new Ciseaux(posI, posJ, no, dessus);
			default:  return null;
		}
	}
		
	protected void drawPile(Graphics g) {
		int size = RochePapierCiseaux.SIZE; //Pour rendre le code plus lisible
		g.setColor(Color.black);
		g.drawRect(x+2, y+2, size-4, size-4);
		dessous.draw(g, 0);
		if(dessus != null)  {
			//On affiche alors le nombre de pièce dans la pile.
			dessus.draw(g, 0);
			g.drawString(String.format("%d", nb), x+2+(size-4)/2, y+17);
		}
		if(selected) {
			g.drawRect(x+3, y+3, size-6, size-6);
			g.drawRect(x+4, y+4, size-8, size-8);
			g.setColor(Color.magenta);
			if(gauche) {
				g.drawLine(x+2, y+2, x+2, y+size-2);
				g.drawLine(x+3, y+2, x+3, y+size-2);
				g.drawLine(x+4, y+2, x+4, y+size-2);
			}
			if(droite) {
				g.drawLine(x+size-2, y+2, x+size-2, y+size-2);
				g.drawLine(x+size-3, y+2, x+size-3, y+size-2);
				g.drawLine(x+size-4, y+2, x+size-4, y+size-2);
			}
			if(haut) {
				g.drawLine(x+2, y+2, x+size-2, y+2);
				g.drawLine(x+2, y+3, x+size-2, y+3);
				g.drawLine(x+2, y+4, x+size-2, y+4);
			}
			if(bas) {
				g.drawLine(x+2, y+size-2, x+size-2, y+size-2);
				g.drawLine(x+2, y+size-3, x+size-2, y+size-3);
				g.drawLine(x+2, y+size-4, x+size-2, y+size-4);
			}
		}
	}
	
	public void setSelected(boolean sel) {
		if (!sel) {
			gauche = droite = bas = haut = false;
			valeurD = valeurG = valeurH = valeurB = -1;
		}
		this.selected = sel;
	}
	
	//Vérifie si on peut empiler sur une autre pile et dans quelle direction
	//elle se trouve.
	protected void checkMove(Pile p, char c) {
		boolean temp;
		if(p.dessus == null) {
			temp = this.dessous.emporteSur(p.dessous);
		} else {
			temp = this.dessous.emporteSur(p.dessus);
		}
		
		if(temp) {
			switch (c) {
			case 'g': gauche = true;
					  break;
			case 'd': droite = true;
					  break;
			case 'h': haut = true;
			          break;
			default:  bas = true;
			          break;
			}
		}
	}
	
	//La pile autre est empilée sur cette Pile.
	//On garde donc la pièce dessus si elle existe, sinon on garde
	//dessous. On met à jour la position de la nouvelle pièce
	// et incrémente le nb de pièces dans la pile.
	protected void eat(Pile autre) {
		if(autre.dessus == null) {
			this.dessus = autre.dessous.newInstance();
			this.dessus.dessus = true;
		} else {
			this.dessus = autre.dessus.newInstance();
		}
		this.dessus.setPos(i, j);
		nb = this.nb + autre.nb;
	}
	
	//Pour le mouseListener
	protected boolean isInside(int xPos, int yPos) {
		return ((xPos >= x && xPos <= (x+RochePapierCiseaux.SIZE)) 
				&& (yPos >= y && yPos <= (y+RochePapierCiseaux.SIZE)));
	}

	//Retourne le type de pièce de p.
	protected char typeOfPiece(Piece p) {
		if(p instanceof Roche) {
			return 'R';
		}
		if(p instanceof Papier) {
			return 'G';
		}
		return 'B';
	}
	
	public int getPosI() {
		return i;
	}
	
	public int getPosJ() {
		return j;
	}
	
	//Information pour pouvoir annuler un mouvement.
	public String information() {
		char temp1;
		int temp2;
		if(dessus == null) {
			temp1 = '-';
			temp2 = -1;
		} else {
			temp1 = typeOfPiece(dessus);
			temp2 = dessus.no;
			
		}
		return String.format("%d,%d,%d,%d,%d,%d,%d,%c,%c", 
				i,j,x,y,nb,dessous.no,temp2, typeOfPiece(dessous),temp1);
	}
	
	protected boolean equals(Pile autre) {
		return (this.i == autre.i && this.j == autre.j);
	}
} //Fin pile