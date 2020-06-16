package org.springframework.samples.petclinic.cache;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.spring.api.DBRider;

import org.hibernate.Session;
import org.junit.Assert;
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

@DBRider
@Disabled
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SecondLevelCacheTest extends PostgresqlDbBaseTest {
  @PersistenceContext
  EntityManager em;

  @SuppressWarnings("unused")
  public ConnectionHolder connectionHolder = () -> dataSource.getConnection();

  @Test
  @DataSet(
      value = {"datasets/second-level-cache.xml"},
      executeScriptsBefore = "datasets/cleanup.sql",
      strategy = SeedStrategy.INSERT
  )
  @Commit
  public void secondLevelCache() {
    // add @Cacheable,@Cache to Vet, Specialty
    Session session = em.unwrap(Session.class);
    Vet vet = em.find(Vet.class, 100);
    org.hibernate.Cache secondLevelCache = session.getSessionFactory().getCache();
    Assert.assertTrue(secondLevelCache.containsEntity(Vet.class, 100));
    //secondLevelCache.evict(Vet.class, 100);
    session.clear(); // clear first level cache
    Vet cachedVet = session.get(Vet.class, 100);
  }

  @Test
  @DataSet(
      value = {"datasets/collection-cache.xml"},
      executeScriptsBefore = "datasets/cleanup.sql",
      strategy = SeedStrategy.INSERT
  )
  public void collectionCache() {
    Session session = em.unwrap(Session.class);
    // 1. just run
    // 2. remove @Cache and @Cacheable from Specialties class
    Vet vet = session.get(Vet.class, 102);
    vet.getNrOfSpecialties();
    System.out.println("--> specialties retrieved for vet");

    session.clear();

    Vet cachedVet = session.get(Vet.class, 102);
    cachedVet.getNrOfSpecialties();
    System.out.println("--> specialties retrieved for cached vet");
  }

  @Test
  @DataSet(
      value = {"datasets/hql-cache.xml"},
      executeScriptsBefore = "datasets/cleanup.sql",
      strategy = SeedStrategy.INSERT
  )
  @Commit
  public void hqlAndCache() {
    Session session = em.unwrap(Session.class);

    // 1. run with HQL: update Vet
    // 2. run with HQL: update Specialty
    Vet vet = session.get(Vet.class, 100);

    session.createQuery("update Vet v set v.firstName = 'new name'").executeUpdate();
    //session.createQuery("update Specialty s set s.name = 'new specialty'").executeUpdate();
    session.flush();
    session.clear();

    Vet cachedCity = session.get(Vet.class, 100);
  }

  @Test
  @DataSet(
      value = {"datasets/hql-cache.xml"},
      executeScriptsBefore = "datasets/cleanup.sql",
      strategy = SeedStrategy.INSERT
  )
  @Commit
  public void nativeSqlAndCache() {
    Session session = em.unwrap(Session.class);

    // 1. run with SQL: update Specialty
    // 2. add createSQLQuery(update Street).addSynchronizedEntityClass(Specialty.class)
    Vet vet = session.get(Vet.class, 100);
    System.out.println(vet.getFirstName());

    session.createSQLQuery("UPDATE specialties SET name = 'new specialty name'")
        .executeUpdate();
    session.flush();
    session.clear();

    Vet cachedVet = session.get(Vet.class, 100);
    System.out.println(cachedVet.getFirstName());
  }
}