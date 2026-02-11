// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.map.laby;

import java.util.concurrent.TimeUnit;

import org.aestia.client.Player;
import org.aestia.common.Formulas;
import org.aestia.common.SocketManager;
import org.aestia.game.scheduler.TimerWaiter;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.map.Case;
import org.aestia.map.Map;

public class Toror {
	private static final long time = 10L;
	private static TimerWaiter waiter;
	private static short demi;
	private static short momi;

	static {
		Toror.waiter = new TimerWaiter();
	}

	public static void initialize() {
		closeAll();
		initializeBoss();
	}

	public static void demi() {
		Toror.waiter.addNow(new Runnable() {
			@Override
			public void run() {
				spawnDemi();
			}
		}, 10L, TimeUnit.MINUTES);
	}

	public static void momi() {
		Toror.waiter.addNow(new Runnable() {
			@Override
			public void run() {
				spawnMomi();
			}
		}, 10L, TimeUnit.MINUTES);
	}

	private static void initializeBoss() {
		Toror.demi = -1;
		Toror.momi = -1;
		spawnDemi();
		spawnMomi();
	}

	private static void spawnDemi() {
		chooseRandomDemi();
		World.getMap(Toror.demi).spawnGroupWith(World.getMonstre(832));
		Console.println("Ajout du deminoboule sur la map " + Toror.demi + ".", Console.Color.GAME);
	}

	private static void spawnMomi() {
		chooseRandomMomi();
		World.getMap(Toror.momi).spawnGroupWith(World.getMonstre(831));
		Console.println("Ajout du mominotoror sur la map " + Toror.momi + ".", Console.Color.GAME);
	}

	private static void chooseRandomDemi() {
		switch (Formulas.getRandomValue(0, 7)) {
		case 0: {
			Toror.demi = 9575;
			break;
		}
		case 1: {
			Toror.demi = 9576;
			break;
		}
		case 2: {
			Toror.demi = 9577;
			break;
		}
		case 3: {
			Toror.demi = 9556;
			break;
		}
		case 4: {
			Toror.demi = 9560;
			break;
		}
		case 5: {
			Toror.demi = 9561;
			break;
		}
		case 6: {
			Toror.demi = 9562;
			break;
		}
		case 7: {
			Toror.demi = 9563;
			break;
		}
		}
		if (Toror.demi == Toror.momi) {
			chooseRandomDemi();
		}
	}

	private static void chooseRandomMomi() {
		switch (Formulas.getRandomValue(0, 7)) {
		case 0: {
			Toror.momi = 9575;
			break;
		}
		case 1: {
			Toror.momi = 9576;
			break;
		}
		case 2: {
			Toror.momi = 9577;
			break;
		}
		case 3: {
			Toror.momi = 9556;
			break;
		}
		case 4: {
			Toror.momi = 9560;
			break;
		}
		case 5: {
			Toror.momi = 9561;
			break;
		}
		case 6: {
			Toror.momi = 9562;
			break;
		}
		case 7: {
			Toror.momi = 9563;
			break;
		}
		}
		if (Toror.demi == Toror.momi) {
			chooseRandomMomi();
		}
	}

	public static void ouvrirHaut(final Map map) {
		closeAll();
		switch (map.getId()) {
		case 9555: {
			openTimer(World.getMap((short) 9574), 51);
			openTimer(World.getMap((short) 9553), 428);
			break;
		}
		case 9556: {
			openTimer(World.getMap((short) 9575), 94);
			openTimer(World.getMap((short) 9564), 429);
			break;
		}
		case 9557: {
			openTimer(World.getMap((short) 9576), 67);
			openTimer(World.getMap((short) 9571), 428);
			break;
		}
		case 9558: {
			openTimer(World.getMap((short) 9577), 50);
			openTimer(World.getMap((short) 9572), 443);
			break;
		}
		case 9559: {
			openTimer(World.getMap((short) 9554), 138);
			openTimer(World.getMap((short) 9573), 440);
			break;
		}
		case 9560: {
			openTimer(World.getMap((short) 9555), 79);
			openTimer(World.getMap((short) 9574), 425);
			break;
		}
		case 9561: {
			openTimer(World.getMap((short) 9556), 64);
			openTimer(World.getMap((short) 9575), 427);
			break;
		}
		case 9562: {
			openTimer(World.getMap((short) 9557), 51);
			openTimer(World.getMap((short) 9576), 441);
			break;
		}
		case 9563: {
			openTimer(World.getMap((short) 9558), 77);
			openTimer(World.getMap((short) 9577), 426);
			break;
		}
		case 9565: {
			openTimer(World.getMap((short) 9559), 65);
			openTimer(World.getMap((short) 9554), 431);
			break;
		}
		case 9566: {
			openTimer(World.getMap((short) 9560), 52);
			openTimer(World.getMap((short) 9555), 440);
			break;
		}
		case 9567: {
			openTimer(World.getMap((short) 9561), 80);
			openTimer(World.getMap((short) 9556), 428);
			break;
		}
		case 9568: {
			openTimer(World.getMap((short) 9562), 52);
			openTimer(World.getMap((short) 9557), 413);
			break;
		}
		case 9569: {
			openTimer(World.getMap((short) 9563), 52);
			openTimer(World.getMap((short) 9558), 427);
			break;
		}
		case 9570: {
			openTimer(World.getMap((short) 9565), 51);
			openTimer(World.getMap((short) 9559), 427);
			break;
		}
		}
	}

