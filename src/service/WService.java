package service;

import java.util.ArrayList;
import java.util.List;


import javax.ws.rs.*;
import com.google.gson.Gson;


import meserreurs.MonException;
import metier.*;
import persistance.DialogueBd;

@Path("/mediatheque")
public class WService {


	/***************************************************/
	/***************Partie sur les adhérents **************/
	/*****************************************************/
	@POST
	@Path("/Adherents/ajout/{unAdh}")
	@Consumes("application/json")	
	public void insertionAdherent(String unAdherent) throws MonException {
		DialogueBd unDialogueBd = DialogueBd.getInstance();
		Gson gson = new Gson();
		Adherent unAdh = gson.fromJson(unAdherent, Adherent.class);
		try {
			String mysql = "";
			mysql = "INSERT INTO adherent (nom_adherent, prenom_adherent, ville_adherent) ";
			mysql += " VALUES ( \'" + unAdh.getNomAdherent()+ "\', \'" + unAdh.getPrenomAdherent();
			mysql+="  \', \'"  + unAdh.getVilleAdherent() +  "\') ";
			unDialogueBd.insertionBD(mysql);

		} catch (MonException e) {
			throw e;
		}
	}


	@POST
	@Path("/Adherents/modifier/{unAdh}")
	@Consumes("application/json")	
	public void modifierAdherent(String unAdherent) throws MonException {
		DialogueBd unDialogueBd = DialogueBd.getInstance();
		Gson gson = new Gson();
		Adherent adherent = gson.fromJson(unAdherent, Adherent.class);
		String mysql;
		try {
			mysql = "UPDATE adherent SET "+
					"nom_adherent = '"+adherent.getNomAdherent()+"', "+
					"prenom_adherent = '"+adherent.getPrenomAdherent()+"', "+
					"ville_adherent = '"+adherent.getVilleAdherent()+"' "+
					"WHERE id_adherent = "+adherent.getIdAdherent();

			unDialogueBd.insertionBD(mysql);
		} catch (MonException e) {
			throw e;
		}
	}

	/**
	 * rechecher un adherent
	 * @return
	 * @throws MonException
	 */
	@GET
	@Path("/Adherents/{Id}")
	@Consumes("application/json")
	public String getAdherent(@PathParam("Id")int numero) throws MonException {

		String requete = "select * from adherent where id_adherent=" + numero;

		List<Object> rs;
		Adherent unAd = null;
		try 
		{
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			rs = unDialogueBd.lecture(requete);
			if (rs.size() > 0) {

				unAd = new Adherent();
				unAd.setIdAdherent(Integer.parseInt(rs.get(0).toString()));
				unAd.setNomAdherent(rs.get(1).toString());
				unAd.setPrenomAdherent(rs.get(2).toString());
				unAd.setVilleAdherent(rs.get(3).toString());
			}
		}

		catch (MonException e) {
			throw e;
		}

		Gson gson = new Gson();
		String adherent = gson.toJson(unAd);
		return adherent;

	}

