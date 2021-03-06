package me.kadarh.mecaworks.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.kadarh.mecaworks.domain.others.Groupe;
import me.kadarh.mecaworks.repo.others.GroupeRepo;
import me.kadarh.mecaworks.service.GroupeService;
import me.kadarh.mecaworks.service.exceptions.OperationFailedException;
import me.kadarh.mecaworks.service.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author kadarH
 */

@Service
@Transactional
@Slf4j
public class GroupeServiceImpl implements GroupeService {

	private GroupeRepo groupeRepo;

	public GroupeServiceImpl(GroupeRepo groupeRepo) {
		this.groupeRepo = groupeRepo;
	}

	/**
	 * @param groupe to add
	 * @return the Groupe
	 */
	@Override
	public Groupe add(Groupe groupe) {
		log.info("Service= GroupeServiceImpl - calling methode add");
		try {
			return groupeRepo.save(groupe);
		} catch (Exception e) {
			log.debug("cannot add groupe , failed operation");
			throw new OperationFailedException("L'ajout du groupe a echouée ", e);
		}
	}

	/**
	 * @param groupe to add
	 * @return the Groupe
	 */
	@Override
	public Groupe update(Groupe groupe) {
		log.info("Service = GroupeServiceImpl - calling methode update");
		try {
			Groupe old = get(groupe.getId());
			if (groupe.getNom() != null) {
				old.setNom(groupe.getNom());
			}
			if (groupe.getLocataire() != null) {
				old.setLocataire(groupe.getLocataire());
			}
			return groupeRepo.save(old);
		} catch (Exception e) {
			log.debug("cannot update Groupe , failed operation");
			throw new OperationFailedException("La modification du Groupe a echouée ", e);
		}
	}

	/**
	 * search with nom
	 *
	 * @param pageable page description
	 * @param search   keyword
	 * @return Page
	 */
	@Override
	public Page<Groupe> groupesList(Pageable pageable, String search) {
		log.info("Service- GroupeServiceImpl Calling GroupeList with params :" + pageable + ", " + search);
		try {
			if (search.isEmpty()) {
				log.debug("fetching Groupe page");
				return groupeRepo.findAll(pageable);
			} else {
				log.debug("Searching by :" + search);
				//creating example
				Groupe groupe = new Groupe();
				groupe.setNom(search);
				//creating matcher
				ExampleMatcher matcher = ExampleMatcher.matchingAny()
						.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
						.withIgnoreCase()
						.withIgnoreNullValues();
				Example<Groupe> example = Example.of(groupe, matcher);
				log.debug("getting search results");
				return groupeRepo.findAll(example, pageable);
			}
		} catch (Exception e) {
			log.debug("Failed retrieving list of groupes");
			throw new OperationFailedException("Operation échouée", e);
		}
	}


	@Override
	public Groupe get(Long id) {
		log.info("Service-GroupeServiceImpl Calling getGroupe with params :" + id);
		try {
			return groupeRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			log.info("Problem , cannot find Groupe with id = :" + id);
			throw new ResourceNotFoundException("Groupe introuvable", e);
		} catch (Exception e) {
			log.info("Problem , cannot get Fournisseur with id = :" + id);
			throw new OperationFailedException("Problème lors de la recherche du Groupe", e);
		}
	}

	/**
	 * @param id of Groupe to delete
	 */
	@Override
	public void delete(Long id) {
		log.info("Service= GroupeServiceImpl - calling methode delete");
		try {
			groupeRepo.deleteById(id);
		} catch (Exception e) {
			log.debug("cannot delete Groupe , failed operation");
			throw new OperationFailedException("La suppression du groupe a echouée ", e);
		}
	}

	/**
	 * @return List of groupes in database
	 */
	@Override
	public List<Groupe> list() {
		log.info("Service= GroupeServiceImpl - calling methode list");
		try {
			return groupeRepo.findAll();
		} catch (Exception e) {
			log.debug("Failed retrieving list of groupes");
			throw new OperationFailedException("Operation échouée lors de la recherche de la liste des groupes", e);
		}
	}


}
