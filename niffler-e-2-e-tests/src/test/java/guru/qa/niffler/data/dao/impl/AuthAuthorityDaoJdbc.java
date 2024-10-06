package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.Authority;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private final Connection connection;

  public AuthAuthorityDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public AuthorityEntity create(AuthorityEntity authority) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO authority (user_id, authority) " +
            "VALUES ( ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setObject(1, authority.getUser().getId());
      ps.setString(2, authority.getAuthority().name());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      authority.setId(generatedKey);
      return authority;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthorityEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM authority WHERE id = ?"
    )) {
      UdUserDao ud = new UdUserDaoJdbc(connection);
      ps.setObject(1, id);

      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setId(rs.getObject("id", UUID.class));
          ae.setUser(
              ud.findById(
                      rs.getObject("user_id", UUID.class))
                  .get()
          );
          ae.setAuthority(Authority.valueOf(rs.getString("authority")));
          return Optional.of(ae);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findAllByUserId(UUID userId) {
    List<AuthorityEntity> authorities = new ArrayList<>();
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM authority WHERE user_id = ?"
    )) {
      UdUserDao ud = new UdUserDaoJdbc(connection);
      ps.setObject(1, userId);

      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setId(rs.getObject("id", UUID.class));
          ae.setUser(
              ud.findById(
                      rs.getObject("user_id", UUID.class))
                  .get()
          );
          ae.setAuthority(Authority.valueOf(rs.getString("authority")));
          authorities.add(ae);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return authorities;
  }

  @Override
  public void delete(AuthorityEntity authority) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM authority WHERE id = ?"
    )) {
      ps.setObject(1, authority.getId());

      ps.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}