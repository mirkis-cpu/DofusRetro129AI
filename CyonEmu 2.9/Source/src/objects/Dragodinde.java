package objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import objects.Objet.ObjTemplate;
import objects.Personnage.Stats;

import common.Constants;
import common.CyonEmu;
import common.Formulas;
import common.SQLManager;
import common.SocketManager;
import common.World;

public class Dragodinde {

	private int _id;
	private int _color;
	private int _sexe;
	private int _amour;
	private int _endurance;
	private int _level;
	private long _exp;
	private String _nom;
	private int _fatigue;
	private int _energie;
	private int _reprod;
	private int _maturite;
	private int _serenite;
	private Stats _stats = new Stats();
	private String _ancetres = ",,,,,,,,,,,,,";
	private String _ability = ",";
	private ArrayList<Objet> _items = new ArrayList<Objet>();
	private List<Integer> capacity = new ArrayList<Integer>();
	
	
	public Dragodinde(int color)
	{
		_id = World.getNextIdForMount();
		_color = color;
		_sexe = Formulas.getRandomValue(0, 1);
		_level = 1;
		_exp = 0;
		_nom = "Sans Nom";
		_fatigue = 0;
		_energie = getMaxEnergie();
		_reprod = 0;
		_maturite = getMaxMatu();
		_serenite = 0;
		_stats = Constants.getMountStats(_color,_level);
		_ancetres = ",,,,,,,,,,,,,";
		_ability = "" + Formulas.getRandomAbi(1, 4, 9) + "";
		World.addDragodinde(this);
		SQLManager.CREATE_MOUNT(this);
	}
	
	public Dragodinde(int id, int color, int sexe, int amour, int endurance,
			int level, long exp, String nom, int fatigue,
			int energie, int reprod, int maturite, int serenite,String items,String anc,String ability)
	{
		_id = id;
		_color = color;
		_sexe = sexe;
		_amour = amour;
		_endurance = endurance;
		_level = level;
		_exp = exp;
		_nom = nom;
		_fatigue = fatigue;
		_energie = energie;
		_reprod = reprod;
		_maturite = maturite;
		_serenite = serenite;
		_ancetres = anc;
		_stats = Constants.getMountStats(_color,_level);
		_ability = ability;
		
		for (String s : ability.split(",", 2))
			if (s != null) {
				int a = Integer.parseInt(s);
				try {
					this.capacity.add(Integer.valueOf(a));
				} catch (Exception localException) {}
			}
		
		for (String str : items.split(";")) {
			try {
				Objet obj = World.getObjet(Integer.parseInt(str));
				if (obj != null)
					_items.add(obj);
			} catch (Exception e) {
				continue;
			}
		}
	}

	public int get_id() {
		return _id;
	}

	public int get_color() {
		return _color;
	}
	
	public String get_color(String a)
	{
		String b = "";
		if (capacity.contains(Integer.valueOf(9))) 
			b = b + "," + a;
		return _color + b;
	}
	
	public int get_sexe() {
		return _sexe;
	}

	public int get_amour() {
		return _amour;
	}

	public String get_ancetres() {
		return _ancetres;
	}

	public int get_endurance() {
		return _endurance;
	}
	public int get_level() {
		return _level;
	}

	public long get_exp() {
		return _exp;
	}

	public String get_nom() {
		return _nom;
	}

	public int get_fatigue() {
		return _fatigue;
	}

	public int get_energie() {
		return _energie;
	}

	public int get_reprod() {
		return _reprod;
	}

	public int get_maturite() {
		return _maturite;
	}

	public int get_serenite() {
		return _serenite;
	}

	public Stats get_stats() {
		return _stats;
	}

	public ArrayList<Objet> get_items() {
		return _items;
	}
	
	public String parse()
	{
		StringBuilder str = new StringBuilder();
		str.append(_id).append(":");
		str.append(_color).append(":");
		str.append(_ancetres).append(":");
		str.append(",,").append(_ability).append(":");
		str.append(_nom).append(":");
		str.append(_sexe).append(":");
		str.append(parseXpString()).append(":");
		str.append(_level).append(":");
		str.append("1").append(":");//FIXME
		str.append(getTotalPod()).append(":");
		str.append("0").append(":");//FIXME podActuel?
		str.append(_endurance).append(",10000:");
		str.append(_maturite).append(",").append(getMaxMatu()).append(":");
		str.append(_energie).append(",").append(getMaxEnergie()).append(":");
		str.append(_serenite).append(",-10000,10000:");
		str.append(_amour).append(",10000:");
		str.append("-1").append(":");//FIXME
		str.append("0").append(":");//FIXME
		str.append(parseStats()).append(":");
		str.append(_fatigue).append(",240:");
		str.append(_reprod).append(",20:");
		return str.toString();
	}