	public static void ouvrirBas(final Map map) {
		closeAll();
		switch (map.getId()) {
		case 9553: {
			openTimer(World.getMap((short) 9574), 425);
			openTimer(World.getMap((short) 9555), 79);
			break;
		}
		case 9564: {
			openTimer(World.getMap((short) 9575), 427);
			openTimer(World.getMap((short) 9556), 64);
			break;
		}
		case 9571: {
			openTimer(World.getMap((short) 9576), 441);
			openTimer(World.getMap((short) 9557), 51);
			break;
		}
		case 9572: {
			openTimer(World.getMap((short) 9577), 426);
			openTimer(World.getMap((short) 9558), 77);
			break;
		}
		case 9573: {
			openTimer(World.getMap((short) 9554), 431);
			openTimer(World.getMap((short) 9559), 65);
			break;
		}
		case 9574: {
			openTimer(World.getMap((short) 9555), 440);
			openTimer(World.getMap((short) 9560), 52);
			break;
		}
		case 9575: {
			openTimer(World.getMap((short) 9556), 428);
			openTimer(World.getMap((short) 9561), 80);
			break;
		}
		case 9576: {
			openTimer(World.getMap((short) 9557), 413);
			openTimer(World.getMap((short) 9562), 52);
			break;
		}
		case 9577: {
			openTimer(World.getMap((short) 9558), 427);
			openTimer(World.getMap((short) 9563), 52);
			break;
		}
		case 9554: {
			openTimer(World.getMap((short) 9559), 427);
			openTimer(World.getMap((short) 9565), 51);
			break;
		}
		case 9555: {
			openTimer(World.getMap((short) 9560), 428);
			openTimer(World.getMap((short) 9566), 51);
			break;
		}
		case 9556: {
			openTimer(World.getMap((short) 9561), 441);
			openTimer(World.getMap((short) 9567), 37);
			break;
		}
		case 9557: {
			openTimer(World.getMap((short) 9562), 429);
			openTimer(World.getMap((short) 9568), 51);
			break;
		}
		case 9558: {
			openTimer(World.getMap((short) 9563), 429);
			openTimer(World.getMap((short) 9569), 64);
			break;
		}
		case 9559: {
			openTimer(World.getMap((short) 9565), 414);
			openTimer(World.getMap((short) 9570), 51);
			break;
		}
		}
	}

