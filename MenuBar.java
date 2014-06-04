/* MenuBar.java
 * Par Guillaume Lahaie
 * 
 * Cette classe gère la barre de menu du jeu, avec les actions qui y sont
 * reliées.
 * 
 * Dernière modification: 5 novembre 2011.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class MenuBar extends JMenuBar implements ActionListener{

	private JMenu fichier, actions, configurations;
	private JMenuItem ouvrir, quitter, eatLeft, eatRight, eatUp, eatDown,
		              moveLeft, moveRight, moveUp, moveDown, recommencer;
	private JMenu[] configFolder;
	private JMenuItem[] config;
	private RochePapierCiseaux rpc;
	private boolean isConfig = false;
	private JMenuItem annuler;
	private JMenuItem solutionner;

	//Constructeur
	public MenuBar (RochePapierCiseaux rpc) {
		this.rpc = rpc;
		creerMenu();
	}
	
	//Initialise les menus,sauf le menu de choix de la configuration.
	private void creerMenu() {
		fichier = new JMenu("Fichier");
		actions = new JMenu("Actions");
		configurations = new JMenu("Configurations");  //On ne l'ajoute pas tout de suite au menu
		
		//Menu fichier
		ouvrir = new JMenuItem("Ouvrir");
		quitter = new JMenuItem("Quitter");
		quitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});				
		ouvrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rpc.lireFichier();
				try {
					rpc.creerMenuConfig();
					rpc.createBoard();
				} catch (NullPointerException npe) {
					rpc.updateTextArea("Problème lors de l'ouverture du fichier.");
				}
			}
		});
		recommencer = new JMenuItem("Recommencer cette configuration");
		recommencer.setMnemonic(KeyEvent.VK_R);
		recommencer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(rpc.config != null)
					rpc.createBoard();
				else
					rpc.updateTextArea("Ne peut recommencer, aucune config.");
			}
		});
		
		annuler = new JMenuItem("Annuler");
		annuler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rpc.plateau.removeFromAnnuler();
			}
		});
		annuler.setEnabled(false);
		
		solutionner = new JMenuItem("Solutionner cette config");
		solutionner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rpc.updateTextArea("Recherche d'une solution pour la configuration.");
				rpc.updateTextArea("Cela peut prendre quelques minutes.");
				rpc.repaint();
				try {
					String[] s = rpc.config.getSelectedConfig();
					Solutionneur temp = new Solutionneur(Solutionneur.creerJeu(s), rpc);
				} catch (NullPointerException npe) {
					rpc.updateTextArea("Ne peut solutionner, aucune config choisie.");
				}
				rpc.repaint();

			}
		});
		fichier.add(ouvrir);
		fichier.add(recommencer);
		fichier.add(annuler);
		fichier.add(solutionner);
		fichier.addSeparator();
		fichier.add(quitter);
		add(fichier);
		
		// Menu action
		eatLeft = new JMenuItem("Manger la pièce de gauche");
		eatLeft.setEnabled(false);
		eatLeft.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				rpc.plateau.eatPile('l');
			}
		});
		eatRight = new JMenuItem("Manger la pièce de droite");
		eatRight.setEnabled(false);
		eatRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				rpc.plateau.eatPile('r');
			}
		});
		eatUp = new JMenuItem("Manger la pièce du haut");
		eatUp.setEnabled(false);
		eatUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				rpc.plateau.eatPile('u');
			}
		});
		eatDown = new JMenuItem("Manger la pièce du bas");
		eatDown.setEnabled(false);
		eatDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				rpc.plateau.eatPile('d');
			}
		});
		actions.add(eatUp);
		actions.add(eatDown);
		actions.add(eatLeft);
		actions.add(eatRight);
		actions.addSeparator();
		moveUp = new JMenuItem("Bouger en haut");
		moveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rpc.plateau.movePile('u');
			}
		});
		moveDown = new JMenuItem("Bouger en bas");
		moveDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rpc.plateau.movePile('d');
			}
		});
		moveLeft = new JMenuItem("Bouger à gauche");
		moveLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rpc.plateau.movePile('l');
			}
		});
		moveRight = new JMenuItem("Bouger à droite");
		moveRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rpc.plateau.movePile('r');
			}
		});
		actions.add(moveUp);
		actions.add(moveDown);
		actions.add(moveLeft);
		actions.add(moveRight);
		add(actions);
	} //Fin créerMenu
	
	//Créé le menu configurations, au besoin. Si les configurations sont null ou
	//qu'il y en a juste une, on ne fait rien. On enlève avant de faire quoi que
	//ce soit les configurations présentes auparavant.
	protected void creerConfig(Configuration c) {
		if(isConfig) {
			configurations.removeAll();
		}
		int longueur;
		if(c == null) {
			longueur = 0;	
		} else {
			longueur = c.longueur();
		}

		int separateur = (longueur % 10 == 0)?(longueur/10):(longueur/10+1);
		if(longueur > 1) {
			config = new JMenuItem[longueur];
			configFolder = new JMenu[separateur];
			
			for(int i = 0; i < separateur; i++) {
				int bas = i*10;
				int haut = ((i*10 +9) > longueur)?(longueur-1):(i*10+9);
				String titre = "Configurations de " + c.getNum(bas) + " à " + c.getNum(haut);
				configFolder[i] = new JMenu(titre);
				for(int j = bas; j <= haut; j++) {
					String temp = String.format("Configuration " + c.getNum(j));
					config[j] = new JMenuItem(temp);
					config[j].addActionListener(this);
					configFolder[i].add(config[j]);
				}
				configurations.add(configFolder[i]);
				configurations.setEnabled(true);
				add(configurations);
				isConfig = true;
			}
		} else {
			if (isConfig) {
				configurations.setEnabled(false);
				remove(configurations);
				isConfig = false;
			}
		}
		revalidate();		
	}
	
	//Permet d'obtenir et de charger la configurations choisie.
	public void actionPerformed (ActionEvent e) {
		for(int i = 0; i < config.length; i++) {
			if(e.getSource().equals(config[i])) {
				rpc.config.setSelected(i);
			}
		}
		rpc.createBoard();
		rpc.requestFocus();
	}
	
	//Active/désactive les menus permettant d'empiler ou de bouger
	//selon les possibilités de la pile choisie.
	protected void actionMenu(Pile p) {
		eatLeft.setEnabled(p.gauche);
		eatRight.setEnabled(p.droite);
		eatUp.setEnabled(p.haut);
		eatDown.setEnabled(p.bas);
		moveLeft.setEnabled(p.valeurG != -1);
		moveRight.setEnabled(p.valeurD != -1);
		moveDown.setEnabled(p.valeurB != -1);
		moveUp.setEnabled(p.valeurH != -1);
	}
	
	//Active/désactive le menu annuler.
	protected void setAnnuler(boolean b) {
		annuler.setEnabled(b);
	}
} //Fin MenuBar