	private String parseStats()
	{
		String stats = "";
		for(Entry<Integer,Integer> entry : _stats.getMap().entrySet())
		{
			if(entry.getValue() <= 0)continue;
			if(stats.length() >0)stats += ",";
			stats += Integer.toHexString(entry.getKey())+"#"+Integer.toHexString(entry.getValue())+"#0#0";
		}
		return stats;
	}

	private int getMaxEnergie()
	{
		int energie = 10000;
		return energie;
	}

	private int getMaxMatu()
	{
		int matu = 1000;
		return matu;
	}

	private int getTotalPod()
	{
		if(isPorteuse() == true)
			return 500 + _level * 25;
		return 500 + _level * 15;
	}
	
	public int getMaxPod() {
		return getTotalPod();
	}

	private String parseXpString()
	{
		return _exp+","+World.getExpLevel(_level).dinde+","+World.getExpLevel(_level+1).dinde;
	}

	public boolean isMountable()
	{
		if(_energie <10
		|| _maturite < getMaxMatu()
		|| _fatigue == 240)return false;
		return true;
	}

	public void setName(String packet)
	{
		_nom = packet;
		SQLManager.UPDATE_MOUNT_INFOS(this);
	}
		
	public void addXp(long amount)
	{
		_exp += amount;

		while(_exp >= World.getExpLevel(_level+1).dinde && _level < CyonEmu.CONFIG_LVLMAXMONTURE)
			levelUp();
		
	}
	
	public void levelUp()
	{
		_level++;
		_stats = Constants.getMountStats(_color,_level);
		if(isInfatiguable() == true) {
			_energie = _energie + 130;
			if(_energie > getMaxEnergie()) _energie = getMaxEnergie();
		} else {
			_energie = _energie + 90;
			if(_energie > getMaxEnergie()) _energie = getMaxEnergie();
		}
		
	}
	
	public void setEnergie(int energie)
    {
    	_energie = energie;
    }
	
	public void castred() {
		_reprod = -1;
	}
	
	public String get_ability() {
		return _ability;
	}
	
	public boolean addCapacity(String capacitys) {
		int c = 0;
		for (String s : capacitys.split(",", 2)) {
			if (capacity.size() >= 2) 
				return false; 
			try
			{
				c = Integer.parseInt(s); 
			} catch (Exception localException) {}
			
			if (c != 0)
				capacity.add(Integer.valueOf(c));
			
			if (capacity.size() == 1)
				_ability = (capacity.get(0) + ",");
			else
				_ability = (capacity.get(0) + "," + this.capacity.get(1));
		}
		return true;
	}
	
	public boolean isInfatiguable() {
		return capacity.contains(Integer.valueOf(1));
	}
	
	public boolean isPorteuse() {
		return capacity.contains(Integer.valueOf(2));
	}
	
	public boolean isSage() {
		return capacity.contains(Integer.valueOf(4));
	}

	public boolean isCameleone() {
		return capacity.contains(Integer.valueOf(9));
	}
	
	public int get_podsActuels() {
		int pods = 0;
		for(Objet value : _items)
		{
			if (value == null)
				continue;
			pods += value.getTemplate().getPod() * value.getQuantity(); 
		}
		return pods;
	}
	