	public static void ouvrirGauche(final Map map) {
		closeAll();
		switch (map.getId()) {
		case 9571: {
			openTimer(World.getMap((short) 9564), 335);
			openTimer(World.getMap((short) 9553), 288);
			break;
		}
		case 9572: {
			openTimer(World.getMap((short) 9571), 277);
			openTimer(World.getMap((short) 9564), 259);
			break;
		}
		case 9573: {
			openTimer(World.getMap((short) 9572), 263);
			openTimer(World.getMap((short) 9571), 331);
			break;
		}
		case 9576: {
			openTimer(World.getMap((short) 9575), 335);
			openTimer(World.getMap((short) 9574), 273);
			break;
		}
		case 9577: {
			openTimer(World.getMap((short) 9576), 306);
			openTimer(World.getMap((short) 9575), 331);
			break;
		}
		case 9554: {
			openTimer(World.getMap((short) 9577), 364);
			openTimer(World.getMap((short) 9576), 317);
			break;
		}
		case 9557: {
			openTimer(World.getMap((short) 9556), 306);
			openTimer(World.getMap((short) 9555), 332);
			break;
		}
		case 9558: {
			openTimer(World.getMap((short) 9557), 306);
			openTimer(World.getMap((short) 9556), 332);
			break;
		}
		case 9559: {
			openTimer(World.getMap((short) 9558), 277);
			openTimer(World.getMap((short) 9557), 230);
			break;
		}
		case 9562: {
			openTimer(World.getMap((short) 9561), 306);
			openTimer(World.getMap((short) 9560), 302);
			break;
		}
		case 9563: {
			openTimer(World.getMap((short) 9562), 320);
			openTimer(World.getMap((short) 9561), 317);
			break;
		}
		case 9565: {
			openTimer(World.getMap((short) 9563), 292);
			openTimer(World.getMap((short) 9562), 303);
			break;
		}
		case 9568: {
			openTimer(World.getMap((short) 9567), 277);
			openTimer(World.getMap((short) 9566), 332);
			break;
		}
		case 9569: {
			openTimer(World.getMap((short) 9568), 277);
			openTimer(World.getMap((short) 9567), 346);
			break;
		}
		case 9570: {
			openTimer(World.getMap((short) 9569), 291);
			openTimer(World.getMap((short) 9568), 346);
			break;
		}
		}
	}

	public static void ouvrirDroite(final Map map) {
		closeAll();
		switch (map.getId()) {
		case 9553: {
			openTimer(World.getMap((short) 9564), 259);
			openTimer(World.getMap((short) 9571), 277);
			break;
		}
		case 9564: {
			openTimer(World.getMap((short) 9571), 331);
			openTimer(World.getMap((short) 9572), 263);
			break;
		}
		case 9571: {
			openTimer(World.getMap((short) 9572), 346);
			openTimer(World.getMap((short) 9573), 219);
			break;
		}
		case 9574: {
			openTimer(World.getMap((short) 9575), 331);
			openTimer(World.getMap((short) 9576), 306);
			break;
		}
		case 9575: {
			openTimer(World.getMap((short) 9576), 317);
			openTimer(World.getMap((short) 9577), 364);
			break;
		}
		case 9576: {
			openTimer(World.getMap((short) 9577), 390);
			openTimer(World.getMap((short) 9554), 306);
			break;
		}
		case 9555: {
			openTimer(World.getMap((short) 9556), 332);
			openTimer(World.getMap((short) 9557), 306);
			break;
		}
		case 9556: {
			openTimer(World.getMap((short) 9557), 230);
			openTimer(World.getMap((short) 9558), 277);
			break;
		}
		case 9557: {
			openTimer(World.getMap((short) 9558), 361);
			openTimer(World.getMap((short) 9559), 277);
			break;
		}
		case 9560: {
			openTimer(World.getMap((short) 9561), 317);
			openTimer(World.getMap((short) 9562), 320);
			break;
		}
		case 9561: {
			openTimer(World.getMap((short) 9562), 303);
			openTimer(World.getMap((short) 9563), 292);
			break;
		}
		case 9562: {
			openTimer(World.getMap((short) 9563), 288);
			openTimer(World.getMap((short) 9565), 262);
			break;
		}
		case 9566: {
			openTimer(World.getMap((short) 9567), 346);
			openTimer(World.getMap((short) 9568), 277);
			break;
		}
		case 9567: {
			openTimer(World.getMap((short) 9568), 346);
			openTimer(World.getMap((short) 9569), 291);
			break;
		}
		case 9568: {
			openTimer(World.getMap((short) 9569), 317);
			openTimer(World.getMap((short) 9570), 306);
			break;
		}
		}
	}

