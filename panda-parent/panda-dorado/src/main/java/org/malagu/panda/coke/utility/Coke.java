package org.malagu.panda.coke.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.Query;
import org.malagu.panda.coke.model.IBase;
import org.malagu.panda.coke.querysupporter.service.DoradoCriteriaBuilder;
import org.malagu.panda.dorado.linq.JpaUtil;
import org.malagu.panda.dorado.linq.lin.Linq;
import com.bstek.dorado.data.provider.Criteria;

public class Coke {
  private static DoradoCriteriaBuilder doradoCriteriaBuilder;

  public static DoradoCriteriaBuilder getDoradoCriteriaBuilder() {
    if (doradoCriteriaBuilder != null) {
      return doradoCriteriaBuilder;
    }

    doradoCriteriaBuilder =
        (DoradoCriteriaBuilder) org.malagu.linq.JpaUtil.getApplicationContext()
            .getBean(DoradoCriteriaBuilder.BEAN_ID);
    return doradoCriteriaBuilder;
  }

  public static <T> Linq query(Class<T> domainClass, Criteria criteria,
      Map<String, Object> parameterMap) {
    if (IBase.class.isAssignableFrom(domainClass)) {
      if (parameterMap == null) {
        parameterMap = new HashMap<>();
        parameterMap.put("deleted", false);
      } else if (!parameterMap.containsKey("deleted")) {
        parameterMap.put("deleted", false);
      }

    }
    criteria =
        getDoradoCriteriaBuilder().mergeQueryParameterCriteria(domainClass, parameterMap, criteria);
    return JpaUtil.linq(domainClass).where(criteria);
  }

  public static <T> Linq query(Class<T> domainClass, Class<?> resultClass, Criteria criteria,
      Map<String, Object> parameterMap) {
    criteria =
        getDoradoCriteriaBuilder().mergeQueryParameterCriteria(domainClass, parameterMap, criteria);
    return JpaUtil.linq(domainClass, resultClass).where(criteria);
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> query(String qlString, Map<String, Object> parameterMap) {
    Query query = JpaUtil.getEntityManager().createQuery(qlString);
    for (Entry<String, Object> entry : parameterMap.entrySet()) {
      query.setParameter(entry.getKey(), entry.getValue());
    }
    return query.getResultList();
  }

  public static void save(Object entityOrEntityList) {
    JpaUtil.save(entityOrEntityList, new CokeSavePolicyAdapter());
  }

  public static void persist(Iterable<?> entities) {
    for (Object entity : entities) {
      BaseEntityUtil.setMeta(entity);
    }
    JpaUtil.persist(entities);
  }

  public static void persist(Object entity) {
    BaseEntityUtil.setMeta(entity);
    JpaUtil.persist(entity);
  }

  public static void remove(Iterable<?> entities) {
    for (Object entity : entities) {
      BaseEntityUtil.setDelMeta(entity);
    }
    JpaUtil.persist(entities);
  }

  public static void remove(Object entity) {
    BaseEntityUtil.setDelMeta(entity);
    JpaUtil.persist(entity);
  }


}
