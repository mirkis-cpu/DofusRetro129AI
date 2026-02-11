// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.fight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.aestia.client.Player;
import org.aestia.common.Formulas;
import org.aestia.common.Pathfinding;
import org.aestia.common.SocketManager;
import org.aestia.fight.spells.SpellEffect;
import org.aestia.game.GameClient;

public class Challenge {
	private int Type;
	private int xpWin;
	private int dropWin;
	private int Arg;
	private long lastActions_time;
	private boolean challengeAlive;
	private boolean challengeWin;
	private String looseBy;
	private String Args;
	private String lastActions;
	private Fight fight;
	private Fighter _cible;
	private List<Fighter> _ordreJeu;

	public Challenge(final Fight fight, final int Type, final int xp, final int drop) {
		this.Arg = 0;
		this.challengeAlive = false;
		this.challengeWin = false;
		this.looseBy = "";
		this.Args = new String();
		this._ordreJeu = new ArrayList<Fighter>();
		this.challengeAlive = true;
		this.fight = fight;
		this.Type = Type;
		this.xpWin = xp;
		this.dropWin = drop;
		this._ordreJeu.clear();
		this._ordreJeu.addAll(fight.getOrderPlaying());
		this.lastActions = "";
		this.lastActions_time = System.currentTimeMillis();
	}

	public int getType() {
		return this.Type;
	}

	public boolean getAlive() {
		return this.challengeAlive;
	}

	public int getXp() {
		return this.xpWin;
	}

	public int getDrop() {
		return this.dropWin;
	}

	public boolean getWin() {
		return this.challengeWin;
	}

	public boolean loose() {
		return this.looseBy.isEmpty();
	}

	public String getPacketEndFight() {
		return this.challengeWin ? ("OK" + this.Type) : ("KO" + this.Type);
	}

	private void challengeWin() {
		this.challengeWin = true;
		this.challengeAlive = false;
		SocketManager.GAME_SEND_CHALLENGE_FIGHT(this.fight, 1, "OK" + this.Type);
	}

	public void challengeLoose(final Fighter fighter) {
		String name = "";
		if (fighter != null && fighter.getPersonnage() != null) {
			name = fighter.getPersonnage().getName();
		}
		this.looseBy = name;
		this.challengeWin = false;
		this.challengeAlive = false;
		SocketManager.GAME_SEND_CHALLENGE_FIGHT(this.fight, 7, "KO" + this.Type);
		SocketManager.GAME_SEND_Im_PACKET_TO_CHALLENGE(this.fight, 1, "0188;" + name);
	}

	public void challengeSpecLoose(final Player player) {
		SocketManager.GAME_SEND_CHALLENGE_PERSO(player, "KO" + this.Type);
		SocketManager.GAME_SEND_Im_PACKET_TO_CHALLENGE_PERSO(player, "0188;" + this.looseBy);
	}

	public String parseToPacket() {
		final StringBuilder packet = new StringBuilder();
		packet.append(this.Type).append(";").append((this._cible != null) ? "1" : "0").append(";")
				.append((this._cible != null) ? this._cible.getId() : "").append(";").append(this.xpWin).append(";0;")
				.append(this.dropWin).append(";0;");
		if (!this.challengeAlive) {
			if (this.challengeWin) {
				packet.append("").append(this.Type);
			} else {
				packet.append("").append(this.Type);
			}
		}
		return packet.toString();
	}

	public void showCibleToPerso(final Player p) {
		if (!this.challengeAlive || this._cible == null || this._cible.getCell() == null || p == null) {
			return;
		}
		final ArrayList<GameClient> Pws = new ArrayList<GameClient>();
		Pws.add(p.getGameClient());
		SocketManager.GAME_SEND_FIGHT_SHOW_CASE(Pws, this._cible.getId(), this._cible.getCell().getId());
	}

