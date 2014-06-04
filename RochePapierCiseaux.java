/* RochePapierCiseaux.java
 * Par Guillaume Lahaie
 * 
 * Cette classe gère surtout les aspects graphiques autres que le plateau de jeu
 * lui-même.
 * 
 * Dernière modification: 5 novembre 2011.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class RochePapierCiseaux extends JPanel {
	
	static protected int SIZE = 100;
	
	private JFrame frame;
	protected JTextArea textArea;
	protected Jeu plateau;
	protected Configuration config;
	protected MenuBar barMenu;
	private JScrollPane scrollPane;
		
	//Constructeur	
	RochePapierCiseaux(String fichier) {
		setLayout(new BorderLayout());
		//Création du jeu avec une configuration par défault 
		creerObjets();
		try {
			FileReader fr = new FileReader(fichier);
			ouvrirFichier(fr);
		} catch (FileNotFoundException e) {
			updateTextArea("Problème lors de l'ouverture du fichier.");
			config = null;
		}
		if (config != null) {
			creerMenuConfig();
			createBoard();
		}
		if (frame != null)
			frame.repaint();
	} //Fin constructeur
	
	public String toString() {
		return String.format("à écrire");
	}

	//Créé les objets sauf le plateau de jeu: la barre de menu et le JTextArea
	//Créé aussi le MouseListener et le KeyListener.
	private void creerObjets() {
		//Creer les elements de l'interface graphique, l'espace de jeu
		//l'espace de texte, les menus
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(!plateau.problemeConfig)
					plateau.mouseClick(e);
			}
		});
		addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(!plateau.problemeConfig)
					plateau.doKeyP(e);
			}
			public void keyReleased(KeyEvent e) {
				if(!plateau.problemeConfig)
					plateau.doKeyR(e);
			}
			public void keyTyped(KeyEvent e) {
			}
		});
		this.setFocusable(true);
		this.requestFocus();
		barMenu = new MenuBar(this);		
		textArea = new JTextArea(7,10);
		textArea.setEditable(false);
		textArea.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(!plateau.problemeConfig)
					plateau.doKeyP(e);
			}
			public void keyReleased(KeyEvent e) {
				if(!plateau.problemeConfig)
					plateau.doKeyR(e);
			}
			public void keyTyped(KeyEvent e) {
			}
		});
		scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.SOUTH);
	}
	
	//Créé le plateau de jeu avec les piles de pièces. S'assure d'enlever
	//le plateau existant avant.
	public void createBoard() {
		Ciseaux.noCiseaux = 1;
		Roche.noRoche = 1;
		Papier.noPapier = 1;
		if(plateau !=null) {
			remove(plateau);
		}
		try {
			plateau = new Jeu(config, barMenu, this);
			add(plateau, BorderLayout.CENTER);
		} catch (IndexOutOfBoundsException e) {
			//On a un problème avec la configuration
			updateTextArea("Problème avec les configurations.");
			updateTextArea("Veuillez essayer un autre fichier.");
		}
		repaint();
	}
	
	//Méthode utilisée pour permettre d'ouvrir un nouveau fichier
	//de configurations durant le déroulement du programme.
	protected void lireFichier() {
		JFileChooser chooser = new JFileChooser();
		FileReader f = null;
		try {
			if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				f = new FileReader(chooser.getSelectedFile());
				ouvrirFichier(f);
			}
		} catch (FileNotFoundException e) {
			updateTextArea("Impossible d'ouvrir le fichier.");
		} 

	}
	
	//Lit et entre les configurations dans l'objet. Si on rencontre un problème,
	//la configuration sera null.
	private void ouvrirFichier (FileReader fr) {
		try {
			config = new Configuration(fr);
		} catch (IOException ioe) {
			updateTextArea("Problème avec la lecture du fichier config.");
			config=null;
		} catch(NullPointerException npe) {
			updateTextArea("Problème à l'intérieur du fichier config.");
			config = null;
		}
	}

	public void creerMenuConfig() {	
		barMenu.creerConfig(config);
		repaint();
	}
	
	//Ajoute le string à textArea, s'assure ensuite que le scrollpane est au plus
	//bas possible.
	public void updateTextArea(String s) {
		textArea.append(s + "\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	
	public static void main(String[] args) {
		RochePapierCiseaux rpc;
		//Si l'utilisateur n'entre pas le fichier en argument,
		//on continue tout de même le déroulement mais on donne une
		//erreur dans textArea.
		try {
			rpc = new RochePapierCiseaux(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			rpc = new RochePapierCiseaux("foo");
		}
		rpc.frame = new JFrame("Roche Papier Ciseau");
		rpc.frame.setSize(4*SIZE+20, 4*SIZE + 170);
		rpc.frame.setJMenuBar(rpc.barMenu);
		rpc.frame.getContentPane().add(rpc);
		rpc.frame.setVisible(true);
		rpc.frame.setLocation(100, 100);
		rpc.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
