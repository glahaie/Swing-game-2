/* Jeu.java
 * Par Guillaume Lahaie
 * 
 * Cette classe gère le plateau de jeu pour roche-papier-ciseau. Elle créée les
 * plateaux de jeu selon la configuration choisie, gère les mouvements entre les piles ainsi
 * que les différentes étapes de jeu (jeu gagné, perdu, annuler un mouvement, vérifier les
 * possibilités de mouvement, etc.
 * 
 * Dernière modification: 5 novembre 2011.
 */

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import javax.swing.JPanel;

class Jeu extends JPanel {
	
	private RochePapierCiseaux rpc;
	private ArrayList<Pile> board = new ArrayList<Pile>();
	private final Set<Integer> pressed = new HashSet<Integer>();
	private MenuBar barMenu;
	protected Stack<String> annuler;
	protected boolean problemeConfig = false;
	
	//Constructeurs	
	Jeu(String[] s, MenuBar b, RochePapierCiseaux rpc) {
		Ciseaux.noCiseaux = 1;
		Roche.noRoche = 1;
		Papier.noPapier = 1;
		annuler = new Stack<String>();
		this.rpc = rpc;
		this.barMenu = b;
		barMenu.setAnnuler(!annuler.isEmpty());
		setSize(4*RochePapierCiseaux.SIZE+25, 4*RochePapierCiseaux.SIZE+10);
		board = creerJeu(s);
		if(!problemeConfig)
			checkNeighbours();
	}
		
	Jeu(Configuration config, MenuBar b, RochePapierCiseaux rpc) {
		this(config.getSelectedConfig(), b, rpc);
	}
		
	public String toString() {
		return "Plateau de jeu contenant présentement " + board.size() + " piles de pièces.";
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(!problemeConfig) {
			for(Pile p:board) {
				p.drawPile(g);
			}
		}
	}
	
	//Crée le plateau de jeu représentant la configuration donnée.
	//On vérifie ici si la configuration est légale. Sinon, on affiche un
	//message à l'utilisateur.
	private ArrayList<Pile> creerJeu(String [] s) {
		ArrayList<Pile> temp = new ArrayList<Pile>();
		int x = 5;
		int y = 5;
		rpc.updateTextArea("Chargement de la Configuration numero: " 
				+ rpc.config.getNumChoisi());
		
		if(goodConfig(s)) {
			for(int i = 0; i < s.length; i++) {
				problemeConfig = false;
				//On lit la ligne et créer les piles
					for(int j = 0; j < s[i].length(); j++) {
						if (s[i].charAt(j) == '-') {
							//On ne fait rien
						} else {
							temp.add(new Pile(s[i].charAt(j),i, j, x, y));
						}
						x = x + RochePapierCiseaux.SIZE;
					}
					x = 5;
					y = y + RochePapierCiseaux.SIZE;
				}
				// On choisit une pile qui sera pour commencer le jeu.
				temp.get(0).setSelected(true);
				return temp;			
		} else {
			problemeConfig = true;
			rpc.updateTextArea("Il y a un problème avec cette configuration.");
			rpc.updateTextArea("Veuillez en choisir une autre.");
			return null;
		}
	} //Fin creerJeu
		
