package fr.istic.prg1.tree;

import fr.istic.prg1.tree_util.Iterator;
import java.util.Scanner;

import fr.istic.prg1.tree_util.AbstractImage;
import fr.istic.prg1.tree_util.Node;

/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 5.0
 * @since 2023-09-23
 * 
 *        Classe décrivant les images en noir et blanc de 256 sur 256 pixels
 *        sous forme d'arbres binaires.
 * 
 */

public class Image extends AbstractImage {
	private static final Scanner standardInput = new Scanner(System.in);

	public Image() {
		super();
	}

	public static void closeAll() {
		standardInput.close();
	}

	/**
	 * this devient identique à image2.
	 *
	 * @param image image à copier
	 *
	 * pre !image2.isEmpty()
	 */
	@Override
	public void affect(AbstractImage image) {
		Iterator<Node> thisIt = this.iterator();
		thisIt.clear();

		affectIt(thisIt,image.iterator());
	}
	private void affectIt(Iterator<Node> it1, Iterator<Node> it2){
		int value = it2.getValue().state;
		it1.addValue(Node.valueOf(value));

		if(value == 2){
			it1.goLeft();
			it2.goLeft();
			affectIt(it1,it2);
			it1.goUp();
			it2.goUp();

			it1.goRight();
			it2.goRight();
			affectIt(it1,it2);
			it1.goUp();
			it2.goUp();
		}
	}

	/**
	 * this devient rotation de image2 à 180 degrés.
	 *
	 * @param image image pour rotation
	 * pre !image2.isEmpty()
	 */
	@Override
	public void rotate180(AbstractImage image) {
		Iterator<Node> thisIt = this.iterator();
		thisIt.clear();

		rotate180It(thisIt,image.iterator());
	}
	private void rotate180It(Iterator<Node> it1, Iterator<Node> it2){
		int value = it2.getValue().state;
		it1.addValue(Node.valueOf(value));

		if(value == 2){
			it1.goRight();
			it2.goLeft();
			rotate180It(it1,it2);
			it1.goUp();
			it2.goUp();

			it1.goLeft();
			it2.goRight();
			rotate180It(it1,it2);
			it1.goUp();
			it2.goUp();
		}
	}
	/**
	 * this devient inverse vidéo de this, pixel par pixel.
	 *
	 * pre !image.isEmpty()
	 */
	@Override
	public void videoInverse() {
		videoInverseIt(this.iterator());
	}
	private void videoInverseIt(Iterator<Node> it){
		int value = it.getValue().state;
		if(value == 2){
			it.goRight();
			videoInverseIt(it);
			it.goUp();
			it.goLeft();
			videoInverseIt(it);
			it.goUp();
		}else
			it.setValue(Node.valueOf(1-value));
	}

	/**
	 * this devient image miroir verticale de image2.
	 *
	 * @param image image à agrandir
	 * pre !image2.isEmpty()
	 */
	@Override
	public void mirrorV(AbstractImage image) {
		Iterator<Node> thisIt = this.iterator();
		thisIt.clear();

		mirrorIt(thisIt,image.iterator(),false);
	}
	/**
	 * this devient image miroir horizontale de image2.
	 *
	 * @param image image à agrandir
	 * pre !image2.isEmpty()
	 */
	@Override
	public void mirrorH(AbstractImage image) {
		Iterator<Node> thisIt = this.iterator();
		thisIt.clear();

		mirrorIt(thisIt,image.iterator(),true);
	}
	/**
	 * it0 devient image miroir horizontale ou verticale de it1.
	 *
	 * @param it0 image receptrice
	 * @param it1 image qui va être copier
	 * @param parity : Verticale si false, Horizontale si vrai
	 * pre it0 est vide
	 */
	private void mirrorIt(Iterator<Node> it0, Iterator<Node> it1, boolean parity){
		int value = it1.getValue().state;
		it0.addValue(Node.valueOf(value));
		if(value == 2) {
			if(parity){
				it0.goLeft();
				it1.goLeft();
				mirrorIt(it0,it1,false);
				it0.goUp();
				it1.goUp();

				it0.goRight();
				it1.goRight();
				mirrorIt(it0,it1,false);
				it0.goUp();
				it1.goUp();
			}else{
				it0.goLeft();
				it1.goRight();
				mirrorIt(it0,it1,true);
				it0.goUp();
				it1.goUp();

				it0.goRight();
				it1.goLeft();
				mirrorIt(it0,it1,true);
				it0.goUp();
				it1.goUp();
			}
		}
	}
	/**
	 * this devient quart supérieur gauche de image2.
	 *
	 * @param image2 image à agrandir
	 * 
	 * pre !image2.isEmpty()
	 */
	@Override
	public void zoomIn(AbstractImage image2) {
		Iterator<Node> it1 = this.iterator();
		Iterator<Node> it2 = image2.iterator();

		it1.clear();
		int value = it2.getValue().state;

		if(value == 2){
			it2.goLeft();
			value = it2.getValue().state;
			if(value == 2){
				it2.goLeft();
				affectIt(it1,it2);
			}
			else it1.addValue(Node.valueOf(value));
		}
		else it1.addValue(Node.valueOf(value));
	}

