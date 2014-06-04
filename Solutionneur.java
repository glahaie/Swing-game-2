/* Solutionneur.java
 * Par Guillaume Lahaie
 * 
 * Cette classe sert à trouver une solution à la configuration qui lui est donnée.
 * Elle n'est pas très efficace(elle trouve la solution, mais elle prend beaucoup de temps et 
 * d'ArrayList différentes!). Elle réutilise surtout les méthodes de Jeu, mais modifié un peu.
 * Seulement les méthodes propres à Solutionneur seront commentées. Elle affiche à l'écran
 * une trace des mouvements à faire pour obtenir seulement une pile. Elle cherche par la force
 * brute, elle essaie tous les mouvements possibles, le seul cas traité ici est lorsqu'une pile
 * n'a pas de voisins (Aucunes piles dans la même ligne et aucune pile dans la même colonne).
 * Dans ce cas, aucune solution est possible, donc on passe à une autre rapidement.
 * Elle bloque l'interface graphique pendant qu'elle fait ses essais.
 * 
 * Dernière modification: 5 novembre 2011.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class Solutionneur {
	
	private ArrayList<Pile> copie;
	private LinkedList<String> mouvements;
	private RochePapierCiseaux rpc;
	private boolean solution;
	
	//Constructeur
	public Solutionneur (ArrayList<Pile> jeu, RochePapierCiseaux rpc) {
		this.rpc = rpc;
		this.copie = copieDe(jeu);
		this.mouvements = new LinkedList<String>();
		trouverSolution(copie);
		this.solution = false;
		afficherSolution();
	}
	
	//Cette méthode fait le gros du travail pour trouver une solution.
	//Si il y a seulement une pile dans l'ArrayList, on a trouvé une 
	//solution. Sinon, on vérifie si une Pile n'a pas de voisins. Si oui,
	//on ne fait rien (on ne trouvera pas de solutions). Sinon, on essai un mouvement
	//possible, et on rappelle trouverSolution sur l'ArrayList ayant une pile de moins.
	private void trouverSolution(ArrayList<Pile> jeu) {
		boolean sansVoisin = false;
		
		if(jeu.size() == 1) {
			solution = true; // On a trouvé la solution, on ne fait plus rien
		} else {
			//Vérifier si une pile n'a pas de voisins. Si oui, on ne peut gagné
			//alors on ne vérifie rien.
			for(Pile p: jeu) {
				p.setSelected(true);
				checkNeighbours(jeu);
				if(pasDeVoisins(p)) {
					p.setSelected(false);
					sansVoisin = true;
					break;
				}
				p.setSelected(false);
			}
		}
		if(!sansVoisin) {
			ArrayList<Pile> temp = new ArrayList<Pile>();
			if(!solution) {
				for(Pile p:jeu) {
					p.setSelected(true);
					checkNeighbours(jeu);
					if(!solution && p.gauche){
						temp = copieDe(jeu);
						for(Pile t:temp) {
							if(t.equals(p)) {
								t.setSelected(true);
								checkNeighbours(temp);
							}
						}
						eatPile('l',temp);
						trouverSolution(temp);
						if(solution)
							break;
						else
							mouvements.removeLast();
					}
					if(!solution && p.droite) {
						temp = copieDe(jeu);
						for(Pile t:temp) {
							if(t.equals(p)) {
								t.setSelected(true);
								checkNeighbours(temp);
							}
						}
						eatPile('r', temp);
						trouverSolution(temp);
						if(solution)
							break;
						else
							mouvements.removeLast();
					}
					if(!solution && p.bas) {
						temp = copieDe(jeu);
						for(Pile t:temp) {
							if(t.equals(p)) {
								t.setSelected(true);
								checkNeighbours(temp);
							}
						}
						eatPile('d', temp);
						trouverSolution(temp);
						if(solution)
							break;
						else
							mouvements.removeLast();
					}
					if(!solution && p.haut){
						temp = copieDe(jeu);
						for(Pile t:temp) {
							if(t.equals(p)) {
								t.setSelected(true);
								checkNeighbours(temp);
							}
						}
						eatPile('u', temp);
						trouverSolution(temp);
						if(solution)
							break;
						else
							mouvements.removeLast();
					}
					p.setSelected(false);
				}
			}
		}
	}
	
	//Vérifie si la pile a au moins un voisin dans une direction.
	private boolean pasDeVoisins(Pile p) {
		return (p.valeurB == -1)  && (p.valeurD == -1) && (p.valeurH == -1) && (p.valeurG == -1);
	}
	
	//Retourne un copie de l'ArrayList, pas seulement une référence.
	private ArrayList<Pile> copieDe (ArrayList<Pile> jeu) {
		Pile tempP;
		ArrayList<Pile> temp = new ArrayList<Pile>();
		for(Pile p:jeu) {
			tempP = Pile.newInstance(p.information().split(","));
			tempP.setSelected(false);
			temp.add(tempP);			
		}
		return temp;
	}

	private void afficherSolution() {
		if(mouvements.isEmpty()) {
			rpc.updateTextArea("Aucune solution trouvée.");
		} else {
			rpc.updateTextArea("Voici la trace d'une solution de cette configuration:");
			for(int i = 0; i < mouvements.size(); i++) {
				rpc.updateTextArea(String.format("%d) %s", i+1, mouvements.get(i)));
			}
		}
	}
	
	//Méthodes de jeu
	
	public void eatPile(char c, ArrayList<Pile> jeu) {
		int indexSel = -1;
		int aEnlever = jeu.indexOf(getSelected(jeu));	
		switch(c) {
		case 'u': 	indexSel = getSelected(jeu).valeurH;
					break;					   
		case 'd': 	indexSel = getSelected(jeu).valeurB;
		   		    break;		   		  
		case 'l':   indexSel = getSelected(jeu).valeurG;
				  	break;					  
		case 'r': 	indexSel = getSelected(jeu).valeurD;
					break;					  
		default:    break;
		}
		if(indexSel != -1) {
			Pile temp = getSelected(jeu);
			mouvements.add("{"+getSelected(jeu)+fleche(c)+"}");
			jeu.get(indexSel).eat(temp);
			jeu.remove(aEnlever);
		}
	}
	
	private Pile getSelected(ArrayList<Pile> jeu) {
		for(Pile p:jeu) {
			if(p.selected)
				return p;
		}
		return null;
	}
	
	public void checkNeighbours(ArrayList<Pile> jeu) {
		Pile temp = getSelected(jeu);
		
		int k = temp.getPosI();
		int l = temp.getPosJ();
		
		//gauche
		int tempPos = -1;
		for(Pile p:jeu) {
			if(p.getPosI() == temp.getPosI() && p.getPosJ() < temp.getPosJ()) {
				if(tempPos < p.getPosJ()) {
					tempPos = p.getPosJ();
					temp.valeurG = jeu.indexOf(p);
				}
			}			
		}
		if(tempPos > -1) {
			temp.checkMove(getPile(k, tempPos, jeu), 'g');
		} else {
			temp.gauche = false;
			temp.valeurG = -1;
		}
		
		//droite
		tempPos = 5;
		for(Pile p:jeu) {
			if(p.getPosI() == temp.getPosI() && p.getPosJ() > temp.getPosJ()) {
				if(tempPos > p.getPosJ()) {
					tempPos = p.getPosJ();
					temp.valeurD = jeu.indexOf(p);
				}
			}			
		}
		if(tempPos < 5) {
			temp.checkMove(getPile(k, tempPos, jeu), 'd');
		} else {
			temp.droite = false;
			temp.valeurD = -1;
		}
		
		//haut
		tempPos = -1;
		for(Pile p:jeu) {
			if(p.getPosJ() == temp.getPosJ() && p.getPosI() < temp.getPosI()) {
				if(tempPos < p.getPosI()) {
					tempPos = p.getPosI();
					temp.valeurH = jeu.indexOf(p);
				}
			}			
		}
		if(tempPos > -1) {
			temp.checkMove(getPile(tempPos, l, jeu), 'h');
		} else {
			temp.haut = false;
			temp.valeurH = -1;
		}
		
		//bas
		tempPos = 5;
		for(Pile p:jeu) {
			if(p.getPosJ() == temp.getPosJ() && p.getPosI() > temp.getPosI()) {
				if(tempPos > p.getPosI()) {
					tempPos = p.getPosI();
					temp.valeurB = jeu.indexOf(p);
				}
			}			
		}
		if(tempPos < 5) {
			temp.checkMove(getPile(tempPos, l, jeu), 'b');
		} else {
			temp.bas = false;
			temp.valeurB = -1;
		}
	}
	
	public Pile getPile(int i, int j, ArrayList<Pile> jeu) {
		for(Pile p:jeu) {
			if(p.getPosI() == i && p.getPosJ() == j)
				return p;
		}
		return null;
	}
	public String fleche(char c) {
		switch (c) {
		case 'u': return "\u2191";
		case 'd': return "\u2193";
		case 'l': return "\u2190";
		case 'r': return "\u2192";
		default: return "";
		}
	}
	
	static ArrayList<Pile> creerJeu(String [] s) {
		Ciseaux.noCiseaux = 1;
		Papier.noPapier = 1;
		Roche.noRoche = 1;
		ArrayList<Pile> temp = new ArrayList<Pile>();
		int x = 0;
		int y = 0;
			
		for(int i = 0; i < s.length; i++) {
		//On lit la ligne et créer les piles
			for(int j = 0; j < s[i].length(); j++) {
				if (s[i].charAt(j) == '-') {
					//On ne fait rien
				} else {
					temp.add(createPile(s[i].charAt(j),i, j, x, y));
				}
				x = x + RochePapierCiseaux.SIZE;
			}
			x = 0;
			y = y + RochePapierCiseaux.SIZE;
		}
		return temp;
	} //Fin creerJeu
	
	static Pile createPile(char c, int i, int j, int x, int y) {
		return new Pile(c, i, j, x, y);
	}
	
} //Fin Solutionneur
