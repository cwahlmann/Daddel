package de.dreierschach.daddel.gfx.sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Ein Partikelschwarm besteht aus vielen Partikeln, die in gewissen Grenzen
 * zufällig erzeugt wurden
 * 
 * @author Christian
 *
 */
public class ParticleSwarm {
	private List<Particle> particles = new ArrayList<>();

	/**
	 * @return alle Partikel, die zu diesem Schwarm gehören
	 */
	public List<Particle> getParticles() {
		return particles;
	}
}