	public void showCibleToFight() {
		if (!this.challengeAlive || this._cible == null || this._cible.getCell() == null) {
			return;
		}
		final ArrayList<GameClient> Pws = new ArrayList<GameClient>();
		for (final Fighter fighter : this.fight.getFighters(1)) {
			if (fighter.hasLeft()) {
				continue;
			}
			if (fighter.getPersonnage() == null) {
				continue;
			}
			if (!fighter.getPersonnage().isOnline()) {
				continue;
			}
			Pws.add(fighter.getPersonnage().getGameClient());
		}
		SocketManager.GAME_SEND_FIGHT_SHOW_CASE(Pws, this._cible.getId(), this._cible.getCell().getId());
	}

	public void fightStart() {
		if (!this.challengeAlive) {
			return;
		}
		switch (this.Type) {
		case 3:
		case 4:
		case 32:
		case 35: {
			if (this._cible == null && this._ordreJeu.size() > 0) {
				final List<Fighter> Choix = new ArrayList<Fighter>();
				Choix.addAll(this._ordreJeu);
				Collections.shuffle(Choix);
				for (final Fighter f : Choix) {
					if (f.getPersonnage() != null) {
						continue;
					}
					if (f.getMob() == null || f.getTeam2() != 2 || f.isDead() || f.isInvocation()) {
						continue;
					}
					this._cible = f;
				}
			}
			this.showCibleToFight();
			break;
		}
		case 10: {
			int levelMin = 2000;
			for (final Fighter fighter : this.fight.getFighters(2)) {
				if (fighter.isInvocation()) {
					continue;
				}
				if (fighter.getPersonnage() != null || fighter.getMob() == null || fighter.getLvl() >= levelMin
						|| fighter.getInvocator() != null) {
					continue;
				}
				levelMin = fighter.getLvl();
				this._cible = fighter;
			}
			if (this._cible != null) {
				this.showCibleToFight();
				break;
			}
			break;
		}
		case 25: {
			int levelMax = 0;
			for (final Fighter fighter2 : this.fight.getFighters(2)) {
				if (!fighter2.isInvocation()) {
					if (fighter2.isDouble()) {
						continue;
					}
					if (fighter2.getPersonnage() != null || fighter2.getMob() == null || fighter2.getInvocator() != null
							|| fighter2.getLvl() <= levelMax) {
						continue;
					}
					levelMax = fighter2.getLvl();
					this._cible = fighter2;
				}
			}
			if (this._cible != null) {
				this.showCibleToFight();
				break;
			}
			break;
		}
		}
	}

	public void fightEnd() {
		if (!this.challengeAlive) {
			return;
		}
		switch (this.Type) {
		case 44:
		case 46: {
			for (final Fighter fighter : this.fight.getFighters(1)) {
				if (!this.Args.contains(String.valueOf(fighter.getId()))) {
					this.challengeLoose(fighter);
					return;
				}
			}
			break;
		}
		}
		this.challengeWin();
	}

	public void onFighterDie(final Fighter fighter) {
		if (!this.challengeAlive) {
			return;
		}
		switch (this.Type) {
		case 33:
		case 49: {
			if (fighter.getPersonnage() != null) {
				this.challengeLoose(this.fight.getFighterByOrdreJeu());
				break;
			}
			break;
		}
		case 44: {
			if (fighter.getPersonnage() != null && !this.Args.contains(String.valueOf(fighter.getId()))) {
				this.challengeLoose(fighter);
				break;
			}
			break;
		}
		}
	}

	public void onFighterAttacked(final Fighter caster, final Fighter target) {
		if (!this.challengeAlive) {
			return;
		}
		switch (this.Type) {
		case 17: {
			if (target.getTeam() == 0 && !target.isInvocation() && target.getBuff(9) == null) {
				this.challengeLoose(target);
				break;
			}
			break;
		}
		case 31: {
			if (caster.getTeam() != 0 || target.getTeam() != 1) {
				break;
			}
			if (this.Args.isEmpty()) {
				this.Args = String.valueOf(this.Args) + target.getId();
				break;
			}
			if (!this.Args.contains(new StringBuilder().append(target.getId()).toString())) {
				this.challengeLoose(caster);
				break;
			}
			break;
		}
		}
	}

