package common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import objects.Carte;
import objects.Fight;
import objects.Fight.Fighter;
import objects.Guild;
import objects.Guild.GuildMember;
import objects.Objet;
import objects.Percepteur;
import objects.Personnage;
import objects.SpellEffect;

import common.World.Couple;

public class Formulas {


	public static int getRandomValue(int i1,int i2)
	{
		Random rand = new Random();
		return (rand.nextInt((i2-i1)+1))+i1;
	}
	
	public static int getRandomJet(String jet)//1d5+6
	{
		try
		{
			int num = 0;
			int des = Integer.parseInt(jet.split("d")[0]);
			int faces = Integer.parseInt(jet.split("d")[1].split("\\+")[0]);
			int add = Integer.parseInt(jet.split("d")[1].split("\\+")[1]);
			for(int a=0;a<des;a++)
			{
				num += getRandomValue(1,faces);
			}
			num += add;
			return num;
		}catch(NumberFormatException e){return -1;}
	}
	
	public static int getMiddleJet(String jet)//1d5+6
	{
		try
		{
			int num = 0;
			int des = Integer.parseInt(jet.split("d")[0]);
			int faces = Integer.parseInt(jet.split("d")[1].split("\\+")[0]);
			int add = Integer.parseInt(jet.split("d")[1].split("\\+")[1]);
			num += ((1+faces)/2)*des;//on calcule moyenne
			num += add;
			return num;
		}catch(NumberFormatException e){return 0;}
	}
	
	public static int getTacleChance(Fighter fight, Fighter fighter) {
		int agiTR = fight.getTotalStats().getEffect(119);
		int agiT = fighter.getTotalStats().getEffect(119);
		int a = agiTR + 25;
		int b = agiTR + agiT + 50;
		if (b <= 0)
			b = 1;
		int chan = (int) ((long) (300 * a / b) - 100);
		if (chan < 10)
			chan = 10;
		if (chan > 90)
			chan = 90;
		return chan;
	}

	public static int calculFinalHeal(Personnage caster,int jet)
	{
		int statC = caster.getTotalStats().getEffect(Constants.STATS_ADD_INTE);
		int soins = caster.getTotalStats().getEffect(Constants.STATS_ADD_SOIN);
		if(statC<0)statC=0;
		return (int)(jet * (100 + statC) / 100 + soins);
	}
	
	public static int calculFinalHealCac(Fighter healer, int rank, boolean isCac) {
		int intel = healer.getTotalStats().getEffect(126);
		int heals = healer.getTotalStats().getEffect(178);
		if (intel < 0)
			intel = 0;
		float adic = 100;
		if (isCac)
			adic = 105;
		return (int) (rank * ( (100.00 + intel) / adic) + heals / 2);
	}
	