	public void addObjDinde(int id, int cant, Personnage perso) {
		Objet objetAdd = World.getObjet(id);
		if (objetAdd.getPosition() != -1)
			return;
		Objet objSimiler = getObjetSimiler(objetAdd);
		int newCant = objetAdd.getQuantity() - cant;
		if (objSimiler == null) {
			if (newCant <= 0) {
				perso.removeItem(objetAdd.getGuid());
				_items.add(objetAdd);
				String str = "O+" + objetAdd.getGuid() + "|" + objetAdd.getQuantity() + "|"
						+ objetAdd.getTemplate().getID() + "|" + objetAdd.parseStatsString();
				SocketManager.GAME_SEND_EsK_PACKET(perso, str);
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(perso, id);
			} else {
				objetAdd.setQuantity(newCant);
				objSimiler = Objet.getCloneObjet(objetAdd, cant);
				World.addObjet(objSimiler, true);
				_items.add(objSimiler);
				String str = "O+" + objSimiler.getGuid() + "|" + objSimiler.getQuantity() + "|"
						+ objSimiler.getTemplate().getID() + "|" + objSimiler.parseStatsString();
				SocketManager.GAME_SEND_EsK_PACKET(perso, str);
				SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(perso, objetAdd);
			}
		} else if (newCant <= 0) {
			perso.removeItem(objetAdd.getGuid());
			objSimiler.setQuantity(objSimiler.getQuantity() + objetAdd.getQuantity());
			String str = "O+" + objSimiler.getGuid() + "|" + objSimiler.getQuantity() + "|"
					+ objSimiler.getTemplate().getID() + "|" + objSimiler.parseStatsString();
			World.removeItem(objetAdd.getGuid());
			SocketManager.GAME_SEND_EsK_PACKET(perso, str);
			SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(perso, id);
		} else {
			objetAdd.setQuantity(newCant);
			objSimiler.setQuantity(objSimiler.getQuantity() + cant);
			String str = "O+" + objSimiler.getGuid() + "|" + objSimiler.getQuantity() + "|"
					+ objSimiler.getTemplate().getID() + "|" + objSimiler.parseStatsString();
			SocketManager.GAME_SEND_EsK_PACKET(perso, str);
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(perso, objetAdd);
		}
		SocketManager.GAME_SEND_Ow_PACKET(perso);
		SocketManager.SEND_Ew_PODS_MONTURE(perso, get_podsActuels());
		SQLManager.UPDATE_MOUNT_INFOS(this);
	}
	
	public void removeFromDinde(int id, int cant, Personnage perso) {
		Objet objremove = World.getObjet(id);
		if (!_items.contains(objremove)) {
			return;
		}
		Objet objSimiler = perso.getSimilarItem(objremove);
		int newCant = objremove.getQuantity() - cant;
		if (objSimiler == null) {
			if (newCant <= 0) {
				_items.remove(objremove);
				if (perso.addObjetSimiler(objremove, true, -1)) {
					World.removeItem(id);
				} else {
					perso.addObjet(objremove);
					SocketManager.GAME_SEND_OAKO_PACKET(perso, objremove);
				}
				String str = "O-" + id;
				SocketManager.GAME_SEND_EsK_PACKET(perso, str);
			} else {
				objSimiler = Objet.getCloneObjet(objremove, cant);
				World.addObjet(objSimiler, true);
				objremove.setQuantity(newCant);
				perso.addObjet(objSimiler);
				SocketManager.GAME_SEND_OAKO_PACKET(perso, objSimiler);
				String str = "O+" + objremove.getGuid() + "|" + objremove.getQuantity() + "|"
						+ objremove.getTemplate().getID() + "|" + objremove.parseStatsString();
				SocketManager.GAME_SEND_EsK_PACKET(perso, str);
			}
		} else if (newCant <= 0) {
			_items.remove(objremove);
			objSimiler.setQuantity(objSimiler.getQuantity() + objremove.getQuantity());
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(perso, objSimiler);
			World.removeItem(objremove.getGuid());
			String str = "O-" + id;
			SocketManager.GAME_SEND_EsK_PACKET(perso, str);
		} else {
			objremove.setQuantity(newCant);
			objSimiler.setQuantity(objSimiler.getQuantity() + cant);
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(perso, objSimiler);
			String str = "O+" + objremove.getGuid() + "|" + objremove.getQuantity() + "|" + objremove.getTemplate().getID()
					+ "|" + objremove.parseStatsString();
			SocketManager.GAME_SEND_EsK_PACKET(perso, str);
		}
		SocketManager.GAME_SEND_Ow_PACKET(perso);
		SocketManager.SEND_Ew_PODS_MONTURE(perso, get_podsActuels());
		SQLManager.UPDATE_MOUNT_INFOS(this);
	}
	
	private Objet getObjetSimiler(Objet obj) {
		for (Objet value : _items) {
			ObjTemplate objetoMod = value.getTemplate();
			if (objetoMod.getType() == 85)
				continue;
			if ((objetoMod.getID() == obj.getTemplate().getID()) && (value.getStats().isSameStats(obj.getStats())))
				return value;
		}
		return null;
	}
	
	public String getItemsId() {
		String str = "";
		for (Objet obj : _items)
			str += (str.length() > 0 ? ";" : "") + obj.getGuid();
		return str;
	}
	
	public String getListeObjDrago() {
		String objets = "";
		for (Objet obj : _items) {
			objets = objets + "O" + obj.parseItem();
		}
		return objets;
	}
	
	public void aumEnergie(int value, int boucle) {
		_energie += value * boucle;
		int maxEnergia = getMaxEnergie();
		if (_energie > maxEnergia)
			_energie = maxEnergia;
	}
}
