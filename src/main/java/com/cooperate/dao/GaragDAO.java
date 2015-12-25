package com.cooperate.dao;

import com.cooperate.entity.Garag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by KuzminKA on 17.06.2015.
 */
@Repository
public interface GaragDAO extends JpaRepository<Garag, Integer> {


    //Метод существования
    //Метод выдает кол-во гаражей с заданным рядом и номером гаража с исключение заданного id гаража.

    Integer countBySeriesAndNumberAndIdNot(String series, String number, Integer id);

    //Метод существования
    //Метод выдает кол-во гаражей с заданным рядом и номером гаража.

    Integer countBySeriesAndNumber(String series, String number);

    //Метод получает всех льготников

    @Query("select g from Garag g inner join g.person p where length(p.benefits) <> 0 ")
    List<Garag> getGaragForPersonBenefits();

    //Метод получает ряды
    @Query("select distinct g.series from Garag g")
    List<String> getSeries();

    //Метод получает должников
    @Query("select distinct g from Garag g inner join g.person p inner join g.contributions c where c.contribute > 0 or c.contLand > 0 " +
            "or c.contTarget > 0 or c.fines > 0 ")
    List<Garag> getGaragDebt();

    //Нахождение гаражей выбранного ряда
    List<Garag> findBySeries(String series);


    @Query("select distinct g.id, CONCAT(g.series,'-',g.number),p.id,CONCAT(p.lastName,' ',p.name,' ',p.fatherName), p.telephone, " +
            "ad.city, ad.street,ad.home, ad.apartment, p.benefits " +
            "from Garag as g LEFT JOIN g.person as p LEFT JOIN p.address as ad order by g.series, g.number")
    List<Object[]> getGaragsView();


}
