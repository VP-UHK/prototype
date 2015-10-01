package model;

import java.util.LinkedList;
import java.util.List;

import agent.Inventory;

public class Agent {

	protected int ID;
	protected String typ;
	protected String produkce;
	protected String poptavka;
	protected List<Agent> partner; // seznam agent� se kter�mi �asto spolupracuje,
	
	public static List<Agent> listAgentu = new LinkedList<Agent>();
								
	/*n�hodn� mno�ina soused� a zt� vyb�r�m, kdy� nenajdu tak se zept�m n�koho jin�ho jesili nezn�, algoritmus na seznamov�n�, FIPA specifikace na zpr�vi*/
	protected List<String> chovani;
	protected Fronta fronata;

	protected Inventory inventory;

	public Agent(int id, String typ, String produkce, String poptavka, List<Agent> partnerr, List<String> chovani, Fronta fronata) {
		super();
		this.ID = id;
		this.typ = typ;
		this.produkce = produkce;
		this.poptavka = poptavka;
		this.partner = partnerr;
		this.chovani = chovani;
		this.fronata = fronata;
		this.inventory = new Inventory();
		listAgentu.add(this);
	}

	public int getId() {
		return ID;
	}

	public void setId(int id) {
		this.ID = id;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getProdukce() {
		return produkce;
	}

	public void setProdukce(String produkce) {
		this.produkce = produkce;
	}

	public String getPoptavka() {
		return poptavka;
	}

	public void setPoptavka(String poptavka) {
		this.poptavka = poptavka;
	}

	public List<Agent> getPartnerr() {
		return partner;
	}

	public void setPartnerr(List<Agent> partner) {
		this.partner = partner;
	}

	public List<String> getChovani() {
		return chovani;
	}

	public void setChovani(List<String> chovani) {
		this.chovani = chovani;
	}

	public Fronta getFronata() {
		return fronata;
	}

	public void setFronata(Fronta fronata) {
		this.fronata = fronata;
	}

	protected void Do() {
		Work();
		Repair();
		Shop();
	}


	public void Work() {
		if (produkce == "food") {
			if (inventory.getFood() <= 50)
				inventory.workFood();
			else
				inventory.rest();
		}
		if (produkce == "painkiller") {
			if (inventory.getPainkiller() <= 15)
				inventory.workPainkiller();
			else
				inventory.rest();
		}
		if (produkce == "tool") {
			if (inventory.getTool() <= 5)
				inventory.workTool();
			else
				inventory.rest();
		}
	}

	public void Repair() {
		if (inventory.getToolHealth() < 10 && inventory.getTool() > 0)
			inventory.useTool();
		else if (inventory.getHealth() <= 90 && inventory.getPainkiller() > 0)
			inventory.usePainkiller();
		else if (inventory.getHunger() <= 97 && inventory.getFood() > 0)
			inventory.useFood();
	}

	public void Shop() {
		Agent a;
		if (produkce == "food" && inventory.getFood()>1) {
			a=VyhledejAgenta("food");
			//TODO Zprava 
			Zprava zprava = new Zprava(""/*chce� obchodovat?*/, a.getId(), ID,"typ");
			this.inventory.sellFood();
			a.inventory.buyFood();
		}
		if (produkce == "painkiller" && inventory.getPainkiller()>1) {
			a=VyhledejAgenta("painkiller");
			//TODO Zprava
			Zprava zprava = new Zprava(""/*chce� obchodovat?*/, a.getId(), ID,"typ");
			this.inventory.sellPainkiller();
			a.inventory.buyPainkiller();
		}
		if (produkce == "tool" && inventory.getTool()>1) {
			a=VyhledejAgenta("tool");
			//TODO Zprava
			Zprava zprava = new Zprava(""/*chce� obchodovat?*/, a.getId(), ID,"typ");
			this.inventory.sellTool();
			a.inventory.buyTool();
		}
	}
	
	protected Agent VyhledejAgenta(String filter) {

		Agent a = null;
		
		if (filter=="food")
		for (Agent agent : partner) {
			if(agent.inventory.getFood()<=50 && agent.getProdukce()!="food" && agent.inventory.getFinance()>=1){
				return a;
			}
		}
		if (filter=="painkiller")
			for (Agent agent : partner) {
				if(agent.inventory.getPainkiller()<=15 && agent.getProdukce()!="painkiller" && agent.inventory.getFinance()>=10){
					return a;
				}
			}
		if (filter=="tool")
			for (Agent agent : partner) {
				if(agent.inventory.getTool()<=5 && agent.getProdukce()!="tool" && agent.inventory.getFinance()>=50){
					return a;
				}
			}
		
				return a;
		
	}
	
	public void setProdukce() {
		double r = Math.random() * 100;
		String produkce;
		if (r < 70)
			produkce="food";
		else if(r < 90)
			produkce="painkiller";
		else
			produkce="tool";
		this.produkce = produkce;
	}
	
	protected void VyhledejAgentaProSouseda(String filtrovaciParametry, int komu) {
		//TODO Kdy� agenta jin� agent po��d�, jesli nem�me ve sv�m seznamu agenta  spl�uj�c� kryt�ria,
		//vyhled�me a po�leme mu nasp�t, ve zpr�v�
		// tak �e zavolame VyhledejAgenta na na�em seznamu Partner.
		//V�sledn�ho agenta po�lem ve zpr�v� na zp�t. Pokud nebudem m�t takov�ho tak nic. Info ohledne fungovan� zpr�v 
		// u Pepy
		
		Agent a = VyhledejAgenta(filtrovaciParametry);
		
		if(a == null){
			//po�li zpr�vu zamitase
			Zprava zprava = new Zprava("CANCEL", komu, ID,"typ");
			//TODO posly zpr�vu
			
		}else {
			//po�li zpravu s agentem
			Zprava zprava = new Zprava(Integer.toString(getId()), komu, ID,"typ");
			//TODO po�li zpr�vu
		}
		
		
	}
	public static Agent najdi(int i){
		for(Agent a : listAgentu){
			if(a.ID == i){
				return a;
			}
		}
		return null;
	}
}