	/**
	 * Le quart supérieur gauche de this devient image2, le reste de this devient
	 * éteint.
	 * 
	 * @param image2 image à réduire
	 * pre !image2.isEmpty()
	 */
	@Override
	public void zoomOut(AbstractImage image2) {
		Iterator<Node> it1 = this.iterator();
		Iterator<Node> it2 = image2.iterator();

		it1.clear();
		if(it2.getValue().state == 0)
			it1.addValue(Node.valueOf(0));
		else {
			it1.addValue(Node.valueOf(2));

			it1.goRight();
			it1.addValue(Node.valueOf(0));
			it1.goUp();

			it1.goLeft();
			it1.addValue(Node.valueOf(2));

			it1.goRight();
			it1.addValue(Node.valueOf(0));
			it1.goUp();

			it1.goLeft();
			zoomOutIt(2,it1, it2);
			if(it1.getValue().state == 0){
				it1.goRoot();
				it1.clear();
				it1.addValue(Node.valueOf(0));
			}
		}
	}
	private void zoomOutIt(int deep, Iterator<Node> it1, Iterator<Node> it2){
		if(deep < 16) {
			int value = it2.getValue().state;
			it1.addValue(Node.valueOf(value));

			if (value == 2) {
				it1.goLeft();
				it2.goLeft();
				zoomOutIt(deep + 1, it1, it2);
				var thisLeft = it1.getValue().state;
				it1.goUp();
				it2.goUp();

				it1.goRight();
				it2.goRight();
				zoomOutIt(deep + 1, it1, it2);
				var thisRight = it1.getValue().state;
				it1.goUp();
				it2.goUp();

				if(thisLeft != 2 && thisRight == thisLeft){
					it1.setValue(Node.valueOf(thisRight));

					it1.goRight();
					it1.remove();
					it1.goUp();

					it1.goLeft();
					it1.remove();
					it1.goUp();
				}
			}
		}
		else {
			if(count0(it2) > count1(it2))
				it1.addValue(Node.valueOf(0));
			else
				it1.addValue(Node.valueOf(1));
		}
	}
	private int count0(Iterator<Node> it){
		int value = it.getValue().state;
		if(value == 2){
			it.goLeft();
			int leftCount = count0(it);
			it.goUp();
			it.goRight();
			int rightCount = count0(it);
			it.goUp();
			return leftCount + rightCount;
		}
		else{
			if(value == 0)return 1;
			else return 0;
		}
	}
	private int count1(Iterator<Node> it){
		int value = it.getValue().state;
		if(value == 2){
			it.goLeft();
			int leftCount = count1(it);
			it.goUp();
			it.goRight();
			int rightCount = count1(it);
			it.goUp();
			return leftCount + rightCount;
		}
		else{
			if(value == 1)return 1;
			else return 0;
		}
	}
	/**
	 * this devient l'intersection de image1 et image2 au sens des pixels allumés.
	 * 
	 * pre !image1.isEmpty() && !image2.isEmpty()
	 * 
	 * @param image1 premiere image
	 * @param image2 seconde image
	 */
	@Override
	public void intersection(AbstractImage image1, AbstractImage image2) {
		Iterator<Node> itThis = this.iterator();
		Iterator<Node> it1 = image1.iterator();
		Iterator<Node> it2 = image2.iterator();

		itThis.clear();
		intersectionIt(itThis,it1,it2);
	}
	private void intersectionIt(Iterator<Node> itThis,Iterator<Node> it1,Iterator<Node> it2){
		int value1 = it1.getValue().state;
		int value2 = it2.getValue().state;

		if(value1 == 0 || value2 == 0)
			itThis.addValue(Node.valueOf(0));
		else if(value1 == 1 && value2 == 1)
			itThis.addValue(Node.valueOf(1));
		else{
			itThis.addValue(Node.valueOf(2));

			itThis.goLeft();
			if(value1 == 2)it1.goLeft();
			if(value2 == 2)it2.goLeft();
			intersectionIt(itThis,it1,it2);
			int thisLeft = itThis.getValue().state;
			itThis.goUp();
			if(value1 == 2)it1.goUp();
			if(value2 == 2)it2.goUp();

			itThis.goRight();
			if(value1 == 2)it1.goRight();
			if(value2 == 2)it2.goRight();
			intersectionIt(itThis,it1,it2);
			int thisRight = itThis.getValue().state;
			itThis.goUp();
			if(value1 == 2)it1.goUp();
			if(value2 == 2)it2.goUp();

			if(thisLeft != 2 && thisRight == thisLeft){
				itThis.setValue(Node.valueOf(thisRight));

				itThis.goRight();
				itThis.remove();
				itThis.goUp();

				itThis.goLeft();
				itThis.remove();
				itThis.goUp();
			}
		}
	}

