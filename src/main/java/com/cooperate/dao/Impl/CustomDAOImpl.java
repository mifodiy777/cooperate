package com.cooperate.dao.Impl;

import com.cooperate.dao.CustomDAO;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Person;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Кирилл on 01.04.2017.
 */
public class CustomDAOImpl implements CustomDAO {

    private static final String SUM_CONTRIBUTE = "SELECT (sum(c.contribute)+sum(c.contLand)+sum(c.contTarget)+sum(c.fines)) AS SUM " +
            "FROM contribution c INNER JOIN garag_contribution gc ON gc.contributions_id_count=c.id_count WHERE gc.Garag_id_garag = :idGarag";

    @PersistenceContext
    private EntityManager em;

    /**
     * Метод вычисления суммы общего долга по определенному гаражу
     *
     * @param id Гаража
     * @return сумма
     */
    @Override
    public Float getSumContribution(Integer id) {
        return Float.valueOf(em.createNativeQuery(SUM_CONTRIBUTE).setParameter("idGarag", id).getSingleResult().toString());
    }

    /**
     * Метод определения существования гаража в базе
     *
     * @param garag Гараж из запроса
     * @return true - гараж существует
     */
    @Override
    public Boolean existGarag(Garag garag) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = builder.createQuery(Long.class);
        Root<Garag> root = cq.from(Garag.class);
        cq.select(builder.count(root));
        List<Predicate> predicates = new ArrayList<>();
        if (garag.getId() != null) {
            predicates.add(builder.notEqual(root.get("id"), garag.getId()));
        }
        predicates.add(builder.equal(root.get("series"), garag.getSeries()));
        predicates.add(builder.equal(root.get("number"), garag.getNumber()));
        cq.where(predicates.toArray(new Predicate[]{}));
        return em.createQuery(cq).getSingleResult() > 0;
    }

}