	private static void closeAll() {
		close(World.getMap((short) 9553), 428);
		close(World.getMap((short) 9553), 288);
		close(World.getMap((short) 9554), 431);
		close(World.getMap((short) 9554), 306);
		close(World.getMap((short) 9554), 138);
		close(World.getMap((short) 9555), 440);
		close(World.getMap((short) 9555), 332);
		close(World.getMap((short) 9555), 79);
		close(World.getMap((short) 9556), 428);
		close(World.getMap((short) 9556), 306);
		close(World.getMap((short) 9556), 332);
		close(World.getMap((short) 9556), 64);
		close(World.getMap((short) 9557), 413);
		close(World.getMap((short) 9557), 306);
		close(World.getMap((short) 9557), 230);
		close(World.getMap((short) 9557), 51);
		close(World.getMap((short) 9558), 427);
		close(World.getMap((short) 9558), 277);
		close(World.getMap((short) 9558), 361);
		close(World.getMap((short) 9558), 77);
		close(World.getMap((short) 9559), 427);
		close(World.getMap((short) 9559), 277);
		close(World.getMap((short) 9559), 65);
		close(World.getMap((short) 9560), 428);
		close(World.getMap((short) 9560), 302);
		close(World.getMap((short) 9560), 52);
		close(World.getMap((short) 9561), 441);
		close(World.getMap((short) 9561), 306);
		close(World.getMap((short) 9561), 317);
		close(World.getMap((short) 9561), 80);
		close(World.getMap((short) 9562), 429);
		close(World.getMap((short) 9562), 320);
		close(World.getMap((short) 9562), 303);
		close(World.getMap((short) 9562), 52);
		close(World.getMap((short) 9563), 429);
		close(World.getMap((short) 9563), 292);
		close(World.getMap((short) 9563), 288);
		close(World.getMap((short) 9563), 52);
		close(World.getMap((short) 9564), 429);
		close(World.getMap((short) 9564), 335);
		close(World.getMap((short) 9564), 259);
		close(World.getMap((short) 9565), 414);
		close(World.getMap((short) 9565), 262);
		close(World.getMap((short) 9565), 51);
		close(World.getMap((short) 9566), 332);
		close(World.getMap((short) 9566), 51);
		close(World.getMap((short) 9567), 277);
		close(World.getMap((short) 9567), 346);
		close(World.getMap((short) 9567), 37);
		close(World.getMap((short) 9568), 277);
		close(World.getMap((short) 9568), 346);
		close(World.getMap((short) 9568), 51);
		close(World.getMap((short) 9569), 291);
		close(World.getMap((short) 9569), 317);
		close(World.getMap((short) 9569), 64);
		close(World.getMap((short) 9570), 306);
		close(World.getMap((short) 9570), 51);
		close(World.getMap((short) 9571), 428);
		close(World.getMap((short) 9571), 277);
		close(World.getMap((short) 9571), 331);
		close(World.getMap((short) 9572), 443);
		close(World.getMap((short) 9572), 263);
		close(World.getMap((short) 9572), 346);
		close(World.getMap((short) 9573), 440);
		close(World.getMap((short) 9573), 219);
		close(World.getMap((short) 9574), 425);
		close(World.getMap((short) 9574), 273);
		close(World.getMap((short) 9574), 51);
		close(World.getMap((short) 9575), 427);
		close(World.getMap((short) 9575), 335);
		close(World.getMap((short) 9575), 331);
		close(World.getMap((short) 9575), 94);
		close(World.getMap((short) 9576), 441);
		close(World.getMap((short) 9576), 306);
		close(World.getMap((short) 9576), 317);
		close(World.getMap((short) 9576), 67);
		close(World.getMap((short) 9577), 426);
		close(World.getMap((short) 9577), 364);
		close(World.getMap((short) 9577), 390);
		close(World.getMap((short) 9577), 50);
	}

