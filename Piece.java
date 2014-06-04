import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Piece {
	protected static BufferedImage rocheImg,papierImg,ciseauxImg ;
	static {
		AffineTransform tx = new AffineTransform();
		tx.scale(0.5, 0.5);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		try {
		    rocheImg=op.filter(ImageIO.read(new File("images/Roche.jpg")),null);
		    papierImg=op.filter(ImageIO.read(new File("images/Papier.jpg")),null);
		    ciseauxImg=op.filter(ImageIO.read(new File("images/Ciseaux.jpg")),null);
		} catch (IOException e) {
			System.err.println("Erreur en lisant les images:"+e);
		}		
	}
	protected int i,j,no;
	protected boolean dessus;
	
	
	Piece(int i, int j){
		this.i=i;
		this.j=j;
		setDessus(false);
	}
	
	// constructeur permettant de cloner la piece
	Piece(int i,int j, int no, boolean dessus){
		this.i=i;
		this.j=j;
		this.no=no;
		this.dessus=dessus;
	}
	
	public abstract Piece newInstance();
	public abstract boolean emporteSur(Piece m);
	public abstract BufferedImage getImage();
	
	public String toString(){
		return i+"@"+j; 
	}
	
	@Override public boolean equals(Object that){
		if(that==null)return false;
		if(!(that instanceof Piece)) return false;
		Piece thatP = (Piece)that;
		if(i!=thatP.i)return false;
		if(j!=thatP.j)return false;
		if(no!=thatP.no)return false;
		
		return true;
	}
	
	public void draw(Graphics g,int nb) {
		BufferedImage img = getImage();
		int x = j*RochePapierCiseaux.SIZE;
		int dx = dessus ? (RochePapierCiseaux.SIZE/2-10) : 15;
		int y = i*RochePapierCiseaux.SIZE;
		int dy=(RochePapierCiseaux.SIZE-img.getHeight())/2;
		g.drawImage(img,x+dx,y+dy,null);
		g.setColor(Color.black);
		g.drawRect(x+dx, y+dy, img.getWidth()-1, img.getHeight()-1);
		g.drawString(""+no, x+dx+img.getWidth()/2-2, y+dy+img.getHeight()+12);
	}
	
	public void setDessus(boolean dessus){
		this.dessus=dessus;
	}
	
	//Seulement pour test, à enlever
	public void setPos(int i, int j) {
		this.i = i;
		this.j = j;
	}
}
