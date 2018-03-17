package de.dreierschach.invader;

import java.util.Random;

import de.dreierschach.daddel.gfx.Gfx;
import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.SpriteGameLoop;
import de.dreierschach.daddel.model.TimelineItem;

public class SpaceInvaderLevelBuilder {
	private SpaceInvader invader;

	public SpaceInvaderLevelBuilder(SpaceInvader invader) {
		this.invader = invader;
	}

	public void erzeugeLevel() {
		invader.anzahlFeinde = 0;
		switch ((invader.level() - 1) % 4) {
		case 0:
			erzeugeFeindeLevel1();
			break;
		case 1:
			erzeugeFeindeLevel2();
			break;
		case 2:
			erzeugeFeindeLevel3();
			break;
		case 3:
		default:
			erzeugeFeindeLevel4();
		}
	}

	private void erzeugeFeindeLevel1() {
		Sprite parent = invader.invisibleSprite(SpaceInvader.TYP_FLOTTE, 0.5);
		erzeugeFlotte(new int[] { 9, 9, 9 }, parent, new Pos(0, -4));
	}

	private void erzeugeFeindeLevel2() {
		Sprite parent = invader.invisibleSprite(SpaceInvader.TYP_FLOTTE, 0.5);
		erzeugeFlotte(new int[] { 3, 5, 4 }, parent, new Pos(0, -7), kreisen(2, 10000));
	}

	private void erzeugeFeindeLevel3() {
		invader.erzeugeSaturn();
		Sprite parent = invader.invisibleSprite(SpaceInvader.TYP_FLOTTE, 0.5).pos(1.5, 0).gameLoop(//
				invader.timeline()//
						.with(move(180, 1), 3000) //
						.with(move(270, 1), 1000) //
						.with(move(0, 1), 3000) //
						.with(move(90, 1), 1000) //
						.cycle());
		erzeugeFlotte(new int[] { 4, 4, 4 }, parent, new Pos(-6, -4), invader.timeline() //
				.with(doNothing, 10000) //
				.with(kreisen(10, 2000), 2000) //
				.cycle());
		erzeugeFlotte(new int[] { 4, 4, 4 }, parent, new Pos(6, -4), invader.timeline() //
				.with(doNothing, 5000) //
				.with(kreisen(10, 2000), 2000) //
				.with(doNothing, 5000) //
				.cycle());
	}

	private void erzeugeFeindeLevel4() {
		invader.erzeugeErdeUndMond();
		Sprite parent = invader.invisibleSprite(SpaceInvader.TYP_FLOTTE, 0.5).pos(0, -8)
				.gameLoop((me, ticks, delta) -> {
					me.direction(90);
					me.move(invader.strecke(delta, 2));
					if (me.pos().y() > 15) {
						me.pos(me.pos().x(), -15);
					}
				});
		erzeugeFlotte(new int[] { 11, 11, 11 }, parent, new Pos(0, 0));
	}

	// -------- Flotte erzeugen

	private void erzeugeFlotte(int[] ufos, Sprite parent, Pos position, SpriteGameLoop... steuerung) {
		Sprite flotte = invader.invisibleSprite(SpaceInvader.TYP_FLOTTE, 0.5).parent(parent).pos(position)
				.gameLoop(steuerung);
		Random random = new Random();
		for (int zeile = 0; zeile < ufos.length; zeile++) {
			double y = zeile - ufos.length / 2;
			for (int spalte = 0; spalte < ufos[zeile]; spalte++) {
				double x = (double) spalte - (double) (ufos[zeile] - 1) / 2d;
				invader.sprite(SpaceInvader.TYP_FEIND, 2.5f, random.nextBoolean() ? Gfx.UFO_1 : Gfx.UFO_2)
						.pos(x * 3f, y * 3f).parent(flotte).gameLoop((spr, ticks, deltatime) -> {
							if (Math.random() < 0.0005 * (double) invader.level()) {
								invader.gegnerischenLaserAbfeuern(spr.effektivePos().add(new Pos(0, 1.5f)));
							}
						}).collision((me, other) -> {
							if (other.type() == SpaceInvader.TYP_SPIELER && invader.raketeSchutzschirm <= 0) {
								invader.gegnerischeTrefferBehandeln.onCollision(me, other);
								invader.anzahlFeinde--;
							}
						});
				invader.anzahlFeinde++;
			}
		}
	}

	private SpriteGameLoop doNothing = (me, ticks, other) -> {
	};

	private SpriteGameLoop kreisen(double speed, long interval) {
		return (me, total, delta) -> {
			double angleSpeed = 360 * 1000d / (double) interval;
			double a = invader.strecke(delta, angleSpeed);
			double s = invader.strecke(delta, speed);
			me.direction(me.direction() + a);
			me.move(s);
		};
	}

	private SpriteGameLoop move(double direction, double speed) {
		return (me, total, delta) -> {
			double s = invader.strecke(delta, speed);
			me.direction(direction);
			me.move(s);
		};
	}
}
