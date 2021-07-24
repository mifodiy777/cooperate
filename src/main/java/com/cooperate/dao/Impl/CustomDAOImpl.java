package com.cooperate.dao.Impl;

import com.cooperate.dao.CustomDAO;
import com.cooperate.dto.CostDTO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.CostType;
import com.cooperate.entity.Garag;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Кастомные методы по работе с БД
 * Created by Кирилл on 01.04.2017.
 */
public class CustomDAOImpl implements CustomDAO {

    private static final String SUM_CONTRIBUTE = "SELECT (sum(c.contribute)+sum(c.contLand)+sum(c.contTarget)+sum(c.fines))+g.old_contribute AS SUM " +
            "FROM contribution c INNER JOIN garag_contribution gc ON gc.contributions_id_count=c.id_count " +
            "INNER JOIN garag g ON gc.garag_id_garag=g.id_garag WHERE gc.Garag_id_garag = :idGarag";

    private static final String FIND_FINES = "FROM Contribution c where c.year = :year and c.finesOn = false and (c.contribute + c.contLand + c.contTarget) != 0";

    private static final String FIND_GROUP_COST = "SELECT t.name as type,COUNT(c.id) as count, sum(c.money) as sum from cost c INNER join costtype t " +
            "ON t.id=c.id_type WHERE c.date >= :start and c.date <= :end group by type";

    private static final String DELETE_COST = "delete from cost where id_type = :id";
    private static final String DELETE_TYPE = "delete from costtype where id = :id";

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

    /**
     * Метод получения списка долговых периодов.
     * Есть долги, начисление пеней выключено
     *
     * @param year Год долгового периода
     * @return список долговых периодов
     */
    @Override
    public List<Contribution> findContributionsByFines(Integer year) {
        return (List<Contribution>) em.createQuery(FIND_FINES).setParameter("year", year).getResultList();
    }

    /**
     * Получить список объектов для отчета по типу расхода за период
     *
     * @param start начало периода
     * @param end   конец периода
     * @return список CostDTO
     */
    @Override
    public List<CostDTO> findGroupCost(Calendar start, Calendar end) {
        List<CostDTO> dtoList = em.createNativeQuery(FIND_GROUP_COST)
                .setParameter("start", start, TemporalType.TIMESTAMP)
                .setParameter("end", end, TemporalType.TIMESTAMP)
                .unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(CostDTO.class)).list();
        return dtoList;
    }

    /**
     * Удаление типа расхода
     *
     * @param id ID типа расхода
     */
    @Override
    public void deleteCostType(Integer id) {
        em.createNativeQuery(DELETE_COST).setParameter("id", id).executeUpdate();
        em.createNativeQuery(DELETE_TYPE).setParameter("id", id).executeUpdate();
    }

    /**
     * Проверка на уникальность
     * @param type Тип расхода
     * @return true - не уникально
     */
    @Override
    public boolean existCostType(CostType type) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = builder.createQuery(Long.class);
        Root<CostType> root = cq.from(CostType.class);
        cq.select(builder.count(root));
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.notEqual(root.get("id"), type.getId()));
        predicates.add(builder.equal(root.get("name"), type.getName()));
        cq.where(predicates.toArray(new Predicate[]{}));
        return em.createQuery(cq).getSingleResult() > 0;
    }
}