	private static void openAll() {
		open(World.getMap((short) 9553), 428);
		open(World.getMap((short) 9553), 288);
		open(World.getMap((short) 9554), 431);
		open(World.getMap((short) 9554), 306);
		open(World.getMap((short) 9554), 138);
		open(World.getMap((short) 9555), 440);
		open(World.getMap((short) 9555), 332);
		open(World.getMap((short) 9555), 79);
		open(World.getMap((short) 9556), 428);
		open(World.getMap((short) 9556), 306);
		open(World.getMap((short) 9556), 332);
		open(World.getMap((short) 9556), 64);
		open(World.getMap((short) 9557), 413);
		open(World.getMap((short) 9557), 306);
		open(World.getMap((short) 9557), 230);
		open(World.getMap((short) 9557), 51);
		open(World.getMap((short) 9558), 427);
		open(World.getMap((short) 9558), 277);
		open(World.getMap((short) 9558), 361);
		open(World.getMap((short) 9558), 77);
		open(World.getMap((short) 9559), 427);
		open(World.getMap((short) 9559), 277);
		open(World.getMap((short) 9559), 65);
		open(World.getMap((short) 9560), 428);
		open(World.getMap((short) 9560), 302);
		open(World.getMap((short) 9560), 52);
		open(World.getMap((short) 9561), 441);
		open(World.getMap((short) 9561), 306);
		open(World.getMap((short) 9561), 317);
		open(World.getMap((short) 9561), 80);
		open(World.getMap((short) 9562), 429);
		open(World.getMap((short) 9562), 320);
		open(World.getMap((short) 9562), 303);
		open(World.getMap((short) 9562), 52);
		open(World.getMap((short) 9563), 429);
		open(World.getMap((short) 9563), 292);
		open(World.getMap((short) 9563), 288);
		open(World.getMap((short) 9563), 52);
		open(World.getMap((short) 9564), 429);
		open(World.getMap((short) 9564), 335);
		open(World.getMap((short) 9564), 259);
		open(World.getMap((short) 9565), 414);
		open(World.getMap((short) 9565), 262);
		open(World.getMap((short) 9565), 51);
		open(World.getMap((short) 9566), 332);
		open(World.getMap((short) 9566), 51);
		open(World.getMap((short) 9567), 277);
		open(World.getMap((short) 9567), 346);
		open(World.getMap((short) 9567), 37);
		open(World.getMap((short) 9568), 277);
		open(World.getMap((short) 9568), 346);
		open(World.getMap((short) 9568), 51);
		open(World.getMap((short) 9569), 291);
		open(World.getMap((short) 9569), 317);
		open(World.getMap((short) 9569), 64);
		open(World.getMap((short) 9570), 306);
		open(World.getMap((short) 9570), 51);
		open(World.getMap((short) 9571), 428);
		open(World.getMap((short) 9571), 277);
		open(World.getMap((short) 9571), 331);
		open(World.getMap((short) 9572), 443);
		open(World.getMap((short) 9572), 263);
		open(World.getMap((short) 9572), 346);
		open(World.getMap((short) 9573), 440);
		open(World.getMap((short) 9573), 219);
		open(World.getMap((short) 9574), 425);
		open(World.getMap((short) 9574), 273);
		open(World.getMap((short) 9574), 51);
		open(World.getMap((short) 9575), 427);
		open(World.getMap((short) 9575), 335);
		open(World.getMap((short) 9575), 331);
		open(World.getMap((short) 9575), 94);
		open(World.getMap((short) 9576), 441);
		open(World.getMap((short) 9576), 306);
		open(World.getMap((short) 9576), 317);
		open(World.getMap((short) 9576), 67);
		open(World.getMap((short) 9577), 426);
		open(World.getMap((short) 9577), 364);
		open(World.getMap((short) 9577), 390);
		open(World.getMap((short) 9577), 50);
	}

	private static void openTimer(final Map map, final int cellId) {
		if (map.getCase(cellId).isWalkable(false)) {
			return;
		}
		open(map, cellId);
		Toror.waiter.addNow(new Runnable() {
			@Override
			public void run() {
				close(map, cellId);
			}
		}, 10L, TimeUnit.MINUTES);
	}

	private static void open(final Map map, final int cellId) {
		sendOpen(map, cellId);
		map.getCases().remove(cellId);
		map.getCases().put(cellId, new Case(map, cellId, true, true, -1));
	}

	private static void close(final Map map, final int cellId) {
		if (!map.getCase(cellId).isWalkable(false)) {
			return;
		}
		sendClose(map, cellId);
		map.getCases().remove(cellId);
		map.getCases().put(cellId, new Case(map, cellId, false, false, -1));
	}

	private static void sendOpen(final Map map, final int cellId) {
		SocketManager.GAME_UPDATE_CELL(map, String.valueOf(cellId) + ";aaGaaaaaaa801;1");
		SocketManager.GAME_SEND_ACTION_TO_DOOR(map, cellId, true);
	}

	private static void sendOpen(final Player p, final int cellId) {
		SocketManager.GAME_UPDATE_CELL(p, String.valueOf(cellId) + ";aaGaaaaaaa801;1");
		SocketManager.GAME_SEND_ACTION_TO_DOOR(p, cellId, true);
	}

	private static void sendClose(final Map map, final int cellId) {
		SocketManager.GAME_UPDATE_CELL(map, String.valueOf(cellId) + ";aaaaaaaaaa801;1");
		SocketManager.GAME_SEND_ACTION_TO_DOOR(map, cellId, false);
	}

	private static void sendClose(final Player p, final int cellId) {
		SocketManager.GAME_UPDATE_CELL(p, String.valueOf(cellId) + ";aaaaaaaaaa801;1");
		SocketManager.GAME_SEND_ACTION_TO_DOOR(p, cellId, false);
	}

