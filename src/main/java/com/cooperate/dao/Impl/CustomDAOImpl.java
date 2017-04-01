package com.cooperate.dao.Impl;

import com.cooperate.dao.CustomDAO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Кирилл on 01.04.2017.
 */
public class CustomDAOImpl implements CustomDAO {

    private static final String SUM_CONTRIBUTE = "SELECT (sum(c.contribute)+sum(c.contLand)+sum(c.contTarget)+sum(c.fines)) AS SUM " +
            "FROM contribution c INNER JOIN garag_contribution gc ON gc.contributions_id_count=c.id_count WHERE gc.Garag_id_garag = :idGarag";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Float getSumContribution(Integer id) {
        return Float.valueOf(em.createNativeQuery(SUM_CONTRIBUTE).setParameter("idGarag", id).getSingleResult().toString());
    }
}