	public void onFightersAttacked(final ArrayList<Fighter> targets, final Fighter caster, final SpellEffect SE,
			final int spell, final boolean isTrap) {
		final int effectID = SE.getEffectID();
		if (!this.challengeAlive) {
			return;
		}
		final String DamagingEffects = "|82|85|86|87|88|89|91|92|93|94|95|96|97|98|99|100|141|";
		final String HealingEffects = "|108|";
		final String MPEffects = "|77|127|";
		final String APEffects = "|84|101|";
		final String OPEffects = "|116|320|";
		Label_1720: {
			switch (this.Type) {
			case 18: {
				if (caster.getTeam() == 0 && !caster.isInvocation() && HealingEffects.contains("|" + effectID + "|")) {
					this.challengeLoose(caster);
					break;
				}
				break;
			}
			case 19: {
				if (caster.getTeam() != 0) {
					return;
				}
				if (caster.isInvocation()) {
					return;
				}
				if (SE.getTurn() > 0) {
					return;
				}
				if (isTrap) {
					return;
				}
				for (final Fighter target : targets) {
					if (target.getTeam() == 1 && !target.isInvocation()) {
						this.challengeLoose(caster);
						break;
					}
				}
				break;
			}
			case 20: {
				if (caster.getTeam() != 0 || !DamagingEffects.contains("|" + effectID + "|") || effectID == 141) {
					break;
				}
				switch (spell) {
				case 106:
				case 108:
				case 111:
				case 123:
				case 126:
				case 135:
				case 149:
				case 435: {
					return;
				}
				default: {
					if (this.Arg == 0) {
						this.Arg = effectID;
						break Label_1720;
					}
					if (this.Arg == effectID) {
						break Label_1720;
					}
					final String eau = "85 91 96";
					final String terre = "86 92 97";
					final String air = "87 93 98";
					final String feu = "88 94 99";
					final String neutre = "89 95 100";
					if (eau.contains(String.valueOf(this.Arg)) && eau.contains(String.valueOf(effectID))) {
						break Label_1720;
					}
					if (terre.contains(String.valueOf(this.Arg)) && terre.contains(String.valueOf(effectID))) {
						break Label_1720;
					}
					if (air.contains(String.valueOf(this.Arg)) && air.contains(String.valueOf(effectID))) {
						break Label_1720;
					}
					if (feu.contains(String.valueOf(this.Arg)) && feu.contains(String.valueOf(effectID))) {
						break Label_1720;
					}
					if (neutre.contains(String.valueOf(this.Arg)) && neutre.contains(String.valueOf(effectID))) {
						break Label_1720;
					}
					this.challengeLoose(caster);
					break Label_1720;
				}
				}
			}
			case 21: {
				if (caster.getTeam() == 0 && MPEffects.contains("|" + effectID + "|")) {
					for (final Fighter target : targets) {
						if (target.getTeam() == 1) {
							this.challengeLoose(caster);
							break;
						}
					}
					break;
				}
				break;
			}
			case 22: {
				if (caster.getTeam() == 0 && APEffects.contains("|" + effectID + "|")) {
					for (final Fighter target : targets) {
						if (target.getTeam() == 1) {
							this.challengeLoose(caster);
							break;
						}
					}
					break;
				}
				break;
			}
			case 23: {
				if (caster.getTeam() == 0 && OPEffects.contains("|" + effectID + "|")) {
					for (final Fighter target : targets) {
						if (target.getTeam() == 1) {
							this.challengeLoose(caster);
							break;
						}
					}
					break;
				}
				break;
			}
			case 32:
			case 34: {
				if (caster.getTeam() == 0 && DamagingEffects.contains("|" + effectID + "|")) {
					for (final Fighter target : targets) {
						if (target.getTeam() == 1 && (this._cible == null || this._cible.getId() != target.getId())) {
							this.challengeLoose(caster);
						}
					}
					break;
				}
				break;
			}
			case 38: {
				if (caster.getTeam() == 0 && DamagingEffects.contains("|" + effectID + "|")) {
					for (final Fighter target : targets) {
						if (target.getTeam() == 1) {
							final StringBuilder ID = new StringBuilder();
							ID.append(";").append(target.getId()).append(",");
							if (this.Args.contains(ID.toString())) {
								continue;
							}
							ID.append(caster.getId());
							this.Args = String.valueOf(this.Args) + ID.toString();
						}
					}
					break;
				}
				break;
			}
			case 43: {
				if (caster.getTeam() == 0 && HealingEffects.contains("|" + effectID + "|")
						&& caster.getInvocator() == null) {
					for (final Fighter target : targets) {
						if (target.getId() == caster.getId()) {
							this.challengeLoose(caster);
						}
					}
					break;
				}
				break;
			}
			case 45:
			case 46: {
				if (caster.getTeam() == 0 && DamagingEffects.contains("|" + effectID + "|")) {
					for (final Fighter target : targets) {
						if (target.getTeam() == 1) {
							if (!this.Args.contains(";" + target.getId() + ",")) {
								this.Args = String.valueOf(this.Args) + ";" + target.getId() + "," + caster.getId()
										+ ";";
							} else {
								if (!this.Args.contains(";" + target.getId() + ",")
										|| this.Args.contains(";" + target.getId() + "," + caster.getId() + ";")) {
									continue;
								}
								this.challengeLoose(target);
							}
						}
					}
					break;
				}
				break;
			}
			case 47: {
				if (DamagingEffects.contains("|" + effectID + "|")) {
					for (final Fighter target : targets) {
						if (target.getTeam() == 0 && !this.Args.contains(";" + target.getId() + ",")) {
							this.Args = String.valueOf(this.Args) + ";" + target.getId() + "," + "3;";
						}
					}
					break;
				}
				break;
			}
			}
		}
	}