	public static void sendPacketMap(final Player perso) {
		final Map map = perso.getCurMap();
		Case c1 = null;
		Case c2 = null;
		Case c3 = null;
		Case c4 = null;
		switch (map.getId()) {
		case 9553: {
			c1 = map.getCase(428);
			c3 = map.getCase(288);
			break;
		}
		case 9554: {
			c1 = map.getCase(431);
			c2 = map.getCase(306);
			c4 = map.getCase(138);
			break;
		}
		case 9555: {
			c1 = map.getCase(440);
			c3 = map.getCase(332);
			c4 = map.getCase(79);
			break;
		}
		case 9556: {
			c1 = map.getCase(428);
			c2 = map.getCase(306);
			c3 = map.getCase(332);
			c4 = map.getCase(64);
			break;
		}
		case 9557: {
			c1 = map.getCase(413);
			c2 = map.getCase(306);
			c3 = map.getCase(230);
			c4 = map.getCase(51);
			break;
		}
		case 9558: {
			c1 = map.getCase(427);
			c2 = map.getCase(277);
			c3 = map.getCase(361);
			c4 = map.getCase(77);
			break;
		}
		case 9559: {
			c1 = map.getCase(427);
			c2 = map.getCase(277);
			c4 = map.getCase(65);
			break;
		}
		case 9560: {
			c1 = map.getCase(428);
			c3 = map.getCase(302);
			c4 = map.getCase(52);
			break;
		}
		case 9561: {
			c1 = map.getCase(441);
			c2 = map.getCase(306);
			c3 = map.getCase(317);
			c4 = map.getCase(80);
			break;
		}
		case 9562: {
			c1 = map.getCase(429);
			c2 = map.getCase(320);
			c3 = map.getCase(303);
			c4 = map.getCase(52);
			break;
		}
		case 9563: {
			c1 = map.getCase(429);
			c2 = map.getCase(292);
			c3 = map.getCase(288);
			c4 = map.getCase(52);
			break;
		}
		case 9564: {
			c1 = map.getCase(429);
			c2 = map.getCase(335);
			c3 = map.getCase(259);
			break;
		}
		case 9565: {
			c1 = map.getCase(414);
			c2 = map.getCase(262);
			c4 = map.getCase(51);
			break;
		}
		case 9566: {
			c3 = map.getCase(332);
			c4 = map.getCase(51);
			break;
		}
		case 9567: {
			c2 = map.getCase(277);
			c3 = map.getCase(346);
			c4 = map.getCase(37);
			break;
		}
		case 9568: {
			c2 = map.getCase(277);
			c3 = map.getCase(346);
			c4 = map.getCase(51);
			break;
		}
		case 9569: {
			c2 = map.getCase(291);
			c3 = map.getCase(317);
			c4 = map.getCase(64);
			break;
		}
		case 9570: {
			c2 = map.getCase(306);
			c4 = map.getCase(51);
			break;
		}
		case 9571: {
			c1 = map.getCase(428);
			c2 = map.getCase(277);
			c3 = map.getCase(331);
			break;
		}
		case 9572: {
			c1 = map.getCase(443);
			c2 = map.getCase(263);
			c3 = map.getCase(346);
			break;
		}
		case 9573: {
			c1 = map.getCase(440);
			c2 = map.getCase(219);
			break;
		}
		case 9574: {
			c1 = map.getCase(425);
			c3 = map.getCase(273);
			c4 = map.getCase(51);
			break;
		}
		case 9575: {
			c1 = map.getCase(427);
			c2 = map.getCase(335);
			c3 = map.getCase(331);
			c4 = map.getCase(94);
			break;
		}
		case 9576: {
			c1 = map.getCase(441);
			c2 = map.getCase(306);
			c3 = map.getCase(317);
			c4 = map.getCase(67);
			break;
		}
		case 9577: {
			c1 = map.getCase(426);
			c2 = map.getCase(364);
			c3 = map.getCase(390);
			c4 = map.getCase(50);
			break;
		}
		}
		if (c1 != null) {
			if (c1.isWalkable(false)) {
				sendOpen(perso, c1.getId());
			} else {
				sendClose(perso, c1.getId());
			}
		}
		if (c2 != null) {
			if (c2.isWalkable(false)) {
				sendOpen(perso, c2.getId());
			} else {
				sendClose(perso, c2.getId());
			}
		}
		if (c3 != null) {
			if (c3.isWalkable(false)) {
				sendOpen(perso, c3.getId());
			} else {
				sendClose(perso, c3.getId());
			}
		}
		if (c4 != null) {
			if (c4.isWalkable(false)) {
				sendOpen(perso, c4.getId());
			} else {
				sendClose(perso, c4.getId());
			}
		}
	}
}