	public static int calculFinalDommage(Fight fight,Fighter caster,Fighter target,int statID,int jet,boolean isHeal, boolean isCaC, int spellid)
	{
		float i = 0;//Bonus maitrise
		float j = 100; //Bonus de Classe
		float a = 1;//Calcul
		float num = 0;
		float statC = 0, domC = 0, perdomC = 0, resfT = 0, respT = 0;
		int multiplier = 0;
		if(!isHeal)
		{
			domC = caster.getTotalStats().getEffect(Constants.STATS_ADD_DOMA);
			perdomC = caster.getTotalStats().getEffect(Constants.STATS_ADD_PERDOM);
			multiplier = caster.getTotalStats().getEffect(Constants.STATS_MULTIPLY_DOMMAGE);
		}else
		{
			domC = caster.getTotalStats().getEffect(Constants.STATS_ADD_SOIN);
		}
		
		switch(statID)
		{
			case Constants.ELEMENT_NULL://Fixe
				statC = 0;
				resfT = 0;
				respT = 0;
				respT = 0;
			break;
			case Constants.ELEMENT_NEUTRE://neutre
				statC = caster.getTotalStats().getEffect(Constants.STATS_ADD_FORC);
				resfT = target.getTotalStats().getEffect(Constants.STATS_ADD_R_NEU);
				respT = target.getTotalStats().getEffect(Constants.STATS_ADD_RP_NEU);
				if(caster.getPersonnage() != null)//Si c'est un joueur
				{
					respT += target.getTotalStats().getEffect(Constants.STATS_ADD_RP_PVP_NEU);
					resfT += target.getTotalStats().getEffect(Constants.STATS_ADD_R_PVP_NEU);
				}
				//on ajoute les dom Physique
				domC += caster.getTotalStats().getEffect(142);
				//Ajout de la resist Physique
				resfT += target.getTotalStats().getEffect(184);
			break;
			case Constants.ELEMENT_TERRE://force
				statC = caster.getTotalStats().getEffect(Constants.STATS_ADD_FORC);
				resfT = target.getTotalStats().getEffect(Constants.STATS_ADD_R_TER);
				respT = target.getTotalStats().getEffect(Constants.STATS_ADD_RP_TER);
				if(caster.getPersonnage() != null)//Si c'est un joueur
				{
					respT += target.getTotalStats().getEffect(Constants.STATS_ADD_RP_PVP_TER);
					resfT += target.getTotalStats().getEffect(Constants.STATS_ADD_R_PVP_TER);
				}
				//on ajout les dom Physique
				domC += caster.getTotalStats().getEffect(142);
				//Ajout de la resist Physique
				resfT += target.getTotalStats().getEffect(184);
			break;
			case Constants.ELEMENT_EAU://chance
				statC = caster.getTotalStats().getEffect(Constants.STATS_ADD_CHAN);
				resfT = target.getTotalStats().getEffect(Constants.STATS_ADD_R_EAU);
				respT = target.getTotalStats().getEffect(Constants.STATS_ADD_RP_EAU);
				if(caster.getPersonnage() != null)//Si c'est un joueur
				{
					respT += target.getTotalStats().getEffect(Constants.STATS_ADD_RP_PVP_EAU);
					resfT += target.getTotalStats().getEffect(Constants.STATS_ADD_R_PVP_EAU);
				}
				//Ajout de la resist Magique
				resfT += target.getTotalStats().getEffect(183);
			break;
			case Constants.ELEMENT_FEU://intell
				statC = caster.getTotalStats().getEffect(Constants.STATS_ADD_INTE);
				resfT = target.getTotalStats().getEffect(Constants.STATS_ADD_R_FEU);
				respT = target.getTotalStats().getEffect(Constants.STATS_ADD_RP_FEU);
				if(caster.getPersonnage() != null)//Si c'est un joueur
				{
					respT += target.getTotalStats().getEffect(Constants.STATS_ADD_RP_PVP_FEU);
					resfT += target.getTotalStats().getEffect(Constants.STATS_ADD_R_PVP_FEU);
				}
				//Ajout de la resist Magique
				resfT += target.getTotalStats().getEffect(183);
			break;
			case Constants.ELEMENT_AIR://agilit�
				statC = caster.getTotalStats().getEffect(Constants.STATS_ADD_AGIL);
				resfT = target.getTotalStats().getEffect(Constants.STATS_ADD_R_AIR);
				respT = target.getTotalStats().getEffect(Constants.STATS_ADD_RP_AIR);
				if(caster.getPersonnage() != null)//Si c'est un joueur
				{
					respT += target.getTotalStats().getEffect(Constants.STATS_ADD_RP_PVP_AIR);
					resfT += target.getTotalStats().getEffect(Constants.STATS_ADD_R_PVP_AIR);
				}
				//Ajout de la resist Magique
				resfT += target.getTotalStats().getEffect(183);
			break;
		}
		//On bride la resistance a 100% si c'est un joueur
		if(target.getMob() == null && respT >100)respT = 100;
		
		if(statC<0)statC=0;
			if(caster.getPersonnage() != null && isCaC)
			{
			int ArmeType = caster.getPersonnage().getObjetByPos(1).getTemplate().getType();
			
			if((caster.getSpellValueBool(392) == true) && ArmeType == 2)//ARC
			{
				i = caster.getMaitriseDmg(392);
			}
			if((caster.getSpellValueBool(390) == true) && ArmeType == 4)//BATON
			{
				i = caster.getMaitriseDmg(390);
			}
			if((caster.getSpellValueBool(391) == true) && ArmeType == 6)//EPEE
			{
				i = caster.getMaitriseDmg(391);
			}
			if((caster.getSpellValueBool(393) == true) && ArmeType == 7)//MARTEAUX
			{
				i = caster.getMaitriseDmg(393);
			}
			if((caster.getSpellValueBool(394) == true) && ArmeType == 3)//BAGUETTE
			{
				i = caster.getMaitriseDmg(394);
			}
			if((caster.getSpellValueBool(395) == true) && ArmeType == 5)//DAGUES
			{
				i = caster.getMaitriseDmg(395);
			}
			if((caster.getSpellValueBool(396) == true) && ArmeType == 8)//PELLE
			{
				i = caster.getMaitriseDmg(396);
			}
			if((caster.getSpellValueBool(397) == true) && ArmeType == 19)//HACHE
			{
				i = caster.getMaitriseDmg(397);
			}
				a = (((100+i)/100)*(j/100));
			}
			
			num = (a*(jet * ((100 + statC + perdomC + (multiplier*100)) / 100 ))+ domC);//d�gats bruts
			
		//Poisons
		if(spellid != -1)
		{
			switch(spellid)
			{
				
				case 66 : 
				statC = caster.getTotalStats().getEffect(Constants.STATS_ADD_AGIL);
				num = (jet * ((100 + statC + perdomC + (multiplier*100)) / 100 ))+ domC;
				if(target.hasBuff(105))
				{
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105, caster.getGUID()+"", target.getGUID()+","+target.getBuff(105).getValue());
					return 0;
				}
				if(target.hasBuff(184))
				{
					SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105, caster.getGUID()+"", target.getGUID()+","+target.getBuff(184).getValue());
					return 0;
				}
				return (int) num;
				
				case 71 :
				case 196:
				case 219:
					statC = caster.getTotalStats().getEffect(Constants.STATS_ADD_FORC);
					num = (jet * ((100 + statC + perdomC + (multiplier*100)) / 100 ))+ domC;
					if(target.hasBuff(105))
					{
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105, caster.getGUID()+"", target.getGUID()+","+target.getBuff(105).getValue());
						return 0;
					}
					if(target.hasBuff(184))
					{
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105, caster.getGUID()+"", target.getGUID()+","+target.getBuff(184).getValue());
						return 0;
					}
				return (int) num;
				
				case 181:
				case 200:
					statC = caster.getTotalStats().getEffect(Constants.STATS_ADD_INTE);
					num = (jet * ((100 + statC + perdomC + (multiplier*100)) / 100 ))+ domC;
					if(target.hasBuff(105))
					{
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105, caster.getGUID()+"", target.getGUID()+","+target.getBuff(105).getValue());
						return 0;
					}
					if(target.hasBuff(184))
					{
						SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105, caster.getGUID()+"", target.getGUID()+","+target.getBuff(184).getValue());
						return 0;
					}
				return (int) num;
			}
		}
		//Renvoie
		int renvoie = target.getTotalStatsLessBuff().getEffect(Constants.STATS_RETDOM);
		if(renvoie >0 && !isHeal)
		{
			if(renvoie > num)renvoie = (int)num;
			num -= renvoie;
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 107, "-1", target.getGUID()+","+renvoie);
			if(renvoie>caster.getPDV())renvoie = caster.getPDV();
			if(num<1)num =0;
			caster.removePDV(renvoie);
			SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 100, caster.getGUID()+"", caster.getGUID()+",-"+renvoie);
		}
		
		if(!isHeal)num -= resfT;//resis fixe
		int reduc =	(int)((num/(float)100)*respT);//Reduc %resis
		if(!isHeal)num -= reduc;
		
		int armor = getArmorResist(target,statID);
		if(!isHeal)num -= armor;
		if(!isHeal)if(armor > 0)SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, 105, caster.getGUID()+"", target.getGUID()+","+armor);
		//d�gats finaux
		if(num < 1)num=0;
		
		// D�but Formule pour les MOBs
		if(caster.getPersonnage() == null && !caster.isPerco())
		{
			if(caster.getMob().getTemplate().getID() == 116)//Sacrifi� Dommage = PDV*2
			{
				return (int)((num/25)*caster.getPDVMAX());
			}else
			{
				return (int)num;
			}
		}
		// Fin Formule pour les MOBs
		else
		{
			//Perte de 10% des PDV MAX par points de degat 10 PDV = 1PDV max en moins
			if(target.getPersonnage()!= null) target.removePDVMAX((int)Math.floor(num/10));
			return (int)num;
		}
	}

	public static int calculZaapCost(Carte map1,Carte map2)
	{
		return (int) (10*(Math.abs(map2.getX()-map1.getX())+Math.abs(map2.getY()-map1.getY())-1));
	}
	private static int getArmorResist(Fighter target, int statID)
	{
		int armor = 0;
		for(SpellEffect SE : target.getBuffsByEffectID(265))
		{
			Fighter fighter;
			
			switch(SE.getSpell())
			{
				case 1://Armure incandescente
					//Si pas element feu, on ignore l'armure
					if(statID != Constants.ELEMENT_FEU)continue;
					//Les stats du f�ca sont prises en compte
					fighter = SE.getCaster();
				break;
				case 6://Armure Terrestre
					//Si pas element terre/neutre, on ignore l'armure
					if(statID != Constants.ELEMENT_TERRE && statID != Constants.ELEMENT_NEUTRE)continue;
					//Les stats du f�ca sont prises en compte
					fighter = SE.getCaster();
				break;
				case 14://Armure Venteuse
					//Si pas element air, on ignore l'armure
					if(statID != Constants.ELEMENT_AIR)continue;
					//Les stats du f�ca sont prises en compte
					fighter = SE.getCaster();
				break;
				case 18://Armure aqueuse
					//Si pas element eau, on ignore l'armure
					if(statID != Constants.ELEMENT_EAU)continue;
					//Les stats du f�ca sont prises en compte
					fighter = SE.getCaster();
				break;
				
				default://Dans les autres cas on prend les stats de la cible et on ignore l'element de l'attaque
					fighter = target;
				break;
			}
			int intell = fighter.getTotalStats().getEffect(Constants.STATS_ADD_INTE);
			int carac = 0;
			switch(statID)
			{
				case Constants.ELEMENT_AIR:
					carac = fighter.getTotalStats().getEffect(Constants.STATS_ADD_AGIL);
				break;
				case Constants.ELEMENT_FEU:
					carac = fighter.getTotalStats().getEffect(Constants.STATS_ADD_INTE);
				break;
				case Constants.ELEMENT_EAU:
					carac = fighter.getTotalStats().getEffect(Constants.STATS_ADD_CHAN);
				break;
				case Constants.ELEMENT_NEUTRE:
				case Constants.ELEMENT_TERRE:
					carac = fighter.getTotalStats().getEffect(Constants.STATS_ADD_FORC);
				break;
			}
			int value = SE.getValue();
			int a = value * (100 + (int)(intell/2) + (int)(carac/2))/100;
			armor += a;
		}
		for(SpellEffect SE : target.getBuffsByEffectID(105))
		{
			int intell = target.getTotalStats().getEffect(Constants.STATS_ADD_INTE);
			int carac = 0;
			switch(statID)
			{
				case Constants.ELEMENT_AIR:
					carac = target.getTotalStats().getEffect(Constants.STATS_ADD_AGIL);
				break;
				case Constants.ELEMENT_FEU:
					carac = target.getTotalStats().getEffect(Constants.STATS_ADD_INTE);
				break;
				case Constants.ELEMENT_EAU:
					carac = target.getTotalStats().getEffect(Constants.STATS_ADD_CHAN);
				break;
				case Constants.ELEMENT_NEUTRE:
				case Constants.ELEMENT_TERRE:
					carac = target.getTotalStats().getEffect(Constants.STATS_ADD_FORC);
				break;
			}
			int value = SE.getValue();
			int a = value * (100 + (int)(intell/2) + (int)(carac/2))/100;
			armor += a;
		}
		return armor;
	}

	public static int getPointsLost(char z, int value, Fighter caster,Fighter target)
	{
		float esquiveC = z=='a'?caster.getTotalStats().getEffect(Constants.STATS_ADD_AFLEE):caster.getTotalStats().getEffect(Constants.STATS_ADD_MFLEE);
		float esquiveT = z=='a'?target.getTotalStats().getEffect(Constants.STATS_ADD_AFLEE):target.getTotalStats().getEffect(Constants.STATS_ADD_MFLEE);
		float ptsMax = z=='a'?target.getTotalStatsLessBuff().getEffect(Constants.STATS_ADD_PA):target.getTotalStatsLessBuff().getEffect(Constants.STATS_ADD_PM);
		
		int retrait = 0;

		for(int i = 0; i < value;i++)
		{
			if(ptsMax == 0 && target.getMob() != null)
			{
				ptsMax= z=='a'?target.getMob().getPA():target.getMob().getPM();
			}
			
			float pts = z =='a'?target.getPA():target.getPM();
			float ptsAct = pts - retrait;
			
			if(esquiveT == 0)esquiveT=1;
			if(esquiveC == 0)esquiveC=1;

			float a = (float)(esquiveC/esquiveT);
			float b = (ptsAct/ptsMax);

			float pourcentage = (float)(a*b*50);
			int chance = (int)Math.ceil(pourcentage);
			
			
			if(chance <0)chance = 0;
			if(chance >100)chance = 100;

			int jet = getRandomValue(0, 99);
			if(jet<chance)
			{
				retrait++;
			}
		}
		return retrait;
	}
	
	public static long getXpWinPerco(Percepteur perco, ArrayList<Fighter> winners,ArrayList<Fighter> loosers,long groupXP)
	{
			Guild G = World.getGuild(perco.get_guildID());
			float sag = G.get_Stats(Constants.STATS_ADD_SAGE);
			float coef = (sag + 100)/100;
			int taux = CyonEmu.XP_PVM;
			long xpWin = 0;
			int lvlmax = 0;
			for(Fighter entry : winners)
			{
				if(entry.get_lvl() > lvlmax)
					lvlmax = entry.get_lvl();
			}
			int nbbonus = 0;
			for(Fighter entry : winners)
			{
				if(entry.get_lvl() > (lvlmax / 3))
					nbbonus += 1;				
			}
			
			double bonus = nbbonus;

			int lvlLoosers = 0;
			for(Fighter entry : loosers)
				lvlLoosers += entry.get_lvl();
			int lvlWinners = 0;
			for(Fighter entry : winners)
				lvlWinners += entry.get_lvl();
			double rapport = 1+((double)lvlLoosers/(double)lvlWinners);
			if (rapport <= 1.3)
				rapport = 1.3;
			int lvl = G.get_lvl();
			double rapport2 = 1 + ((double)lvl / (double)lvlWinners);

			xpWin = (long) (groupXP * rapport * bonus * taux *coef * rapport2);
			return xpWin;
	}
	
	public static long getXpWinPvm(Fighter perso, ArrayList<Fighter> winners,ArrayList<Fighter> loosers,long groupXP, int star)
	{
		if(perso.getPersonnage()== null)return 0;
		if(winners.contains(perso))//Si winner
		{
			float sag = perso.getTotalStats().getEffect(Constants.STATS_ADD_SAGE);
			float coef = (sag + 100)/100;
			int taux = CyonEmu.XP_PVM;
			long xpWin = 0;
			int lvlmax = 0;
			for(Fighter entry : winners)
			{
				if(entry.get_lvl() > lvlmax)
					lvlmax = entry.get_lvl();
			}
			int nbbonus = 0;
			for(Fighter entry : winners)
			{
				if(entry.get_lvl() > (lvlmax / 3))
					nbbonus += 1;				
			}
			
			double bonus = nbbonus;

			int lvlLoosers = 0;
			for(Fighter entry : loosers)
				lvlLoosers += entry.get_lvl();
			int lvlWinners = 0;
			for(Fighter entry : winners)
				lvlWinners += entry.get_lvl();
			double rapport = 1+((double)lvlLoosers/(double)lvlWinners);
			if (rapport <= 1.3)
				rapport = 1.3;

			int lvl = perso.get_lvl();
			double rapport2 = 1 + ((double)lvl / (double)lvlWinners);

			xpWin = (long) (groupXP * rapport * bonus * taux * coef * rapport2);
			if(star > 0)
				xpWin = (long) (xpWin + xpWin*(star/100));
			return xpWin;	
		}
		return 0;
	}
	
	public static long getXpWinPvP(Fighter perso, ArrayList<Fighter> winners, ArrayList<Fighter> looser)
	{
		if(perso.getPersonnage()== null)return 0;
		if(winners.contains(perso.getGUID()))//Si winner
		{
			int lvlLoosers = 0;
			for(Fighter entry : looser)
				lvlLoosers += entry.get_lvl();
		
			int lvlWinners = 0;
			for(Fighter entry : winners)
				lvlWinners += entry.get_lvl();
			int taux = CyonEmu.XP_PVP;
			float rapport = (float)lvlLoosers/(float)lvlWinners;
			long xpWin = (long)(
						(
							rapport
						*	getXpNeededAtLevel(perso.getPersonnage().get_lvl())
						/	100
						)
						*	taux
					);
			return xpWin;
		}
		return 0;
	}
	
	private static long getXpNeededAtLevel(int lvl)
	{
		long xp = (World.getPersoXpMax(lvl) - World.getPersoXpMin(lvl));
		
		return xp;
	}

	public static long getGuildXpWin(Fighter perso, AtomicReference<Long> xpWin)
	{
		if(perso.getPersonnage()== null)return 0;
		if(perso.getPersonnage().getGuildMember() == null)return 0;
		

		GuildMember gm = perso.getPersonnage().getGuildMember();
		
		double xp = (double)xpWin.get(), Lvl = perso.get_lvl(),LvlGuild = perso.getPersonnage().get_guild().get_lvl(),pXpGive = (double)gm.getPXpGive()/100;
		
		double maxP = xp * pXpGive * 0.10;	//Le maximum donn� � la guilde est 10% du montant pr�lev� sur l'xp du combat
		double diff = Math.abs(Lvl - LvlGuild);	//Calcul l'�cart entre le niveau du personnage et le niveau de la guilde
		double toGuild;
		if(diff >= 70)
		{
			toGuild = maxP * 0.10;	//Si l'�cart entre les deux level est de 70 ou plus, l'experience donn�e a la guilde est de 10% la valeur maximum de don
		}
		else if(diff >= 31 && diff <= 69)
		{
			toGuild = maxP - ((maxP * 0.10) * (Math.floor((diff+30)/10)));
		}
		else if(diff >= 10 && diff <= 30)
		{
			toGuild = maxP - ((maxP * 0.20) * (Math.floor(diff/10))) ;
		}
		else	//Si la diff�rence est [0,9]
		{
			toGuild = maxP;
		}
		xpWin.set((long)(xp - xp*pXpGive));
		return (long) Math.round(toGuild);
	}
	
	public static long getMountXpWin(Fighter perso, AtomicReference<Long> xpWin)
	{
		if(perso.getPersonnage()== null)return 0;
		if(perso.getPersonnage().getMount() == null)return 0;
		

		int diff = Math.abs(perso.get_lvl() - perso.getPersonnage().getMount().get_level());
		
		double coeff = 0;
		double xp = (double) xpWin.get();
		double pToMount = (double)perso.getPersonnage().getMountXpGive() / 100 + 0.2;
		
		if(diff >= 0 && diff <= 9)
			coeff = 0.1;
		else if(diff >= 10 && diff <= 19)
			coeff = 0.08;
		else if(diff >= 20 && diff <= 29)
			coeff = 0.06;
		else if(diff >= 30 && diff <= 39)
			coeff = 0.04;
		else if(diff >= 40 && diff <= 49)
			coeff = 0.03;
		else if(diff >= 50 && diff <= 59)
			coeff = 0.02;
		else if(diff >= 60 && diff <= 69)
			coeff = 0.015;
		else
			coeff = 0.01;
		
		if(pToMount > 0.2)
			xpWin.set((long)(xp - (xp*(pToMount-0.2))));
		
		return (long)Math.round(xp * pToMount * coeff);
	}

	public static int getKamasWin(Fighter i, ArrayList<Fighter> winners, int maxk, int mink)
	{
		maxk++;
		int rkamas = (int)(Math.random() * (maxk-mink)) + mink;
		return rkamas*CyonEmu.KAMAS;
	}
	
	public static int getKamasWinPerco(int maxk, int mink)
	{
		maxk++;
		int rkamas = (int)(Math.random() * (maxk-mink)) + mink;
		return rkamas*CyonEmu.KAMAS;
	}
	
	public static int calculElementChangeChance(int lvlM,int lvlA,int lvlP)
	{
		int K = 350;
		if(lvlP == 1)K = 100;
		else if (lvlP == 25)K = 175;
		else if (lvlP == 50)K = 350;
		return (int)((lvlM*100)/(K + lvlA));
	}

	public static int calculHonorWin(ArrayList<Fighter> winner, ArrayList<Fighter> looser, Fighter F) {
		float totalGradeWin = 0;
		float totalLevelWin = 0;
		float totalGradeLoose = 0;
		float totalLevelLoose = 0;
		boolean Prisme = false;
		int fighters = 0;
		for (Fighter  f : winner) {
			if (f.getPersonnage() == null && f.getPrisme() == null)
				continue;
			if (f.getPersonnage() != null) {
				totalLevelWin += f.get_lvl();
				totalGradeWin += f.getPersonnage().getGrade();
			} else {
				Prisme = true;
				totalLevelWin += (f.getPrisme().getlevel() * 15 + 80);
				totalGradeWin += f.getPrisme().getlevel();
			}
		}
		for (Fighter f : looser) {
			if (f.getPersonnage() == null && f.getPrisme() == null)
				continue;
			if (f.getPersonnage() != null) {
				totalLevelLoose += f.get_lvl();
				totalGradeLoose += f.getPersonnage().getGrade();
				fighters++;
			} else {
				Prisme = true;
				totalLevelLoose += (f.getPrisme().getlevel() * 15 + 80);
				totalGradeLoose += f.getPrisme().getlevel();
			}
		}
		if (!Prisme)
			if (totalLevelWin - totalLevelLoose > 15 * fighters)
				return 0;
		int base = (int) (100 * (float) ( (totalGradeLoose * totalLevelLoose) / (totalGradeWin * totalLevelWin)))
				/ winner.size();
		if (Prisme && base <= 0)
			return 100;
		if (looser.contains(F))
			base = -base;
		return base * CyonEmu.HONOR;
	}
	
	public static Couple<Integer, Integer> decompPierreAme(Objet toDecomp)
	{
		Couple<Integer, Integer> toReturn;
		String[] stats = toDecomp.parseStatsString().split("#");
		int lvlMax = Integer.parseInt(stats[3],16);
		int chance = Integer.parseInt(stats[1],16);
		toReturn = new Couple<Integer,Integer>(chance,lvlMax);
		
		return toReturn;
	}
	
	public static int totalCaptChance(int pierreChance, Personnage p)
	{
		int sortChance = 0;

		if(p.getSortStatBySortIfHas(413) == null) return pierreChance;
		switch(p.getSortStatBySortIfHas(413).getLevel())
		{
			case 1:
				sortChance = 1;
				break;
			case 2:
				sortChance = 3;
				break;
			case 3:
				sortChance = 6;
				break;
			case 4:
				sortChance = 10;
				break;
			case 5:
				sortChance = 15;
				break;
			case 6:
				sortChance = 25;
				break;
		}
		
		return sortChance + pierreChance;
	}
	
	public static String parseReponse(String reponse)
	{
		StringBuilder toReturn = new StringBuilder("");
		
		String[] cut = reponse.split("[%]");
		
		if(cut.length == 1)return reponse;
		
		toReturn.append(cut[0]);
		
		char charact;
		for (int i = 1; i < cut.length; i++)
		{
			charact = (char) Integer.parseInt(cut[i].substring(0, 2),16);
			toReturn.append(charact).append(cut[i].substring(2));
		}
		
		return toReturn.toString();
	}
	
	public static int spellCost(int nb)
	{
		int total = 0;
		for (int i = 1; i < nb ; i++)
		{
			total += i;
		}
		
		return total;
	}
	
	public static int ChanceFM(int poidItemBase, int poidItemActual, int poidBaseJet, int poidActualJet, double poidRune, int Puis, double Coef)
	{
	       int Chance = 0;
	       int a = poidItemBase + poidBaseJet + Puis * CyonEmu.PORC_FM;;
	       int b = (int)Math.sqrt(poidItemActual + poidActualJet + poidRune);
	       if (b <= 0) b = 1;
	          Chance = (int)Math.floor((a / b * Coef)/10);
	    return Chance;
	}
	
	public static long getTraqueXP(int lvl)
	{
		long minExp = World.getPersoXpMin(lvl);
		long maxExp = World.getPersoXpMax(lvl);
		long newExp = (long) (((maxExp - minExp) / 100.0));
		return newExp;
	}
	
	public static int getLoosEnergy(int lvl, boolean isAgression, boolean isPerco)
	{
		int returned = 5*lvl;
		if(isAgression) returned *= (7/4);
		if(isPerco) returned *= (3/2);
		return returned;
	}
	
	public static int getRandomChallenge(Personnage _perso)
	{
	   int challenge = 0;
	   
	      challenge++; 
	   
	   if(_perso.get_curCarte().hasEndFightAction(0))
	   {
	      challenge++;
	   }
	   return challenge;
	}
	
	public static long XPDefie(objects.Fight.Fighter perso, @SuppressWarnings("rawtypes") ArrayList winners, @SuppressWarnings("rawtypes") ArrayList looser)
	  {
	      int lvlLoosers = 0;
	      for(@SuppressWarnings("rawtypes")
		Iterator iterator = looser.iterator(); iterator.hasNext();)
	      {
	          objects.Fight.Fighter entry = (objects.Fight.Fighter)iterator.next();
	          lvlLoosers += entry.get_lvl();
	      }

	      int lvlWinners = 0;
	      for(@SuppressWarnings("rawtypes")
		Iterator iterator1 = winners.iterator(); iterator1.hasNext();)
	      {
	          objects.Fight.Fighter entry = (objects.Fight.Fighter)iterator1.next();
	          lvlWinners += entry.get_lvl();
	      }

	      int taux = CyonEmu.XP_PVP;
	      float rapport = (float)lvlLoosers / (float)lvlWinners;
	      int malus = 1;
	      if((double)rapport < 0.84999999999999998D)
	          malus = 6;
	      if(rapport >= 1.0F)
	          malus = 1;
	      long xpWin = (long)(((rapport * (float)getXpNeededAtLevel(perso.getPersonnage().get_lvl())) / 10F) * (float)taux) / (long)malus;
	      return xpWin;
	  }
	
	public static int getRandomAbi(int i1,int i2, int i3)
	{
		int Luck = 0;
		int Random = Formulas.getRandomValue(1, 10);
        switch(Random)
        {
               case 1:
                	Luck = i1;
               break;
               case 2:
                	Luck = i2;
               break;
               case 3:
                	Luck = i3;
               break;
               case 4:
               		Luck = 0;
               break;
               case 5:
               		Luck = 0;
               break;
               case 6:
               		Luck = 0;
               break;
               case 7:
               		Luck = 0;
               break;
               case 8:
               		Luck = 0;
               break;
               case 9:
            	   	Luck = 0;
               break;
               case 10:
               		Luck = 0;
               break;
        }
        return Luck;
	}
	
}