	public void onMobDie(final Fighter mob, final Fighter killer) {
		if (mob.getMob() == null) {
			return;
		}
		if (mob.getPersonnage() != null) {
			return;
		}
		if (mob.getTeam() != 1) {
			return;
		}
		if (mob.isInvocation() && mob.getInvocator().getPersonnage() != null) {
			return;
		}
		final boolean isKiller = killer.getId() != mob.getId();
		if (!this.challengeAlive) {
			return;
		}
		switch (this.Type) {
		case 3: {
			if (this._cible == null) {
				return;
			}
			if (mob.getInvocator() != null && mob.getInvocator().getId() == this._cible.getId()) {
				return;
			}
			if (this._cible.getId() != mob.getId()) {
				this.challengeLoose(this.fight.getFighterByOrdreJeu());
			} else {
				this.challengeWin();
			}
			this._cible = null;
			break;
		}
		case 4: {
			if (this._cible == null) {
				return;
			}
			if (this._cible.getId() == mob.getId() && !this.fight.verifIfTeamIsDead()) {
				this.challengeLoose(this.fight.getFighterByOrdreJeu());
				break;
			}
			break;
		}
		case 28: {
			if (isKiller && killer.getPersonnage() != null && killer.getPersonnage().getSexe() == 0) {
				this.challengeLoose(this.fight.getFighterByOrdreJeu());
				break;
			}
			break;
		}
		case 29: {
			if (isKiller && killer.getPersonnage() != null && killer.getPersonnage().getSexe() == 1) {
				this.challengeLoose(this.fight.getFighterByOrdreJeu());
				break;
			}
			break;
		}
		case 31: {
			if (mob.getPersonnage() != null) {
				break;
			}
			if (killer.getMob() != null) {
				break;
			}
			if (this.Args.contains(new StringBuilder().append(mob.getId()).toString())) {
				this.Args = "";
				break;
			}
			this.challengeLoose(killer);
			break;
		}
		case 32: {
			if (this._cible.getId() == mob.getId()) {
				this.challengeWin();
				break;
			}
			break;
		}
		case 34: {
			this._cible = null;
			break;
		}
		case 42:
		case 44:
		case 46: {
			switch (this.Type) {
			case 42: {
				if (mob.isInvocation() || killer.isInvocation()) {
					return;
				}
				break;
			}
			}
			if (isKiller) {
				this.Args = String.valueOf(this.Args) + (this.Args.isEmpty() ? killer.getId() : (";" + killer.getId()));
				break;
			}
			break;
		}
		case 30:
		case 48: {
			if (mob.isInvocation() || mob.isDouble()) {
				return;
			}
			if (mob.getId() == killer.getId()) {
				break;
			}
			int lvlMin = 5000;
			for (final Fighter f : this.fight.getFighters2(1)) {
				if (f.isInvocation()) {
					continue;
				}
				if (f.getLvl() >= lvlMin) {
					continue;
				}
				lvlMin = f.getLvl();
			}
			if (killer.getLvl() > lvlMin) {
				this.challengeLoose(this.fight.getFighterByOrdreJeu());
				break;
			}
			break;
		}
		case 35: {
			if (this._cible == null) {
				return;
			}
			if (mob == killer) {
				return;
			}
			if (this._cible.getId() == mob.getId()) {
				try {
					this._cible = null;
					final ArrayList<Fighter> fighters = new ArrayList<Fighter>(this.fight.getFighters(2));
					final Iterator<Fighter> it = fighters.iterator();
					while (it.hasNext()) {
						final Fighter f2 = it.next();
						if (f2.isInvocation() || f2.isDead() || f2.getPersonnage() != null) {
							it.remove();
						}
					}
					Collections.sort(fighters);
					for (final Fighter f : fighters) {
						if (!f.isInvocation() && !f.isDead() && f.getPersonnage() == null) {
							this._cible = f;
							break;
						}
					}
					this.showCibleToFight();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			if (!mob.isInvocation()) {
				this.challengeLoose(this.fight.getFighterByOrdreJeu());
				break;
			}
			break;
		}
		case 10: {
			if (this._cible == null) {
				return;
			}
			if (this._cible.isInvocation() || this._cible.isDouble() || mob.getPersonnage() != null) {
				return;
			}
			if (this._cible.getId() == mob.getId() || this._cible.getLvl() == mob.getLvl()) {
				try {
					int levelMin = 2000;
					for (final Fighter fighter : this.fight.getFighters(2)) {
						if (!fighter.isInvocation() && !fighter.isDouble() && fighter.getPersonnage() == null) {
							if (fighter.isDead()) {
								continue;
							}
							if (fighter.getPersonnage() != null || fighter.getLvl() >= levelMin) {
								continue;
							}
							levelMin = fighter.getLvl();
							this._cible = fighter;
						}
					}
					if (this._cible != null) {
						this.showCibleToFight();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			if (mob.getLvl() > this._cible.getLvl()) {
				this.challengeLoose(this.fight.getFighterByOrdreJeu());
				break;
			}
			break;
		}
		case 25: {
			if (this._cible == null) {
				return;
			}
			if (mob.isInvocation() || mob.isDouble() || mob.getPersonnage() != null) {
				return;
			}
			if (this._cible.getId() == mob.getId()) {
				try {
					int levelMax = 0;
					for (final Fighter fighter : this.fight.getFighters(2)) {
						if (!fighter.isInvocation() && !fighter.isDouble() && fighter.getPersonnage() == null) {
							if (fighter.isDead()) {
								continue;
							}
							if (fighter.getLvl() <= levelMax) {
								continue;
							}
							levelMax = fighter.getLvl();
							this._cible = fighter;
						}
					}
					if (this._cible != null) {
						this.showCibleToFight();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			if (mob.getLvl() < this._cible.getLvl()) {
				this.challengeLoose(this.fight.getFighterByOrdreJeu());
				break;
			}
			break;
		}
		}
	}

	public void onPlayerMove(final Fighter fighter) {
		if (!this.challengeAlive) {
			return;
		}
		switch (this.Type) {
		case 1: {
			if (this.fight.getCurFighterUsedPm() > 1) {
				this.challengeLoose(this.fight.getFighterByOrdreJeu());
				break;
			}
			break;
		}
		}
	}

	public void onPlayerAction(final Fighter fighter, final int actionID) {
		if (!this.challengeAlive || fighter.getTeam() == 1) {
			return;
		}
		if (System.currentTimeMillis() - this.lastActions_time < 500L) {
			return;
		}
		this.lastActions_time = System.currentTimeMillis();
		final StringBuilder action = new StringBuilder();
		action.append(";").append(fighter.getId());
		action.append(",").append(actionID).append(";");
		switch (this.Type) {
		case 5:
		case 6: {
			if (this.lastActions.contains(action.toString())) {
				this.challengeLoose(this.fight.getFighterByOrdreJeu());
			}
			this.lastActions = String.valueOf(this.lastActions) + action.toString();
			break;
		}
		case 24: {
			if (!this.lastActions.contains(action.toString())
					&& this.lastActions.contains(";" + fighter.getId() + ",")) {
				this.challengeLoose(this.fight.getFighterByOrdreJeu());
			}
			this.lastActions = String.valueOf(this.lastActions) + action.toString();
			break;
		}
		}
	}

	public void onPlayerCac(final Fighter fighter) {
		if (!this.challengeAlive) {
			return;
		}
		switch (this.Type) {
		case 11: {
			this.challengeLoose(this.fight.getFighterByOrdreJeu());
			break;
		}
		case 5:
		case 6: {
			if (System.currentTimeMillis() - this.lastActions_time < 500L) {
				return;
			}
			this.lastActions_time = System.currentTimeMillis();
			final StringBuilder action = new StringBuilder();
			action.append(";").append(fighter.getId());
			action.append(",").append("cac").append(";");
			if (this.lastActions.contains(action.toString())) {
				this.challengeLoose(this.fight.getFighterByOrdreJeu());
			}
			this.lastActions = String.valueOf(this.lastActions) + action.toString();
			break;
		}
		}
	}

	public void onPlayerSpell(final Fighter fighter) {
		if (!this.challengeAlive) {
			return;
		}
		if (fighter.getPersonnage() == null) {
			return;
		}
		switch (this.Type) {
		case 9: {
			this.challengeLoose(this.fight.getFighterByOrdreJeu());
			break;
		}
		}
	}

	public void onPlayerStartTurn(final Fighter fighter) {
		if (!this.challengeAlive) {
			return;
		}
		switch (this.Type) {
		case 2: {
			if (fighter.getPersonnage() == null) {
				return;
			}
			this.Arg = fighter.getCell().getId();
			break;
		}
		case 6: {
			this.lastActions = "";
			break;
		}
		case 34: {
			if (fighter.getTeam() == 1) {
				return;
			}
			try {
				int noBoucle = 0;
				int GUID = 0;
				this._cible = null;
				while (this._cible == null) {
					if (this._ordreJeu.size() > 0) {
						GUID = Formulas.getRandomValue(0, this._ordreJeu.size() - 1);
						final Fighter f = this._ordreJeu.get(GUID);
						if (f.getPersonnage() == null && !f.isDead()) {
							this._cible = f;
						}
						if (++noBoucle > 150) {
							return;
						}
						continue;
					}
				}
				this.showCibleToFight();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case 38: {
			if (fighter.getTeam() != 1 || !this.Args.contains(";" + fighter.getId() + ",")) {
				break;
			}
			if (fighter.isDead()) {
				return;
			}
			final String[] str = this.Args.split(";");
			int fighterID = 0;
			String[] array;
			for (int length = (array = str).length, i = 0; i < length; ++i) {
				final String string = array[i];
				if (string.contains(new StringBuilder().append(fighter.getId()).toString())) {
					String[] split;
					for (int length2 = (split = string.split(",")).length, j = 0; j < length2; ++j) {
						final String test = split[j];
						fighterID = Integer.parseInt(test);
					}
					break;
				}
			}
			for (final Fighter f : this.fight.getFighters(1)) {
				if (f.getId() == fighterID) {
					this.challengeLoose(f);
				}
			}
			break;
		}
		case 47: {
			if (fighter.getTeam() != 0) {
				break;
			}
			final String str2 = ";" + fighter.getId() + ",";
			if (this.Args.contains(String.valueOf(str2) + "1;")) {
				this.challengeLoose(fighter);
				break;
			}
			if (this.Args.contains(String.valueOf(str2) + "2;")) {
				this.Args = String.valueOf(this.Args) + str2 + "1;";
				break;
			}
			if (this.Args.contains(String.valueOf(str2) + "3;")) {
				this.Args = String.valueOf(this.Args) + str2 + "2;";
				break;
			}
			break;
		}
		}
	}

	public void onPlayerEndTurn(final Fighter fighter) {
		boolean hasFailed = false;
		if (!this.challengeAlive) {
			return;
		}
		ArrayList<Fighter> Neighbours = new ArrayList<Fighter>();
		Neighbours = Pathfinding.getFightersAround(fighter.getCell().getId(), this.fight.getMap(), this.fight);
		switch (this.Type) {
		case 1: {
			if (this.fight.getCurFighterUsedPm() <= 0) {
				this.challengeLoose(fighter);
				break;
			}
			break;
		}
		case 2: {
			if (fighter.getPersonnage() != null && fighter.getCell().getId() != this.Arg) {
				this.challengeLoose(fighter);
				break;
			}
			break;
		}
		case 7: {
			if (fighter.getPersonnage() != null && fighter.canLaunchSpell(367)) {
				this.challengeLoose(fighter);
				break;
			}
			break;
		}
		case 8: {
			if (fighter.getCurPm(this.fight) > 0) {
				this.challengeLoose(fighter);
				break;
			}
			break;
		}
		case 12: {
			if (fighter.getPersonnage() != null && fighter.canLaunchSpell(373)) {
				this.challengeLoose(fighter);
				break;
			}
			break;
		}
		case 14: {
			if (fighter.getPersonnage() != null && fighter.canLaunchSpell(101)) {
				this.challengeLoose(fighter);
				break;
			}
			break;
		}
		case 15: {
			if (fighter.getPersonnage() != null && fighter.canLaunchSpell(370)) {
				this.challengeLoose(fighter);
				break;
			}
			break;
		}
		case 36: {
			hasFailed = true;
			if (!Neighbours.isEmpty()) {
				for (final Fighter f : Neighbours) {
					if (f.getTeam() != fighter.getTeam()) {
						hasFailed = false;
					}
				}
				break;
			}
			break;
		}
		case 37: {
			hasFailed = true;
			if (!Neighbours.isEmpty()) {
				for (final Fighter f : Neighbours) {
					if (f.getTeam() == fighter.getTeam()) {
						hasFailed = false;
					}
				}
				break;
			}
			break;
		}
		case 39: {
			if (!Neighbours.isEmpty()) {
				for (final Fighter f : Neighbours) {
					if (f.getTeam() == fighter.getTeam()) {
						this.challengeLoose(fighter);
					}
				}
				break;
			}
			break;
		}
		case 40: {
			if (!Neighbours.isEmpty()) {
				for (final Fighter f : Neighbours) {
					if (f.getTeam() != fighter.getTeam()) {
						this.challengeLoose(fighter);
					}
				}
				break;
			}
			break;
		}
		case 41: {
			if (fighter.getCurPa(this.fight) > 0) {
				this.challengeLoose(fighter);
				break;
			}
			break;
		}
		case 42: {
			if (this.Args.isEmpty()) {
				break;
			}
			if (this.Args.split("\\;").length % 2 == 0) {
				hasFailed = false;
				break;
			}
			this.challengeLoose(fighter);
			break;
		}
		}
		if (hasFailed) {
			this.challengeLoose(fighter);
		}
	}
}
