package fr.istic.prg1.tree_util;

/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 5.0
 * @since 2016-09-26
 * 
 *        Classe représentant les états des noeuds : 0 (carré/rectangle éteint),
 *        1 (carré/rectangle allumé) ou 2.
 * 
 *        Les trois objets sont non modifiables et existent en 1 seul
 *        exemplaire.
 */
public final class Node {

	public final int state;

	private static final Node ZERO = new Node(0);
	private static final Node ONE = new Node(1);
	private static final Node TWO = new Node(2);

	/**
	 * Constructeur <b>privé</b> pour créer les objets.
	 * 
	 * @param nodeValue valeur du noeud
	 */
	private Node(int nodeValue) {
		state = nodeValue;
	}

	/**
	 * Usine à objets.
	 * 
	 * @param nodeValue valeur du noeud
	 * @return noeud avec la valeur x
	 */
	public static Node valueOf(int nodeValue) {
		if (nodeValue == 0) {
			return ZERO;
		} else if (nodeValue == 1) {
			return ONE;
		} else if (nodeValue == 2) {
			return TWO;
		}
		throw new IllegalArgumentException("Valeur incorrecte pour Node");
	}

	@Override
	public boolean equals(Object o) {
		/*
		 * pas besoin de tester le contenu, il n'y a pas d'objets identiques
		 */
		return this == o;
	}

	@Override
	public int hashCode() {
		/*
		 * toute classe doit normalement implémenter hashCode
		 */
		return state;
	}

	@Override
	public String toString() {
		return "state = " + state;
	}
}