	/**
	 * this devient l'union de image1 et image2 au sens des pixels allumés.
	 * 
	 * pre !image1.isEmpty() && !image2.isEmpty()
	 * 
	 * @param image1 premiere image
	 * @param image2 seconde image
	 */
	@Override
	public void union(AbstractImage image1, AbstractImage image2) {
		Iterator<Node> itThis = this.iterator();
		Iterator<Node> it1 = image1.iterator();
		Iterator<Node> it2 = image2.iterator();

		itThis.clear();
		unionIt(itThis,it1,it2);
	}
	private void unionIt(Iterator<Node> itThis,Iterator<Node> it1,Iterator<Node> it2){
		int value1 = it1.getValue().state;
		int value2 = it2.getValue().state;

		if(value1 == 0 && value2 == 0)
			itThis.addValue(Node.valueOf(0));
		else if(value1 == 1 || value2 == 1)
			itThis.addValue(Node.valueOf(1));
		else{
			itThis.addValue(Node.valueOf(2));

			itThis.goLeft();
			if(value1 == 2)it1.goLeft();
			if(value2 == 2)it2.goLeft();
			unionIt(itThis,it1,it2);
			int thisLeft = itThis.getValue().state;
			itThis.goUp();
			if(value1 == 2)it1.goUp();
			if(value2 == 2)it2.goUp();

			itThis.goRight();
			if(value1 == 2)it1.goRight();
			if(value2 == 2)it2.goRight();
			unionIt(itThis,it1,it2);
			int thisRight = itThis.getValue().state;
			itThis.goUp();
			if(value1 == 2)it1.goUp();
			if(value2 == 2)it2.goUp();

			if(thisLeft != 2 && thisRight == thisLeft){
				itThis.setValue(Node.valueOf(thisRight));

				itThis.goRight();
				itThis.remove();
				itThis.goUp();

				itThis.goLeft();
				itThis.remove();
				itThis.goUp();
			}
		}
	}

	/**
	 * Attention : cette fonction ne doit pas utiliser la commande isPixelOn
	 * 
	 * @return true si tous les points de la forme (x, x) (avec 0 <= x <= 255)
	 *         sont allumés dans this, false sinon
	 */
	@Override
	public boolean testDiagonal() {
		Iterator<Node> thisIt = this.iterator();
		return testDiagonalIt(thisIt);
	}
	private boolean testDiagonalIt(Iterator<Node> thisIt){
		boolean rep;
		int value = thisIt.getValue().state;

		if(value == 2){
			//Test haut gauche
			thisIt.goLeft();
			value = thisIt.getValue().state;
			if(value == 2){
				thisIt.goLeft();
				rep = testDiagonalIt(thisIt);
				thisIt.goUp();
			}else rep = value == 1;
			thisIt.goUp();

			//Test bas droit
			if (rep) {
				thisIt.goRight();
				value = thisIt.getValue().state;
				if (value == 2) {
					thisIt.goRight();
					rep = testDiagonalIt(thisIt);
					thisIt.goUp();
				} else rep = value == 1;
				thisIt.goUp();
			}
		}else rep = value==1;

		return rep;
	}