	@GET
	@Path("/Adherents")
	@Produces("application/json")
	public String rechercheLesAdherents() throws MonException {
		List<Object> rs;
		List<Adherent> mesAdherents = new ArrayList<Adherent>();
		int index = 0;
		try {
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			String mysql = "";

			mysql = "SELECT * FROM adherent ORDER BY id_adherent ASC";

			rs = unDialogueBd.lecture(mysql);

			while (index < rs.size()) {

				// On crï¿½e un objet Adherent
				Adherent unAdh = new Adherent();
				unAdh.setIdAdherent(Integer.parseInt(rs.get(index + 0).toString()));
				unAdh.setNomAdherent(rs.get(index + 1).toString());
				unAdh.setPrenomAdherent(rs.get(index + 2).toString());
				unAdh.setVilleAdherent(rs.get(index + 3).toString());
				index = index + 4;

				mesAdherents.add(unAdh);
			}		

			Gson gson = new Gson();
			String json = gson.toJson(mesAdherents);
			return json;

		} catch (MonException e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	/**
	 * rechercher la liste de tous les propriétaires
	 */


	/***************************************************/
	/***************Partie sur les oeuvres  **************/
	/*****************************************************/


	@POST
	@Path("/Oeuvre/ajout/{uneOvr}")
	@Consumes("application/json")	
	public void insererOeuvre(String oeuvre) throws MonException {
		DialogueBd unDialogueBd = DialogueBd.getInstance();
		Gson gson = new Gson();
		Oeuvrevente oVente = gson.fromJson(oeuvre, Oeuvrevente.class);
		try {
			String mysql = "INSERT INTO oeuvrevente  (titre_oeuvrevente, etat_oeuvrevente,prix_oeuvrevente, id_proprietaire) values (" +
					"'" + oVente.getTitreOeuvrevente() +
					"','" + oVente.getEtatOeuvrevente() + 
					"','" + oVente.getPrixOeuvrevente() +
					"','" + oVente.getProprietaire().getIdProprietaire() + 
					"')";
			unDialogueBd.insertionBD(mysql);

		} catch (MonException e) {
			throw e;
		}
	}


	@GET
	@Path("/Oeuvres/{Id}")
	@Produces("application/json")
	public String rechercherOeuvreId(@PathParam("Id")  String idOeuvre) throws MonException, Exception
	{

		String mysql = "";
		String json ="";
		Oeuvrevente uneOeuvre;
		try
		{
			mysql = "SELECT id_oeuvrevente, titre_oeuvrevente, etat_oeuvrevente,prix_oeuvrevente,id_proprietaire";
			mysql += " FROM Oeuvrevente WHERE id_Oeuvrevente = " + idOeuvre + ";";
			uneOeuvre = rechercherOeuvre(mysql); 
			Gson gson = new Gson();
			json = gson.toJson(uneOeuvre);

		} catch (MonException e)
		{
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
		return json;
	}

	@GET
	@Path("/Oeuvres/lib/{libelle}")
	@Produces("application/json")
	public String rechercheOeuvreLibelle(@PathParam("libelle") String lib) throws MonException, Exception
	{
		String json="";
		String  mysql;
		Oeuvrevente uneOeuvre;
		try
		{
			mysql = "select id_oeuvrevente, titre_oeuvrevente, etat_oeuvrevente,prix_oeuvrevente,id_proprietaire";
			mysql += " from Oeuvrevente where titre_oeuvrevente = " + lib+ ";";
			uneOeuvre = rechercherOeuvre(mysql);
			Gson gson = new Gson();
			json = gson.toJson(uneOeuvre);

		} catch (MonException e) {
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
		return json;
	}


	// recherche d'une Oeuvre
	// On factorise la requête qui doit rendre une oeuvre en vente
	public Oeuvrevente rechercherOeuvre(String requete) throws MonException
	{

		List<Object> rs;
		Oeuvrevente uneOeuvre=null;
		try
		{
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			rs = unDialogueBd.lecture(requete);
			if (rs.size() > 0) {

				uneOeuvre = new Oeuvrevente();
				uneOeuvre.setIdOeuvrevente(Integer.parseInt(rs.get(0).toString()));
				uneOeuvre.setTitreOeuvrevente(rs.get(1).toString());
				uneOeuvre.setEtatOeuvrevente(rs.get(2).toString());
				uneOeuvre.setPrixOeuvrevente(Float.parseFloat(rs.get(3).toString()));
				int id = Integer.parseInt(rs.get(4).toString());
				Gson gson = new Gson();
				// On appelle la recherche d'un propriétaire
				uneOeuvre.setProprietaire(gson.fromJson(rechercherProprietaire(id),Proprietaire.class));
			}
		}

		catch (MonException e) {
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
		return uneOeuvre;

	}

	//****************************
	// Recherche d'un propriétaire 
	//****************************
	@GET
	@Path("/Proprietaire/{Id}")
	@Produces("application/json")
	public String rechercherProprietaire(@PathParam("Id")int  id) throws MonException
	{

		List<Object> rs;
		Proprietaire  unProprietaire=null;
		String requete = " select * from Proprietaire where id_Proprietaire =" + id + ";";
		try 
		{
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			rs = unDialogueBd.lecture(requete);
			if (rs.size() > 0) {

				unProprietaire = new Proprietaire();

				unProprietaire.setIdProprietaire(Integer.parseInt(rs.get(0).toString()));
				unProprietaire.setNomProprietaire(rs.get(1).toString());
				unProprietaire.setPrenomProprietaire(rs.get(2).toString());
			}
		}

		catch (MonException e) {
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
		Gson gson = new Gson();
		String proprietaire = gson.toJson(unProprietaire);
		return proprietaire;
	}

	/**
	 * liste des propriétaires
	 * @throws MonException 
	 */
	@GET
	@Path("/Proprietaires")
	@Produces("application/json")
	public String getListeProprietaires () throws MonException {

		List<Object> rs;
		List<Proprietaire> mesProprietaires = new ArrayList<Proprietaire>();
		int index = 0;
		try {
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			rs = unDialogueBd.lecture("select * from proprietaire");

			while (index < rs.size()) {

				Proprietaire unP = new Proprietaire();
				// il faut redecouper la liste pour retrouver les lignes
				unP.setIdProprietaire(Integer.parseInt(rs.get(index + 0).toString()));
				unP.setNomProprietaire(rs.get(index + 1).toString());
				unP.setPrenomProprietaire(rs.get(index + 2).toString());
				// incrémenter tous les 3 champs
				index = index + 3;
				mesProprietaires.add(unP);
			}
			Gson gson = new Gson();
			String json = gson.toJson(mesProprietaires);
			return json;
		} catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
	}

	//****************************
	// Liste des oeuvres 
	//****************************


	@GET
	@Path("/Oeuvres")
	@Produces("application/json")
	public  String  consulterListeOeuvre() throws MonException {
		List<Object> rs;
		List<Oeuvrevente> mesOeuvres = new ArrayList<Oeuvrevente>();
		String mysql="";

		int index = 0;
		try {
			mysql = "SELECT id_oeuvrevente, titre_oeuvrevente";
			mysql += " FROM Oeuvrevente order by titre_oeuvrevente ;";
			DialogueBd unDialogueBd = DialogueBd.getInstance();
			rs =unDialogueBd.lecture(mysql);
			while (index < rs.size()) {
				// On crée un stage
				Oeuvrevente uneOeuvre = new Oeuvrevente();
				// il faut redecouper la liste pour retrouver les lignes
				uneOeuvre.setIdOeuvrevente(Integer.parseInt(rs.get( index + 0).toString()));
				uneOeuvre.setTitreOeuvrevente(rs.get( index + 1 ).toString());
				// On incrémente tous les 2 champs
				index = index + 2;
				mesOeuvres.add(uneOeuvre);
			}
			Gson gson = new Gson();
			String json = gson.toJson(mesOeuvres);
			return json;
		} catch (MonException e) {
			throw e;
		}
		catch (Exception exc) {
			throw new MonException(exc.getMessage(), "systeme");
		}
	}


}