	//On vérifie les voisins de la pile choisie, s'ils existent, et on vérifie aussi
	//s'il est possible d'empiler dessus.
	private void checkNeighbours() {
		Pile temp = getSelected();
		
		int k = temp.getPosI();
		int l = temp.getPosJ();
		int tempPosG = -1;
		int tempPosD = 5;
		int tempPosH = -1;
		int tempPosB = 5;
		for(Pile p:board) {
			//Gauche.
			if(p.getPosI() == temp.getPosI() && p.getPosJ() < temp.getPosJ()) {
				if(tempPosG < p.getPosJ()) {
					tempPosG = p.getPosJ();
					temp.valeurG = board.indexOf(p);
				}
			}
			//Droite.
			if(p.getPosI() == temp.getPosI() && p.getPosJ() > temp.getPosJ()) {
				if(tempPosD > p.getPosJ()) {
					tempPosD = p.getPosJ();
					temp.valeurD = board.indexOf(p);
				}
			}
			//Haut
			if(p.getPosJ() == temp.getPosJ() && p.getPosI() < temp.getPosI()) {
				if(tempPosH < p.getPosI()) {
					tempPosH = p.getPosI();
					temp.valeurH = board.indexOf(p);
				}
			}
			//Bas
			if(p.getPosJ() == temp.getPosJ() && p.getPosI() > temp.getPosI()) {
				if(tempPosB > p.getPosI()) {
					tempPosB = p.getPosI();
					temp.valeurB = board.indexOf(p);
				}
			}	
		}
		if(tempPosG > -1) {
			temp.checkMove(getPile(k, tempPosG), 'g');
		} else {
			temp.gauche = false;
			temp.valeurG = -1;
		}
		if(tempPosD < 5) {
			temp.checkMove(getPile(k, tempPosD), 'd');
		} else {
			temp.droite = false;
			temp.valeurD = -1;
		}		
		if(tempPosH > -1) {
			temp.checkMove(getPile(tempPosH, l), 'h');
		} else {
			temp.haut = false;
			temp.valeurH = -1;
		}		
		if(tempPosB < 5) {
			temp.checkMove(getPile(tempPosB, l), 'b');
		} else {
			temp.bas = false;
			temp.valeurB = -1;
		}
		barMenu.actionMenu(temp);
	} //Fin checkNeighbours

	//Appelé par le KeyListener. On entre les touches pressés dans un set, on vérifie ensuite
	// si la combinaison des touches dans le set permet une action.
	
	protected void doKeyP (KeyEvent e) {		
		pressed.add(e.getKeyCode());

		if(pressed.contains(KeyEvent.VK_SHIFT)) {
			if (pressed.contains(KeyEvent.VK_UP) && getSelected().haut) {
				eatPile('u');
			} else if(pressed.contains(KeyEvent.VK_DOWN) && getSelected().bas) {
				eatPile('d');
			} else if (pressed.contains(KeyEvent.VK_LEFT) && getSelected().gauche) {
				eatPile('l');
			} else if (pressed.contains(KeyEvent.VK_RIGHT) && getSelected().droite) {
				eatPile('r');
			}
		} else if (pressed.contains(KeyEvent.VK_ALT) && pressed.contains(KeyEvent.VK_R)){
			rpc.createBoard();
			rpc.repaint();
		} else {
			if (pressed.contains(KeyEvent.VK_DOWN)) {
				movePile('d');
			} else if (pressed.contains(KeyEvent.VK_UP)){
				movePile('u');
			} else if (pressed.contains(KeyEvent.VK_RIGHT)) {
				movePile('r');
			} else if (pressed.contains(KeyEvent.VK_LEFT)) {
				movePile('l');
			}
		}
		repaint();
	}

	//Appelé par le listener lorsqu'on ne presse plus une touche. Enlève
	//la touche du set.
	protected void doKeyR (KeyEvent e) {
		pressed.remove(e.getKeyCode());
	}

	//Appelé par le MouseListener lors d'un click. Si on clique dans une pile,
	//on choisit cette pile et on met à jour le plateau.
	protected void mouseClick(MouseEvent e) {
		rpc.requestFocus();
		int x = e.getX();
		int y = e.getY();
		for(Pile p:board) {
			if(p.isInside(x, y)) {
				if(!p.equals(getSelected())) {
					getSelected().setSelected(false);
					p.setSelected(true);
					checkNeighbours();
				}
			}
		}
		repaint();
	}
	
	//On déplace l'attention vers la pile demandée. On met à jour le plateau ensuite.
	//Si le mouvement n'est pas possible, on ne fait rien.
	protected void movePile(char c) {
		int indexSel = indexOfMove(c);
		if(indexSel != -1) {
			getSelected().setSelected(false);
			board.get(indexSel).setSelected(true);
			checkNeighbours();
		}
		repaint();
	} //fin movePile
	