	/**
	 * @param x abscisse du point
	 * @param y ordonnée du point
	 * pre !this.isEmpty()
	 * @return true, si le point (x, y) est allumé dans this, false sinon
	 */
	@Override
	public boolean isPixelOn(int x, int y) {
		int posX = 128;
		int posY = 128;
		int varX = 64;
		int varY = 64;
		boolean parity = false;
		Iterator<Node> itThis = this.iterator();
		while (itThis.getValue().state == 2){
			if(parity){
				if(x < posX){
					posX -= varX;
					itThis.goLeft();
				} else {
					posX += varX;
					itThis.goRight();
				}
				varX/=2;
			}
			else{
				if(y < posY) {
					posY -= varY;
					itThis.goLeft();
				} else {
					posY += varY;
					itThis.goRight();
				}
				varY/=2;
			}
			parity = !parity;
		}
		return itThis.getValue().state == 1;
	}

	/**
	 * @param x1 abscisse du premier point
	 * @param y1 ordonnée du premier point
	 * @param x2 abscisse du deuxième point
	 * @param y2 ordonnée du deuxième point
	 * @ !this.isEmpty()
	 * @return true si les deux points (x1, y1) et (x2, y2) sont représentés par la
	 *         même feuille de this, false sinon
	 */
	@Override
	//TODO testSameLeaf2 : test 4
	public boolean sameLeaf(int x1, int y1, int x2, int y2) {
		if(x1 == x2 && y1 == y2)return true;
		else {
			int posX = 128;
			int posY = 128;
			int varX = 64;
			int varY = 64;
			boolean parity = false;
			boolean rep = !((y1 < posY && posY <= y2) || (y2 <= posY && posY < y1));
			Iterator<Node> itThis = this.iterator();
			int value = itThis.getValue().state;

			while (value == 2 && rep) {
				if(parity){
					if(x1 <= posX){
						posX -= varX;
						itThis.goLeft();
					} else {
						posX += varX;
						itThis.goRight();
					}
					varX/=2;
					rep = !((y1 < posY && posY <= y2) || (y2 <= posY && posY < y1));
				}
				else{
					if(y1 <= posY) {
						posY -= varY;
						itThis.goLeft();
					} else {
						posY += varY;
						itThis.goRight();
					}
					varY/=2;
					rep = !((x1 < posX && posX <= x2) || (x2 <= posX && posX < x1));
				}
				parity = !parity;
				value = itThis.getValue().state;
			}
			return rep;
		}
	}

	/**
	 * @param image autre image
	 * pre !this.isEmpty() && !image2.isEmpty()
	 * @return true si this est incluse dans image2 au sens des pixels allumés false
	 *         sinon
	 */
	@Override
	public boolean isIncludedIn(AbstractImage image) {
		Iterator<Node> it1 = this.iterator();
		Iterator<Node> it2 = image.iterator();
		return isIncludedInIt(it1,it2);
	}
	private boolean isIncludedInIt(Iterator<Node> it1, Iterator<Node> it2){
		int value1 = it1.getValue().state;
		int value2 = it2.getValue().state;
		boolean rep;

		if(value1 == 2 && value2 == 2){
			it1.goLeft();
			it2.goLeft();
			rep = isIncludedInIt(it1, it2);
			it1.goUp();
			it2.goUp();

			if(rep) {
				it1.goRight();
				it2.goRight();
				rep = isIncludedInIt(it1, it2);
				it1.goUp();
				it2.goUp();
			}
		}else {
			rep = !(value1 == 1 || value2 == 0) &&
					(value1 == 0 || value2 == 1);
		}
		return rep;
	}
}