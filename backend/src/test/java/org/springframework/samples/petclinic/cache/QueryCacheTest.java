package org.springframework.samples.petclinic.cache;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.spring.api.DBRider;

import org.hibernate.jpa.QueryHints;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.util.PostgresqlDbBaseTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@DBRider
@DataJpaTest
@Disabled
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QueryCacheTest extends PostgresqlDbBaseTest {

  @PersistenceContext
  EntityManager em;

  @SuppressWarnings("unused")
  public ConnectionHolder connectionHolder = () -> dataSource.getConnection();

  @Test
  @DataSet(
      value = {"datasets/query-cache.xml"},
      executeScriptsBefore = "datasets/cleanup.sql",
      strategy = SeedStrategy.INSERT
  )
  @Commit
  public void queryCache() {
    String hql = "select v from Vet v where v.id = :id";
    TypedQuery<Vet> query = em.createQuery(hql, Vet.class)
        .setHint(QueryHints.HINT_CACHEABLE, true);

    query.setParameter("id", 100).getResultList();
    query.setParameter("id", 100).getResultList();
    query.setParameter("id", 100).getResultList();
    query.setParameter("id", 101).getResultList();
  }
}