	//On empile la pile choisie sur la pile voisine demandée. Ce mouvement peut ne pas être
	//possible, à ce moment on ne fait rien. On met aussi dans un stack des informations
	//pour pouvoir annuler le mouvement. Ensuite on fait une mise à jour du plateau de jeu.
	//On affiche aussi à l'écran une trace du mouvement effectué.
	protected void eatPile(char c) {
		int indexSel = indexOfMove(c);
		int aEnlever = board.indexOf(getSelected());	

		if(indexSel != -1) {
			Pile temp = getSelected();
			rpc.updateTextArea("{"+getSelected()+fleche(c)+"}");
			addToAnnuler(temp.information(), board.get(indexSel).information());
			board.get(indexSel).eat(temp);
			board.get(indexSel).setSelected(true);
			board.remove(aEnlever);
			checkNeighbours();
			if(board.size() == 1) {
				rpc.updateTextArea("Bravo, vous avez gagné!");
			} else if (noMoreMoves()) {
				rpc.updateTextArea("Plus de mouvements possibles, vous avez perdu. :(\n");
			}
		}
		barMenu.setAnnuler(!annuler.isEmpty());
	}
	
	//Retourne l'index dans l'ArrayList du voisin demandé.
	private int indexOfMove(char c) {
		switch(c) {
		case 'u': 	return getSelected().valeurH;				   
		case 'd': 	return getSelected().valeurB;		   		  
		case 'l':   return getSelected().valeurG;				  
		case 'r': 	return getSelected().valeurD;				  
		default:    return -1;
		}
	}
	
	//Retourne la pile choisie, s'il y en a une.
	private Pile getSelected() {
		for(Pile p:board) {
			if(p.selected)
				return p;
		}
		return null;
	}
	
	//Retourne la valeur utf-8 des flèches, pour la trace.
	private String fleche(char c) {
		switch (c) {
		case 'u': return "\u2191";
		case 'd': return "\u2193";
		case 'l': return "\u2190";
		case 'r': return "\u2192";
		default: return "";
		}
	}
	
	//Retourne la pile à la position i, j dans le tableau.
	private Pile getPile(int i, int j) {
		for(Pile p:board) {
			if(p.getPosI() == i && p.getPosJ() == j)
				return p;
		}
		return null;
	}
	
	//Vérifie s'il reste des possibilités de mouvement dans le tableau.
	private boolean noMoreMoves() {
		 int indexOfSel = board.indexOf(getSelected());
		 board.get(indexOfSel).setSelected(false);
		 for(Pile p:board) {
			 p.setSelected(true);
			 checkNeighbours();
			 if(p.gauche || p.droite || p.haut || p.bas) {
				 p.setSelected(false);
				 board.get(indexOfSel).setSelected(true);
				 checkNeighbours();
				 return false;
			 }
			 p.setSelected(false);
		 }
		 board.get(indexOfSel).setSelected(true);
		 checkNeighbours();
		 return true;
	}
	
	//On ajoute les informations du mouvement effectué pour pouvoir l'annuler.
	private void addToAnnuler(String s1, String s2) {
		annuler.push(s1 + "." +s2);
	}
	
	//On annule le dernier mouvement fait. On recrée le plateau tel qu'il était auparavant.
	protected void removeFromAnnuler() {
		String[] temp = annuler.pop().split("\\.");
		board.add(Pile.newInstance(temp[0].split(",")));
		Pile aRemplacer = Pile.newInstance(temp[1].split(","));
		int posARemplacer = board.indexOf(getPile(aRemplacer.getPosI(), aRemplacer.getPosJ()));
		board.remove(posARemplacer);
		board.add(aRemplacer);
		for(Pile p:board) {
			p.setSelected(false);
		}
		aRemplacer.setSelected(true);
		checkNeighbours();
		barMenu.setAnnuler(!annuler.isEmpty());
		rpc.updateTextArea("Annulation du dernier mouvement.");
		repaint();
	}
	
	//Vérifie si la configuration est valide. Si elle comprend des caractères
	//différents de -,R, G, B, ou V, on retourne false.
	private boolean goodConfig(String[] s) {
		for(int i = 0; i < s.length; i++) {
			for(int j = 0; j < s[i].length(); j++) {
				char tempC = s[i].charAt(j);
				if(tempC != '-' && tempC != 'B' && tempC != 'G' && tempC != 'R' && tempC != 'V')
					return false;
			}
		}
		return true;
	}
	
} //Fin jeu